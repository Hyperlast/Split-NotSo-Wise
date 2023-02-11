package bg.sofia.uni.fmi.mjt.splitnotsowise.exception;

public class NegativeAmountException extends Exception {
    public NegativeAmountException(String msg) {
        super(msg);
    }

    public NegativeAmountException(String msg, Throwable e) {
        super(msg, e);
    }
}
