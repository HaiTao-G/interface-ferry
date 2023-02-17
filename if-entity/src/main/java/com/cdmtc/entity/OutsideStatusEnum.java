package com.cdmtc.entity;

/**
 * @ClassName OutsideStatusEnum
 * @Description TODO
 * @Author Tao-pc
 * @Date 2022/8/31 14:36
 */
public enum OutsideStatusEnum {

    NOT_REQUESTED("未请求",0),
    SUCCESS_REQUESTED("请求成功",1),
    TIMED_OUT_REQUESTED("请求超时",2),
    FALSE_REQUESTED("请求失败",2);

    private String name;

    private int code;

    OutsideStatusEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }
}
