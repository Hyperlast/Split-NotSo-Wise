package bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.transaction;

import bg.sofia.uni.fmi.mjt.splitnotsowise.command.Command;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.CommandRunner;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.entity.User;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository.ConnectionObserver;
import bg.sofia.uni.fmi.mjt.splitnotsowise.exception.BadResponseException;
import bg.sofia.uni.fmi.mjt.splitnotsowise.exception.NoServiceAvailableException;
import bg.sofia.uni.fmi.mjt.splitnotsowise.external.Currency;
import bg.sofia.uni.fmi.mjt.splitnotsowise.external.CurrencyCache;
import bg.sofia.uni.fmi.mjt.splitnotsowise.external.Exchange;
import bg.sofia.uni.fmi.mjt.splitnotsowise.log.Logger;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.Validator;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.OutputCreator;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.LocalDateTime;

public class SwitchCurrencyCommand implements Command {

    private static final String UNAVAILABLE_SERVICE_MSG = "Conversion cannot be done," +
            " try again later or contact administrator";
    public static final int ARGUMENT_COUNT = 2;
    private final String socketChannel;

    private final String currency;

    public SwitchCurrencyCommand(String socketChannel, String currency) {
        Validator.checkIfNull(socketChannel, currency);
        Validator.isBlank(socketChannel, currency);

        this.currency = currency;
        this.socketChannel = socketChannel;
    }

    @Override
    public String execute(Logger logger) {
        try {
            ConnectionObserver.isLogged(socketChannel);

            Currency wantedCurrency = Currency.parseLabel(currency);
            if (wantedCurrency.equals(Currency.UNKNOWN)) {
                return Currency.UNKNOWN.getLabel();
            }

            HttpClient httpClient = HttpClient.newHttpClient();

            checkServiceAvailability(wantedCurrency);

            checkQueryAvailability(httpClient, wantedCurrency);

            User user = CommandRunner.getUser(socketChannel);

            if (!user.setCurrency(wantedCurrency)) {
                return "Could not load conversion to user";
            }

        } catch (Exception e) {
            logger.log(LocalDateTime.now(), OutputCreator.getFullExceptionMessage(e), logger.getLogWriter());
            return e.getMessage();
        }


        return "Successful conversion" + System.lineSeparator();
    }

    private void checkServiceAvailability(Currency wantedCurrency) throws NoServiceAvailableException {
        if (!Exchange.canQuery() && !CurrencyCache.getInstance().isConversionAvailable(wantedCurrency)) {
            throw new NoServiceAvailableException(UNAVAILABLE_SERVICE_MSG);
        }
    }

    private void checkQueryAvailability(HttpClient httpClient, Currency wantedCurrency) throws BadResponseException,
            IOException, InterruptedException, NoServiceAvailableException {
        if (!Exchange.getExchange(httpClient) && !CurrencyCache.getInstance()
                .isConversionAvailable(wantedCurrency)) {
            throw new NoServiceAvailableException(UNAVAILABLE_SERVICE_MSG);
        }
    }
}
