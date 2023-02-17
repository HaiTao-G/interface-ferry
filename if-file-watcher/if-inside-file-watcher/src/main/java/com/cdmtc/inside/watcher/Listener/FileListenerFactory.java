package com.cdmtc.inside.watcher.Listener;

import com.cdmtc.curator.client.CuratorClient;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName FileListenerFactory
 * @Description 监听文件工厂bean
 * @Author Tao-pc
 * @Date 2022/8/25 10:05
 */
@Component
@RequiredArgsConstructor
public class FileListenerFactory {

    // 设置轮询间隔
    private final long interval = TimeUnit.SECONDS.toMillis(1);

    // 自动注入业务服务
    final UploadFileService uploadFileService;

    final CuratorClient curatorClient;

    @Value("${inside.interfacePath}")
    private String interfacePath;

    public FileAlterationMonitor getMonitor() {
        // 创建过滤器
        IOFileFilter files = FileFilterUtils.and(
                FileFilterUtils.fileFileFilter(),
                FileFilterUtils.suffixFileFilter(".json"));
        IOFileFilter filter = FileFilterUtils.or(files);

        // 装配过滤器
        FileAlterationObserver observer = new FileAlterationObserver(new File(interfacePath), filter);

        // 向监听者添加监听器，并注入业务服务
        observer.addListener(new FileListener(uploadFileService,curatorClient));

        // 返回监听者
        return new FileAlterationMonitor(interval, observer);
    }
}
