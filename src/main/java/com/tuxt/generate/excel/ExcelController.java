package com.tuxt.generate.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@Slf4j
public class ExcelController {

    String SOURCE_PATH;

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        List<TaskTempRecordExport> demoDataList=new ArrayList<>();
        TaskTempRecordExport taskTempRecordExport=new TaskTempRecordExport();
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
            List<URL> urls=new ArrayList<>();
            List<String> imagePathList = item.getImagePathList();
            if(imagePathList!=null&&!imagePathList.isEmpty()){
                for (String value : imagePathList) {
                    try {
                        if(!StringUtils.isEmpty(value)){//value:就是图片后续地址
                            //添加服务器的地址 SOURCE_PATH：http://www.baidu.com
//                            String urlPath = SOURCE_PATH + value;
                            urls.add(new URL(value));
                        }
                    } catch (MalformedURLException e) {
                        log.error("查询到的url转换展示url", e);
                    }
                }
                item.setWriteCellDataFile(urls);
            }
        });
        log.debug("需要导出临时记录的数据："+ JSON.toJSONString(demoDataList));
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String fileName="带图片的excel";
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
