package bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.social.misc;

import bg.sofia.uni.fmi.mjt.splitnotsowise.command.Command;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.CommandRunner;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.entity.User;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository.ConnectionObserver;
import bg.sofia.uni.fmi.mjt.splitnotsowise.log.Logger;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.Validator;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.OutputCreator;

public class StatusCommand implements Command {

    private final String socketChannel;
    public StatusCommand(String socketChannel) {
        Validator.checkIfNull(socketChannel);
        Validator.isBlank(socketChannel);
        this.socketChannel = socketChannel;
    }

    @Override
    public String execute() {
        try {
            ConnectionObserver.isLogged(socketChannel);
            User user = CommandRunner.getUser(socketChannel);
            return CommandRunner.getStatus(user);
        } catch (Exception e) {
            LOGGER.log(OutputCreator.getFullExceptionMessage(e), LOGGER.getLogWriter());
            return e.getMessage();
        }
    }
}
