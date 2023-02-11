package bg.sofia.uni.fmi.mjt.splitnotsowise.external;

import bg.sofia.uni.fmi.mjt.splitnotsowise.exception.BadResponseException;
import bg.sofia.uni.fmi.mjt.splitnotsowise.external.response.CurrencyResponse;
import bg.sofia.uni.fmi.mjt.splitnotsowise.external.response.ErrorResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.DEFAULT_EXCHANGE_CONFIG_FILE_NAME;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.DEFAULT_LOG_DIR_PATH;


public class Exchange {

    private static Exchange instance;
    private static final int DAY_HOURS = 24;
    private static final int STATUS_OK = 200;
    private static final Gson GSON = new Gson();
    private static final String EXCHANGE_API_URL = "https://api.apilayer.com/exchangerates_data/latest" +
            "?base=BGN" +
            "&symbols=EUR,USD";

    private static String apiKey;

    private static LocalDateTime lastQuery;

    public static final String EXCHANGE_CONFIG_PATH = DEFAULT_LOG_DIR_PATH + DEFAULT_EXCHANGE_CONFIG_FILE_NAME;

    private Exchange() { }

    public static Exchange getInstance() {
        if (instance == null) {
            return new Exchange();
        }
        return instance;
    }

    private  void updateQueryTime() {
        lastQuery = LocalDateTime.now();
    }

    public void addAPIKEY(String key) {
        apiKey = key;
    }
    public boolean canQuery() {
        if (lastQuery == null) {
            return true;
        }
        return ChronoUnit.HOURS.between(lastQuery, LocalDateTime.now()) >= DAY_HOURS;
    }

    public boolean getExchange(HttpClient client) throws IOException, InterruptedException,
            BadResponseException {

        if (!canQuery() || apiKey.isBlank()) {
            return false;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(EXCHANGE_API_URL))
                .setHeader("apikey", apiKey)
                .setHeader("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        errorCheck(response);

        //System.out.println(response.body());

        CurrencyResponse result = GSON.fromJson(response.body(), CurrencyResponse.class);

        //System.out.println(result);

        CurrencyCache.getInstance().addRates(result);
        updateQueryTime();

        return true;
    }

    public void errorCheck(HttpResponse<String> response) throws BadResponseException {
        int statusCode = response.statusCode();

        if (statusCode != STATUS_OK) {
            for (ErrorResponse err: ErrorResponse.values()) {
                if (err.getCode() == statusCode) {
                    throw new BadResponseException(statusCode, err.getMessage());
                }
            }
        }
    }
}
