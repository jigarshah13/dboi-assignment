package com.dboi.controller;

import com.dboi.model.Trade;
import com.dboi.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @PostMapping("/trade")
    public ResponseEntity<String> storeTrade(final @RequestBody Trade trade) {
        tradeService.storeTrade(trade);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
