package com.dboi.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "trades")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Trade {

    @Id private String tradeId;
    private int version;
    private String counterPartyId;
    private String bookId;
    private LocalDate maturityDate;
    private LocalDate createdDate;
    private boolean expired;

    @Override
    public String toString() {
        return "{" +
                "\"tradeId\":\"" + tradeId + '"' +
                ", \"version\":" + version +
                ", \"counterPartyId\":\"" + counterPartyId + '"' +
                ", \"bookId\":\"" + bookId + '"' +
                ", \"maturityDate\":\"" + maturityDate + '"' +
                ", \"createdDate\":\"" + createdDate + '"' +
                ", \"expired\":" + expired +
                '}';
    }
}

