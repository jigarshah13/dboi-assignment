package com.dboi.exception;

public class LowerVersionTradeException extends RuntimeException {

    public LowerVersionTradeException(final String tradeId) {
        super("Version of the trade {" + tradeId + "} is lower than the trade available in store.");
    }
}
