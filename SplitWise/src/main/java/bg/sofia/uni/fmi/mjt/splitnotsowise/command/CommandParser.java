package bg.sofia.uni.fmi.mjt.splitnotsowise.command;

import bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.social.AddFriendCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.social.CreateGroupCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.auth.LoginCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.auth.LogoutCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.auth.RegisterCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.helper.HelpCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.helper.UnknownCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.misc.TransactionCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.transaction.PayedCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.transaction.SplitCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.transaction.SplitGroupCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.misc.StatusCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.transaction.SwitchCurrencyCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.exception.UnrecognizedValueException;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.Validator;

import java.util.Arrays;

import static bg.sofia.uni.fmi.mjt.splitnotsowise.command.helper.UnknownCommand.GENERIC_ARGUMENTS_MESSAGE;

public class CommandParser {
    private static final String PAYMENT_AMOUNT_REGEX = "[0-9]+\\.?[0-9]*";
    private static final String DELIMITER = " ";
    private static final int COMMAND = 0;

    public enum AvailableCommands {
        LOGIN("login"),
        REGISTER("register"),
        ADD("add-friend"),
        CREATE("create-group"),
        SPLIT("split"),
        GROUPSPLIT("split-group"),
        STATUS("status"),
        PAYED("payed"),
        LOGOUT("logout"),

        HELP("help"),

        HISTORY("history"),

        SWITCH_CURRENCY("switch-currency"),

        UNKNOWN("unknown");
        private final String name;

        AvailableCommands(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
    private CommandParser() { }


    public static Command of(String command, String socketChannel) {
        Validator.checkIfNull(command, socketChannel);
        Validator.isBlank(command, socketChannel);

        String[] arguments = command.split(DELIMITER);

        return switch (getCommandType(arguments[COMMAND])) {
            case LOGIN -> parseLogin(arguments, socketChannel);
            case REGISTER -> parseRegister(arguments, socketChannel);
            case ADD -> parseAddFriend(arguments, socketChannel);
            case SPLIT -> parseSplit(arguments, socketChannel);
            case CREATE -> parseCreateGroup(arguments, socketChannel);
            case GROUPSPLIT -> parseSplitGroup(arguments, socketChannel);
            case STATUS -> parseStatus(socketChannel);
            case PAYED -> parsePayed(arguments, socketChannel);
            case HELP -> parseHelp();
            case HISTORY -> parseHistory(socketChannel);
            case LOGOUT -> parseLogout(socketChannel);
            case SWITCH_CURRENCY -> parseSwitch(socketChannel, arguments);
            default -> new UnknownCommand("Unknown command: " + arguments[COMMAND] + System.lineSeparator());
        };
    }

    private static Command parseSwitch(String socketChannel, String[] arguments) {
        final int currency = 1;
        if (Validator.checkSpecificArgCount(arguments, SwitchCurrencyCommand.ARGUMENT_COUNT)) {
            return new SwitchCurrencyCommand(socketChannel, arguments[currency]);
        }
        return new UnknownCommand("Switch" + GENERIC_ARGUMENTS_MESSAGE);
    }

    private static Command parseHelp() {
        return new HelpCommand();
    }

    private static Command parseHistory(String socketChannel) {
        return new TransactionCommand(socketChannel);
    }

    private static Command parseLogout(String socketChannel) {
        return new LogoutCommand(socketChannel);
    }

    private static Command parsePayed(String[] args, String socketChannel) {
        if (Validator.checkArgCount(args, PayedCommand.ARGUMENT_COUNT)) {
            return new PayedCommand(args, socketChannel);
        }
        return new UnknownCommand("Payed " + GENERIC_ARGUMENTS_MESSAGE);
    }

    private static Command parseStatus(String socketChannel) {
        return new StatusCommand(socketChannel);
    }


    private static Command parseSplitGroup(String[] args, String socketChannel) {
        if (Validator.checkArgCount(args, SplitGroupCommand.ARGUMENT_COUNT)) {
            return new SplitGroupCommand(args, socketChannel);
        }
        return new UnknownCommand("SplitGroup " + GENERIC_ARGUMENTS_MESSAGE);
    }

    private static Command parseCreateGroup(String[] args, String socketChannel) {
        if (Validator.checkArgCount(args, CreateGroupCommand.ARGUMENT_COUNT)) {
            return new CreateGroupCommand(args, socketChannel);
        }

        return new UnknownCommand("Create-Group " + GENERIC_ARGUMENTS_MESSAGE);
    }

    private static Command parseSplit(String[] args, String socketChannel) {
        if (Validator.checkArgCount(args, SplitCommand.ARGUMENT_COUNT)) {
            return new SplitCommand(args, socketChannel);
        }
        return new UnknownCommand("Split " + GENERIC_ARGUMENTS_MESSAGE);
    }

    private static Command parseAddFriend(String[] args, String socketChannel) {
        if (Validator.checkSpecificArgCount(args, AddFriendCommand.ARGUMENT_COUNT)) {
            return new AddFriendCommand(args, socketChannel);
        }
        return new UnknownCommand("Add Friend Command " + GENERIC_ARGUMENTS_MESSAGE);
    }

    private static Command parseRegister(String[] args, String socketChannel) {
        if (Validator.checkSpecificArgCount(args, RegisterCommand.ARGUMENT_COUNT)) {
            return new RegisterCommand(args, socketChannel);
        }
        return new UnknownCommand("Register " + GENERIC_ARGUMENTS_MESSAGE);
    }

    private static Command parseLogin(String[] args, String socketChannel) {
        if (Validator.checkSpecificArgCount(args, LoginCommand.ARGUMENT_COUNT)) {
            return new LoginCommand(args, socketChannel);
        }
        return new UnknownCommand("Login " + GENERIC_ARGUMENTS_MESSAGE);
    }

    private static AvailableCommands getCommandType(String command) {
        return Arrays.stream(AvailableCommands.values())
                .filter(type -> command.equalsIgnoreCase(type.getName()))
                .findFirst()
                .orElse(AvailableCommands.UNKNOWN);
    }

    public static void checkIfCorrect(String amount) throws UnrecognizedValueException {
        if (!amount.matches(PAYMENT_AMOUNT_REGEX)) {
            throw new UnrecognizedValueException("Incorrect amount representation");
        }
    }
}
