package com.cream.exception;

/**
 * Author:Cream
 * Date:2018/4/15
 * Description:
 */
public class RepeatKillException extends SeckillException{

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }

}
