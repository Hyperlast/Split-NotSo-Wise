package bg.sofia.uni.fmi.mjt.splitnotsowise.server;

import bg.sofia.uni.fmi.mjt.splitnotsowise.command.Command;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.CommandParser;
import bg.sofia.uni.fmi.mjt.splitnotsowise.log.Logger;

public class InputHandler {

    private final Logger serverLogger;

    public InputHandler(Logger serverLogger) {
        this.serverLogger = serverLogger;
    }

    public String executeInput(String command, String socketChannel) {
        Command userCommand = CommandParser.of(command, socketChannel);

        return userCommand.execute(serverLogger);
    }
}
