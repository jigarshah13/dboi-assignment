package com.dboi.service;

import com.dboi.db.TradeRepository;
import com.dboi.exception.InvalidMaturityDateException;
import com.dboi.model.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    public boolean storeTrade(final Trade trade) {
        if(validateTrade(trade)) {
            tradeRepository.save(trade);
            System.out.println(tradeRepository.count());
        }
        return true; // have to change to Trade and so test cases
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
