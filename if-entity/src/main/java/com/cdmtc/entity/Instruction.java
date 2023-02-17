package com.cdmtc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 指令json数据结构
 * {
 *     //一次请求唯一标识id,同时也是zk节点的名称,暂时使用uuid填充
 *     "requestId" : "",
 *     "orderId": 0,
 *     "userId" : 0,
 *     //每种接口摆渡的topic队列名称
 *     "topicName" : "",
 *     //回调参数,每种接口摆渡任务的回调参数不同,根据业务场景填充
 *     "callback_parameters":{
 *         "xxxx":"xxxx"
 *     },
 *     //请求参数,每种接口摆渡任务的请求参数不同,根据业务场景填充
 *     "parameters":{
 *         //列入截图服务仅需要一个外网下载截图的url地址
 *         "url":"",
 *         "xxx":"xxx"
 *     },
 *     //外网请求执行状态（0:未请求,1:请求成功,2:请求超时,3请求错误)
 *     "outsideStatus":0,
 *     //外网请求成功信息,每种接口摆渡任务的成功信息不同,根据业务场景填充
 *     "result":{
 *         "xxx":"xxx"
 *     },
 *     //外网请求错误信息
 *     "err_msg":"",
 *     //外网附件列表名称
 *     "outsideFileName":["xxx","xxx"],
 *     //内网文件服务器列表名称
 *     "withinFiLeName":["xxxx","xxx"],
 *     //创建请求json数据的时间
 *     "createTime":"",
 *     //修改请求json数据的时间
 *     "updateTime":""
 * }
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Instruction<T1,T2,T3> implements Serializable {
    @JsonProperty("requestId")
    private String requestId;
    @JsonProperty("orderId")
    private Integer orderId;
    @JsonProperty("userId")
    private Integer userId;
    @JsonProperty("topicName")
    private String topicName;
    @JsonProperty("callbackParameters")
    private T1 callbackParameters;
    @JsonProperty("parameters")
    private T2 parameters;
    @JsonProperty("outsideStatus")
    private Integer outsideStatus;
    @JsonProperty("result")
    private T3 result;
    @JsonProperty("errMsg")
    private String errMsg;
    @JsonProperty("outsideFileName")
    private List<String> outsideFileName;
    @JsonProperty("withinFiLeName")
    private List<String> withinFiLeName;
    @JsonProperty("createTime")
    private String createTime;
    @JsonProperty("updateTime")
    private String updateTime;
}
