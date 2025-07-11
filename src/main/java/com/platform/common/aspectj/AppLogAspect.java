package com.platform.common.aspectj;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.shiro.ShiroUserVo;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.utils.IpUtils;
import com.platform.common.utils.LogUtils;
import com.platform.common.utils.ServletUtils;
import com.platform.modules.sys.domain.SysLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 操作日志记录处理
 */
@Aspect
@Component
@Slf4j
public class AppLogAspect {

    /**
     * 配置织入点
     */
    @Pointcut("@annotation(com.platform.common.aspectj.AppLog)")
    public void logPointCut() {
    }

    /**
     * 处理完请求后执行
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
        handleLog(joinPoint, null, jsonResult);
    }

    /**
     * 拦截异常操作
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult) {
        try {
            // 获得注解
            AppLog appLog = getAnnotationLog(joinPoint);
            if (appLog == null) {
                return;
            }
            // 当前的用户
            ShiroUserVo shiroUserVo = ShiroUtils.getLoginUser();
            // *========数据库日志=========*//
            SysLog sysLog = new SysLog();
            sysLog.setStatus(YesOrNoEnum.YES);
            // 请求的地址
            String ipAddr = IpUtils.getIpAddr(ServletUtils.getRequest());
            sysLog.setIpAddr(ipAddr);
            // 返回参数
            sysLog.setMessage(JSONUtil.toJsonStr(jsonResult));
            sysLog.setRequestUrl(ServletUtils.getRequest().getRequestURI());
            if (shiroUserVo != null) {
                sysLog.setUsername(shiroUserVo.getUsername());
            }
            if (e != null) {
                sysLog.setStatus(YesOrNoEnum.NO);
                sysLog.setMessage(StrUtil.sub(e.getMessage(), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            sysLog.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            sysLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            // 设置操作时间
            sysLog.setCreateTime(DateUtil.date());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, appLog, sysLog);
            // 远程查询操作地点
            sysLog.setLocation(IpUtils.getIpAddr(ipAddr));
            // 保存数据库
            LogUtils.recordOper(sysLog);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage(), e);
        }
    }

    /**
     * 注解中对方法的描述信息 用于Controller层注解
     */
    protected void getControllerMethodDescription(JoinPoint joinPoint, AppLog appLog, SysLog sysLog) {
        // 设置action动作
        sysLog.setLogType(appLog.type());
        // 设置标题
        sysLog.setTitle(appLog.value());
        // 是否需要保存request，参数和值
        if (appLog.save()) {
            // 参数的信息，传入到数据库中。
            setRequestValue(joinPoint, sysLog);
        }
    }

    /**
     * 请求的参数，放到log中
     */
    protected void setRequestValue(JoinPoint joinPoint, SysLog sysLog) {
        String requestMethod = sysLog.getRequestMethod();
        if ("POST".equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs());
            sysLog.setRequestParam(StrUtil.sub(params, 0, 2000));
        } else {
            Map<?, ?> paramsMap = (Map<?, ?>) ServletUtils.getRequest().getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            sysLog.setRequestParam(StrUtil.sub(paramsMap.toString(), 0, 2000));
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    protected AppLog getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation(AppLog.class);
        }
        return null;
    }

    /**
     * 参数拼装
     */
    protected String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray == null) {
            return params;
        }
        for (Object object : paramsArray) {
            if (!isFilterObject(object)) {
                params += JSONUtil.toJsonStr(object) + " ";
            }
        }
        return params.trim();
    }

    /**
     * 判断是否需要过滤的对象。
     */
    protected boolean isFilterObject(final Object o) {
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
    }

}
