package com.cdmtc.screenshot.consumer.service;

import com.cdmtc.screenshot.consumer.config.ChromeDriverRemoteConfig;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * @author yxl
 * @date 2020/3/12
 * @description 截图服务类
 */
@Slf4j
@Service
@EnableConfigurationProperties(ChromeDriverRemoteConfig.class)
public class ScreenShotService {

    @Autowired
    private ChromeDriverRemoteConfig chromeDriverRemoteConfig;

    /**
     * @return 截图文件
     */
    public InputStream screenShotToFile(String url) {
        InputStream inputStreamScreenShot = null;
        //设置ChromeOptions打开方式，设置headless：不弹出浏览器
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
        WebDriver driver = null;
        try {
            driver = new RemoteWebDriver(new URL(chromeDriverRemoteConfig.remoteUrl), options);
        } catch (MalformedURLException e) {
            log.error("Selenium Grid 地址有误!请求失败!,{}", e);
            return inputStreamScreenShot;
        }
        try {
            driver.manage().timeouts().setScriptTimeout(Duration.ofSeconds(chromeDriverRemoteConfig.pageLoadTimeout));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(chromeDriverRemoteConfig.pageLoadTimeout));
            log.info("截图url:{}", url);
            driver.get(url);
            waitForLoad(driver);
            Long width = (Long) ((JavascriptExecutor) driver).executeScript("return document.documentElement.scrollWidth");
            Long height = (Long) ((JavascriptExecutor) driver).executeScript("return document.documentElement.scrollHeight");
            //设置浏览器弹窗页面的大小
            driver.manage().window().setSize(new Dimension(width.intValue(), height.intValue()));
            byte[] screenShotByte = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            inputStreamScreenShot = new ByteArrayInputStream(screenShotByte);
        } catch (Exception e) {
            log.error("浏览器页面加载时间过长!失败原因:{}", e);
        } finally {
            driver.quit();
        }
        return inputStreamScreenShot;
    }

    /**
     * 页面加载最大时间等待
     *
     * @param driver
     */
    private void waitForLoad(WebDriver driver) {
        new WebDriverWait(driver, Duration.ofSeconds(chromeDriverRemoteConfig.webWaitTimeout)).until((ExpectedCondition<Boolean>) wd ->
                ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
    }
}
