package com.tuxt.generate.excel;
 
 
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ImageData;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.IoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
 
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
 
 
@Slf4j
public class ExcelUrlConverterUtil implements Converter<List<URL>> {
    @Override
    public Class supportJavaTypeKey() {
        return List.class;
    }
 
    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.EMPTY;
    }
 
    @Override
    public List convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return null;
    }
 
    @Override
    public WriteCellData<?> convertToExcelData(List<URL> value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        // 这里进行对数据实体类URL集合处理
        List<ImageData> data = new ArrayList<>();
        ImageData imageData;
        // for 循环一次读取
        for (URL url : value) {
            try (InputStream inputStream = url.openStream();) {
                byte[] bytes = IoUtils.toByteArray(inputStream);
                imageData = new ImageData();
                imageData.setImage(bytes);
                data.add(imageData);
            } catch (Exception e) {
                log.error("导出临时记录图片异常：", e);
            }
        }
        WriteCellData<?> cellData = new WriteCellData<>();
        if (CollectionUtils.isNotEmpty(data)) {
            // 图片返回图片列表
            cellData.setImageDataList(data);
            cellData.setType(CellDataTypeEnum.EMPTY);
        } else {
            // 没有图片使用汉字表示
            cellData.setStringValue("无图");
            cellData.setType(CellDataTypeEnum.STRING);
        }
        return cellData;
    }
}
 