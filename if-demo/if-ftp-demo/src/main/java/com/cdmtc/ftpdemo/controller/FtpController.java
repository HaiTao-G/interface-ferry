package com.cdmtc.ftpdemo.controller;

import com.cdmtc.ftp.service.FtpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.lang.ELArithmetic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
public class FtpController {

    @Autowired
    private FtpService ftpService;

    @PostMapping("uploadFile")
    public String uploadFile(MultipartFile file,
                             String fileName,
                             String path) throws IOException {

        try {
            if (ftpService.uploadFileToFtp(path,fileName,file.getInputStream())){
                return "上传成功!";
            }else {
                return "上传失败!";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
