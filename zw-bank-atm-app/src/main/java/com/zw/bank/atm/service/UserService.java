package com.zw.bank.atm.service;

import com.zw.bank.atm.domain.User;
import com.zw.bank.atm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void init() {
        User user1 = User.builder()
                .accountNumber("123456789")
                .pin(1234)
                .build();

        User user2 = User.builder()
                .accountNumber("987654321")
                .pin(4321)
                .build();
        userRepository.saveAll(List.of(user1, user2));
    }

    public Boolean validate(User user) {
        Boolean status = Boolean.FALSE;
        Optional<User> userById = userRepository.findById(user.getAccountNumber());
        if (userById.isPresent() && (user.getPin() == (userById.get().getPin()))) {
            status = Boolean.TRUE;
        }
        return status;
    }

}

