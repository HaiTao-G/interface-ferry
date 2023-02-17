package com.cdmtc.ftp.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.extra.ftp.AbstractFtp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.ObjectPool;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class FtpService {

    private static final ThreadLocal<AbstractFtp> FTP_THREAD_LOCAL = new ThreadLocal<>();

    private final ObjectPool<AbstractFtp> ftpPool;

    public FtpService(ObjectPool<AbstractFtp> objectPool) {
        this.ftpPool = objectPool;
    }

    /**
     * 创建连接
     */
    private void connect() {
        try {
            AbstractFtp ftp = ftpPool.borrowObject();
            FTP_THREAD_LOCAL.set(ftp);
        } catch (Exception e) {
            throw new IORuntimeException("ftp connect error", e);
        }
    }

    /**
     * 释放连接
     */
    private void release() {
        try {
            AbstractFtp ftp = FTP_THREAD_LOCAL.get();
            ftpPool.returnObject(ftp);
        } catch (Exception e) {
            log.error("ftp release fail ", e);
        } finally {
            FTP_THREAD_LOCAL.remove();
        }
    }

    /**
     * 文件上传
     *
     * @param filePath    文件上传路径
     * @param fileName    文件上传名称
     * @param inputStream 本地文件流
     */
    public boolean uploadFileToFtp(String filePath, String fileName, InputStream inputStream) {
        log.info("ftp上传文件... {}", fileName);
        File file = null;
        try {
            connect();
            FtpUtils ftpUtils = new FtpUtils(FTP_THREAD_LOCAL.get());
            file = FileUtil.writeFromStream(inputStream, new File(FtpUtils.FILE_TEMP_PATH + "/" + fileName));
            return ftpUtils.uploadFile(filePath, file);
        } catch (Exception e) {
            log.error("ftp上传文件异常 ", e);
            return false;
        } finally {
            if (Objects.nonNull(file)) {
                FileUtil.del(file);
            }
            release();
        }
    }

    /**
     * 文件上传
     *
     * @param filePath 文件上传路径
     * @param fileName 文件上传名称
     * @param file     本地文件
     * @return boolean
     */
    public boolean uploadFileToFtp(String filePath, String fileName, File file) {
        log.info("ftp上传文件... {}", fileName);
        try {
            connect();
            FtpUtils ftpUtils = new FtpUtils(FTP_THREAD_LOCAL.get());
            filePath = "." + filePath;
            return ftpUtils.uploadFile(filePath, file);
        } catch (Exception e) {
            log.error("ftp上传文件异常 ", e);
            return false;
        } finally {
            release();
        }
    }

    /**
     * 文件下载
     *
     * @param filePath 文件下载路径
     * @param fileName 文件下载名称
     * @param outFile  本地文件
     */
    public void downloadFile(String filePath, String fileName, File outFile) {
        log.info("ftp下载文件... {}", fileName);
        try {
            connect();
            FtpUtils ftpUtils = new FtpUtils(FTP_THREAD_LOCAL.get());
            filePath = "." + filePath + "/" + fileName;
            ftpUtils.downloadFile(filePath, outFile);
        } catch (Exception e) {
            log.error("ftp下载文件异常 ", e);
        } finally {
            release();
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @param fileName 文件名称
     */
    public boolean deleteFile(String filePath, String fileName) {
        log.info("ftp删除文件... {}", fileName);
        try {
            connect();
            FtpUtils ftpUtils = new FtpUtils(FTP_THREAD_LOCAL.get());
            filePath = "." + filePath + "/" + fileName;
            return ftpUtils.delFile(filePath);
        } catch (Exception e) {
            log.error("ftp删除文件异常 ", e);
            return false;
        } finally {
            release();
        }
    }

    /**
     * 获取文件列表
     *
     * @param filePath 文件路径
     * @param pattern  正则匹配
     * @return java.util.List<java.lang.String>
     */
    public List<String> getFileList(String filePath, String pattern) {
        log.info("ftp获取文件列表... {}", pattern);
        try {
            connect();
            FtpUtils ftpUtils = new FtpUtils(FTP_THREAD_LOCAL.get());
            filePath = "." + filePath;
            return ftpUtils.ls(filePath, pattern);
        } catch (Exception e) {
            log.error("ftp获取文件列表异常 ", e);
            return new ArrayList<>();
        } finally {
            release();
        }
    }

    /**
     * 文件读取
     *
     * @param filePath 文件路径
     * @param fileName 文件名称
     * @return java.util.List<java.lang.String>
     */
    public List<String> readFile(String filePath, String fileName) {
        log.info("ftp读取文件... {}", fileName);
        try {
            connect();
            FtpUtils ftpUtils = new FtpUtils(FTP_THREAD_LOCAL.get());
            filePath = "." + filePath + "/" + fileName;
            return ftpUtils.readFileLines(filePath);
        } catch (Exception e) {
            log.error("ftp读取文件异常 ", e);
            return new ArrayList<>();
        } finally {
            release();
        }
    }

    /**
     * 文件流读取
     *
     * @param filePath 文件路径
     * @param fileName 文件名称
     * @return java.io.InputStream
     */
    public InputStream readFileInputStream(String filePath, String fileName) {
        log.info("ftp文件流读取... {}", fileName);
        try {
            connect();
            FtpUtils ftpUtils = new FtpUtils(FTP_THREAD_LOCAL.get());
            filePath = "." + filePath + "/" + fileName;
            return ftpUtils.readFileInputStream(filePath);
        } catch (Exception e) {
            log.error("ftp读取文件异常 ", e);
            return null;
        } finally {
            release();
        }
    }

}
