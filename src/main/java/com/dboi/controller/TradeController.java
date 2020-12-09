package com.dboi.controller;

import com.dboi.model.Trade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TradeController {

    @PostMapping("/trade")
    public ResponseEntity<String> storeTrade(final @RequestBody Trade trade){
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
