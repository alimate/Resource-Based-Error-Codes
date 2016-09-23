package me.alidg.rest.geeks;

import me.alidg.rest.errors.ErrorCode;
import org.springframework.http.HttpStatus;

enum GeeksApiErrorCodes implements ErrorCode {
    GEEK_ALREADY_EXISTS("geeks-1", HttpStatus.BAD_REQUEST);

    private final String code;
    private final HttpStatus httpStatus;

    GeeksApiErrorCodes(String code, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }
}