package de.lusiardi.proxy.exceptions;

/**
 * Exception to indicate a problem while parsing a HTTP response.
 *
 * @author Joachim Lusiardi
 */
public class ResponseParseException extends Exception {

    /**
     * Constructs a new exception.
     *
     * @param message the message of the exception
     */
    public ResponseParseException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception.
     *
     * @param message the message of the exception
     * @param cause the causing {@link Throwable} of this exception
     */
    public ResponseParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
