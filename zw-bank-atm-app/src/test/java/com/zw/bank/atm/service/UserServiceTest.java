package com.zw.bank.atm.service;

import com.zw.bank.atm.domain.User;
import com.zw.bank.atm.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void init() {
        userService = new UserService(userRepository);
    }

    @Test
    void testValidUser() {
        Optional<User> user = Optional.of(
                User.builder()
                        .accountNumber("987654321")
                        .pin(4321)
                        .build()
        );
        Mockito.when(userRepository.findById(any())).thenReturn(user);
        Assertions.assertTrue(userService.validate(user.get()));
    }

    @Test
    void testInvalidValidUser() {
        Optional<User> user = Optional.of(
                User.builder()
                        .accountNumber("987654321")
                        .pin(4321)
                        .build()
        );
        Mockito.when(userRepository.findById(any())).thenReturn(user);
        Assertions.assertFalse(userService.validate(User.builder()
                .accountNumber("987654321")
                .pin(4231)
                .build()));
    }
}
