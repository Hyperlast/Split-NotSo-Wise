package bg.sofia.uni.fmi.mjt.splitnotsowise.exception;

public class UnrecognizedValueException extends Exception {
    public UnrecognizedValueException(String msg) {
        super(msg);
    }

    public UnrecognizedValueException(String msg, Throwable e) {
        super(msg, e);
    }
}
