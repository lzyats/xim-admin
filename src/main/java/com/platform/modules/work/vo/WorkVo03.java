package com.platform.modules.work.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true) // 链式调用
public class WorkVo03 {

    private List<String> dataList;

}
