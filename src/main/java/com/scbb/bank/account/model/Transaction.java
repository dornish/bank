package com.scbb.bank.account.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scbb.bank.account.model.enums.EntryType;
import com.scbb.bank.person.model.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Transaction {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private EntryType entryType;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.PERSIST)
    private List<Entry> entryList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
