package com.cdmtc.inside.watcher.Listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @ClassName FileListenerRunner
 * @Description TODO
 * @Author Tao-pc
 * @Date 2022/8/25 10:08
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FileListenerRunner implements CommandLineRunner {

    final FileListenerFactory fileListenerFactory;

    @Override
    public void run(String... args) {
        // 创建监听者
        FileAlterationMonitor fileAlterationMonitor = fileListenerFactory.getMonitor();
        try {
            // do not stop this thread
            fileAlterationMonitor.start();
        } catch (Exception e) {
            log.error("文件监听服务异常：" + e.toString());
            e.printStackTrace();
        }
    }
}
