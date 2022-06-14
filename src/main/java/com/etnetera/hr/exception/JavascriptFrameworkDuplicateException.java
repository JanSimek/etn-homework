package com.etnetera.hr.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class JavascriptFrameworkDuplicateException extends RuntimeException {

    public JavascriptFrameworkDuplicateException(String message) {
        super(message);
    }

    public JavascriptFrameworkDuplicateException() {
        super();
    }
}
