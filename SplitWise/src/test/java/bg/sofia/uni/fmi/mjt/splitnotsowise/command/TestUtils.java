package bg.sofia.uni.fmi.mjt.splitnotsowise.command;

import bg.sofia.uni.fmi.mjt.splitnotsowise.log.Logger;

import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.SPACE_DELIMITER;

public class TestUtils {
    public static final String[] registerArgs = "register admin Admin1!".split(SPACE_DELIMITER);
    public static final String[] loginArgs = "login admin Admin1!".split(SPACE_DELIMITER);
    public static final Logger logger = Logger.builder("path","path")
        .setHandlerThrows(false)
        .setLogs(false)
        .build();
    public static final String ADMIN_ADDRESS = "ADDRESS";
}
