package bg.sofia.uni.fmi.mjt.splitnotsowise.exception;

import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.SPACE_DELIMITER;

public class BadResponseException extends Exception {
    public BadResponseException(int statusCode, String msg) {
        super(statusCode + SPACE_DELIMITER + msg);
    }

    public BadResponseException(int statusCode, String msg, Throwable e) {
        super(statusCode + SPACE_DELIMITER + msg, e);
    }
}
