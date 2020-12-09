package com.dboi.service;

import com.dboi.db.TradeRepository;
import com.dboi.exception.InvalidMaturityDateException;
import com.dboi.exception.LowerVersionTradeException;
import com.dboi.model.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TradeService {

    private static final Logger log = LoggerFactory.getLogger(TradeService.class);

    @Autowired
    private TradeRepository tradeRepository;

    public Trade storeTrade(final Trade trade) {
        log.debug("Request to store trade {} is received", trade.getTradeId());
        Trade persistedTrade = null;
        if (validateTrade(trade)) {
            persistedTrade = tradeRepository.save(trade);
        }
        return persistedTrade;
    }

    /**
     * Perform validations on the Trade data.
     *
     * @param trade
     * @return
     */
    private boolean validateTrade(final Trade trade) {
        log.debug("Validations for trade {} started..", trade.getTradeId());
        if (verifyMaturityDate(trade)) {
            if (!validateVersion(trade)) {
                throw new LowerVersionTradeException(trade.getTradeId());
            }
        } else {
            throw new InvalidMaturityDateException(trade.getTradeId());
        }
        return true;
    }

    /**
     * During transmission if the lower version is being received by the store it will reject the trade
     * and throw an exception. If the version is same it will override the existing record.
     *
     * @param trade
     * @return
     */
    private boolean validateVersion(final Trade trade) {
        log.debug("Validating version for trade {}", trade.getTradeId());
        Optional<Trade> storedTrade = tradeRepository.findById(trade.getTradeId());
        return storedTrade.map(value -> trade.getVersion() >= value.getVersion()).orElse(true);
    }

    /**
     * Store should not allow the trade which has less maturity date then today date.
     *
     * @param trade
     * @return
     */
    private boolean verifyMaturityDate(final Trade trade) {
        log.debug("Validating maturity date for trade {}", trade.getTradeId());
        return LocalDate.now().isBefore(trade.getMaturityDate());
    }

    public List<Trade> getTrades() {
        return tradeRepository.findAll();
    }

    @Scheduled(cron = "0/10 * 0 ? * *")
    public void reportCurrentTime() {
        tradeRepository.findAll().stream().forEach(t -> {
            if (!verifyMaturityDate(t)) {
                log.debug("Changing expiry of "+t.getTradeId());
                t.setExpired("Y");
                tradeRepository.save(t);
            }
        });
    }
}

