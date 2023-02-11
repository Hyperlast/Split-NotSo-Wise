package bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FriendListManager extends GroupListManager {

    public void payed(String sender, BigDecimal amount) {
        BigDecimal currentBalance = super.groupDebt.get(sender);
        groupDebt.put(sender, currentBalance.add(amount));
    }

    public void split(String sender, BigDecimal amount) {
        BigDecimal currentBalance = super.groupDebt.get(sender);
        groupDebt.put(sender, currentBalance.add(amount.divide(BigDecimal.valueOf(2), RoundingMode.FLOOR)));
    }

}
