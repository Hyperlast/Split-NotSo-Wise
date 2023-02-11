package bg.sofia.uni.fmi.mjt.splitnotsowise.exception;

public class NoServiceAvailableException extends Exception {
    public NoServiceAvailableException(String msg) {
        super(msg);
    }

    public NoServiceAvailableException(String msg, Throwable e) {
        super(msg, e);
    }
}
