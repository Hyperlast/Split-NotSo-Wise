package bg.sofia.uni.fmi.mjt.splitnotsowise.server;


import bg.sofia.uni.fmi.mjt.splitnotsowise.log.Logger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class InputHandlerTest {

    @Test
    void testInputHandlerWithUnknownCommand() {
        Logger log = mock(Logger.class);
        InputHandler handler = new InputHandler(log);

        assertEquals("Unknown command: balbafdlkb" + System.lineSeparator(),handler.executeInput("balbafdlkb", "Me"),
                "Expected Unknown command message");
    }
}