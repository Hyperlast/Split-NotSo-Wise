package bg.sofia.uni.fmi.mjt.splitnotsowise.command.auth;

import bg.sofia.uni.fmi.mjt.splitnotsowise.command.Command;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository.ConnectionObserver;
import bg.sofia.uni.fmi.mjt.splitnotsowise.log.Logger;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.OutputCreator;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.PasswordUtils;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.Validator;


import java.time.LocalDateTime;

public class RegisterCommand implements Command {

    private static final String SUCCESS = "Successful registry" + System.lineSeparator();
    public static final int USERNAME = 1;
    public static final int PASSWORD = 2;
    public static final int ARGUMENT_COUNT = 3;

    private final String[] parameters;
    private final String socketChannel;
    public RegisterCommand(String[] parameters, String socketChannel) {
        Validator.checkIfNullArray(parameters);
        Validator.checkIfNull(socketChannel);
        Validator.isBlank(socketChannel);

        this.parameters = parameters;
        this.socketChannel = socketChannel;
    }
    @Override
    public String execute(Logger logger) {
        try {
            ConnectionObserver.isNotLogged(socketChannel);
            PasswordUtils.verifyPasswordRegex(parameters[PASSWORD]);
            REGISTRY.doesExist(parameters[USERNAME]);
            REGISTRY.addUserToRegistry(parameters[USERNAME],  PasswordUtils.hashPass(parameters[PASSWORD],
                    PasswordUtils.getSalt()));

            return SUCCESS;
        } catch (Exception e) {
            logger.log(LocalDateTime.now(), OutputCreator.getFullExceptionMessage(e), logger.getLogWriter());
            return e.getMessage();
        }
    }

}
