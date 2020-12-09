package com.dboi.controller;

import com.dboi.model.Trade;
import com.dboi.service.TradeService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeControllerTest {

    MockMvc mockMvc;

    @Autowired
    private TradeController tradeController;

    @MockBean
    private TradeService tradeService;

    @Test
    public void storeTrade() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.tradeController).build();

        final Trade trade = Trade.builder().tradeId("T1").bookId("B1").counterPartyId("C1").expired(false).version(1).maturityDate(LocalDate.now()).createdDate(LocalDate.now()).build();
        Mockito.when(tradeService.storeTrade(trade)).thenReturn(true);
        System.out.println(trade.toString());
        this.mockMvc.perform(MockMvcRequestBuilders.post("/dboi/trade").content(trade.toString()).contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}