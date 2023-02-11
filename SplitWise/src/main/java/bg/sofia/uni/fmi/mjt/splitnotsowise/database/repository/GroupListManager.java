package bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository;

import bg.sofia.uni.fmi.mjt.splitnotsowise.external.Currency;
import bg.sofia.uni.fmi.mjt.splitnotsowise.external.CurrencyCache;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.Validator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.SPACE_DELIMITER;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.OutputCreator.DDOT_DELIMITER;

public class GroupListManager {

    private static final String PREFIX = "* ";

    private static final String DEFAULT_VALUE = "0.00";
    protected Map<String, BigDecimal> groupDebt;

    public GroupListManager() {
        groupDebt = new HashMap<>();
    }

    public boolean isMember(String username) {
        return groupDebt.containsKey(username);
    }
    public void addGroupMember(String username) {
        Validator.checkIfNull(username);
        Validator.isBlank(username);

        groupDebt.putIfAbsent(username, new BigDecimal(DEFAULT_VALUE));
    }

    public boolean splitWithGroup(String username, BigDecimal amount) {
        if (!isMember(username)) {
            return false;
        }

        final int groupSize = groupDebt.size();
        BigDecimal currentBalance = groupDebt.get(username);
        groupDebt.put(username,
                currentBalance.add(amount.divide(BigDecimal.valueOf(groupSize), 2, RoundingMode.FLOOR)));

        return true;
    }

    public List<String> getGroupMembers() {
        return groupDebt.keySet().stream().toList();
    }


    public String getStatus(String debtName, String username, Currency currency) {
        StringBuilder result = new StringBuilder();
        result.append(PREFIX).append(debtName).append(SPACE_DELIMITER).append(System.lineSeparator());

        groupDebt.forEach((key, value) -> {
            if (!key.equals(username)) {
                result.append(key)
                        .append(DDOT_DELIMITER)
                        .append(SPACE_DELIMITER)
                        .append(getDebtMessage(value, groupDebt.get(username), currency))
                        .append(System.lineSeparator());
            }

        });

        return result.toString();
    }

    public static String getDebtMessage(BigDecimal friendBalance, BigDecimal balance, Currency currency) {
        CurrencyCache cache = CurrencyCache.getInstance();
        int result = balance.compareTo(friendBalance);

        if (result == 0) {
            return " You don't have debts with this user" + System.lineSeparator();
        } else if (result > 0) {
            return " Owes you " + cache.convertBaseToCurrency(balance.subtract(friendBalance), currency);
        }

        return " You owe " + cache.convertBaseToCurrency(friendBalance.subtract(balance), currency);
    }

}
