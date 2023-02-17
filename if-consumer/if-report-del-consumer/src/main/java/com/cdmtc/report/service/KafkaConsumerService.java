package com.cdmtc.report.service;

import cn.hutool.http.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cdmtc.common.util.JsonFileUtil;
import com.cdmtc.entity.Instruction;
import com.cdmtc.entity.OutsideStatusEnum;
import com.cdmtc.entity.callbackparameters.ReportdelCallbackParameters;
import com.cdmtc.entity.parameters.ReportdelParameters;
import com.cdmtc.ftp.service.FtpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @Author: xiaobin
 * @Date: 2022/8/24 16:12
 * @Mail: xiaobin@bxtdata.com
 * @Describe: TODO
 */
@Slf4j
@Service
public class KafkaConsumerService {

    @Value("${inside.interfacePath}")
    private String interfacePath;

    @Autowired
    private FtpService ftpService;

    @KafkaListener(topics = "#{'${kakfa.topics}'}")
    public void taskStateConsumer(ConsumerRecord<?, ?> record) {
        try {
            // 消费的哪个topic、partition的消息,打印出消息内容
            log.info("报删消费:" + record.topic() + "-" + record.partition() + "-" + record.value());
            String instructionJsonString = record.value().toString();
            //1,json字符串反序列化为Instruction
            Instruction<ReportdelCallbackParameters, ReportdelParameters, String> instruction =
                    JSON.parseObject(instructionJsonString, new TypeReference<Instruction<ReportdelCallbackParameters, ReportdelParameters, String>>() {
                    });
            //2,进行业务处理
            ReportdelParameters parameters = instruction.getParameters();
            try {
                HttpRequest request = HttpUtil.createPost(parameters.getUrl());
                request.body(parameters.getParams());
                log.info("报删核查外网请求url:{},请求参数:{}",parameters.getUrl(),parameters.getParams());
                request.header(Header.ACCEPT, "application/json;charset=UTF-8");
                request.method(Method.POST);
                request.timeout(100 * 1000);
                String resStr = request.execute().body();
                log.info("报删核查外网请求返回数据:{}",resStr);
                instruction.setResult(resStr);
                instruction.setOutsideStatus(OutsideStatusEnum.SUCCESS_REQUESTED.getCode());
            } catch (HttpException e) {
                instruction.setOutsideStatus(OutsideStatusEnum.TIMED_OUT_REQUESTED.getCode());
                log.error("外网报账核查请求失败!",e);
            }
            //3,Instruction对象转成.json格式文件输入流
            InputStream jsonStream = JsonFileUtil.createJsonInputStream(instruction);
            String interfaceFileNmae = instruction.getRequestId().concat(".json");
            log.info("发送内网的指令数据:{}", JSON.toJSONString(instruction));
            ftpService.uploadFileToFtp(interfacePath, interfaceFileNmae, jsonStream);
        } catch (Exception e) {
            log.error("外网报账核查服务消费异常{}",e);
        }
    }
}
