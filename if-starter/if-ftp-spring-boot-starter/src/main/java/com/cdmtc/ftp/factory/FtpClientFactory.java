package com.cdmtc.ftp.factory;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ftp.AbstractFtp;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpMode;
import cn.hutool.extra.ssh.Sftp;
import com.cdmtc.ftp.config.FtpConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.util.StringUtils;

import java.io.InvalidObjectException;
import java.lang.annotation.ElementType;
import java.nio.charset.Charset;

@Slf4j
public class FtpClientFactory implements PooledObjectFactory<AbstractFtp> {
    private final FtpConfig ftpConfig;

    public FtpClientFactory(FtpConfig ftpConfig) {
        this.ftpConfig = ftpConfig;
    }

    /**
     * 创建一个对象实例，并将其包装成一个PooledObject
     * @return
     */
    @Override
    public PooledObject<AbstractFtp> makeObject() {
        String type = ftpConfig.getType();
        String host = ftpConfig.getHost();
        Integer port = ftpConfig.getPort();
        String username = ftpConfig.getUsername();
        String password = ftpConfig.getPassword();
        String encoding = ftpConfig.getEncoding();
        Boolean passiveMode = ftpConfig.getPassiveMode();

        FtpMode model = passiveMode ? FtpMode.Passive : FtpMode.Active;
        Charset charset = null;
        if (StringUtils.hasText(encoding)) {
            charset = Charset.forName(encoding);
        }

        AbstractFtp ftp = null;
        try {
            switch (type) {
                case "ftp":
                    ftp = new Ftp(host, port, username, password, charset, null, null, model);
                    break;
                case "sftp":
                    ftp = new Sftp(host, port, username, password, charset);
                    break;
                default:
                    throw new IllegalArgumentException("Illegal type of ftp");
            }
            log.info("ftp connected {}:{}", host, port);
        } catch (Exception e) {
            log.error("ftp connect failed {}:{} ", host, port, e);
        }
        DefaultPooledObject<AbstractFtp> abstractFtpDefaultPooledObject = new DefaultPooledObject<>(ftp);
        log.info("创建对象成功!");
        return abstractFtpDefaultPooledObject;
    }

    /**
     * 销毁对象
     * 对象要被销毁时(validateObject方法返回false或者超时)后被调用
     * @param pooledObject a {@code PooledObject} wrapping the instance to be destroyed
     *
     */
    @Override
    public void destroyObject(PooledObject<AbstractFtp> pooledObject) {
        try {
            AbstractFtp ftp = pooledObject.getObject();
            if (ftp != null) {
                ftp.close();
            }
            log.info("ftp conn destroyed");
        } catch (Exception e) {
            log.error("ftp conn destroy failed ", e);
        }
    }

    /**
     * 校验对象，确保对象池返回的对象是OK的
     * 每次获取对象和还回对象时会被调用，如果返回false会销毁对象
     * @param pooledObject a {@code PooledObject} wrapping the instance to be validated
     *
     * @return
     */
    @Override
    public boolean validateObject(PooledObject<AbstractFtp> pooledObject) {
        try {
            String pwd = null;
            AbstractFtp ftp = pooledObject.getObject();
            if (ftp == null) {
                throw new InvalidObjectException("pooled object is null");
            }
            pwd = ftp.pwd();
            if (StrUtil.isNotBlank(pwd)){
                log.info("ftp conn validated");
            }else {
                log.error("ftp链接超时!!!");
                return false;
            }
        } catch (Exception e) {
            log.error("ftp conn validate failed ", e);
            return false;
        }
        return true;
    }

    /**
     * 重新初始化对象
     * 调用获取对象方法前被调用
     * 此方法一般进行一些前置操作
     * @param pooledObject a {@code PooledObject} wrapping the instance to be activated
     *
     */
    @Override
    public void activateObject(PooledObject<AbstractFtp> pooledObject) {
        log.info("激活对象!!!");
        String basePath = ftpConfig.getBasePath();
        AbstractFtp ftp = pooledObject.getObject();
        ftp.cd(basePath);
    }

    /**
     * 取消初始化对象。GenriObjectPool 的 addIdIeObject、returnObject、evict调用该方法
     * 当还回对象并且validateObject方法返回true后被调用
     * 一般在此方法中对刚刚使用完成的对象进行重置
     * @param pooledObject a {@code PooledObject} wrapping the instance to be passivated
     *
     */
    @Override
    public void passivateObject(PooledObject<AbstractFtp> pooledObject) {
        log.info("钝化对象!!!");
        // do something before return
    }
}
