package com.zw.bank.atm.enums;

public enum Denomination {
    FIVE(5),
    TEN(10),
    TWENTY(20),
    FIFTY(50);

    private int value;

    Denomination(int amount) {
        value = amount;
    }

    public int value() {
        return value;
    }
}
