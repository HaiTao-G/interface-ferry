package com.cdmtc.inside.watcher.Listener;

import com.alibaba.fastjson.JSON;
import com.cdmtc.entity.Instruction;
import com.ykrenz.fastdfs.FastDfs;
import com.ykrenz.fastdfs.model.fdfs.StorePath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UploadFile
 * @Description TODO
 * @Author Tao-pc
 * @Date 2022/8/25 14:00
 */
@Slf4j
@Component
public class UploadFileService {

    /**
     * 内网附件存放路径
     */
    @Value("${inside.filePath}")
    private String filePath;

    @Autowired
    private FastDfs Fastdfs;

    /**
     * value：抛出指定异常才会重试
     * include：和value一样，默认为空，当exclude也为空时，默认所有异常
     * exclude：指定不处理的异常
     * maxAttempts：最大重试次数，默认3次
     * backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为2000L；multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为2秒，第二次为3秒，第三次为4.5秒。
     *
     * @param outsideFileNameList
     * @param instruction
     */
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public void fileExistenceAndUpload(List<String> outsideFileNameList, Instruction instruction) throws Exception {
        List<String> withinFiLeNameList = new ArrayList<>(outsideFileNameList.size());
        for (String outsideFileName : outsideFileNameList) {
            File outsideFileNameFile = new File(filePath.concat(File.separator).concat(outsideFileName));
            if (outsideFileNameFile.exists()) {
                //TODO 上传文件服务器得到内网的存放地址并写入指令对象
                StorePath withinFiLeNamePath = Fastdfs.uploadFile(outsideFileNameFile);
                withinFiLeNameList.add(Fastdfs.accessUrl(withinFiLeNamePath.getGroup(), withinFiLeNamePath.getPath()));
                outsideFileNameFile.delete();
            } else {
                log.error("不存在的文件名称:{}", outsideFileName);
                throw new Exception("文件不存在!");
            }
        }
        instruction.setWithinFiLeName(withinFiLeNameList);
        log.info("更新附件地址后的指令数据:{}", JSON.toJSONString(instruction));
    }

    @Recover
    public void recover(Exception e, Instruction instruction) {
        log.info("附件检测回调方法执行！！！！");
        log.error("内网附件上传失败!", e);
        //记日志到数据库,删除zk中的node
    }
}
