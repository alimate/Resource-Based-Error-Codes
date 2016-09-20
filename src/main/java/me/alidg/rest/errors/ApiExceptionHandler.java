package me.alidg.rest.errors;

import me.alidg.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;
import java.util.Objects;

/**
 * Exception handler that catches all exceptions thrown by the REST layer
 * and convert them to the appropriate {@linkplain ErrorResponse}s with a
 * suitable HTTP status code.
 *
 * @see ErrorCode
 * @see ErrorCodes
 * @see ErrorResponse
 *
 * @author Ali Dehghani
 */
@ControllerAdvice
class ApiExceptionHandler {
    private static final String NO_MESSAGE_AVAILABLE = "No message available";
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

    /**
     * Factory to convert the given {@linkplain Exception} to an instance of
     * {@linkplain ErrorCode}
     */
    private final ErrorCodes errorCodes;

    /**
     * Responsible for finding the appropriate error message(s) based on the given
     * {@linkplain ErrorCode} and {@linkplain Locale}
     */
    private final MessageSource apiErrorMessageSource;

    /**
     * Construct a valid instance of the exception handler
     *
     * @throws NullPointerException If either of required parameters were {@code null}
     */
    ApiExceptionHandler(ErrorCodes errorCodes, MessageSource apiErrorMessageSource) {
        Objects.requireNonNull(errorCodes);
        Objects.requireNonNull(apiErrorMessageSource);

        this.errorCodes = errorCodes;
        this.apiErrorMessageSource = apiErrorMessageSource;
    }

    /**
     * Catches all non-validation exceptions and tries to convert them to appropriate HTTP Error
     * responses
     *
     * <p>First using the {@linkplain #errorCodes} will find the corresponding {@linkplain ErrorCode}
     * for the given {@code exception}. Then based on the resolved {@linkplain Locale}, a suitable
     * instance of {@linkplain ErrorResponse} with appropriate and localized message will return
     * to the client. {@linkplain ErrorCode} itself determines the HTTP status of the response.
     *
     * @param exception The exception to convert
     * @param locale The locale that usually resolved by {@code Accept-Language} header. This locale
     *               will determine the language of the returned error message.
     * @return An appropriate HTTP Error Response with suitable status code and error messages
     */
    @ExceptionHandler(ServiceException.class)
    ResponseEntity<ErrorResponse> handleExceptions(ServiceException exception, Locale locale) {
        ErrorCode errorCode = errorCodes.of(exception);
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.httpStatus(), toApiError(errorCode, locale));

        return ResponseEntity.status(errorCode.httpStatus()).body(errorResponse);
    }

    /**
     * Convert the passed {@code errorCode} to an instance of {@linkplain ErrorResponse} using
     * the given {@code locale}
     */
    private ErrorResponse.ApiError toApiError(ErrorCode errorCode, Locale locale) {
        String codeInString = errorCode.code() + "";

        String message;
        try {
            message = apiErrorMessageSource.getMessage(codeInString, new Object[]{}, locale);
        } catch (NoSuchMessageException e) {
            LOGGER.error("Couldn't find any message for {} code under {} locale", codeInString, locale);
            message = NO_MESSAGE_AVAILABLE;
        }

        return new ErrorResponse.ApiError(errorCode.code(), message);
    }
}