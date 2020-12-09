package com.dboi.service;

import com.dboi.model.Trade;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeServiceTest {

    @Autowired
    private TradeService tradeService;

    @Test
    public void whenMaturityDateOfTradeIsLTTodayDateThenStoreShouldNotAllowed() {
        final Trade trade = new Trade();
        trade.setTradeId("T1");
        trade.setMaturityDate(LocalDate.of(2020, 12, LocalDate.now().getDayOfMonth()-1));
        final boolean isValidDate = tradeService.storeTrade(trade);
        Assert.assertFalse("Maturity date of trade is less than today date", isValidDate);
    }

    @Test
    public void whenMaturityDateIsGTOrEqualToTodayDateThenStoreShouldAllowed() {
        final Trade trade = new Trade();
        trade.setTradeId("T1");
        trade.setMaturityDate(LocalDate.of(2020, 12, LocalDate.now().getDayOfMonth()+1));
        final boolean isValidDate = tradeService.storeTrade(trade);
        Assert.assertTrue("Maturity date of trade is greater than today date", isValidDate);
    }
}