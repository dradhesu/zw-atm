package com.zw.bank.atm.service;

import com.zw.bank.atm.domain.*;
import com.zw.bank.atm.enums.Denomination;
import com.zw.bank.atm.repository.AccountRepository;
import com.zw.bank.atm.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ATMService {

    private final UserService userService;
    private final AccountRepository accountRepository;
    private final NoteRepository noteRepository;

    private void init() {
        Account account1 = Account.builder()
                .accountNumber("123456789")
                .openingBalance(800l)
                .overdraft(200l)
                .build();
        Account account2 = Account.builder()
                .accountNumber("987654321")
                .openingBalance(1230l)
                .overdraft(150l)
                .build();
        accountRepository.saveAll(List.of(account1, account2));
    }

    public String load(Money money) {
        //Load init account data
        init();
        userService.init();
        //load atm specific amount
        Note fifties = Note.builder()
                .id(50)
                .type(Denomination.FIFTY)
                .totalNotes(money.getFifties())
                .build();
        Note twenties = Note.builder()
                .id(20)
                .type(Denomination.TWENTY)
                .totalNotes(money.getTwenties())
                .build();
        Note tens = Note.builder()
                .id(10)
                .type(Denomination.TEN)
                .totalNotes(money.getTens())
                .build();
        Note fives = Note.builder()
                .id(5)
                .type(Denomination.FIVE)
                .totalNotes(money.getFives())
                .build();
        List<Note> notes = List.of(fives, tens, twenties, fifties);
        noteRepository.saveAll(notes);
        return "done";
    }

    public void updateATMCash(Map<Denomination, Integer> notes) {
        Set<Denomination> denominations = notes.keySet();
        for (Denomination denomination : denominations) {
            Note noteByType = noteRepository.findByType(denomination);
            if (noteByType != null) {
                int totalNotes = noteByType.getTotalNotes();
                int newTotalNoteCount = totalNotes - notes.get(denomination);
                noteByType.setTotalNotes(newTotalNoteCount);
                noteRepository.save(noteByType);
            }
        }
    }

    public String validateATMCash(int amount, Map<Denomination, Integer> notes) {
        Set<Denomination> denominations = notes.keySet();
        Set<Denomination> inSufficientDenominations = new HashSet<>();
        for (Denomination denomination : denominations) {
            Note noteByIdy = noteRepository.findByType(denomination);
            if (noteByIdy.getTotalNotes() < notes.get(denomination)) {
                inSufficientDenominations.add(denomination);
            }
        }
        for (Denomination denomination : inSufficientDenominations) {
            notes.remove(denomination);
        }
        if (!inSufficientDenominations.isEmpty()) {
            Map<Denomination, Integer> newDenominationMap = noteDispense(amount, notes.keySet().toArray(new Denomination[0]));
            if (!newDenominationMap.isEmpty()) {
                validateATMCash(amount, newDenominationMap);
            }
        }
        if (inSufficientDenominations.isEmpty()) {
            return "success";
        }
        return "in sufficient funds in ATM";
    }

    public Map<Denomination, Integer> noteDispense(int amount, Denomination[] denominations) {
        Map<Denomination, Integer> notes = new HashMap<>();
        if (isMultiples(amount)) {
            for (int i = denominations.length - 1; i >= 0; i--) {
                if (amount > 0) {
                    notes.put(denominations[i], (amount / denominations[i].value()));
                    amount = amount - denominations[i].value() * (amount / denominations[i].value());
                }
            }
        } else {
            // throw Exception;
        }
        return notes;
    }

    private boolean isMultiples(int amount, int value) {
        return ((amount % value) == 0) ? true : false;
    }

    public boolean isMultiples(int amount) {
        boolean status = false;
        Denomination[] denominations = Denomination.values();
        for (int i = 0; i < denominations.length; i++) {
            if (!status) {
                status = isMultiples(amount, denominations[i].value());
            }
        }
        return status;
    }


    public WithdrawalResponse withdrawal(WithdrawalRequest withdrawalRequest) {
        //Validate user credentials
        boolean status = userService.validate(User.builder()
                .accountNumber(withdrawalRequest.getAccountNumber())
                .pin(withdrawalRequest.getPin())
                .build());

        //ge account details
        Optional<Account> accountById;
        if (status) {
            status = Boolean.FALSE;
            accountById = accountRepository.findById(withdrawalRequest.getAccountNumber());
            if (accountById.isPresent() &&
                    (accountById.get().getOpeningBalance() >= withdrawalRequest.getWithdrawalAmount())) {
                //Split this into two invalid accountId and insufficient balance
                status = Boolean.TRUE;
            }
            if (!status) {
                return WithdrawalResponse.builder()
                        .errorMessages("In sufficient funds in your account")
                        .build();
            }
        } else {
            return WithdrawalResponse.builder()
                    .errorMessages("in valid user")
                    .build();
        }
        Map<Denomination, Integer> denominationMap = noteDispense(withdrawalRequest.getWithdrawalAmount(), Denomination.values());
        String finalState = validateATMCash(withdrawalRequest.getWithdrawalAmount(), denominationMap);
        String accountBalance = "";
        if ("success".equals(finalState)) {
            updateATMCash(denominationMap);
            Account account = accountById.get();
            account.setOpeningBalance(account.getOpeningBalance() - withdrawalRequest.getWithdrawalAmount());
            accountBalance = account.getOpeningBalance() + " Rupees";
            accountRepository.save(account);
        }
        return WithdrawalResponse.builder()
                .denominationMap(denominationMap)
                .remainingBalance(accountBalance)
                .errorMessages(finalState)
                .build();

    }
}
