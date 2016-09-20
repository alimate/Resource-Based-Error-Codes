package me.alidg.rest.errors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

/**
 * An immutable data structure representing HTTP error response bodies. JSON
 * representation of this class would be something like the following:
 * <pre>
 *     {
 *         "status_code": 404,
 *         "reason_phrase": "Not Found",
 *         "errors": [
 *             {"code": 15, "message": "some, hopefully localized, error message"},
 *             {"code": 16, "message": "yet another message"}
 *         ]
 *     }
 * </pre>
 *
 * @author Ali Dehghani
 */
@JsonAutoDetect(fieldVisibility = ANY)
class ErrorResponse {
    /**
     * The 4xx or 5xx status code for error cases, e.g. 404
     */
    private final int statusCode;

    /**
     * The HTTP reason phrase corresponding the {@linkplain #statusCode}, e.g. Not Found
     *
     * @see <a href="https://www.w3.org/Protocols/rfc2616/rfc2616-sec6.html">Status Code and Reason Phrase</a>
     */
    private final String reasonPhrase;

    /**
     * List of application-level error code and message combinations. Using these errors
     * we provide more information about the actual error
     */
    private final List<ApiError> errors;

    /**
     * Construct a valid instance of the {@linkplain ErrorResponse}
     *
     * @throws IllegalArgumentException If one of passed parameters is null or invalid
     */
    private ErrorResponse(int statusCode, String reasonPhrase, List<ApiError> errors) {
        if (statusCode < 400 || statusCode > 600)
            throw new IllegalArgumentException("Error Status codes should be between 400 and 599");

        if (reasonPhrase ==  null || reasonPhrase.trim().isEmpty())
            throw new IllegalArgumentException("HTTP Response reason phrase can't be null or blank");

        if (errors == null || errors.isEmpty())
            throw new IllegalArgumentException("Errors list can't be null or empty");

        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.errors = errors;
    }

    /**
     * Static factory method to create a {@linkplain ErrorResponse} with multiple
     * {@linkplain ApiError}s. The canonical use case of this factory method is when
     * we're handling validation exceptions, since we may have multiple validation
     * errors.
     *
     * @param status The {@linkplain HttpStatus} encapsulating both HTTP status code and it's
     *               reason phrase
     * @param errors List of {@linkplain ApiError}s for each application-level error.
     * @return An instance of {@linkplain ErrorResponse} with multiple {@linkplain ApiError}s
     */
    static ErrorResponse ofErrors(HttpStatus status, List<ApiError> errors) {
        return new ErrorResponse(status.value(), status.getReasonPhrase(), errors);
    }

    /**
     * Static factory method to create a {@linkplain ErrorResponse} with a single
     * {@linkplain ApiError}. The canonical use case for this method is when we trying
     * to create {@linkplain ErrorResponse}es for regular non-validation exceptions.
     *
     * @param status The {@linkplain HttpStatus} encapsulating both status code and reason
     *               phrase
     * @param error The {@linkplain ApiError} encapsulating application-level error code
     *              and message
     * @return An instance of {@linkplain ErrorResponse} with just one {@linkplain ApiError}
     */
    static ErrorResponse of(HttpStatus status, ApiError error) {
        return ofErrors(status, Collections.singletonList(error));
    }

    /**
     * An immutable data structure representing each application-level error. JSON
     * representation of this class would be something like the following:
     * <pre>
     *     {"code": 12, "message": "some error"}
     * </pre>
     *
     * @author Ali Dehghani
     */
    @JsonAutoDetect(fieldVisibility = ANY)
    static class ApiError {
        /**
         * The error code
         */
        private final int errorCode;

        /**
         * Possibly localized error message
         */
        private final String message;

        ApiError(int errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }
    }
}