package com.queirozjp.nexcoin.exception;

public class ToAddressNotFoundException extends RuntimeException{
    public ToAddressNotFoundException(String message){
        super(message);
    }
}
