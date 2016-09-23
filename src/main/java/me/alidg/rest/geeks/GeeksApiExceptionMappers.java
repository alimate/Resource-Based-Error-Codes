package me.alidg.rest.geeks;

import me.alidg.rest.errors.ErrorCode;
import me.alidg.rest.errors.ExceptionToErrorCode;
import me.alidg.service.geeks.GeekAlreadyExists;
import org.springframework.stereotype.Component;

class GeeksApiExceptionMappers {
    @Component
    static class GeeksAlreadyExceptionToErrorCode implements ExceptionToErrorCode {
        @Override
        public boolean canHandle(Exception exception) {
            return exception instanceof GeekAlreadyExists;
        }

        @Override
        public ErrorCode toErrorCode(Exception exception) {
            return GeeksApiErrorCodes.GEEK_ALREADY_EXISTS;
        }
    }
}