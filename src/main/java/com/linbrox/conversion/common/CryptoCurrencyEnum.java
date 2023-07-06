package com.linbrox.conversion.common;

public enum CryptoCurrencyEnum {
    BTC(90),
    ETH(80);

    private final int id;
    CryptoCurrencyEnum(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
}
