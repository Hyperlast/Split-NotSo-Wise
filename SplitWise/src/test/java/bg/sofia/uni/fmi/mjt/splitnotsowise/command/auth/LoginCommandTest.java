package bg.sofia.uni.fmi.mjt.splitnotsowise.command.auth;


import bg.sofia.uni.fmi.mjt.splitnotsowise.command.CommandRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static bg.sofia.uni.fmi.mjt.splitnotsowise.command.TestUtils.ADMIN_ADDRESS;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.command.TestUtils.loginArgs;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.command.TestUtils.registerArgs;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.command.TestUtils.logger;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginCommandTest {

    public final LoginCommand loginCommand = new LoginCommand(loginArgs, ADMIN_ADDRESS);
    public final LogoutCommand logoutCommand = new LogoutCommand(ADMIN_ADDRESS);

    @BeforeAll
    static void init() {
        CommandRunner.toggleSaveToDefaultFiles();
        RegisterCommand registerCommand = new RegisterCommand(registerArgs, ADMIN_ADDRESS);
        registerCommand.execute(logger);
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
    void testLoginCommand() {

        String result = loginCommand.execute(logger);
        assertEquals("Successful Login"
                + System.lineSeparator()
                + "No new notifications!" , result, "Expected correct login");

    }
}