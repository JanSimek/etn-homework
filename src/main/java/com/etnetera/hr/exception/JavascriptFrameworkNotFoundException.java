package com.etnetera.hr.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class JavascriptFrameworkNotFoundException extends RuntimeException {

    public JavascriptFrameworkNotFoundException(String message) {
        super(message);
    }

    public JavascriptFrameworkNotFoundException() {
        super();
    }
}
