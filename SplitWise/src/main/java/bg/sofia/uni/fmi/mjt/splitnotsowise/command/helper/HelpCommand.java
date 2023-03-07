package bg.sofia.uni.fmi.mjt.splitnotsowise.command.helper;


import bg.sofia.uni.fmi.mjt.splitnotsowise.command.Command;


public class HelpCommand implements Command {
    public static final String HELP_MSG = "add-friend <username> (adds friend)"
            + System.lineSeparator() +
            "create-group <group_name> <username> <username> ... <username> (creates a group with users)"
            + System.lineSeparator() +
            "payed <amount> <username> (accepts payment amount from user)"
            + System.lineSeparator() +
            "split <amount> <username> <reason_for_payment> (splits money with user)"
            + System.lineSeparator() +
            "split-group <amount> <group_name> <reason_for_payment> (splits money with whole group)"
            + System.lineSeparator() +
            "history (shows logged user transaction history)" +
            "status (shows debts)"
            + System.lineSeparator() +
            "login <username> <password> (logs user)"
            + System.lineSeparator() +
            "register <username> <password> (registers user)"
            + System.lineSeparator() +
            "logout (logs out a logged in user)"
            + System.lineSeparator() +
            "switch-currency <Currency> (USD|EUR|BGN available : switches currencies)"
            + System.lineSeparator();

    @Override
    public String execute() {
        return HELP_MSG;
    }
}
