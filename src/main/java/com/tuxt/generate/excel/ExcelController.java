package com.tuxt.generate.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.tuxt.generate.minio.MinioImageUploader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@Slf4j
public class ExcelController {
    @Autowired
    private MinioImageUploader uploader;

    @PostMapping("/readExcelWithImages")
    public void readExcelWithImages(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        if (!file.isEmpty()) {
            try (InputStream inputStream = file.getInputStream()) {
                List<TaskTempRecordImport> dataList = new ArrayList<>();
                Workbook workbook = WorkbookFactory.create(inputStream);
                // 默认处理第一个sheet
                XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
                for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                    XSSFRow xssfRow = sheet.getRow(rowNum);
                    TaskTempRecordImport recordImport=new TaskTempRecordImport();
                    if (xssfRow != null) {
                        recordImport.setPlanName(getValue(xssfRow.getCell(ImportTemplete.planName)));
                        recordImport.setRemark(getValue(xssfRow.getCell(ImportTemplete.remark)));
                        dataList.add(recordImport);
                    }
                }
                Map<Integer,String> map=new HashMap<Integer,String>();
                List<POIXMLDocumentPart> relations = sheet.getRelations();
                // 存储已经提取的图片位置
                Set<String> extractedPositions = new HashSet<>();
                for (POIXMLDocumentPart relation : relations) {
                    if (relation instanceof XSSFDrawing) {
                        XSSFDrawing drawing = (XSSFDrawing) relation;
                        for (XSSFShape shape : drawing.getShapes()) {
                            if (shape instanceof XSSFPicture) {
                                XSSFPicture picture = (XSSFPicture) shape;
                                // 获取图片的客户端锚点，可以确定图片在单元格中的位置
                                int row = picture.getPreferredSize().getFrom().getRow();
                                int col = picture.getPreferredSize().getFrom().getCol();
                                String positionKey = row + "_" + col;
                                System.out.println(positionKey);
                                // 检查是否已经提取了该位置的图片
                                if (!extractedPositions.contains(positionKey)){
                                    // 获取图片的二进制数据
                                    byte[] data = picture.getPictureData().getData();
                                    // 这里可以根据row和col以及data来处理图片，比如保存或上传
                                    String image = uploader.uploadImage(data);
                                    extractedPositions.add(positionKey);
                                    map.put(row,image);
                                }

                            }
                        }
                    }
                }



                for (int i = 0; i < dataList.size(); i++) {
                    dataList.get(i).setImageUrl(map.get(i+1));
                }
                System.out.println(JSON.toJSON(dataList));
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
    private String getValue(XSSFCell xssfCell) {
        if (xssfCell!=null) {
            if (xssfCell.getCellType() == CellType.BOOLEAN) {
                return String.valueOf(xssfCell.getBooleanCellValue());
            } else if (xssfCell.getCellType() == CellType.NUMERIC) {
                return new BigDecimal(xssfCell.getNumericCellValue()).toPlainString();
            } else {
                return String.valueOf(xssfCell.getStringCellValue());
            }
        }else {
            return "";
        }
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        List<TaskTempRecordExport> demoDataList = new ArrayList<>();
        TaskTempRecordExport taskTempRecordExport = new TaskTempRecordExport();
        taskTempRecordExport.setPlanName("测试");
        taskTempRecordExport.setImagePathList(Arrays.asList("http://127.0.0.1:9001/api/v1/download-shared-object/aHR0cDovLzEyNy4wLjAuMTo5MDAwL3Rlc3QvJUU1JUJFJUFFJUU0JUJGJUExJUU1JTlCJUJFJUU3JTg5JTg3XzIwMjQwNDA1MDk1OTE4LmpwZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPVJCNlVCVjhaV082MEpMWEsyQVVNJTJGMjAyNDA4MjMlMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjQwODIzVDA5NTQwMFomWC1BbXotRXhwaXJlcz00MzE5NyZYLUFtei1TZWN1cml0eS1Ub2tlbj1leUpoYkdjaU9pSklVelV4TWlJc0luUjVjQ0k2SWtwWFZDSjkuZXlKaFkyTmxjM05MWlhraU9pSlNRalpWUWxZNFdsZFBOakJLVEZoTE1rRlZUU0lzSW1WNGNDSTZNVGN5TkRRME9Ua3pNQ3dpY0dGeVpXNTBJam9pYldsdWFXOWhaRzFwYmlKOS52TUNyVXI4a2txRldNLV95T2ZrTmdxTWYwTm5TOXhDclFneDM2aGJZMUNmd0FYZ1pBcHlJUzYzWlNRa1AxZW1EOEdUS1NKME5EVXhzdEs2cHAzd19WQSZYLUFtei1TaWduZWRIZWFkZXJzPWhvc3QmdmVyc2lvbklkPW51bGwmWC1BbXotU2lnbmF0dXJlPWQ0ZDc1ZDdkMTYwZGU2MTU4ZGUwMWFhNTk5Y2U4NmVkNzQyZGQ2ZWRmNWJhMWJiNTMyZjg5ZTkxOTJmOGM0MjU"));
        demoDataList.add(taskTempRecordExport);
        if (CollectionUtils.isEmpty(demoDataList)) {
            return;
        }
        //图片列最大图片数
        AtomicReference<Integer> maxImageSize = new AtomicReference<>(0);
        demoDataList.forEach(item -> {
            //最大图片数大小
            if (!CollectionUtils.isEmpty(item.getImagePathList()) && item.getImagePathList().size() > maxImageSize.get()) {
                maxImageSize.set(item.getImagePathList().size());
            }
        });
        //设置图片的url，添加到展示的url上
        demoDataList.forEach(item -> {
            List<URL> urls = new ArrayList<>();
            List<String> imagePathList = item.getImagePathList();
            if (imagePathList != null && !imagePathList.isEmpty()) {
                for (String value : imagePathList) {
                    try {
                        if (!StringUtils.isEmpty(value)) {//value:就是图片后续地址
                            urls.add(new URL(value));
                        }
                    } catch (MalformedURLException e) {
                        log.error("查询到的url转换展示url", e);
                    }
                }
                item.setWriteCellDataFile(urls);
            }
        });
        log.debug("需要导出临时记录的数据：" + JSON.toJSONString(demoDataList));
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String fileName = "带图片的excel";
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        //导出临时记录的数据（带图片）
        EasyExcel.write(response.getOutputStream(), TaskTempRecordExport.class)
                .autoCloseStream(true)
                .registerWriteHandler(new CustomImageModifyHandler())
                .sheet("临时记录")
                .doWrite(demoDataList);
    }

}
