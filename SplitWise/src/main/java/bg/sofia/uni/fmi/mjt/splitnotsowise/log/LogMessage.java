package bg.sofia.uni.fmi.mjt.splitnotsowise.log;

import java.time.LocalDateTime;

public record LogMessage(LocalDateTime stamp, String msg) {
    private static final String FORMAT = "|%s| : %s" + System.lineSeparator();

    @Override
    public String toString() {
        return String.format(FORMAT, stamp, msg);
    }
}
