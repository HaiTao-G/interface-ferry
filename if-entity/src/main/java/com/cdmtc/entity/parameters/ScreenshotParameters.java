package com.cdmtc.entity.parameters;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 外网截图服务请求参数
 */
@NoArgsConstructor
@Data
public class ScreenshotParameters {

    /**
     * 外网截图url链接
     */
    private String url;

}
