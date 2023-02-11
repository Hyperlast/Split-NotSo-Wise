package bg.sofia.uni.fmi.mjt.splitnotsowise.command;

import bg.sofia.uni.fmi.mjt.splitnotsowise.log.Logger;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository.UserRegistry;

public interface Command {
    final UserRegistry REGISTRY = UserRegistry.getInstance();
    String execute(Logger logger);

}
