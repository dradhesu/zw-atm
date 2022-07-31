package com.zw.bank.atm.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalRequest {
    private String accountNumber;
    private int pin;
    private int withdrawalAmount;
}
