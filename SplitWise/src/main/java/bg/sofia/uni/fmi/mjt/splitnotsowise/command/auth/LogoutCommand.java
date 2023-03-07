package bg.sofia.uni.fmi.mjt.splitnotsowise.command.auth;

import bg.sofia.uni.fmi.mjt.splitnotsowise.command.Command;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository.ConnectionObserver;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.Validator;

public class LogoutCommand implements Command {
    private static final String UNSUCCESSFUL = "You are currently not logged in" + System.lineSeparator();
    private static final String SUCCESSFUL = "Successful Logout" + System.lineSeparator();
    private final String socketChannel;
    public LogoutCommand(String socketChannel) {
        Validator.checkIfNull(socketChannel);
        Validator.isBlank(socketChannel);

        this.socketChannel = socketChannel;
    }

    @Override
    public String execute() {
        if (!ConnectionObserver.removeConnection(socketChannel)) {
            return UNSUCCESSFUL;
        }

        return SUCCESSFUL;
    }
}
