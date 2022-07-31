package com.zw.bank.atm.service;

import com.zw.bank.atm.domain.*;
import com.zw.bank.atm.enums.Denomination;
import com.zw.bank.atm.repository.AccountRepository;
import com.zw.bank.atm.repository.NoteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ATMServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private NoteRepository noteRepository;
    private ATMService atmService;

    @BeforeEach
    public void init() {
        atmService = new ATMService(userService, accountRepository, noteRepository);
    }

    @Test
    void testLoad() {
        Mockito.doAnswer(invocation -> invocation.getArguments()[0]).when(noteRepository).saveAll(any());
        String loadResponse = atmService.load(Money.builder()
                .fifties(10)
                .twenties(20)
                .tens(10)
                .fives(10)
                .build());
        Assertions.assertEquals("done", loadResponse);
    }

    @Test
    void testNoteDispenseSuccess() {
        Map<Denomination, Integer> denominationIntegerMap = atmService.noteDispense(175, Denomination.values());
        AtomicInteger totalAmount = new AtomicInteger();
        denominationIntegerMap.keySet().forEach(
                denomination -> {
                    totalAmount.set(totalAmount.get() + (denomination.value() * denominationIntegerMap.get(denomination)));
                }
        );
        Assertions.assertEquals(175, totalAmount.get());
    }

    @Test
    void testNoteDispenseFail() {
        Map<Denomination, Integer> denominationIntegerMap = atmService.noteDispense(177, Denomination.values());
        AtomicInteger totalAmount = new AtomicInteger();
        denominationIntegerMap.keySet().forEach(
                denomination -> {
                    totalAmount.set(totalAmount.get() + (denomination.value() * denominationIntegerMap.get(denomination)));
                }
        );
        Assertions.assertNotEquals(177, totalAmount.get());
    }

    @Test
    void testWithdrawalSuccess() {
        Optional<Account> account = Optional.of(
                Account.builder()
                        .accountNumber("123456789")
                        .overdraft(200l)
                        .openingBalance(500l)
                        .build()

        );
        Note note50 = Note.builder()
                .type(Denomination.FIFTY)
                .id(50)
                .totalNotes(10)
                .build();
        Note note20 = Note.builder()
                .type(Denomination.TWENTY)
                .id(20)
                .totalNotes(30)
                .build();
        Note note10 = Note.builder()
                .type(Denomination.TEN)
                .id(10)
                .totalNotes(10)
                .build();
        Note note5 = Note.builder()
                .type(Denomination.FIVE)
                .id(5)
                .totalNotes(10)
                .build();
        Mockito.when(userService.validate(any())).thenReturn(true);
        Mockito.when(accountRepository.findById(any())).thenReturn(account);
        Mockito.when(noteRepository.findByType(Denomination.FIFTY)).thenReturn(note50);
        Mockito.when(noteRepository.findByType(Denomination.TWENTY)).thenReturn(note20);
        Mockito.when(noteRepository.findByType(Denomination.TEN)).thenReturn(note10);
        Mockito.when(noteRepository.findByType(Denomination.FIVE)).thenReturn(note5);
        WithdrawalRequest withdrawalRequest = WithdrawalRequest.builder()
                .accountNumber("123456789")
                .pin(1234)
                .withdrawalAmount(215)
                .build();
        WithdrawalResponse withdrawalResponse = atmService.withdrawal(withdrawalRequest);
        Assertions.assertEquals("success", withdrawalResponse.getErrorMessages());
    }

    @Test
    void testWithdrawalFailDueToInsufficientFundsInAccount() {
        Optional<Account> account = Optional.of(
                Account.builder()
                        .accountNumber("123456789")
                        .overdraft(200l)
                        .openingBalance(500l)
                        .build()

        );
        Mockito.when(userService.validate(any())).thenReturn(true);
        Mockito.when(accountRepository.findById(any())).thenReturn(account);
        WithdrawalRequest withdrawalRequest = WithdrawalRequest.builder()
                .accountNumber("123456789")
                .pin(1234)
                .withdrawalAmount(515)
                .build();
        WithdrawalResponse withdrawalResponse = atmService.withdrawal(withdrawalRequest);
        Assertions.assertEquals("In sufficient funds in your account", withdrawalResponse.getErrorMessages());
    }

    @Test
    void testWithdrawalFailDueToInsufficientFundsInATM() {
        Optional<Account> account = Optional.of(
                Account.builder()
                        .accountNumber("123456789")
                        .overdraft(200l)
                        .openingBalance(500l)
                        .build()

        );
        Note note50 = Note.builder()
                .type(Denomination.FIFTY)
                .id(50)
                .totalNotes(1)
                .build();
        Mockito.when(userService.validate(any())).thenReturn(true);
        Mockito.when(accountRepository.findById(any())).thenReturn(account);
        Mockito.when(noteRepository.findByType(Denomination.FIFTY)).thenReturn(note50);
        WithdrawalRequest withdrawalRequest = WithdrawalRequest.builder()
                .accountNumber("123456789")
                .pin(1234)
                .withdrawalAmount(300)
                .build();
        WithdrawalResponse withdrawalResponse = atmService.withdrawal(withdrawalRequest);
        Assertions.assertEquals("in sufficient funds in ATM", withdrawalResponse.getErrorMessages());
    }

    @Test
    void testValidateATMCashSuccess() {
        Note note50 = Note.builder()
                .type(Denomination.FIFTY)
                .id(50)
                .totalNotes(4)
                .build();
        Note note20 = Note.builder()
                .type(Denomination.TWENTY)
                .id(20)
                .totalNotes(0)
                .build();
        Note note10 = Note.builder()
                .type(Denomination.TEN)
                .id(1)
                .totalNotes(2)
                .build();
        Note note5 = Note.builder()
                .type(Denomination.FIVE)
                .id(5)
                .totalNotes(2)
                .build();

        Map<Denomination, Integer> denominationIntegerMap = new HashMap<>();
        denominationIntegerMap.put(Denomination.FIFTY, 0);
        denominationIntegerMap.put(Denomination.TWENTY, 0);
        denominationIntegerMap.put(Denomination.TEN, 2);
        denominationIntegerMap.put(Denomination.FIVE, 0);
        Mockito.when(noteRepository.findByType(Denomination.FIFTY)).thenReturn(note50);
        Mockito.when(noteRepository.findByType(Denomination.TWENTY)).thenReturn(note20);
        Mockito.when(noteRepository.findByType(Denomination.TEN)).thenReturn(note10);
        Mockito.when(noteRepository.findByType(Denomination.FIVE)).thenReturn(note5);
        String validateATMCash = atmService.validateATMCash(215, denominationIntegerMap);
        Assertions.assertEquals("success", validateATMCash);
    }

    @Test
    void testValidateATMCashInSufficientFundsInATM() {
        Note note50 = Note.builder()
                .type(Denomination.FIFTY)
                .id(50)
                .totalNotes(3)
                .build();
        Note note20 = Note.builder()
                .type(Denomination.TWENTY)
                .id(20)
                .totalNotes(0)
                .build();
        Note note10 = Note.builder()
                .type(Denomination.TEN)
                .id(1)
                .totalNotes(2)
                .build();
        Note note5 = Note.builder()
                .type(Denomination.FIVE)
                .id(5)
                .totalNotes(0)
                .build();

        Map<Denomination, Integer> denominationIntegerMap = new HashMap<>();
        denominationIntegerMap.put(Denomination.FIFTY, 4);
        denominationIntegerMap.put(Denomination.TWENTY, 0);
        denominationIntegerMap.put(Denomination.TEN, 2);
        denominationIntegerMap.put(Denomination.FIVE, 0);
        Mockito.when(noteRepository.findByType(Denomination.FIFTY)).thenReturn(note50);
        Mockito.when(noteRepository.findByType(Denomination.TWENTY)).thenReturn(note20);
        Mockito.when(noteRepository.findByType(Denomination.TEN)).thenReturn(note10);
        Mockito.when(noteRepository.findByType(Denomination.FIVE)).thenReturn(note5);
        String validateATMCash = atmService.validateATMCash(220, denominationIntegerMap);
        Assertions.assertEquals("in sufficient funds in ATM", validateATMCash);
    }
}