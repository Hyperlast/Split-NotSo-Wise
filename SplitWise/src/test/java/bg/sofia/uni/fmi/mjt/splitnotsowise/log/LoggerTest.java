package bg.sofia.uni.fmi.mjt.splitnotsowise.log;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LoggerTest {
    private Logger logger = Logger.builder("Stoyo","Boyo")
            .setHandlerThrows(false)
            .setLogs(true)
            .setCreatesFile(false).build();

    @Test
    void testLog() throws IOException {
        try (StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw)) {
            logger.log("Stoyo boyo random string", pw);

            assertTrue(sw.toString().contains("Stoyo boyo random string"),
                    "Expected message to be logged");
        }

    }
}