package com.zw.bank.atm.controller;

import com.zw.bank.atm.domain.AccountRequest;
import com.zw.bank.atm.domain.AccountResponse;
import com.zw.bank.atm.domain.User;
import com.zw.bank.atm.service.AccountService;
import com.zw.bank.atm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zw/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;

    @GetMapping(path = "/account/{id}")
    public AccountResponse getBalance(@PathVariable(name = "id") String id) {
        return accountService.getBalance(id);
    }

    @PostMapping(path = "/account/balance")
    public AccountResponse getBalance(@RequestBody AccountRequest accountRequest) {
        if (userService.validate(User.builder()
                .accountNumber(accountRequest.getAccountId())
                .pin(accountRequest.getPin())
                .build())) {
            return accountService.getBalance(accountRequest.getAccountId());
        } else {
            return AccountResponse.builder()
                    .error("InValid User")
                    .build();
        }
    }
}
