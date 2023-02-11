package bg.sofia.uni.fmi.mjt.splitnotsowise.exception;

public class UnauthorizedAccessToServerDataException extends Exception {
    public UnauthorizedAccessToServerDataException(String msg) {
        super(msg);
    }
    public UnauthorizedAccessToServerDataException(String msg, Throwable e) {
        super(msg, e);
    }
}
