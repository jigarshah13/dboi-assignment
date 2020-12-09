package com.dboi.service;

import com.dboi.db.TradeRepository;
import com.dboi.exception.InvalidMaturityDateException;
import com.dboi.exception.LowerVersionTradeException;
import com.dboi.model.Trade;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeServiceTest {

    @Autowired
    private TradeService tradeService;

    @MockBean
    private TradeRepository tradeRepository;

    @Test
    public void whenMaturityDateOfTradeIsLTTodayDateThenStoreShouldNotAllowed() {
        final Trade trade = new Trade();
        trade.setTradeId("T1");
        trade.setMaturityDate(LocalDate.of(2020, 12, LocalDate.now().getDayOfMonth() - 1));
        InvalidMaturityDateException exception = Assert.assertThrows(InvalidMaturityDateException.class, () -> {
            tradeService.storeTrade(trade);
        });
        Assertions.assertEquals("Maturity date of the trade {" + trade.getTradeId() + "} is less than today date.", exception.getMessage());
    }

    @Test
    public void whenMaturityDateIsGTOrEqualToTodayDateThenStoreShouldAllowed() {
        final Trade trade = new Trade();
        trade.setTradeId("T1");
        trade.setMaturityDate(LocalDate.of(2020, 12, LocalDate.now().getDayOfMonth() + 1));

        Mockito.when(tradeRepository.save(trade)).thenReturn(trade);
        final Trade persistedTrade = tradeService.storeTrade(trade);
        Assert.assertNotNull(persistedTrade);
        Assert.assertEquals(trade.getTradeId(), persistedTrade.getTradeId());
    }

    @Test
    public void whenVersionOfTradeIsLTStoredTradeVersionThenStoreShouldBeRejected() {
        final Trade trade = new Trade();
        trade.setTradeId("T1");
        trade.setVersion(2);
        trade.setMaturityDate(LocalDate.of(2020, 12, LocalDate.now().getDayOfMonth() + 1));
        Mockito.when(tradeRepository.findById(trade.getTradeId())).thenReturn(Optional.of(Trade.builder().tradeId("T1").version(3).build()));
        LowerVersionTradeException exception = Assert.assertThrows(LowerVersionTradeException.class, () -> {
            tradeService.storeTrade(trade);
        });
        Assertions.assertEquals("Version of the trade {" + trade.getTradeId() + "} is lower than the trade available in store.", exception.getMessage());
    }

    @Test
    public void whenVersionOfTradeIsGTStoredTradeVersionThenStoreShouldBeUpdated() {
        final Trade trade = new Trade();
        trade.setTradeId("T1");
        trade.setVersion(4);
        trade.setMaturityDate(LocalDate.of(2020, 12, LocalDate.now().getDayOfMonth() + 1));
        Mockito.when(tradeRepository.findById(trade.getTradeId())).thenReturn(Optional.of(Trade.builder().tradeId("T1").version(3).build()));
        Mockito.when(tradeRepository.save(trade)).thenReturn(trade);

        final Trade persistedTrade = tradeService.storeTrade(trade);
        Assert.assertNotNull(persistedTrade);
        Assert.assertEquals(trade.getTradeId(), persistedTrade.getTradeId());
    }
}