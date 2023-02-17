package com.cdmtc.screenshottooldemo.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.word.PicType;
import cn.hutool.poi.word.Word07Writer;
import com.cdmtc.screenshottooldemo.entity.WordEntity;
import com.cdmtc.screenshottooldemo.job.IndexJob;
import com.cdmtc.screenshottooldemo.service.IndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName IndexServiceImpl
 * @Description TODO
 * @Author Tao-pc
 * @Date 2022/10/20 16:29
 */
@Service
@RequiredArgsConstructor
public class IndexServiceImpl implements IndexService {

    private final static Map<String, Map<String, Object>> excelDataToMap;

    /**
     * 一批word的最大数量
     */
    private final static Integer MAXBATCHNUMBER = 100;

    private final IndexJob indexJob;

    private final String template = "将“%s”错误表述为“%s”";

    static {
        ExcelReader reader = ExcelUtil.getReader(ResourceUtil.getStream("excel/cuoqing.xlsx"));
        List<Map<String, Object>> excelData = reader.readAll();
        excelDataToMap = excelData.stream().collect(Collectors.toMap(e -> e.get("错误").toString(), Function.identity(),(key1, key2) -> key2));
    }


    @Override
    @Async
    public void fileUpload(List<Map<String, Object>> readAll, String fileName) {
        AtomicInteger index = new AtomicInteger();
        List<List<Map<String, Object>>> splitReadAll = ListUtil.split(readAll, MAXBATCHNUMBER);
        splitReadAll.stream().forEach(list -> {
            index.getAndIncrement();
            List<Future<WordEntity>> result = new ArrayList<>();
            list.stream().forEach(read -> {
                try {
                    IndexJob.blockingQueue.put(1);
                    result.add(indexJob.insideCdlMethod(read));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            writorWord(result, fileName, index.get());
        });
    }

    /**
     * 生成word
     *
     * @param futureList
     */
    public void writorWord(List<Future<WordEntity>> futureList, String fileName, Integer index) {
        Word07Writer writer = new Word07Writer();
        futureList.stream().forEach(future -> {
            try {
                WordEntity wordEntity = future.get();
                if (Objects.nonNull(wordEntity)) {
                    String[] keywords = wordEntity.getKeywords();
                    InputStream[] inputStreamScreenShots = wordEntity.getInputStreamScreenShots();
                    for (int i = 0; i < keywords.length; i++) {
                        String trueKeyword ;
                        if (excelDataToMap.containsKey(keywords[i])){
                            Map<String, Object> stringObjectMap = excelDataToMap.get(keywords[i]);
                            trueKeyword = stringObjectMap.get("正确").toString();
                        } else {
                            trueKeyword = "暂无正确相关词!";
                        }
                        // 添加段落（修改建议）
                        writer.addText(new Font("宋体", Font.PLAIN, 13), String.format(template,keywords[i],trueKeyword));
                        if (Objects.nonNull(inputStreamScreenShots[i])){
                            writer.addPicture(inputStreamScreenShots[i], PicType.PNG, "", 400, 400);
                        }
                    }
                    // 添加段落（正文）
                    writer.addText(new Font("宋体", Font.PLAIN, 13), wordEntity.getUrl());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        // 写出到文件
        writer.flush(FileUtil.file("/data/word/" + fileName + index + ".docx"));
        // 关闭
        writer.close();
    }
}
