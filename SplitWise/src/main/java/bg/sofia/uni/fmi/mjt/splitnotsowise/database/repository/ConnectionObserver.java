package bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository;

import bg.sofia.uni.fmi.mjt.splitnotsowise.exception.IncorrectCommandUsageException;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.Validator;

import java.util.HashMap;
import java.util.Map;

public final class ConnectionObserver {
    private static final Map<String, String> CONNECTED_USERS = new HashMap<>();

    private ConnectionObserver() { }

    public static void addConnection(String socketName, String username) throws IncorrectCommandUsageException {
        Validator.checkIfNull(socketName, username);
        Validator.isBlank(socketName, username);

        if (CONNECTED_USERS.containsKey(socketName) && CONNECTED_USERS.get(socketName) != null) {
            throw new IncorrectCommandUsageException("You are already logged in");
        }

        CONNECTED_USERS.put(socketName, username);

    }

    public static boolean removeConnection(String socketName) {
        Validator.checkIfNull(socketName);
        Validator.isBlank(socketName);

        if (!CONNECTED_USERS.containsKey(socketName)) {
            return false;
        }
        CONNECTED_USERS.remove(socketName);
        return true;
    }

    public static void isLogged(String socketName) throws IncorrectCommandUsageException {
        Validator.checkIfNull(socketName);
        if (!CONNECTED_USERS.containsKey(socketName) || CONNECTED_USERS.get(socketName) == null) {
            throw new IncorrectCommandUsageException("Not logged in" + System.lineSeparator());
        }
    }

    public static void isNotLogged(String socketName) throws IncorrectCommandUsageException {
        Validator.checkIfNull(socketName);
        if (CONNECTED_USERS.containsKey(socketName) && CONNECTED_USERS.get(socketName) != null) {
            throw new IncorrectCommandUsageException("You are logged in, try to logout and type the command again!"
                    + System.lineSeparator());
        }
    }

    public static String getUserName(String socketName) throws IncorrectCommandUsageException {
        Validator.checkIfNull(socketName);
        Validator.isBlank(socketName);
        isLogged(socketName);

        return CONNECTED_USERS.get(socketName);
    }

}
