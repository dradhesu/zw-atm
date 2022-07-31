package com.zw.bank.atm.repository;

import com.zw.bank.atm.domain.Note;
import com.zw.bank.atm.enums.Denomination;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long> {
    Note findByType(Denomination type);
}
