package com.wei.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(Throwable.class)
    public String test(Throwable e){
        e.printStackTrace();
        return "error";
    }
}
