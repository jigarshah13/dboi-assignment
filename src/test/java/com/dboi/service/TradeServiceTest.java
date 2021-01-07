package com.dboi.service;

import com.dboi.db.TradeRepository;
import com.dboi.exception.InvalidMaturityDateException;
import com.dboi.exception.LowerVersionTradeException;
import com.dboi.model.Trade;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
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
        trade.setMaturityDate(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth() + 1));

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
        trade.setMaturityDate(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth() + 1));
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
        trade.setMaturityDate(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth() + 1));
        Mockito.when(tradeRepository.findById(trade.getTradeId())).thenReturn(Optional.of(Trade.builder().tradeId("T1").version(3).build()));
        Mockito.when(tradeRepository.save(trade)).thenReturn(trade);

        final Trade persistedTrade = tradeService.storeTrade(trade);
        Assert.assertNotNull(persistedTrade);
        Assert.assertEquals(trade.getTradeId(), persistedTrade.getTradeId());
    }

    @Test
    public void testExpiryManagement() {
        Mockito.when(tradeRepository.findAll()).thenReturn(dummyTradeList());
        Mockito.when(tradeRepository.save(ArgumentMatchers.any(Trade.class))).thenReturn(Trade.builder().tradeId("T1").version(1).expired("Y").maturityDate(LocalDate.of(2020, 12, 11)).build());
        tradeService.validateAndManageExpiry();
    }

    @Test
    public void testGetTrades() {
        Mockito.when(tradeRepository.findAll()).thenReturn(dummyTradeList());
        List<Trade> trades = tradeService.getTrades();
        Assertions.assertEquals(2, trades.size());
    }

    private List<Trade> dummyTradeList() {
        final List<Trade> trades = Lists.newArrayList();
        trades.add(Trade.builder().tradeId("T1").version(1).expired("N").maturityDate(LocalDate.of(2020, 12, 9)).build());
        trades.add(Trade.builder().tradeId("T2").version(1).expired("N").maturityDate(LocalDate.of(2020, 12, 9)).build());
        return trades;
    }

}