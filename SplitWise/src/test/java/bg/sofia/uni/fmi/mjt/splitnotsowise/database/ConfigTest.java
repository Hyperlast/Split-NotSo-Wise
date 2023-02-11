package bg.sofia.uni.fmi.mjt.splitnotsowise.database;


import bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository.UserRegistry;
import bg.sofia.uni.fmi.mjt.splitnotsowise.exception.IncorrectCommandUsageException;
import bg.sofia.uni.fmi.mjt.splitnotsowise.log.Logger;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.StringReader;


import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ConfigTest {

    Logger log = mock(Logger.class);
    @Test
    void testParseUserInfo() {

        JsonReader reader = new JsonReader(new StringReader(
                "{\"userInfo\":{\"Tomi\":{\"username\":\"Tomi\",\"hashedPass\":\"d67d99f2704088a2cacff50ae2411e55\",\"notifications\":[\"Kalin added you as a friend\",\"Kalin split 5.0 EUR with you for: bananas\"],\"friends\":{\"Kalin\":{\"groupDebt\":{\"Tomi\":0.00,\"Kalin\":4.89}}},\"groups\":{}},\"Kalin\":{\"username\":\"Kalin\",\"hashedPass\":\"ea21067446d2e72854d48d5bd5f75c8e\",\"notifications\":[],\"friends\":{\"Tomi\":{\"groupDebt\":{\"Tomi\":0.00,\"Kalin\":4.89}}},\"groups\":{}}}}"));
        Config config = new Config(log);

        config.loadUsers(reader);

        assertThrows(IncorrectCommandUsageException.class,() -> UserRegistry.getInstance().doesExist("Tomi"),
                "Expected Registry to contain Tomi ");
    }

    @Test
    void testParseTransactionHistory() {
        JsonReader reader = new JsonReader(new StringReader(
                "{\"transactions\":{\"Pavel\":\"\",\"Grom\":\"2023-02-12T00:36:56.747170200:Tomi took split 5.0 EUR for bananas\\r\\n\"}}"));
        Config config = new Config(log);
        config.loadTransactions(reader);
        assertNotEquals("No transactions logged!",UserRegistry.getInstance().getTransaction("Grom")
                ,"Expected Grom to have a transaction in the history");
    }
}