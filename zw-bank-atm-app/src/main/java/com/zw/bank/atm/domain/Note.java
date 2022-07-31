package com.zw.bank.atm.domain;

import com.zw.bank.atm.enums.Denomination;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Note {
    @Column(unique = true)
    Denomination type;
    int totalNotes;
    @Id
    private int id;
}
