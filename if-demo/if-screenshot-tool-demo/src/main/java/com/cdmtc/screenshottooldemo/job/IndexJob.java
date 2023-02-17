package com.cdmtc.screenshottooldemo.job;

import cn.hutool.core.collection.CollectionUtil;
import com.cdmtc.screenshottooldemo.entity.WordEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @ClassName IndexJob
 * @Description TODO
 * @Author Tao-pc
 * @Date 2022/10/20 17:06
 */
@Slf4j
@Component
public class IndexJob {

    public static final BlockingQueue<Integer> blockingQueue = new LinkedBlockingDeque<>(4);

    @Async("taskExecutor")
    public Future<WordEntity> insideCdlMethod(Map<String, Object> read) {
        WordEntity wordEntity = new WordEntity();
        String keyword = read.get("违规关键词").toString();
        String url = read.get("url").toString();
        String title = read.get("标题").toString();
        String siteName = read.get("站点名称").toString();
        try {
            if (StringUtils.isNoneBlank(url)) {
                wordEntity.setInputStreamScreenShots(textJs(url, keyword));
            } else {
                return new AsyncResult(null);
            }
        } catch (Exception e) {

        }
        wordEntity.setUrl(url);
        wordEntity.setTitle(title);
        wordEntity.setKeywords(keyword.split(","));
        wordEntity.setSiteName(siteName);
        try {
            blockingQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new AsyncResult(wordEntity);
    }

    public InputStream[] textJs(String url, String keyword) throws Exception {
        String[] splitKeyword = keyword.split(",");
        InputStream[] inputStreamScreenShots = new InputStream[splitKeyword.length];
        //设置ChromeOptions打开方式，设置headless：不弹出浏览器
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
        WebDriver driver = null;
        try {
            driver = new RemoteWebDriver(new URL("http://10.51.225.93:4444/wd/hub"), options);
//            driver = new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub"), options);
        } catch (MalformedURLException e) {
            log.error("Selenium Grid 地址有误!请求失败!,{}", e);
        }
        try {
            driver.manage().timeouts().setScriptTimeout(Duration.ofSeconds(30));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            driver.get(url);
            waitForLoad(driver);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            for (int i = 0; i < splitKeyword.length; i++) {
                List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(),'" + splitKeyword[i] + "')]"));
                if (CollectionUtil.isNotEmpty(elements)){
                    WebElement firstElement = elements.get(elements.size() - 1);
                    String text = firstElement.getText();
                    if (StringUtils.isNoneBlank(text)) {
                        text = text.replace(splitKeyword[i], "<div style=\"border: 3px solid #F00; display: inline-block\">" + splitKeyword[i] + "</div>");
                        js.executeScript("arguments[0].innerHTML=" + "'" + text + "'", firstElement);
                    }
                    js.executeScript("arguments[0].scrollIntoView(false);", firstElement);
                    byte[] screenShotByte = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    inputStreamScreenShots[i] = new ByteArrayInputStream(screenShotByte);
                }
            }
        } catch (Exception e) {
            log.error("浏览器页面加载时间过长!失败原因:{}", e);
        } finally {
            driver.quit();
        }
        return inputStreamScreenShots;
    }

    private void waitForLoad(WebDriver driver) {
        new WebDriverWait(driver, Duration.ofSeconds(30)).until((ExpectedCondition<Boolean>) wd ->
                ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
    }
}
