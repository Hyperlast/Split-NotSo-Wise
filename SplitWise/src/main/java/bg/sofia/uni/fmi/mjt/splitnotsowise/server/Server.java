package bg.sofia.uni.fmi.mjt.splitnotsowise.server;

import bg.sofia.uni.fmi.mjt.splitnotsowise.database.Config;
import bg.sofia.uni.fmi.mjt.splitnotsowise.database.repository.ConnectionObserver;
import bg.sofia.uni.fmi.mjt.splitnotsowise.external.Exchange;
import bg.sofia.uni.fmi.mjt.splitnotsowise.log.Logger;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.OutputCreator;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Set;

import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.DEFAULT_LOG_DIR_PATH;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.DEFAULT_LOG_LOGS;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.DEFAULT_LOG_THROW;
import static bg.sofia.uni.fmi.mjt.splitnotsowise.utils.message.Constants.DEFAULT_SERVER_FILE_NAME;


public class Server {
    public static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 8192;


    public static void main(String[] args) {
        Logger logger = Logger.builder(DEFAULT_LOG_DIR_PATH, DEFAULT_SERVER_FILE_NAME)
                .setHandlerThrows(DEFAULT_LOG_THROW)
                .setLogs(DEFAULT_LOG_LOGS)
                .build();

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
             JsonReader transactionReader = new JsonReader(new FileReader(Config.TRANSACTION_CONFIG_PATH));
             JsonReader userReader = new JsonReader(new FileReader(Config.USER_CONFIG_PATH));
             BufferedReader keyReader = new BufferedReader(new FileReader(Exchange.EXCHANGE_CONFIG_PATH))) {

            Exchange.getInstance().addAPIKEY(keyReader.readLine());

            InputHandler userHandler = new InputHandler(logger);

            Config config = new Config(logger);
            config.configFiles();
            if (!Files.exists(Path.of(Config.TRANSACTION_CONFIG_PATH))
                    || !Files.exists(Path.of(Config.USER_CONFIG_PATH))) {
                return;
            }

            config.loadTransactions(transactionReader);
            config.loadUsers(userReader);

            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            serverSocketChannel.configureBlocking(false);

            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            while (true) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();

                        buffer.clear();
                        int r;
                        try {
                            r = sc.read(buffer);
                        } catch (IOException e) {
                            logger.log("There is a problem with the server socket: " +
                                            OutputCreator.getFullExceptionMessage(e), logger.getLogWriter());
                            continue;
                        }


                        if (r < 0) {
                            String session = sc.socket().getInetAddress().toString();
                            ConnectionObserver.removeConnection(sc.getRemoteAddress().toString());
                            System.out.println("Client" + session + "has closed the connection");
                            sc.close();
                            continue;
                        }

                        buffer.flip();

                        String line = StandardCharsets.UTF_8.decode(buffer).toString();
                        System.out.println(line);

                        String response = userHandler.executeInput(line, sc.getRemoteAddress().toString());

                        System.out.println(response);
                        sc.write(ByteBuffer.wrap(response.getBytes()));

                    } else if (key.isAcceptable()) {
                        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = sockChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println(socketChannel.getRemoteAddress().toString());
                    }

                    keyIterator.remove();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
            logger.log("There is a problem with the server socket: " + e.getMessage(),
                    logger.getLogWriter());

        }
    }
}
