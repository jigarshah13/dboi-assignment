package com.dboi.controller;

import com.dboi.model.Trade;
import com.dboi.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/dboi")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @RequestMapping(value = "/trade", method = RequestMethod.POST)
    public ResponseEntity<String> storeTrade(final @RequestBody Trade trade) {
        tradeService.storeTrade(trade);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(value = "/trade", method = RequestMethod.GET)
    public List<Trade> fetchAllTrades() {
        return tradeService.getTrades();
    }
}
