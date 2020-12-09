package com.dboi.exception;

public class InvalidMaturityDateException extends RuntimeException {

    public InvalidMaturityDateException(final String tradeId) {
        super("Maturity date of the trade {" + tradeId + "} is less than today date.");
    }
}
