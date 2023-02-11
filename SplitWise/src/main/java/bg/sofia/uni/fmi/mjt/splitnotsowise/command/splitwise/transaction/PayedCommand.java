package bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.transaction;

import bg.sofia.uni.fmi.mjt.splitnotsowise.command.Command;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.CommandRunner;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository.ConnectionObserver;
import bg.sofia.uni.fmi.mjt.splitnotsowise.log.Logger;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.Validator;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.OutputCreator;

import java.math.BigDecimal;

public class PayedCommand implements Command {

    private static final String SUCCESS = "Successfully accepted payment" + System.lineSeparator();
    public static final int ARGUMENT_COUNT = 4;

    private static final int SENDER = 2;
    private static final int AMOUNT = 1;
    private final String[] args;
    private final String socketChannel;
    public PayedCommand(String[] args, String socketChannel) {
        Validator.checkIfNullArray(args);
        Validator.checkIfNull(socketChannel);
        Validator.isBlank(socketChannel);

        this.args = args;
        this.socketChannel = socketChannel;
    }

    @Override
    public String execute(Logger logger) {
        try {
            CommandRunner.checkAmount(args[AMOUNT], socketChannel, args[SENDER]);
            BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(args[AMOUNT]));
            String username = ConnectionObserver.getUserName(socketChannel);
            CommandRunner.updateRepoWithPayed(username, args[SENDER], amount);
            return SUCCESS;
        } catch (Exception e) {
            logger.log(OutputCreator.getFullExceptionMessage(e), logger.getLogWriter());
            return e.getMessage();
        }
    }
}
