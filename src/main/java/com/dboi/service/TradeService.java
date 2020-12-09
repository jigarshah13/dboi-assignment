package com.dboi.service;

import com.dboi.model.Trade;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TradeService {


    public boolean storeTrade(final Trade trade) {
        return validateTrade(trade);
    }

    private boolean validateTrade(final Trade trade) {
        return verifyMaturityDate(trade);
    }

    private boolean verifyMaturityDate(final Trade trade) {
        return LocalDate.now().isBefore(trade.getMaturityDate());
    }
}
