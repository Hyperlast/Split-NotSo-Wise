package bg.sofia.uni.fmi.mjt.splitnotsowise.command.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnknownCommandTest {
    @Test
    void testUnknownCommand() {
        assertEquals("bob",new UnknownCommand("bob").execute(null), "expected equal messages");
    }
}