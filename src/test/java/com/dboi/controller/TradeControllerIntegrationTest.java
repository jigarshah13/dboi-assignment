package com.dboi.controller;

import com.dboi.exception.InvalidMaturityDateException;
import com.dboi.model.Trade;
import com.dboi.service.TradeService;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeControllerIntegrationTest {

    MockMvc mockMvc;

    @Autowired
    private TradeController tradeController;

    @Test
    public void storeTradeSuccessfullyWhenAllValidationsPassed() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.tradeController).build();

        final Trade trade = Trade.builder()
                                .tradeId("T4")
                                .bookId("B1")
                                .counterPartyId("C1")
                                .expired("N")
                                .version(1)
                                .maturityDate(LocalDate.of(2020, 12, LocalDate.now().getDayOfMonth()+2))
                                .createdDate(LocalDate.now()).build();

        // Calling API to store valid trade information
        this.mockMvc.perform(MockMvcRequestBuilders.post("/dboi/trade").content(trade.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        // Validate Count in H2 DB which should be 1
        this.mockMvc.perform(MockMvcRequestBuilders.get("/dboi/trade"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", IsCollectionWithSize.hasSize(1)));

        // Calling API one more time to store valid trade information
        trade.setTradeId("T1");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/dboi/trade").content(trade.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        // Validate Count in H2 DB which should be 2
        this.mockMvc.perform(MockMvcRequestBuilders.get("/dboi/trade"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", IsCollectionWithSize.hasSize(2)));
    }

    @Test
    public void doNotStoreTradeAndThrowsExceptionWhenMaturityDateisLTTodayDate() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.tradeController).build();

        final Trade trade = Trade.builder()
                .tradeId("T4")
                .bookId("B1")
                .counterPartyId("C1")
                .expired("N")
                .version(1)
                .maturityDate(LocalDate.of(2020, 12, LocalDate.now().getDayOfMonth()-2))
                .createdDate(LocalDate.now()).build();

        try {
            this.mockMvc.perform(MockMvcRequestBuilders.post("/dboi/trade").content(trade.toString())
                    .contentType(MediaType.APPLICATION_JSON));
        } catch (Exception e) {
            Assertions.assertEquals("Maturity date of the trade {T4} is less than today date.", e.getMessage());
        }

    }
}