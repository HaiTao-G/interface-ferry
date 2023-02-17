package com.cdmtc.screenshottooldemo.entity;

import cn.hutool.core.io.file.FileReader;
import lombok.Data;

import java.io.InputStream;
import java.util.List;

/**
 * @ClassName WordEntity
 * @Description TODO
 * @Author Tao-pc
 * @Date 2022/10/20 17:11
 */
@Data
public class WordEntity {
    /**
     * 站点名称
     */
    private String siteName;
    /**
     * 标题
     */
    private String title;
    /**
     * 链接
     */
    private String url;
    /**
     * 图片流
     */
    private InputStream[] inputStreamScreenShots;
    /**
     * 违规关键词
     */
    private String[] keywords;

    public static void main(String[] args) {
        String s = "INSERT INTO `territory_inspect`.`tb_100340_words_library_news` (`words_type`, `words_text`, `category`, `create_user_id`, `create_user_name`, `modify_user_id`, `modify_user_name`, `create_time`, `update_time`) " +
                "VALUES (6, '%s', 1, NULL, NULL, NULL, NULL, '2022-10-27 10:30:00', '2022-10-27 10:30:00');";
        FileReader fileReader = new FileReader("C:\\Users\\Tao-pc\\Desktop\\correct_phrases.txt");
        List<String> strings = fileReader.readLines();
        strings.stream().forEach(e->{
            String format = String.format(s, e);
            System.out.println(format);
        });
    }
}
