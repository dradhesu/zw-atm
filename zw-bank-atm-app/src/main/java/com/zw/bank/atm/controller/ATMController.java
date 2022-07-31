package com.zw.bank.atm.controller;

import com.zw.bank.atm.domain.Money;
import com.zw.bank.atm.domain.WithdrawalRequest;
import com.zw.bank.atm.domain.WithdrawalResponse;
import com.zw.bank.atm.service.ATMService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zw/atm")
@RequiredArgsConstructor
public class ATMController {

    private final ATMService atmService;

    @PostMapping(path = "/initialise")
    public String initialiseAtm(@RequestBody Money money) {
        return atmService.load(money);
    }

    @PutMapping(path = "/withdraw")
    public WithdrawalResponse withdrawal(@RequestBody WithdrawalRequest withdrawalRequest) {
        return atmService.withdrawal(withdrawalRequest);
    }
}
