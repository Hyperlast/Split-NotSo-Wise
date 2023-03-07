package bg.sofia.uni.fmi.mjt.splitnotsowise.server;

import bg.sofia.uni.fmi.mjt.splitnotsowise.command.Command;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.CommandParser;


public class InputHandler {

    public String executeInput(String command, String socketChannel) {
        Command userCommand = CommandParser.of(command, socketChannel);

        return userCommand.execute();
    }
}
