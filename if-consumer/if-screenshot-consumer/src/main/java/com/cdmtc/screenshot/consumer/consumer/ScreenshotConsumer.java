package com.cdmtc.screenshot.consumer.consumer;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cdmtc.common.util.JsonFileUtil;
import com.cdmtc.curator.client.CuratorClient;
import com.cdmtc.entity.Instruction;
import com.cdmtc.entity.OutsideStatusEnum;
import com.cdmtc.entity.callbackparameters.ScreenshotCallbackParameters;
import com.cdmtc.entity.parameters.ScreenshotParameters;
import com.cdmtc.entity.result.ScreenshotResult;
import com.cdmtc.ftp.service.FtpService;
import com.cdmtc.screenshot.consumer.service.ScreenShotService;
import com.google.common.collect.Lists;
import com.ykrenz.fastdfs.FastDfs;
import com.ykrenz.fastdfs.model.fdfs.StorePath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
public class ScreenshotConsumer {

    @Value("${inside.interfacePath}")
    private String interfacePath;

    @Value("${inside.filePath}")
    private String filePath;

    @Value("${isIntranet}")
    private boolean isIntranet;

    @Autowired
    private ScreenShotService screenShotService;

    @Autowired(required = false)
    private FtpService ftpService;

    @Autowired(required = false)
    private FastDfs fastDfs;

    @Autowired(required = false)
    private CuratorClient curatorClient;

    // 消费监听
    @KafkaListener(topics = "${kafka.topic}")
    public void onMessage1(ConsumerRecord<?, ?> record) {
        try {
            // 消费的哪个topic、partition的消息,打印出消息内容
            log.info("截图消费:" + record.topic() + "-" + record.partition() + "-" + record.value());
            String instructionJsonString = record.value().toString();
            //1,json字符串反序列化为Instruction
            Instruction<ScreenshotCallbackParameters, ScreenshotParameters, ScreenshotResult> instruction =
                    JSON.parseObject(instructionJsonString, new TypeReference<Instruction<ScreenshotCallbackParameters, ScreenshotParameters, ScreenshotResult>>() {
                    });
            //2,拿到外网截图的url
            String url = instruction.getParameters().getUrl();
            //3,截图
            InputStream inputStream = screenShotService.screenShotToFile(url);
            instruction.setUpdateTime(DateUtil.now());
            if (Objects.nonNull(inputStream)) {
                instruction.setOutsideStatus(OutsideStatusEnum.SUCCESS_REQUESTED.getCode());
            } else {
                instruction.setOutsideStatus(OutsideStatusEnum.FALSE_REQUESTED.getCode());
            }
            //4,回写指令数据,并上传截图照片
            if (isIntranet) {
                ftpUploadFile(inputStream, instruction);
            } else {
                fastdfsUploadFile(inputStream, instruction);
                updateZk(instruction);
            }
        } catch (Exception e) {
            log.error("截图服务消费异常{}", e);
        }
    }

    /**
     * 内外网环境截图服务指令上传ftp
     *
     * @param inputStream
     * @param instruction
     */
    private void ftpUploadFile(InputStream inputStream, Instruction instruction) {
        if (Objects.nonNull(inputStream)){
            String fileName = UUID.randomUUID().toString().concat(".png");
            ftpService.uploadFileToFtp(filePath, fileName, inputStream);
            instruction.setOutsideFileName(new ArrayList<String>() {{
                add(fileName);
            }});
        }
        //Instruction对象转成.json格式文件输入流
        InputStream jsonStream = JsonFileUtil.createJsonInputStream(instruction);
        String interfaceFileNmae = instruction.getRequestId().concat(".json");
        log.info("发送内网的指令数据:{}", JSON.toJSONString(instruction));
        ftpService.uploadFileToFtp(interfacePath, interfaceFileNmae, jsonStream);
    }

    /**
     * 纯外网环境截图服务截图文件上传fastdfs
     *
     * @param inputStream
     * @param instruction
     */
    private void fastdfsUploadFile(InputStream inputStream, Instruction instruction) throws IOException {
        if (Objects.nonNull(inputStream)) {
            StorePath pngStorePath = fastDfs.uploadFile(inputStream, inputStream.available(), "png");
            String pngUrl = fastDfs.accessUrl(pngStorePath.getGroup(), pngStorePath.getPath());
            instruction.setWithinFiLeName(Lists.newArrayList(pngUrl));
        }
    }

    /**
     * 纯外网环境指令文件修改zk数据
     *
     * @param instruction
     */
    private void updateZk(Instruction instruction) {
        curatorClient.setNodeData("/".concat(instruction.getRequestId()), JSON.toJSONString(instruction));
    }

}
