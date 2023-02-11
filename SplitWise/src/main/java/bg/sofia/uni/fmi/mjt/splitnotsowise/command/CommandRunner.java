package bg.sofia.uni.fmi.mjt.splitnotsowise.command;

import bg.sofia.uni.fmi.mjt.splitnotsowise.database.Config;
import bg.sofia.uni.fmi.mjt.splitnotsowise.exception.IncorrectCommandUsageException;
import bg.sofia.uni.fmi.mjt.splitnotsowise.exception.NoSuchEntityException;
import bg.sofia.uni.fmi.mjt.splitnotsowise.exception.UnauthorizedAccessToServerDataException;
import bg.sofia.uni.fmi.mjt.splitnotsowise.exception.UnrecognizedValueException;
import bg.sofia.uni.fmi.mjt.splitnotsowise.external.Currency;
import bg.sofia.uni.fmi.mjt.splitnotsowise.external.CurrencyCache;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository.ConnectionObserver;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository.FriendListManager;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository.GroupListManager;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository.UserRegistry;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.entity.User;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.OutputCreator;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.Validator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.SPACE_DELIMITER;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.OutputCreator.createPayedTransactionMessage;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.OutputCreator.createSplitFriendTransactionMessage;

public final class CommandRunner {

    private static final UserRegistry REGISTRY = UserRegistry.getInstance();

    public static void addToSameGroup(String groupName, GroupListManager group, User user) throws IOException {
        user.addToGroup(groupName, group);
        REGISTRY.addGroup(groupName, group);
        updateUserInfo();
    }

    public static void updateRepoSplitWithGroup(String groupName, User user, BigDecimal amount, String reason)
            throws NoSuchEntityException, IOException {

        Currency fieldCurrency = user.getCurrency();
        BigDecimal parsedAmount = CurrencyCache.getInstance().convertCurrencyToBase(amount, fieldCurrency);

        GroupListManager manager = REGISTRY.getGroupInstance(groupName);
        if (!manager.splitWithGroup(user.getUsername(), parsedAmount)) {
            throw new NoSuchEntityException("Member' not of this group");
        }

        REGISTRY.addTransaction(user.getUsername(),
                OutputCreator.createSplitWithGroupTransactionMessage(groupName, amount, fieldCurrency, reason));

        String notification = user.getUsername()
                + " has spit "
                + amount
                + SPACE_DELIMITER
                + fieldCurrency.getLabel()
                + " in -> "
                + groupName
                + " because: "
                + reason;

        List<String> users = REGISTRY.getGroupInstance(groupName).getGroupMembers();
        for (String usr: users) {
            if (!usr.equals(user.getUsername())) {
                User temp = REGISTRY.getUserEntity(usr);
                temp.addNotification(notification);
            }
        }

        updateUserInfo();
        updateTransaction();
    }

    public static void updateRepoWithSplit(String payer, String receiver, BigDecimal amount, String reason)
            throws NoSuchEntityException, IOException {
        Validator.checkIfNull(payer, receiver, amount, reason);
        Validator.isBlank(payer, receiver, reason);

        User payerUser;
        User receiverUser;
        payerUser = REGISTRY.getUserEntity(payer);
        receiverUser = REGISTRY.getUserEntity(receiver);

        Currency fieldCurrency = payerUser.getCurrency();
        BigDecimal parsedAmount = CurrencyCache.getInstance().convertCurrencyToBase(amount, fieldCurrency);


        if (!payerUser.isFriend(receiver)) {
            throw new NoSuchEntityException("Couldn't find a user with such name in your friend list");
        }

        payerUser.split(receiver, parsedAmount);
        REGISTRY.addTransaction(payer, createSplitFriendTransactionMessage(receiver, amount, fieldCurrency, reason));
        receiverUser.addNotification(payer
                + " split "
                + amount
                + SPACE_DELIMITER
                + fieldCurrency.getLabel()
                + " with you for: "
                + reason);

        updateUserInfo();
        updateTransaction();
    }
    public static void updateRepoWithPayed(String initiatorUsername, String payerUsername, BigDecimal amount)
            throws NoSuchEntityException, IOException {
        Validator.checkIfNull(initiatorUsername, payerUsername, amount);
        Validator.isBlank(initiatorUsername, payerUsername);


        User initiator = REGISTRY.getUserEntity(initiatorUsername);
        User payer = REGISTRY.getUserEntity(payerUsername);
        Currency fieldCurrency = payer.getCurrency();

        if (!initiator.isFriend(payerUsername)) {
            throw new NoSuchEntityException("Couldn't find a user with such name in your friend list");
        }

        REGISTRY.addTransaction(payerUsername, createPayedTransactionMessage(initiatorUsername, amount));
        payer.addNotification(initiatorUsername
                + " has accepted your last payment of: "
                + amount
                + SPACE_DELIMITER
                + fieldCurrency.getLabel());

        BigDecimal parsedAmount = CurrencyCache.getInstance().convertCurrencyToBase(amount, fieldCurrency);
        initiator.payed(payerUsername, parsedAmount);

        updateUserInfo();
        updateTransaction();
    }



    public static String getNotifications(User user) {
        Validator.checkIfNull(user);
        return user.getNotifications();
    }

    public static void updateRepoWithAddFriend(String initiatorUsername, String username) throws IOException,
            NoSuchEntityException, IncorrectCommandUsageException {
        Validator.checkIfNull(initiatorUsername, username);
        Validator.isBlank(initiatorUsername, username);

        User friend = REGISTRY.getUserEntity(username);
        User initiator = REGISTRY.getUserEntity(initiatorUsername);

        FriendListManager friendDebt = new FriendListManager();

        if (!initiator.addFriend(username, friendDebt)) {
            throw new IncorrectCommandUsageException("User already added to friends");
        }

        friend.addFriend(initiatorUsername, friendDebt);
        friend.addNotification(initiatorUsername + " added you as a friend");
        updateUserInfo();
    }

    public static String getStatus(User user) {
        Validator.checkIfNull(user);

        return user.getFullStatus();
    }

    public static User getUser(String socketChannel)
            throws UnauthorizedAccessToServerDataException, NoSuchEntityException, IncorrectCommandUsageException {

        String username = ConnectionObserver.getUserName(socketChannel);

        return REGISTRY.getUserEntity(username);
    }

    public static void checkAmount(String amount, String socketChannel, String otherUser)
            throws UnrecognizedValueException, IncorrectCommandUsageException {

        CommandParser.checkIfCorrect(amount);

        ConnectionObserver.isLogged(socketChannel);

        REGISTRY.doesNotExist(otherUser);
    }

    public static void updateTransaction() throws IOException {
        try (BufferedWriter transactionWriter = new BufferedWriter(new FileWriter(Config.TRANSACTION_CONFIG_PATH))) {
            UserRegistry.getInstance().updateTransactionsMap(transactionWriter);
            transactionWriter.flush();
        }
    }

    public static void updateUserInfo() throws IOException {
        try (BufferedWriter userWriter = new BufferedWriter(new FileWriter(Config.USER_CONFIG_PATH))) {
            UserRegistry.getInstance().updateUserInfoMap(userWriter);
            userWriter.flush();
        }

    }

}
