package bg.sofia.uni.fmi.mjt.splitnotsowise.command.auth;

import bg.sofia.uni.fmi.mjt.splitnotsowise.command.Command;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.CommandRunner;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.entity.User;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository.ConnectionObserver;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.PasswordUtils;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.Validator;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.OutputCreator;

public class LoginCommand implements Command {

    private static final String SUCCESS = "Successful Login" + System.lineSeparator();
    public static final int ARGUMENT_COUNT = 3;
    public static final int USERNAME = 1;
    public static final int PASSWORD = 2;
    private final String socketChannel;
    private final String[] parameters;
    public LoginCommand(String[] parameters, String socketChannel) {
        Validator.checkIfNullArray(parameters);
        Validator.checkIfNull(socketChannel);
        Validator.isBlank(socketChannel);

        this.parameters = parameters;
        this.socketChannel = socketChannel;
    }

    @Override
    public String execute() {
        try {
            ConnectionObserver.isNotLogged(socketChannel);

            String  hashedPassword = PasswordUtils.hashPass(parameters[PASSWORD], PasswordUtils.getSalt());

            REGISTRY.isPasswordCorrect(parameters[USERNAME], hashedPassword);

            ConnectionObserver.addConnection(socketChannel, parameters[USERNAME]);

            User user = REGISTRY.getUserEntity(parameters[USERNAME]);
            String notifications = CommandRunner.getNotifications(user);
            CommandRunner.updateUserInfo();

            return SUCCESS + notifications;
        } catch (Exception e) {
            LOGGER.log(OutputCreator.getFullExceptionMessage(e), LOGGER.getLogWriter());
            return e.getMessage();
        }

    }
}
