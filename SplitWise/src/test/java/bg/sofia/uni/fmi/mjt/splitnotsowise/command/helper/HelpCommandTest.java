package bg.sofia.uni.fmi.mjt.splitnotsowise.command.helper;

import org.junit.jupiter.api.Test;

import static bg.sofia.uni.fmi.mjt.splitnotsowise.command.helper.HelpCommand.HELP_MSG;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HelpCommandTest {

    @Test
    void testHelp(){
        HelpCommand command = new HelpCommand();
        assertEquals(HELP_MSG, command.execute(null), "expected equal messages");
    }
}