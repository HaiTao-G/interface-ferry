package com.cdmtc.inside.watcher.Listener;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.cdmtc.curator.client.CuratorClient;
import com.cdmtc.entity.Instruction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @ClassName FileListener
 * @Description TODO
 * @Author Tao-pc
 * @Date 2022/8/25 9:49
 */
@Slf4j
@AllArgsConstructor
public class FileListener extends FileAlterationListenerAdaptor {

    // 声明业务服务
    private UploadFileService uploadFileService;

    private CuratorClient curatorClient;

    // 文件创建执行
    @Override
    public void onFileCreate(File file) {
        try {
            //获得文件内容
            String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            log.info("内网json文件名称:{},文件内容:{}",file.getName(),content);
            Instruction instruction = JSON.parseObject(content, Instruction.class);
            //获取外网请求的标识id
            String requestId = instruction.getRequestId();
            //获取外网执行结果
            Integer outsideStatus = instruction.getOutsideStatus();
            //获取外网同步过来的附件名单列表
            List<String> outsideFileName = instruction.getOutsideFileName();
            if (CollUtil.isNotEmpty(outsideFileName)){
                uploadFileService.fileExistenceAndUpload(outsideFileName,instruction);
            }
            //更改zk的node节点
            curatorClient.setNodeData("/".concat(requestId),JSON.toJSONString(instruction));
            //删除内网的文件
            file.delete();
        }catch (Exception e){
            log.error("内网指令json文件读取失败!,失败原因:{}",e);
        }
    }

    // 文件创建修改
    @Override
    public void onFileChange(File file) {
//        log.info(listenerService.test("【修改】", file));
    }

    // 文件创建删除
    @Override
    public void onFileDelete(File file) {
//        log.info(listenerService.test("【删除】", file));
    }

    // 目录创建
    @Override
    public void onDirectoryCreate(File directory) {
//        log.info(listenerService.test("【新建目录】", directory));
    }

    // 目录修改
    @Override
    public void onDirectoryChange(File directory) {
//        log.info(listenerService.test("【修改目录】", directory));
    }

    // 目录删除
    @Override
    public void onDirectoryDelete(File directory) {
//        log.info(listenerService.test("【删除目录】", directory));
    }


    // 轮询开始
    @Override
    public void onStart(FileAlterationObserver observer) {
    }

    // 轮询结束
    @Override
    public void onStop(FileAlterationObserver observer) {
    }
}
