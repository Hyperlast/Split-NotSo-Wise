package bg.sofia.uni.fmi.mjt.splitnotsowise.command.helper;

import bg.sofia.uni.fmi.mjt.splitnotsowise.command.Command;
import bg.sofia.uni.fmi.mjt.splitnotsowise.log.Logger;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.Validator;

public class UnknownCommand implements Command {

    public static final String GENERIC_ARGUMENTS_MESSAGE = " command passed with Illegal argument count!"
            + System.lineSeparator() ;

    private final String message;

    public UnknownCommand(String message) {
        Validator.checkIfNull(message);
        Validator.isBlank(message);

        this.message = message;
    }

    @Override
    public String execute(Logger logger) {
        return message;
    }
}
