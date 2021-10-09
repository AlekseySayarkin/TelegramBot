package com.telegram.bot.exception;

public class BeanNotFoundException extends RuntimeException {

    public BeanNotFoundException(String message) {
        super(message);
    }
}
