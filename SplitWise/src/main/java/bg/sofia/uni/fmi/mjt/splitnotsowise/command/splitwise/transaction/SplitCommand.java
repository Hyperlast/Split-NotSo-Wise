package bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.transaction;

import bg.sofia.uni.fmi.mjt.splitnotsowise.command.CommandRunner;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.Command;

import bg.sofia.uni.fmi.mjt.splitnotsowise.log.Logger;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository.ConnectionObserver;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.OutputCreator;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.Validator;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SplitCommand implements Command {
    private static final String SUCCESS = "Successfully split";
    public static final int ARGUMENT_COUNT = 4;
    public static final int AMOUNT = 1;
    public static final int RECEIVER = 2;
    public static final int REASON = 3;
    private final String[] args;
    private final String socketChannel;

    public SplitCommand(String[] args, String socketChannel) {
        Validator.checkIfNullArray(args);
        Validator.checkIfNull(socketChannel);
        Validator.isBlank(socketChannel);

        this.args = args;
        this.socketChannel = socketChannel;
    }

    @Override
    public String execute(Logger logger) {
        try {
            CommandRunner.checkAmount(args[AMOUNT], socketChannel, args[RECEIVER]);
            BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(args[AMOUNT]));
            Validator.checkAmount(amount);
            String username = ConnectionObserver.getUserName(socketChannel);
            CommandRunner.updateRepoWithSplit(username, args[RECEIVER], amount, args[REASON]);

            return SUCCESS;
        } catch (Exception e) {
            logger.log(LocalDateTime.now(), OutputCreator.getFullExceptionMessage(e), logger.getLogWriter());
            return e.getMessage();
        }
    }
}
