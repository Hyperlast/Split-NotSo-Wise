package bg.sofia.uni.fmi.mjt.splitnotsowise.command;

import bg.sofia.uni.fmi.mjt.splitnotsowise.command.auth.LoginCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.auth.LogoutCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.auth.RegisterCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.helper.HelpCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.helper.UnknownCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.misc.StatusCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.misc.TransactionCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.social.AddFriendCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.social.CreateGroupCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.transaction.PayedCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.transaction.SplitCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.transaction.SplitGroupCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.transaction.SwitchCurrencyCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.exception.UnrecognizedValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandParserTest {
    private static final String DEFAULT_SOCKET_NAME = "null";
    private static final String DEFAULT_COMMAND_NAME = "UNKNOWN";
    private String arg;

    @Test
    void testParseNullArgs() {
        assertThrows(IllegalArgumentException.class, () -> CommandParser.of(null, null),
                "Expected method to throw exception when called with null args ");

        assertThrows(IllegalArgumentException.class, () -> CommandParser.of(null, DEFAULT_SOCKET_NAME),
                "Expected method to throw exception when called with null args ");

        assertThrows(IllegalArgumentException.class, () -> CommandParser.of(DEFAULT_COMMAND_NAME, null),
                "Expected method to throw exception when called with null args ");
    }

    @Test
    void testParseBlankArgs() {
        assertThrows(IllegalArgumentException.class, () -> CommandParser.of(" ", "  "),
                "Expected method to throw exception when called with Blank args ");

        assertThrows(IllegalArgumentException.class, () -> CommandParser.of(" ", DEFAULT_SOCKET_NAME),
                "Expected method to throw exception when called with Blank args ");

        assertThrows(IllegalArgumentException.class, () -> CommandParser.of(DEFAULT_COMMAND_NAME, " "),
                "Expected method to throw exception when called with Blank args ");
    }

    @Test
    void testParseUnknownCommand() {
        arg = "dab";
        assertTrue(CommandParser.of(arg, DEFAULT_SOCKET_NAME) instanceof UnknownCommand,
                "Expected instance of Unknown command when passing \"" + arg + "\"");
    }

    @Test
    void testParseLoginLessArgs() {
        arg = "login";
        assertTrue(CommandParser.of(arg, DEFAULT_SOCKET_NAME) instanceof UnknownCommand,
                "Expected instance of Unknown command when passing \"" + arg + "\"");
    }

    @Test
    void testParseLogin() {
        arg = "login name password";
        assertTrue(CommandParser.of(arg, DEFAULT_SOCKET_NAME) instanceof LoginCommand,
                "Expected instance of Login command when passing \"" + arg + "\"");
    }

    @Test
    void testParseLogoutArgs() {
        arg = "logout random strings";
        assertTrue(CommandParser.of(arg, DEFAULT_SOCKET_NAME) instanceof LogoutCommand,
                "Expected instance of Logout command when passing \"" + arg + "\"");
    }

    @Test
    void testParseLogout() {
        arg = "logout";
        assertTrue(CommandParser.of(arg, DEFAULT_SOCKET_NAME) instanceof LogoutCommand,
                "Expected instance of Logout command when passing \"" + arg + "\"");
    }

    @Test
    void testParseHelp() {
        arg = "help";
        assertTrue(CommandParser.of(arg, DEFAULT_SOCKET_NAME) instanceof HelpCommand,
                "Expected instance of Help command when passing \"" + arg + "\"");
    }

    @Test
    void testParseRegisterLessArgs() {
        arg = "register";
        assertTrue(CommandParser.of(arg, DEFAULT_SOCKET_NAME) instanceof UnknownCommand,
                "Expected instance of Unknown command when passing \"" + arg + "\"");
    }

    @Test
    void testParseRegister() {
        arg = "register name password";
        assertTrue(CommandParser.of(arg, DEFAULT_SOCKET_NAME) instanceof RegisterCommand,
                "Expected instance of register command when passing \"" + arg + "\"");
    }

    @Test
    void testParseAddFriendLessArgs() {
        arg = "add-friend";
        assertTrue(CommandParser.of(arg, DEFAULT_SOCKET_NAME) instanceof UnknownCommand,
                "Expected instance of Unknown command when passing \"" + arg + "\"");
    }

    @Test
    void testParseAddFriend() {
        arg = "add-friend name";
        assertTrue(CommandParser.of(arg, DEFAULT_SOCKET_NAME) instanceof AddFriendCommand,
                "Expected instance of AddFriend command when passing \"" + arg + "\"");
    }

    @Test
    void testParseSplitLessArgs() {
        arg = "split ameno dorime";
        assertTrue(CommandParser.of(arg, DEFAULT_SOCKET_NAME) instanceof UnknownCommand,
                "Expected instance of Unknown command when passing \"" + arg + "\"");
    }

    @Test
    void testParseSplit() {
        arg = "split 5 ne6to qko";
        assertTrue(CommandParser.of(arg, DEFAULT_SOCKET_NAME) instanceof SplitCommand,
                "Expected instance of AddFriend command when passing \"" + arg + "\"");
    }

    @Test
    void testParseCreateGroupLessArgs() {
        arg = "create-group ameno";
        assertTrue(CommandParser.of(arg, DEFAULT_SOCKET_NAME) instanceof UnknownCommand,
                "Expected instance of Unknown command when passing \"" + arg + "\"");
    }

    @Test
    void testParseCreateGroup() {
        arg = "create-group ameno dorime";
        assertTrue(CommandParser.of(arg, DEFAULT_SOCKET_NAME) instanceof CreateGroupCommand,
                "Expected instance of AddFriend command when passing \"" + arg + "\"");
    }

    @Test
    void testParseSplitGroupLessArgs() {
        arg = "split-group 5 ameno";
        assertTrue(CommandParser.of(arg, DEFAULT_SOCKET_NAME) instanceof UnknownCommand,
                "Expected instance of Unknown command when passing \"" + arg + "\"");
    }

    @Test
    void testParseSplitGroup() {
        arg = "split-group 5 ameno dorime";
        assertTrue(CommandParser.of(arg, DEFAULT_SOCKET_NAME) instanceof SplitGroupCommand,
                "Expected instance of AddFriend command when passing \"" + arg + "\"");
    }

    @Test
    void testParseStatus() {
        arg = "status";
        assertTrue(CommandParser.of(arg, DEFAULT_SOCKET_NAME) instanceof StatusCommand,
                "Expected instance of Status command when passing \"" + arg + "\"");
    }

    @Test
    void testParsePayedLessArgs() {
        arg = "payed 5 lmao";
        assertTrue(CommandParser.of(arg, DEFAULT_SOCKET_NAME) instanceof UnknownCommand,
                "Expected instance of Unknown command when passing \"" + arg + "\"");
    }

    @Test
    void testParsePayed() {
        arg = "payed 5 lmao dorime";
        assertTrue(CommandParser.of(arg, DEFAULT_SOCKET_NAME) instanceof PayedCommand,
                "Expected instance of AddFriend command when passing \"" + arg + "\"");
    }

    @Test
    void testParseHistory() {
        arg = "history";
        assertTrue(CommandParser.of(arg, DEFAULT_SOCKET_NAME) instanceof TransactionCommand,
                "Expected instance of Transaction command when passing \"" + arg + "\"");
    }


    @Test
    void testParseSwitchLessArgs() {
        arg = "switch-currency";
        assertTrue(CommandParser.of(arg, DEFAULT_SOCKET_NAME) instanceof UnknownCommand,
                "Expected instance of Unknown command when passing \"" + arg + "\"");
    }

    @Test
    void testParseSwitch() {
        arg = "switch-currency USD";
        assertTrue(CommandParser.of(arg, DEFAULT_SOCKET_NAME) instanceof SwitchCurrencyCommand,
                "Expected instance of AddFriend command when passing \"" + arg + "\"");
    }

    @Test
    void testCheckIfCorrectIllegalAmount() {
        assertThrows(UnrecognizedValueException.class, () -> CommandParser.checkIfCorrect(DEFAULT_SOCKET_NAME),
                "Expected exception to be thrown when checking amount ");
    }
}
