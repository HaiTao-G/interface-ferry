package com.cdmtc.screenshottooldemo.service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IndexService
 * @Description TODO
 * @Author Tao-pc
 * @Date 2022/10/20 16:27
 */
public interface IndexService {

    /**
     * 数据表格截图
     * @param readAll
     */
    void fileUpload(List<Map<String, Object>> readAll,String fileName);
}
