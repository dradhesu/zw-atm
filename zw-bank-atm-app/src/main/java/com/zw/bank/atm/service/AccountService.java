package com.zw.bank.atm.service;

import com.zw.bank.atm.domain.Account;
import com.zw.bank.atm.domain.AccountResponse;
import com.zw.bank.atm.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountResponse getBalance(String accountNumber) {
        Optional<Account> accountDetails = accountRepository.findById(accountNumber);
        if (accountDetails.isPresent())
            return AccountResponse.builder()
                    .accountId(accountNumber)
                    .balance(accountDetails.get().getOpeningBalance() + " Rupees")
                    .build();
        return AccountResponse.builder()
                .balance(" Account Not Found ")
                .build();
    }
}
