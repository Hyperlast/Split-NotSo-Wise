package bg.sofia.uni.fmi.mjt.splitnotsowise.command;

import bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository.UserRegistry;
import bg.sofia.uni.fmi.mjt.splitnotsowise.log.Logger;

import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.DEFAULT_LOG_DIR_PATH;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.DEFAULT_LOG_LOGS;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.DEFAULT_LOG_THROW;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.DEFAULT_SERVER_FILE_NAME;

public interface Command {
    UserRegistry REGISTRY = UserRegistry.getInstance();

    Logger LOGGER = Logger.builder(DEFAULT_LOG_DIR_PATH, DEFAULT_SERVER_FILE_NAME)
            .setHandlerThrows(DEFAULT_LOG_THROW)
            .setLogs(DEFAULT_LOG_LOGS)
            .build();
    String execute();


}
