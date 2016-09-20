package me.alidg.rest.errors;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

/**
 * Acts as a factory for {@linkplain ExceptionToErrorCode} implementations. By calling
 * the {@linkplain #of(Exception)} factory method, clients can find the corresponding
 * {@linkplain ErrorCode} for the given {@linkplain Exception}. Behind the scenes, this
 * factory method, first finds all available implementations of the {@linkplain ExceptionToErrorCode}
 * strategy interface. Then selects the first implementation that can actually handles
 * the given exception and delegate the exception translation process to that implementation.
 *
 * @implNote If the factory method couldn't find any implementation for the given
 * {@linkplain Exception}, It would return the {@linkplain me.alidg.rest.errors.ErrorCode.UnknownErrorCode}
 * which represent an Unknown Error.
 *
 * @author Ali Dehghani
 */
@Component
class ErrorCodes {
    private final ApplicationContext context;

    ErrorCodes(ApplicationContext context) {
        this.context = context;
    }

    /**
     * Factory method to find the right {@linkplain ExceptionToErrorCode} implementation
     * and delegates the conversion task to that implementation. If it couldn't find any
     * registered implementation, It would return an Unknown Error represented by the
     * {@linkplain UnknownError} implementation.
     *
     * @implNote Currently this method queries Spring's {@linkplain ApplicationContext} to
     * find the {@linkplain ExceptionToErrorCode} implementations. So in order to register
     * your {@linkplain ExceptionToErrorCode} implementation, you should annotate your
     * implementation with one of Spring Stereotype annotations, e.g. {@linkplain Component}.
     * Our recommendation is to use the {@linkplain Component} annotation or another meta annotation
     * based on this annotation.
     *
     * @param exception The exception to find the implementation based on that
     * @return An instance of {@linkplain ErrorCode} corresponding the given {@code exception}
     */
    ErrorCode of(Exception exception) {
        return implementations()
                .filter(impl -> impl.canHandle(exception))
                .findFirst()
                .map(impl -> impl.toErrorCode(exception))
                .orElse(ErrorCode.UnknownErrorCode.INSTANCE);
    }

    /**
     * Query the {@linkplain #context} to find all available implementations of
     * {@linkplain ExceptionToErrorCode}.
     */
    private Stream<ExceptionToErrorCode> implementations() {
        return context.getBeansOfType(ExceptionToErrorCode.class).values().stream();
    }
}