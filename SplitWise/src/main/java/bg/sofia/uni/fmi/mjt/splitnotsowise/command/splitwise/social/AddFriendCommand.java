package bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.social;

import bg.sofia.uni.fmi.mjt.splitnotsowise.command.Command;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.CommandRunner;
import bg.sofia.uni.fmi.mjt.splitnotsowise.log.Logger;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository.ConnectionObserver;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.OutputCreator;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.Validator;

import java.time.LocalDateTime;

public class AddFriendCommand implements Command {

    private static final String SUCCESS = "Successfully added %s to friends" + System.lineSeparator();
    private static final String UNSUCCESSFUL = "You cannot add yourself as a friend" + System.lineSeparator();
    public static final int ARGUMENT_COUNT = 2;
    public static final int FRIEND_USERNAME = 1;
    private final String[] args;
    private final String socketChannel;

    public AddFriendCommand(String[] args, String socketChannel) {
        Validator.checkIfNullArray(args);
        Validator.checkIfNull(socketChannel, args[FRIEND_USERNAME]);
        Validator.isBlank(socketChannel, args[FRIEND_USERNAME]);
        this.args = args;
        this.socketChannel = socketChannel;
    }

    @Override
    public String execute(Logger logger) {

        try {
            ConnectionObserver.isLogged(socketChannel);
            String initiatorUsername = ConnectionObserver.getUserName(socketChannel);

            if (initiatorUsername.equals(args[FRIEND_USERNAME])) {
                return UNSUCCESSFUL;
            }
            REGISTRY.doesNotExist(args[FRIEND_USERNAME]);

            CommandRunner.updateRepoWithAddFriend(initiatorUsername, args[FRIEND_USERNAME]);
            return String.format(SUCCESS, args[FRIEND_USERNAME]);

        } catch (Exception e) {
            logger.log(LocalDateTime.now(), OutputCreator.getFullExceptionMessage(e), logger.getLogWriter());
            return e.getMessage();
        }
    }
}
