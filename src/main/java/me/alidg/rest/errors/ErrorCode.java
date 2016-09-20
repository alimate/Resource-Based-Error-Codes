package me.alidg.rest.errors;

import org.springframework.http.HttpStatus;

/**
 * Represents API error code. Each API should implement this interface to
 * provide an error code for each error case.
 *
 * @implNote Enum implementations are good fit for this scenario.
 *
 * @author Ali Dehghani
 */
public interface ErrorCode {
    int ERROR_CODE_FOR_UNKNOWN_ERROR = 1;

    /**
     * Represents the error code.
     *
     * @return The integral error code
     */
    int code();

    /**
     * The corresponding HTTP status for the given error code
     *
     * @return Corresponding HTTP status code, e.g. 400 Bad Request for a validation
     * error code
     */
    HttpStatus httpStatus();

    /**
     * Default implementation representing the Unknown Error Code. When the
     * {@linkplain ErrorCodes} couldn't find any appropriate {@linkplain ErrorCode} for
     * any given {@linkplain Exception}, it will use this implementation by default.
     */
    enum UnknownErrorCode implements ErrorCode {
        INSTANCE;

        @Override
        public int code() {
            return ERROR_CODE_FOR_UNKNOWN_ERROR;
        }

        @Override
        public HttpStatus httpStatus() {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}