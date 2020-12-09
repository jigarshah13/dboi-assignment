package com.dboi.controller;

import com.dboi.model.Trade;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeControllerTest {

    @Autowired
    private TradeController tradeController;

    @Test
    public void storeTrade() {
        final Trade trade = new Trade();
        ResponseEntity<String> response = tradeController.storeTrade(trade);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}