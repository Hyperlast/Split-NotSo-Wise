package bg.sofia.uni.fmi.mjt.splitnotsowise.database.entity;


import bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository.FriendListManager;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository.GroupListManager;
import bg.sofia.uni.fmi.mjt.splitnotsowise.external.Currency;
import bg.sofia.uni.fmi.mjt.splitnotsowise.external.CurrencyCache;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    private final String username;

    private final String hashedPass;

    private final List<String> notifications;

    private final Map<String, FriendListManager> friends;
    private final Map<String, GroupListManager> groups;

    private transient Currency currency;

    public User(String username, String hashedPass) {
        this.username = username;
        this.hashedPass = hashedPass;
        this.notifications = new ArrayList<>();
        this.friends = new HashMap<>();
        this.groups = new HashMap<>();
        this.currency = Currency.BGN;
    }

    public Currency getCurrency() {
        if (this.currency == null) {
            return Currency.BGN;
        }
        return currency;
    }

    public Map<String, FriendListManager> getFriends() {
        return friends;
    }
    public Map<String, GroupListManager>  getGroups() {
        return groups;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPass() {
        return hashedPass;
    }

    public boolean setCurrency(Currency currency) {
        if (CurrencyCache.getInstance().isConversionAvailable(currency)) {
            this.currency = currency;
            return true;
        }
        return false;
    }

    public void addToGroup(String groupName, GroupListManager instance) {
        groups.put(groupName, instance);
        addNotification("You are a part of Group " + groupName);
    }

    public void addNotification(String notification) {
        notifications.add(notification);
    }

    public void payed(String sender, BigDecimal amount) {
        friends.get(sender).payed(sender, amount);
    }

    public void split(String receiver, BigDecimal amount) {
        friends.get(receiver).split(username, amount);
    }

    public boolean addFriend(String username, FriendListManager friendBalance) {
        if (friends.containsKey(username)) {
            return false;
        }

        friends.put(username, friendBalance);
        friends.get(username).addGroupMember(this.username);
        friends.get(username).addGroupMember(username);

        return true;
    }

    public boolean isFriend(String username) {
        return friends.containsKey(username);
    }

    public String getNotifications() {
        if (notifications.isEmpty()) {
            return "No new notifications!";
        }

        StringBuilder result = new StringBuilder();
        for (String str: notifications) {
            result.append(str).append(System.lineSeparator());
        }

        notifications.clear();
        return result.toString();
    }
    
    public String getFullStatus() {
        StringBuilder builder = new StringBuilder();
        builder.append("Friends:").append(System.lineSeparator());

        for (Map.Entry<String, FriendListManager> entry : friends.entrySet()) {
            builder.append(entry.getValue().getStatus(entry.getKey(), username, currency))
                    .append(System.lineSeparator());
        }

        builder.append(System.lineSeparator()).append("Groups:").append(System.lineSeparator());

        for (Map.Entry<String, GroupListManager> entry : groups.entrySet()) {
            builder.append("-").append(entry.getKey()).append(": ")
                    .append(entry.getValue().getStatus(entry.getKey(), username, currency))
                    .append(System.lineSeparator());
        }
        return builder.toString();
    }

}

