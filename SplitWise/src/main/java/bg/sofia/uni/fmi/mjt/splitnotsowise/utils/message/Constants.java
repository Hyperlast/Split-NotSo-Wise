package bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message;


import java.io.File;

public final class Constants {

    public static final String SPACE_DELIMITER = " ";
    public static final String SEPARATOR = File.separator;
    public static final String DEFAULT_SERVER_FILE_NAME = "logs_server_%d.txt";
    public static final String DEFAULT_CLIENT_FILE_NAME = "logs_client_%d.txt";

    public static final String DEFAULT_TRANSACTION_FILE_NAME = "transactions.json";
    public static final String DEFAULT_USER_FILE_NAME = "users.json";
    public static final String DEFAULT_EXCHANGE_CONFIG_FILE_NAME = "api_key.txt";

    public static final File DIR = new File(".");

    public static final String DEFAULT_LOG_DIR_PATH = DIR.getAbsolutePath() + SEPARATOR +
            "src"
            + SEPARATOR
            + "main"
            + SEPARATOR
            + "resources"
            + SEPARATOR;
    public static final boolean DEFAULT_LOG_THROW = false;

    public static final boolean DEFAULT_LOG_LOGS = true;

    private Constants() { }
}
