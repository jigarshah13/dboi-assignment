package com.dboi.controller;

import com.dboi.model.Response;
import com.dboi.model.Trade;
import com.dboi.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "/dboi")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @RequestMapping(value = "/trade", method = RequestMethod.POST)
    public ResponseEntity<Response> storeTrade(final @RequestBody Trade trade) {
        try {
            Trade persistedTrade = tradeService.storeTrade(trade);
            ;
            return ResponseEntity.accepted().body(generateSuccessResponse(Collections.singletonList(persistedTrade)));
        } catch (Exception e) {
            return ResponseEntity.accepted().body(generateErrorResponse(e));
        }
    }

    private Response generateSuccessResponse(final List<Trade> trades) {
        final Response response = new Response();
        response.setTrades(trades);
        response.setMessage("Success");
        response.setStatus(200);
        response.setErrorCode(0);
        return response;
    }

    private Response generateErrorResponse(final Exception exception) {
        final Response response = new Response();
        response.setMessage(exception.getMessage());
        response.setErrorCode(100);
        return response;
    }

    @RequestMapping(value = "/trade", method = RequestMethod.GET)
    public ResponseEntity<Response> fetchAllTrades() {
        return ResponseEntity.accepted().body(generateSuccessResponse(tradeService.getTrades()));
    }
}
