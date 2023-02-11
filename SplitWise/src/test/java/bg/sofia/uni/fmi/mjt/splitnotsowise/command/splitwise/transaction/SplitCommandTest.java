package bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.transaction;

import bg.sofia.uni.fmi.mjt.splitnotsowise.command.CommandRunner;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.TestUtils;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.auth.LoginCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.auth.LogoutCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.auth.RegisterCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.social.AddFriendCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.log.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static bg.sofia.uni.fmi.mjt.splitnotsowise.command.TestUtils.logger;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.command.TestUtils.loginArgs;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.SPACE_DELIMITER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SplitCommandTest {
    private static final String[] DEFAULT_FRIEND_PARAMS = "register friend1 Friend1!".split(SPACE_DELIMITER);
    private static final String DEFAULT_FRIEND_SOCKET_NAME = "FRIEND";
    public static final LoginCommand loginCommand = new LoginCommand(loginArgs, "ADDRESS");
    public static final LogoutCommand logoutCommand = new LogoutCommand("ADDRESS");

    public static final String ADMIN_ADDRESS = "ADDRESS";

    @BeforeAll
    static void init() {
        CommandRunner.toggleSaveToDefaultFiles();
        RegisterCommand registerCommand = new RegisterCommand(TestUtils.registerArgs, ADMIN_ADDRESS);
        registerCommand.execute(logger);
        RegisterCommand registerFriend = new RegisterCommand(DEFAULT_FRIEND_PARAMS, DEFAULT_FRIEND_SOCKET_NAME);
        registerFriend.execute(logger);
        RegisterCommand registerFriend2 = new RegisterCommand("register friend2 Friend2!".split(SPACE_DELIMITER),
                "FRIEND2");
        registerFriend2.execute(logger);
        AddFriendCommand addFriendCommand = new AddFriendCommand("add-friend friend1".split(SPACE_DELIMITER),
                ADMIN_ADDRESS);
        loginCommand.execute(logger);
        addFriendCommand.execute(logger);
    }

    @BeforeEach
    void initToggle() {
        CommandRunner.toggleSaveToDefaultFiles();
    }

    @AfterEach
    public void logout() {
        logoutCommand.execute(logger);
    }

    @Test
    void testSplitNotLoggedIn() {
        Logger log = mock(Logger.class);
        String[] args = "split 5 bill clinton".split(SPACE_DELIMITER);
        SplitCommand payedCommand = new SplitCommand(args, ADMIN_ADDRESS);
        payedCommand.execute(log);
        verify(log,atLeastOnce()).log(anyString(),any());
    }

    @Test
    void testSplitIncorrectAmount() {
        Logger log = mock(Logger.class);
        String[] args = "split 5d bill clinton".split(SPACE_DELIMITER);
        SplitCommand payedCommand = new SplitCommand(args, ADMIN_ADDRESS);
        payedCommand.execute(log);
        verify(log,atLeastOnce()).log(anyString(),any());
    }

    @Test
    void testSplitNonExistentUser() {
        Logger log = mock(Logger.class);
        loginCommand.execute(logger);
        String[] args = "split 5 bill clinton".split(SPACE_DELIMITER);
        SplitCommand payedCommand = new SplitCommand(args, ADMIN_ADDRESS);
        payedCommand.execute(log);
        verify(log,atLeastOnce()).log(anyString(),any());
    }

    @Test
    void testSplitNegativeAmount() {
        Logger log = mock(Logger.class);
        loginCommand.execute(logger);
        String[] args = "split -5 friend1 beans".split(SPACE_DELIMITER);
        SplitCommand payedCommand = new SplitCommand(args, ADMIN_ADDRESS);
        payedCommand.execute(log);
        verify(log,atLeastOnce()).log(anyString(),any());
    }

    @Test
    void testSplitNonFriend() {
        Logger log = mock(Logger.class);

        loginCommand.execute(logger);
        String[] args = "split 5 friend2 beans".split(SPACE_DELIMITER);
        SplitCommand payedCommand = new SplitCommand(args, ADMIN_ADDRESS);
        payedCommand.execute(log);
        verify(log,atLeastOnce()).log(anyString(),any());
    }

    @Test
    void testSplit() {
        Logger log = mock(Logger.class);

        loginCommand.execute(logger);
        String[] args = "split 5 friend1 beans".split(SPACE_DELIMITER);
        SplitCommand payedCommand = new SplitCommand(args, ADMIN_ADDRESS);
        payedCommand.execute(log);
        verify(log, never()).log(anyString(),any());
    }
}