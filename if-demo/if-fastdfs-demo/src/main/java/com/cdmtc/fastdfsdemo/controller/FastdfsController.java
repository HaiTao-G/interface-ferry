package com.cdmtc.fastdfsdemo.controller;

import com.ykrenz.fastdfs.FastDfs;
import com.ykrenz.fastdfs.model.fdfs.StorePath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @ClassName FastdfsController
 * @Description TODO
 * @Author Tao-pc
 * @Date 2022/11/29 10:02
 */
@RestController
public class FastdfsController {

    @Autowired
    private FastDfs fastDfs;

    @PostMapping("uploadFile")
    public String uploadFile(MultipartFile file) throws IOException {
        String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        StorePath storePath = fastDfs.uploadFile(file.getInputStream(), file.getSize(), type);
        return "上传成功,地址:" + fastDfs.accessUrl(storePath.getGroup(),storePath.getPath());
    }
}
