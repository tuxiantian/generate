package com.tuxt.generate.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class TaskTempRecordImport {
    @ExcelProperty(value = "计划名称")
    private String planName;
    @ExcelProperty(value = "描述")
    private String remark;

    private String imageUrl;
}
