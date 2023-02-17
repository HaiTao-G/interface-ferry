package com.cdmtc.entity.callbackparameters;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName ReportdelCallbackParameters
 * @Description TODO
 * @Author Tao-pc
 * @Date 2022/11/2 10:42
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ReportdelCallbackParameters implements Serializable {

    private String caseId;

}
