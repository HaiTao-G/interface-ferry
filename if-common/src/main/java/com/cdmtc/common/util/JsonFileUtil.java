package com.cdmtc.common.util;

import com.alibaba.fastjson.JSON;
import com.cdmtc.entity.Instruction;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

/**
 * json指令文件生成与读取工具类
 */
public class JsonFileUtil {

    /**
     * .json指令文件转成Instruction对象
     * @param file
     * @return
     * @throws IOException
     */
    private static Instruction jacksonMethod(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(file, Instruction.class);
    }

    /**
     * 生成.json格式文件
     */
    public static boolean createJsonFile(String jsonString, String filePath, String fileName) {
        // 标记文件生成是否成功
        boolean flag = true;

        // 拼接文件完整路径
        String fullPath = filePath + File.separator + fileName + ".json";

        // 生成json格式文件
        try {
            // 保证创建一个新文件
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();

            // 格式化json字符串
            jsonString = JsonFormatToolUtil.formatJson(jsonString);

            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(jsonString);
            write.flush();
            write.close();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }

        // 返回是否成功的标记
        return flag;
    }

    /**
     * 生成.json格式文件输入流
     * @param jsonString
     * @return
     */
    public static InputStream createJsonInputStream(String jsonString) {
        InputStream JsonInputStream = null;
        // 生成json格式文件输入流
        try {
            // 格式化json字符串
            jsonString = JsonFormatToolUtil.formatJson(jsonString);
            // 将格式化后的字符串写入输入流
            JsonInputStream = new ByteArrayInputStream(jsonString.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonInputStream;
    }

    public static InputStream createJsonInputStream(Object data) {
        return  createJsonInputStream(JSON.toJSONString(data));
    }

}
