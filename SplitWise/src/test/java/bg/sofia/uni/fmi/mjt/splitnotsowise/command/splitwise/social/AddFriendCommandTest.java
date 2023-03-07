package bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.social;


import bg.sofia.uni.fmi.mjt.splitnotsowise.command.CommandRunner;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.TestUtils;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.auth.LoginCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.auth.LogoutCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.auth.RegisterCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.log.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static bg.sofia.uni.fmi.mjt.splitnotsowise.command.TestUtils.logger;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.command.TestUtils.loginArgs;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.SPACE_DELIMITER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AddFriendCommandTest {

    @Mock
    Logger log;

    private static final String[] DEFAULT_FRIEND_PARAMS = "register friend1 Friend1!".split(SPACE_DELIMITER);
    private static final String DEFAULT_FRIEND_SOCKET_NAME = "FRIEND";
    public final LoginCommand loginCommand = new LoginCommand(loginArgs, ADMIN_ADDRESS);
    public final LogoutCommand logoutCommand = new LogoutCommand(ADMIN_ADDRESS);

    public static final String ADMIN_ADDRESS = "ADDRESS";

    @BeforeAll
    static void init() {
        CommandRunner.toggleSaveToDefaultFiles();
        RegisterCommand registerCommand = new RegisterCommand(TestUtils.registerArgs, ADMIN_ADDRESS);
        registerCommand.execute(logger);
        RegisterCommand registerFriend = new RegisterCommand(DEFAULT_FRIEND_PARAMS, DEFAULT_FRIEND_SOCKET_NAME);
        registerFriend.execute(logger);
    }

    @BeforeEach
    void initToggle() {
        CommandRunner.toggleSaveToDefaultFiles();
        loginCommand.execute(logger);
    }

    @AfterEach
    public void logout() {
        logoutCommand.execute(logger);
    }

    @Test
    void testAddNotLoggedIn() {
        logoutCommand.execute(logger);
        String[] args = "add-friend bob".split(SPACE_DELIMITER);
        AddFriendCommand addFriendCommand = new AddFriendCommand(args, ADMIN_ADDRESS);
        assertEquals("Not logged in" + System.lineSeparator() , addFriendCommand.execute(logger),
                "Expected not logged in user message");
    }

    @Test
    void testAddYourself() {
        String result = "You cannot add yourself as a friend" + System.lineSeparator();
        String[] args = "add-friend admin".split(SPACE_DELIMITER);
        AddFriendCommand addFriendCommand = new AddFriendCommand(args, ADMIN_ADDRESS);

        assertEquals(result , addFriendCommand.execute(logger),
                "Expected not logged in user message");
    }

    @Test
    void testAddNonExistentAccount() {
        String[] args = "add-friend BobSnyder".split(SPACE_DELIMITER);
        AddFriendCommand addFriendCommand = new AddFriendCommand(args, ADMIN_ADDRESS);

        addFriendCommand.execute(log);
        verify(log, times(1)).log(anyString(), any());
    }

    @Test
    void testAddAlreadyExistingFriend() {

        String[] args = "add-friend friend1".split(SPACE_DELIMITER);
        AddFriendCommand addFriendCommand = new AddFriendCommand(args, ADMIN_ADDRESS);

        addFriendCommand.execute(log);
        addFriendCommand.execute(log);
        verify(log, times(1)).log(anyString(), any());
    }

}