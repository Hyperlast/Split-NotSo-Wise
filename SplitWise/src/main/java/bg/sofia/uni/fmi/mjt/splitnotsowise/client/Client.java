package bg.sofia.uni.fmi.mjt.splitnotsowise.client;

import bg.sofia.uni.fmi.mjt.splitnotsowise.log.Logger;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.OutputCreator;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Scanner;

import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.DEFAULT_CLIENT_FILE_NAME;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.DEFAULT_LOG_DIR_PATH;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.DEFAULT_LOG_LOGS;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.DEFAULT_LOG_THROW;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.OutputCreator.USER_EXCEPTION_RETURN_MESSAGE;

public class Client {
    public static final String NEWLY_CONNECTED_CLIENT = "Welcome to SplitNotSoWise" + System.lineSeparator();
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final String QUIT_COMMAND = "quit";
    private static final int BUFFER_SIZE = 8192;

    private static final ByteBuffer BUFFER = ByteBuffer.allocate(BUFFER_SIZE);


    public static void main(String[] args) {
        Logger logger = Logger.builder(DEFAULT_LOG_DIR_PATH, DEFAULT_CLIENT_FILE_NAME)
                .setHandlerThrows(DEFAULT_LOG_THROW)
                .setLogs(DEFAULT_LOG_LOGS)
                .build();

        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

            System.out.println(NEWLY_CONNECTED_CLIENT);
            while (true) {
                String command = scanner.nextLine();
                if (QUIT_COMMAND.equals(command)) {
                    break;
                }

                BUFFER.clear();
                BUFFER.put(command.getBytes(StandardCharsets.UTF_8));
                BUFFER.flip();
                socketChannel.write(BUFFER);
                BUFFER.clear();

                socketChannel.read(BUFFER);
                BUFFER.flip();

                byte[] byteArray = new byte[BUFFER.remaining()];
                BUFFER.get(byteArray);
                String reply = new String(byteArray, StandardCharsets.UTF_8);

                System.out.println(reply);
            }

        } catch (IOException e) {
            System.out.println(USER_EXCEPTION_RETURN_MESSAGE + Path.of(DEFAULT_LOG_DIR_PATH).normalize());
            logger.log("There is a problem with the network communication: " +
                    OutputCreator.getFullExceptionMessage(e),
                    logger.getLogWriter());
        }

    }
}
