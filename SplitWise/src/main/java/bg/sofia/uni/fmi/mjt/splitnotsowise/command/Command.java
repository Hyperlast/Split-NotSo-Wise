package bg.sofia.uni.fmi.mjt.splitnotsowise.command;

import bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository.UserRegistry;
import bg.sofia.uni.fmi.mjt.splitnotsowise.log.Logger;

public interface Command {
    UserRegistry REGISTRY = UserRegistry.getInstance();
    String execute(Logger logger);

}
