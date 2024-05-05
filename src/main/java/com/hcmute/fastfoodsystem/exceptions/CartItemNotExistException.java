package com.hcmute.fastfoodsystem.exceptions;

public class CartItemNotExistException extends IllegalArgumentException{
    public CartItemNotExistException(String msg) {
        super(msg);
    }
}
