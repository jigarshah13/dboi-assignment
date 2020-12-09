package com.dboi.service;

import com.dboi.exception.InvalidMaturityDateException;
import com.dboi.model.Trade;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TradeService {

    public boolean storeTrade(final Trade trade) {
        return validateTrade(trade);
    }

    private boolean validateTrade(final Trade trade) {
        if(verifyMaturityDate(trade)) {
            return true;
        } else {
            throw new InvalidMaturityDateException(trade.getTradeId());
        }
    }

    /**
     * Store should not allow the trade which has less maturity date then today date.
     * @param trade
     * @return
     */
    private boolean verifyMaturityDate(final Trade trade) {
        return LocalDate.now().isBefore(trade.getMaturityDate());
    }
}
