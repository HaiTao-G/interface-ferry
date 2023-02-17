package com.cdmtc.ftp.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.extra.ftp.AbstractFtp;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class FtpUtils {

    public static final String FILE_TEMP_PATH = "./ftpfile";

    private final AbstractFtp ftp;

    public FtpUtils(AbstractFtp ftp) {
        this.ftp = ftp;
    }

    /**
     * 目录下所有文件
     * @param path 目录
     * @return java.util.List<java.lang.String>
     */
    public List<String> ls(String path) {
        return ftp.ls(path);
    }

    /**
     * 目录下所有文件
     * @param path    目录
     * @param pattern 正则匹配
     * @return java.util.List<java.lang.String>
     */
    public List<String> ls(String path, String pattern) {
        List<String> list = ftp.ls(path);
        return list.stream().filter(filename -> Pattern.matches(pattern, filename)).collect(Collectors.toList());
    }

    /**
     * 文件上传
     * @param filePath 文件路径
     * @param inFile   上传文件
     * @return boolean
     */
    public boolean uploadFile(String filePath, File inFile) {
        return ftp.upload(filePath, inFile);
    }

    /**
     * 文件下载
     * @param filePath 文件路径
     * @param outFile  下载文件
     */
    public void downloadFile(String filePath, File outFile) {
        ftp.download(filePath, outFile);
    }

    /**
     * 删除文件
     * @param filePath 文件路径
     * @return boolean
     */
    public boolean delFile(String filePath) {
        return ftp.delFile(filePath);
    }

    /**
     * 文件流读取
     * @param filePath 文件路径
     * @return java.io.InputStream
     */
    public InputStream readFileInputStream(String filePath) {
        String fileName = FileUtil.getName(filePath);
        File file = new File(FILE_TEMP_PATH + "/" + fileName);
        try {
            ftp.download(filePath, file);
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new IORuntimeException("File not exist [" + file.getName() + "]");
        } finally {
            FileUtil.del(file);
        }
    }

    /**
     * 文件内容读取
     * @param filePath 文件路径
     * @return java.util.List<java.lang.String>
     */
    public List<String> readFileLines(String filePath) {
        String fileName = FileUtil.getName(filePath);
        File file = new File(FILE_TEMP_PATH + "/" + fileName);
        try {
            ftp.download(filePath, file);
            return FileUtil.readLines(file, "UTF-8");
        } finally {
            FileUtil.del(file);
        }
    }

}
