//package com.cdmtc.screenshot.consumer.service;
//
//import cn.hutool.core.io.FileUtil;
//import com.ykrenz.fastdfs.FastDfs;
//import com.ykrenz.fastdfs.model.fdfs.StorePath;
//import org.apache.commons.io.IOUtils;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//class ScreenShotServiceTest {
//
//    @Autowired
//    private FastDfs fastDfs;
//
//    @Test
//    public void test() throws IOException {
//        FileInputStream fileInputStream = new FileInputStream("E:\\1.txt");
//        new File("").length();
//        StorePath pngStorePath = fastDfs.uploadFile(fileInputStream, fileInputStream.available(), "txt");
//        String pngUrl = fastDfs.accessUrl(pngStorePath.getGroup(), pngStorePath.getPath());
//        System.out.println(pngUrl);
//    }
//
//}