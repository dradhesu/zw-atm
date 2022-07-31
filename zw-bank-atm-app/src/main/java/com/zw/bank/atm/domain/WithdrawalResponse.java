package com.zw.bank.atm.domain;


import com.zw.bank.atm.enums.Denomination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalResponse {
    private String errorMessages;
    private Map<Denomination, Integer> denominationMap;
    private String remainingBalance;
}
