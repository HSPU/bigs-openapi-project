package com.example.bigsopenapiproject.domain.entity;

public enum CategoryCode {
    POP("강수확률", "%"),
    PTY("강수형태", ""),
    PCP("1시간 강수량", "mm"),
    REH("습도", "%"),
    SNO("1시간 신적설", "cm"),
    SKY("하늘상태", ""),
    TMP("1시간 기온", "℃"),
    TMN("아침 최저기온", "℃"),
    TMX("낮 최고기운", "℃"),
    UUU("풍속(동서성분)", "m/s"),
    VVV("풍속(남북성분)", "m/s"),
    WAV("파고", "M"),
    VEC("풍향", "deg"),
    WSD("풍속", "m/s"),
    ETC("기타", "");

    private final String name;
    private final String unit;

    CategoryCode(String name, String unit) {
        this.name = name;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }
}