package com.zw.bank.atm.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Money {
    private int fifties;
    private int tens;
    private int fives;
    private int twenties;
}
