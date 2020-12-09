package com.dboi.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "trades")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Trade {

    @Id private String tradeId;
    @Column private int version;
    @Column private String counterPartyId;
    @Column private String bookId;
    @Column private LocalDate maturityDate;
    @Column private LocalDate createdDate;
    @Column private String expired;

    @Override
    public String toString() {
        return "{" +
                "\"tradeId\":\"" + tradeId + '"' +
                ", \"version\":" + version +
                ", \"counterPartyId\":\"" + counterPartyId + '"' +
                ", \"bookId\":\"" + bookId + '"' +
                ", \"maturityDate\":\"" + maturityDate + '"' +
                ", \"createdDate\":\"" + createdDate + '"' +
                ", \"expired\":\"" + expired + '"' +
                '}';
    }
}

