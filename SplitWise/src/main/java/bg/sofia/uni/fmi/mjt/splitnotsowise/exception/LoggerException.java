package bg.sofia.uni.fmi.mjt.splitnotsowise.exception;

public class LoggerException extends RuntimeException {
    public LoggerException(String msg) {
        super(msg);
    }

    public LoggerException(String msg, Throwable e) {
        super(msg, e);
    }
}
