package com.zw.bank.atm.exceptions;

public class InSufficientFunds extends Exception {
    public InSufficientFunds(String message) {
        super(message);
    }
}
