package com.linbrox.conversion.common;

public enum HyundaiModelEnum {
    SANTAFE(1023),
    TUCSON(1031),
    ACCENT(1036),
    GRANDi10(1038);
    private final int modelNumber;
    HyundaiModelEnum(int modelNumber) {
        this.modelNumber = modelNumber;
    }
    public int getModelNumber() {
        return modelNumber;
    }
}
