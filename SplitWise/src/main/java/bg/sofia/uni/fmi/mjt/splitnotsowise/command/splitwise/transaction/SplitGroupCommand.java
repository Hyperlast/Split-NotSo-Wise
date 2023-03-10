package bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.transaction;

import bg.sofia.uni.fmi.mjt.splitnotsowise.command.Command;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.CommandParser;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.CommandRunner;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.entity.User;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository.ConnectionObserver;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.Validator;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.OutputCreator;

import java.math.BigDecimal;
import java.util.Arrays;

public class SplitGroupCommand implements Command {

    private static final String SUCCESS = "SuccessFull payment";
    public static final int ARGUMENT_COUNT = 4;
    private static final int AMOUNT = 1;
    private static final int GROUP_NAME = 2;
    private static final int REASON = 3;
    private final String[] args;
    private final String socketChannel;

    public SplitGroupCommand(String[] args, String socketChannel) {
        Validator.checkIfNullArray(args);
        Validator.checkIfNull(socketChannel);
        Validator.isBlank(socketChannel);

        this.args = args;
        this.socketChannel = socketChannel;
    }

    @Override
    public String execute() {

        try {
            CommandParser.checkIfCorrect(args[AMOUNT]);
            ConnectionObserver.isLogged(socketChannel);

            if (!REGISTRY.doesGroupExist(args[GROUP_NAME])) {
                return "Group doesn't exist";
            }

            BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(args[AMOUNT]));

            User user = CommandRunner.getUser(socketChannel);

            CommandRunner.updateRepoSplitWithGroup(args[GROUP_NAME], user, amount, String.join(" ",
                    Arrays.copyOfRange(args, REASON, args.length)));

            return SUCCESS;
        } catch (Exception e) {
            LOGGER.log(OutputCreator.getFullExceptionMessage(e), LOGGER.getLogWriter());
            return e.getMessage();
        }
    }
}
