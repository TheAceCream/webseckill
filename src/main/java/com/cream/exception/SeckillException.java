package com.cream.exception;

/**
 * Author:Cream
 * Date:2018/4/15
 * Description:
 */
public class SeckillException extends RuntimeException{

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message,Throwable cause) {
        super(message,cause);
    }
}
