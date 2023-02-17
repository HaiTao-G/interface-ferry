package com.cdmtc.again.consumer.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cdmtc.common.enums.OutStatus;
import com.cdmtc.common.util.JsonFileUtil;
import com.cdmtc.entity.Instruction;
import com.cdmtc.ftp.service.FtpService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class AgainConsumer {

    @Autowired
    private FtpService ftpService;
    @Value("${pagecrawl_url}")
    private String pagecrawlUrl;

    @KafkaListener(topics = "#{'${kakfa.topics}'}", groupId = "#{'${kakfa.groupId}'}")
    public void consumer(String mes) throws IOException {
        Gson gson = new Gson();
        Instruction instruction = gson.fromJson(mes, Instruction.class);
        log.info("获取到kafka参数：{}",mes);
        // 执行重采请求
        this.execute(instruction);
        instruction.setUpdateTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        String fileName = UUID.randomUUID() + ".json";
        Path write = Files.write(Paths.get(fileName), gson.toJson(instruction).getBytes(StandardCharsets.UTF_8));
        File file = write.toFile();
        InputStream in = new FileInputStream(file);
        /**
         * TODO
         */
//        ftpService.uploadFile(fileName, in);
        FileUtil.del(file);
    }

    /**
     * 执行请求
     * @param instruction
     * @return
     */
    public Instruction execute(Instruction instruction) {
        log.info("#########执行属地重采任务#######");
        try {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            log.info("请求地址：{}", pagecrawlUrl);
            log.info("请求参数：{}", instruction.getParameters());
            Gson gson = new Gson();
            JSONObject jsonObject = JSONObject.parseObject(gson.toJson(instruction.getParameters()));
            HttpRequest request = HttpUtil.createPost(pagecrawlUrl);
            request.body(JSONObject.toJSONString(jsonObject.get("params")));
            request.header(Header.ACCEPT, "application/json;charset=UTF-8");
            request.method(Method.POST);
            request.timeout(100 * 1000);
            String resStr = request.execute().body();
            instruction.setResult(resStr);
            instruction.setOutsideStatus(OutStatus.SUCCESS.getCode());
        } catch (Exception e) {
            instruction.setOutsideStatus(OutStatus.FALSE.getCode());
            instruction.setErrMsg(e.getMessage());
            log.error("执行属地重采任务失败!错误信息：{}", e);
        }
        return instruction;
    }
}
