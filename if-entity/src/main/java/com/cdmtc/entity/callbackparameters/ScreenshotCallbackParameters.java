package com.cdmtc.entity.callbackparameters;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 外网截图服务回调参数
 */
@NoArgsConstructor
@Data
public class ScreenshotCallbackParameters {
    /**
     * 数据库主键id
     */
    private int id;

    private String serviceId;
}
