package com.pro.sky.ScoolMagic.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExceptionApp extends IllegalArgumentException{
    public ExceptionApp(String s){
        super(s);
    }
}
