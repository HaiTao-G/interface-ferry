package com.cdmtc.ftp.service;

import com.cdmtc.ftp.config.FtpConfig;
import com.cdmtc.ftp.factory.FtpClientFactory;
import com.cdmtc.ftp.pool.FtpClientPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class FtpService {
    private FtpClientPool pool;

    public FtpService(FtpConfig properties) throws Exception {
        FtpClientPool pool = new FtpClientPool(new FtpClientFactory(properties));
        this.pool = pool;
    }

    /**
     * 上传文件
     * @param fileName ---文件名称
     * @param inputStream ---文件路
     */
    public void uploadFile(String fileName, InputStream inputStream) {
        FTPClient ftpClient = null;
        try {
            ftpClient = pool.borrowObject();
            boolean result = ftpClient.storeFile(fileName, inputStream);
            log.info("文件是否保存成功：" + result);
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ftpClient.completePendingCommand();
                if (ftpClient != null) {
                    pool.returnObject(ftpClient);
                }
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下载文件
     */
    public void download(String remote, String local) {
        FTPClient ftpClient = null;
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            ftpClient = pool.borrowObject();
            File file = new File(local);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fileOutputStream = new FileOutputStream(file);
            inputStream = ftpClient.retrieveFileStream(remote);
            int size;
            byte[] bytes = new byte[ftpClient.getBufferSize()];
            while ((size = inputStream.read(bytes)) > 0) {
                fileOutputStream.write(bytes, 0, size);
                fileOutputStream.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ftpClient.completePendingCommand();
                if (ftpClient != null) {
                    pool.returnObject(ftpClient);
                }
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
