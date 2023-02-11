package bg.sofia.uni.fmi.mjt.splitnotsowise.command.splitwise.transaction;


import bg.sofia.uni.fmi.mjt.splitnotsowise.command.CommandRunner;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.auth.LoginCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.auth.LogoutCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.command.auth.RegisterCommand;
import bg.sofia.uni.fmi.mjt.splitnotsowise.exception.BadResponseException;
import bg.sofia.uni.fmi.mjt.splitnotsowise.external.Currency;
import bg.sofia.uni.fmi.mjt.splitnotsowise.external.Exchange;
import bg.sofia.uni.fmi.mjt.splitnotsowise.log.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


import java.io.IOException;

import static bg.sofia.uni.fmi.mjt.splitnotsowise.command.TestUtils.ADMIN_ADDRESS;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.command.TestUtils.logger;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.command.TestUtils.loginArgs;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.command.TestUtils.registerArgs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class SwitchCurrencyCommandTest {

    public final LoginCommand loginCommand = new LoginCommand(loginArgs, ADMIN_ADDRESS);
    public final LogoutCommand logoutCommand = new LogoutCommand(ADMIN_ADDRESS);

    private static Exchange exchange = mock(Exchange.class);
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
    void testSwitchCurrencyNotLogged() {
        Logger log = mock(Logger.class);
        SwitchCurrencyCommand switchCurrencyCommand = new SwitchCurrencyCommand(ADMIN_ADDRESS,"BGN");

        switchCurrencyCommand.execute(log);
        verify(log, atLeastOnce()).log(anyString(), any());
    }

    @Test
    void testSwitchCurrencyUnsupportedCurrency() {
        loginCommand.execute(logger);
        SwitchCurrencyCommand switchCurrencyCommand = new SwitchCurrencyCommand(ADMIN_ADDRESS,"AZT");
        assertEquals(Currency.UNKNOWN.getLabel(), switchCurrencyCommand.execute(logger),
                "Expected unknown currency message");
    }



    @Test
    void testSwitchCurrencyCouldNotSet() throws BadResponseException, IOException, InterruptedException {
        loginCommand.execute(logger);
        Logger log = mock(Logger.class);

        when(exchange.canQuery()).thenReturn(true);
        when(exchange.getExchange(any())).thenReturn(true);

        SwitchCurrencyCommand switchCurrencyCommand = new SwitchCurrencyCommand(ADMIN_ADDRESS,"EUR");

        switchCurrencyCommand.execute(log);
        verify(log, never()).log(anyString(), any());
    }
}