package com.dboi.controller;

import com.dboi.exception.InvalidMaturityDateException;
import com.dboi.exception.LowerVersionTradeException;
import com.dboi.model.Trade;
import com.dboi.service.TradeService;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeControllerTest {

    MockMvc mockMvc;

    @Autowired
    private TradeController tradeController;

    @MockBean
    private TradeService tradeService;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.tradeController).build();
    }

    @Test
    public void whenAllValidationsPassedThenTradeShouldBePersisted() throws Exception {

        final Trade trade = Trade.builder().tradeId("T1").bookId("B1").counterPartyId("C1").expired("N").version(1).maturityDate(LocalDate.now()).createdDate(LocalDate.now()).build();
        Mockito.when(tradeService.storeTrade(trade)).thenReturn(trade);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/dboi/trade").content(trade.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void whenMaturityDateIsLTTodayDateThenExceptionShouldBeThrown() throws Exception {


        final Trade trade = Trade.builder().
                tradeId("T1").
                bookId("B1").
                counterPartyId("C1").
                expired("N").
                version(1).
                maturityDate(LocalDate.of(2020, 12, 10)).createdDate(LocalDate.now()).build();
        Mockito.when(tradeService.storeTrade(any(Trade.class))).thenThrow(new InvalidMaturityDateException(trade.getTradeId()));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/dboi/trade").content(trade.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath(".message", Matchers.hasItem(Matchers.is("Maturity date of the trade {T1} is less than today date."))));
        ;
    }

    @Test
    public void whenVersionIsLTVersionOfPersistedTradehenExceptionShouldBeThrown() throws Exception {


        final Trade trade = Trade.builder().
                tradeId("T1").
                bookId("B1").
                counterPartyId("C1").
                expired("N").
                version(1).
                maturityDate(LocalDate.of(2020, 12, 10)).createdDate(LocalDate.now()).build();
        Mockito.when(tradeService.storeTrade(any(Trade.class))).thenThrow(new LowerVersionTradeException(trade.getTradeId()));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/dboi/trade").content(trade.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath(".message", Matchers.hasItem(Matchers.is("Version of the trade {T1} is lower than the trade available in store."))));
        ;
    }

    @Test
    public void fetchAllTrades() throws Exception {

        Mockito.when(tradeService.getTrades()).thenReturn(dummyTradeList());
        this.mockMvc.perform(MockMvcRequestBuilders.get("/dboi/trade"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath(".message", Matchers.hasItem(Matchers.is("Success"))));
        ;
    }

    private List<Trade> dummyTradeList() {
        final List<Trade> trades = Lists.newArrayList();
        trades.add(Trade.builder().tradeId("T1").version(1).build());
        trades.add(Trade.builder().tradeId("T2").version(1).build());
        return trades;
    }
}