package bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository;

import bg.sofia.uni.fmi.mjt.splitnotsowise.command.Command;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.entity.ClientHistory;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.entity.User;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.entity.UserParsed;
import bg.sofia.uni.fmi.mjt.splitnotsowise.exception.IncorrectCommandUsageException;
import bg.sofia.uni.fmi.mjt.splitnotsowise.exception.NoSuchEntityException;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.Validator;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.splitnotsowise.command.CommandRunner.updateTransaction;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.command.CommandRunner.updateUserInfo;

public class UserRegistry {

    private static UserRegistry registry;
    private static final String EMPTY_NOTIFICATION_MESSAGE = "No transactions logged!";
    private static final String COULD_NOT_FIND_PREFIX = "Couldn't find ";
    private static final String EMPTY = "";
    private final Gson gson = new Gson();
    private Map<String, String> userCredentials;
    private Map<String, User> userInfo;
    private Map<String, String> history;
    private Map<String, GroupListManager> groups;

    private UserRegistry() {
        this.userInfo = new HashMap<>();
        this.userCredentials = new HashMap<>();
        this.groups = new HashMap<>();
        this.history = new HashMap<>();
    }

    public static UserRegistry getInstance() {
        if (registry == null) {
            registry = new UserRegistry();
        }

        return registry;
    }

    public String getTransaction(String user) {
        Validator.checkIfNull(user);

        if (history.get(user).isEmpty()) {
            return EMPTY_NOTIFICATION_MESSAGE;
        }

        return history.get(user);
    }

    public void addTransaction(String user, String transaction) {
        history.put(user, history.get(user) + transaction + System.lineSeparator());
    }

    public void addGroup(String groupName, GroupListManager manager) {
        groups.putIfAbsent(groupName, manager);
    }

    public User getUserEntity(String username) throws NoSuchEntityException {
        if (!userInfo.containsKey(username)) {
            throw new NoSuchEntityException(COULD_NOT_FIND_PREFIX + username);
        }

        return userInfo.get(username);
    }

    public GroupListManager getGroupInstance(String name) throws NoSuchEntityException {
        if (!groups.containsKey(name)) {
            throw new NoSuchEntityException(COULD_NOT_FIND_PREFIX + name );
        }
        return groups.get(name);
    }

    public boolean doesGroupExist(String groupName) {
        return groups.containsKey(groupName);
    }

    public void doesExist(String userName) throws IncorrectCommandUsageException {
        Validator.checkIfNull(userName);
        Validator.isBlank(userName);

        if (userCredentials.containsKey(userName)) {
            throw new IncorrectCommandUsageException("User already exists" + System.lineSeparator());
        }

    }

    public void doesNotExist(String userName) throws IncorrectCommandUsageException {
        Validator.checkIfNull(userName);
        Validator.isBlank(userName);

        if (!userCredentials.containsKey(userName)) {
            throw new IncorrectCommandUsageException("User Doesn't exists" + System.lineSeparator());
        }

    }

    public void addUserToRegistry(String username, String hashedPass) throws IOException {
        Validator.checkIfNull(username, hashedPass);
        Validator.isBlank(username, hashedPass);

        userCredentials.put(username, hashedPass);
        userInfo.put(username, new User(username, hashedPass));
        if (!history.containsKey(username)) {
            history.put(username, EMPTY);
        }

        updateUserInfo();
        updateTransaction();
    }

    public void isPasswordCorrect(String username, String password) throws IncorrectCommandUsageException {
        Validator.checkIfNull(username, password);
        Validator.isBlank(username, password);
        doesNotExist(username);

        if (!userCredentials.get(username).equals(password)) {
            throw new IncorrectCommandUsageException("Wrong user info");
        }

    }


    public void importLoadedTransactions(Map<String, String> transactions) {
        if (transactions != null && !transactions.isEmpty()) {
            this.history = transactions;
        }
    }

    public void importLoadedUserInfo(Map<String, User> loader) {
        if (loader != null && !loader.isEmpty()) {
            parseLoader(loader);
        }
    }

    public void updateTransactionsMap(Writer writer) {
        if (!history.isEmpty()) {
            gson.toJson(new ClientHistory(history), ClientHistory.class, writer);
        }
    }

    public void updateUserInfoMap(Writer writer) {
        if (!userInfo.isEmpty()) {
            gson.toJson(new UserParsed(userInfo), UserParsed.class, writer);
        }

    }

    private void parseLoader(Map<String, User> loader) {
        Map<String, GroupListManager> groupMatcher = new HashMap<>();
        Map<String, String> credentials = new HashMap<>();

        for (User user : loader.values()) {
            user.getGroups().entrySet()
                    .forEach(set -> {
                        fillGroupMap(groupMatcher, set.getKey(), set);
                    });

            credentials.put(user.getUsername(), user.getHashedPass());
        }

        this.userCredentials = credentials;
        this.groups = groupMatcher;
        this.userInfo = loader;

        for (User user : loader.values()) {
            for (Map.Entry<String, FriendListManager> curr : user.getFriends().entrySet()) {
                User friend = null;
                try {
                    friend = getUserEntity(curr.getKey());
                } catch (NoSuchEntityException e) {
                    Command.LOGGER.log(e.getMessage(), Command.LOGGER.getLogWriter());
                    continue;
                }
                friend.getFriends().put(user.getUsername(), curr.getValue());
            }
        }
    }

    private void fillGroupMap(Map<String, GroupListManager> map, String name,
                                                           Map.Entry<String, GroupListManager> user) {
        if (map.containsKey(name)) {
            user.setValue(map.get(name));
        } else {
            map.put(name, user.getValue());
        }
    }

}
