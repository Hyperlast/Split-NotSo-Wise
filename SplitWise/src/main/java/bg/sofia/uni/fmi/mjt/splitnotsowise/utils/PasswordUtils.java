package bg.sofia.uni.fmi.mjt.splitnotsowise.utils;


import bg.sofia.uni.fmi.mjt.splitnotsowise.exception.UnrecognizedValueException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.DEFAULT_LOG_DIR_PATH;

public final class PasswordUtils {
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 128;
    private static final int RADIX = 16;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final String PASSWORD_REGEX =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,20}$";

    private static final byte[] DEFAULT_SALT;

    private static final Path SALT_FILE_PATH = Path.of(DEFAULT_LOG_DIR_PATH + "salt.txt");

    static {
        try {
            DEFAULT_SALT = Files.readAllBytes(SALT_FILE_PATH);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't access SALT file/ Couldn't read salt file", e);
        }
    }

    private PasswordUtils() { }

    public static byte[] getSalt() {
        return DEFAULT_SALT;
    }

    public static String hashPass(String pass, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(pass.toCharArray(), salt, ITERATION_COUNT , KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);

        byte[] hash = factory.generateSecret(spec).getEncoded();

        return new BigInteger(1, hash).toString(RADIX);
    }


    public static void verifyPasswordRegex(String password) throws UnrecognizedValueException {
        if (!password.matches(PASSWORD_REGEX)) {
            throw new UnrecognizedValueException("Password must contain (one digit," +
                    "one lowercase  character," +
                    "one uppercase  character," +
                    "one special character like ! @ # & ( )," +
                    "Password length: 6 - 20 characters." +
                    System.lineSeparator());
        }
    }

}
