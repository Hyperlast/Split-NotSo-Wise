package bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message;

import bg.sofia.uni.fmi.mjt.splitnotsowise.external.Currency;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.SPACE_DELIMITER;

public final class OutputCreator {

    public static final String USER_EXCEPTION_RETURN_MESSAGE = "Oops, something happened"
            + System.lineSeparator()
            + "Try again later, or contact administrator with logs from -> ";

    public static final String DDOT_DELIMITER = ":";

    private OutputCreator() { }

    public static String createPayedTransactionMessage(String receiver, BigDecimal amount, Currency currency) {
        return LocalDateTime.now()
                + DDOT_DELIMITER
                + receiver
                + " was payed "
                + amount
                + SPACE_DELIMITER
                + currency.getLabel();
    }

    public static String createSplitFriendTransactionMessage(String sender, BigDecimal amount,
                                                             Currency currency, String reason) {
        return LocalDateTime.now()
                + DDOT_DELIMITER
                + sender
                + " took split "
                + amount
                + SPACE_DELIMITER
                + currency.getLabel()
                + " for "
                + reason;
    }

    public static String createSplitWithGroupTransactionMessage(String groupName, BigDecimal amount,
                                                                Currency currency, String reason) {
        return LocalDateTime.now()
                + DDOT_DELIMITER
                + " Split: "
                + amount
                + currency
                + " with group -> "
                + groupName
                + System.lineSeparator()
                + " because: "
                + reason;
    }

    public static String getFullExceptionMessage(Exception e) {
        try (StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw)) {
            e.printStackTrace(pw);
            return e.getMessage() + DDOT_DELIMITER + sw;
        } catch (IOException ex) {
            return "Couldn't log because " + ex.getMessage();
        }
    }

}
