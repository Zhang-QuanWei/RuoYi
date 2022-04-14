package com.ruoyi.common.enums.spider;

public enum BookStatus {
    IN_SERIAL("0","连载中"),
    CLOSED("1","已完结");


    private final String code;
    private final String info;

    BookStatus(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
