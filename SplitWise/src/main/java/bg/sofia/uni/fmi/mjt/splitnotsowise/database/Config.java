package bg.sofia.uni.fmi.mjt.splitnotsowise.database;

import bg.sofia.uni.fmi.mjt.splitnotsowise.database.entity.ClientHistory;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.entity.UserParsed;
import bg.sofia.uni.fmi.mjt.splitnotsowise.log.Logger;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository.UserRegistry;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.Validator;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;


import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.DEFAULT_LOG_DIR_PATH;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.DEFAULT_TRANSACTION_FILE_NAME;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.DEFAULT_USER_FILE_NAME;

public class Config {

    public static final String TRANSACTION_CONFIG_PATH = DEFAULT_LOG_DIR_PATH  + DEFAULT_TRANSACTION_FILE_NAME ;
    public static final String USER_CONFIG_PATH = DEFAULT_LOG_DIR_PATH + DEFAULT_USER_FILE_NAME;

    private static final String PATH_ERROR_PREFIX = "Couldn't create ";
    private UserParsed userInfo;
    private ClientHistory history;

    private static final Gson GSON = new Gson();

    private final Logger logger;

    public Config(Logger logger) {
        this.logger = logger;
        userInfo = new UserParsed(new HashMap<>());
        history = new ClientHistory(new HashMap<>());
    }


    public void loadUsers(JsonReader reader) {
        Validator.checkIfNull(reader);
        userInfo = GSON.fromJson(reader, UserParsed.class);

        if (userInfo == null) {
            return;
        }
        System.out.println(userInfo);
        UserRegistry.getInstance().importLoadedUserInfo(userInfo.userInfo());
    }

    public void loadTransactions(JsonReader reader) {
        Validator.checkIfNull(reader);
        history = GSON.fromJson(reader, ClientHistory.class);

        if (history == null) {
            return;
        }

        UserRegistry.getInstance().importLoadedTransactions(history.transactions());
    }

    public void configFiles() {
        createFiles(TRANSACTION_CONFIG_PATH);
        createFiles(USER_CONFIG_PATH);
    }

    private void createFiles(String path) {
        Path filePath = Path.of(path);

        if (!Files.exists(filePath)) {
            try {
                Files.createFile(filePath);
            } catch (IOException e) {
                logger.log(LocalDateTime.now(), PATH_ERROR_PREFIX + path, logger.getLogWriter());
            }
        }
    }

}
