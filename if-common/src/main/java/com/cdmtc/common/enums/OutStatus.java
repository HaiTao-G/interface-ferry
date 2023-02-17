package com.cdmtc.common.enums;


public enum OutStatus {

    SUCCESS("成功",1),FALSE("失败",2);

    private String name;

    private int code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    OutStatus(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public static String getName(int code) {
        for (OutStatus c : OutStatus.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return null;
    }

    public static int getCode(String name) {
        for (OutStatus c : OutStatus.values()) {
            if (c.getName().equals(name)) {
                return c.code;
            }
        }
        return -1;
    }
}
