package com.cream.exception;

import com.cream.entity.Seckill;

/**
 * Author:Cream
 * Date:2018/4/15
 * Description:
 */
public class SeckillCloseException extends SeckillException {

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
