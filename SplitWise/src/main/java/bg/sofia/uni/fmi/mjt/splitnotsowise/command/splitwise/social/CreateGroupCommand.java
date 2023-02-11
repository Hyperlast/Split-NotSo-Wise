package bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.social;

import bg.sofia.uni.fmi.mjt.splitnotsowise.command.Command;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.CommandRunner;
import bg.sofia.uni.fmi.mjt.splitnotsowise.log.Logger;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository.ConnectionObserver;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository.GroupListManager;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.entity.User;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.OutputCreator;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.Validator;

import java.time.LocalDateTime;
import java.util.Arrays;


public class CreateGroupCommand implements Command {
    public static final int ARGUMENT_COUNT = 3;
    public static final int GROUP_NAME = 1;
    private final String[] args;
    private final String socketChannel;

    public CreateGroupCommand(String[] args, String socketChannel) {
        Validator.checkIfNullArray(args);
        Validator.checkIfNull(socketChannel);
        Validator.isBlank(socketChannel);

        this.args = args;
        this.socketChannel = socketChannel;
    }

    @Override
    public String execute(Logger logger) {
        String[] users = Arrays.copyOfRange(args, GROUP_NAME + 1, args.length);
        try {
            ConnectionObserver.isLogged(socketChannel);

            if (REGISTRY.doesGroupExist(args[GROUP_NAME])) {
                return "Group with that name already exists\n";
            }

            GroupListManager groupDebtInstance = new GroupListManager();
            for (String user : users) {
                CommandRunner.addToSameGroup(args[GROUP_NAME], groupDebtInstance, REGISTRY.getUserEntity(user));
                groupDebtInstance.addGroupMember(user);
            }

            User  user = CommandRunner.getUser(socketChannel);
            CommandRunner.addToSameGroup(args[GROUP_NAME], groupDebtInstance, user);
            groupDebtInstance.addGroupMember(user.getUsername());

            return "SuccessFully created the group";
        } catch (Exception e) {
            logger.log(LocalDateTime.now(), OutputCreator.getFullExceptionMessage(e), logger.getLogWriter());
            return e.getMessage();
        }



    }
}
