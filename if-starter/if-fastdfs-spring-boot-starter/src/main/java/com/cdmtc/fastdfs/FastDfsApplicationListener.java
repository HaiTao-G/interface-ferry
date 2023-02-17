package com.cdmtc.fastdfs;

import com.ykrenz.fastdfs.FastDfs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.Map;

/**
 * @ClassName FastDfsApplicationListener
 * @Description TODO
 * @Author Tao-pc
 * @Date 2022/11/28 18:03
 */
@Slf4j
public class FastDfsApplicationListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        Map<String, FastDfs> fastDfsClientMap = event.getApplicationContext().getBeansOfType(FastDfs.class);
        log.info("{} FastDfsClients will be shutdown soon", fastDfsClientMap.size());
        fastDfsClientMap.keySet().forEach(beanName -> {
            log.info("shutdown FastDfs Client: {}", beanName);
            fastDfsClientMap.get(beanName).shutdown();
        });
    }

}
