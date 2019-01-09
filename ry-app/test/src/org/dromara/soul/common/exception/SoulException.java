package org.dromara.soul.common.exception;

public class SoulException extends RuntimeException {
    private static final long serialVersionUID = 8068509879445395353L;

    /**
     * Instantiates a new Soul exception.
     *
     * @param e the e
     */
    public SoulException(final Throwable e) {
        super(e);
    }

    /**
     * Instantiates a new Soul exception.
     *
     * @param message the message
     */
    public SoulException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new Soul exception.
     *
     * @param message   the message
     * @param throwable the throwable
     */
    public SoulException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
