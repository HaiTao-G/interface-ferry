package com.cdmtc.ftp.pool;

import cn.hutool.extra.ftp.AbstractFtp;
import com.cdmtc.ftp.factory.FtpClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

@Slf4j
public class FtpClientPool extends GenericObjectPool<AbstractFtp> {
    public FtpClientPool(FtpClientFactory ftpPooledObjectFactory) {
        super(ftpPooledObjectFactory);
    }

    public FtpClientPool(FtpClientFactory ftpPooledObjectFactory, GenericObjectPoolConfig<AbstractFtp> genericObjectPoolConfig) {
        super(ftpPooledObjectFactory, genericObjectPoolConfig);
    }
}
