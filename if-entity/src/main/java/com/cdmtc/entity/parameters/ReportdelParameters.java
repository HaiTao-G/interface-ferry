package com.cdmtc.entity.parameters;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @ClassName ReportdelParameters
 * @Description TODO
 * @Author Tao-pc
 * @Date 2022/11/2 10:42
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ReportdelParameters {

    private String url;

    private String params;
}
