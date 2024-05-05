package com.hcmute.fastfoodsystem.exceptions;

public class OrderNotFoundException extends IllegalArgumentException{
    public OrderNotFoundException(String msg) {
        super(msg);
    }
}
