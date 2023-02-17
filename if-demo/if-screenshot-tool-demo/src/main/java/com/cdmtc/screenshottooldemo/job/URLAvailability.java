package com.cdmtc.screenshottooldemo.job;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @ClassName URLAvailability
 * @Description TODO
 * @Author Tao-pc
 * @Date 2022/10/27 15:04
 */
@Slf4j
public class URLAvailability {
    private static URL url;
    private static HttpURLConnection con;
    private static int state = -1;

    /**
     * 功能：检测当前URL是否可连接或是否有效,
     * 描述：最多连接网络 5 次, 如果 5 次都不成功，视为该地址不可用
     *
     * @param urlStr 指定URL网络地址
     * @return URL
     */
    public synchronized boolean isConnect(String urlStr) {
        boolean available = false;
        if (StrUtil.isNotBlank(urlStr)) {
            return false;
        }
        try {
            url = new URL(urlStr);
            con = (HttpURLConnection) url.openConnection();
            state = con.getResponseCode();
            if (state == 200) {
                log.info("URL{}可用！", urlStr);
                available = true;
            }
        } catch (Exception ex) {
            log.error("URL{}不可用！", urlStr);
            available = false;
        }
        return available;
    }
}
