package bg.sofia.uni.fmi.mjt.splitnotsowise.external;

import bg.sofia.uni.fmi.mjt.splitnotsowise.exception.BadResponseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;


import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeTest {

    @Mock
    HttpClient client;
    @Mock
    HttpResponse<String> response;

    @Test
    void testGetExchange() throws IOException, InterruptedException, BadResponseException {
        int code = 200;
        String jsonResponse = "{\n" +
                "\t\"success\": \"true\",\n" +
                "\t\"timestamp\": \"1519296206\",\n" +
                "\t\"base\": \"BGN\",\n" +
                "\t\"date\": \"2021 - 03 - 17\",\n" +
                "\t\"rates\": {\n" +
                "\t\t\"EUR\": \"0.72007\",\n" +
                "\t\t\"USD\": \"107.346001\"\n" +
                "\t}\n" +
                "}";
        Exchange.getInstance().addAPIKEY("Rando");

        when(client.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);
        when(response.statusCode()).thenReturn(code);
        when(response.body()).thenReturn(jsonResponse);

        Exchange.getInstance().getExchange(client);
        assertTrue(CurrencyCache.getInstance().isConversionAvailable(Currency.USD),
                "Expected conversion to be available");
        assertTrue(CurrencyCache.getInstance().isConversionAvailable(Currency.EUR),
                "Expected conversion to be available");

        assertFalse(Exchange.getInstance().canQuery(), "Expected query timer to get updated");
    }

    @Test
    void testErrorCheckStatusOk() {
        int code = 403;
        when(response.statusCode()).thenReturn(code);
        assertThrows(BadResponseException.class,() -> Exchange.getInstance().errorCheck(response),
                "Expected an exception to be thrown");
    }
}