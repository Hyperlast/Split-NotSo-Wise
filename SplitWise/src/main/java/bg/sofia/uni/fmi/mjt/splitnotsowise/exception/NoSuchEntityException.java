package bg.sofia.uni.fmi.mjt.splitnotsowise.exception;

public class NoSuchEntityException extends Exception {
    public NoSuchEntityException(String msg) {
        super(msg);
    }

    public NoSuchEntityException(String msg, Throwable e) {
        super(msg, e);
    }
}
