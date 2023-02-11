package bg.sofia.uni.fmi.mjt.splitnotsowise.exception;

public class IncorrectCommandUsageException extends Exception {
    public IncorrectCommandUsageException(String msg) {
        super(msg);
    }
    public IncorrectCommandUsageException(String msg, Throwable e) {
        super(msg, e);
    }
}
