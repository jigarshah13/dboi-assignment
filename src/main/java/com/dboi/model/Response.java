package com.dboi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Response {

    private int status;
    private int errorCode;
    private String message;
    private List<Trade> trades;
}
