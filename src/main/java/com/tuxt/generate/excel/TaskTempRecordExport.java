package com.tuxt.generate.excel;
 
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;

import lombok.Data;

import java.net.URL;
import java.util.List;
 
@ContentRowHeight(15)
@HeadRowHeight(20)
@Data
public class TaskTempRecordExport {

        @ColumnWidth(20)
        @ExcelProperty(value = "计划名称")
        private String planName;
        @ColumnWidth(20)
        @ExcelProperty(value = "描述")
        private String remark;
        @ColumnWidth(20)
        @ExcelProperty(value = "图片" ,converter = ExcelUrlConverterUtil.class)
        private List<URL> writeCellDataFile;
        /**
         * 图片路径
         */
        @ExcelIgnore
        private List<String> imagePathList;
 
}