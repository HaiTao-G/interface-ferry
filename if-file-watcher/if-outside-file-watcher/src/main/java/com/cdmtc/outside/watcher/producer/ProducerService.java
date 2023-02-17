package com.cdmtc.outside.watcher.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @ClassName ProducerService
 * @Description TODO
 * @Author Tao-pc
 * @Date 2022/8/25 10:01
 */
@Slf4j
@Component
public class ProducerService {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void send(String topic, String msg){
        //消息发送
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, msg);
        //添加回调
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.error("sendMessageAsync failure! topic : {}, message: {}", topic, msg);
            }

            @Override
            public void onSuccess(SendResult<String, String> stringStringSendResult) {
                log.info("sendMessageAsync success! topic: {}, message: {}", topic, msg);
            }
        });
    }
}
