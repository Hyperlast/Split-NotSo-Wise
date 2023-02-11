package bg.sofia.uni.fmi.mjt.splitnotsowise.log;

import bg.sofia.uni.fmi.mjt.splitnotsowise.exception.LoggerException;
import bg.sofia.uni.fmi.mjt.splitnotsowise.utils.Validator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static java.nio.file.StandardOpenOption.APPEND;

public class Logger {

    public final String fileName;
    private final String logPath ;

    private final boolean doesHandlerThrow;

    private final boolean doesCreateFiles;

    private final boolean doesLog;

    private Logger(LoggerBuilder builder) {
        this.logPath = builder.logPath;
        this.fileName = builder.fileName;
        this.doesHandlerThrow = builder.doesHandlerThrow;
        this.doesCreateFiles = builder.doesCreateFiles;
        this.doesLog = builder.doesLog;
    }

    public static LoggerBuilder builder(String path, String fileName){
        return new LoggerBuilder(path,fileName);
    }

    public static class LoggerBuilder {
        private final String logPath;
        public final String fileName;
        private boolean doesHandlerThrow = true;

        private boolean doesLog = false;

        private boolean doesCreateFiles = false;

        public LoggerBuilder(String path, String fileName) {
            Validator.checkIfNull(path, fileName);
            Validator.isBlank(path, fileName);
            this.logPath = path;
            this.fileName = fileName;
        }

        public LoggerBuilder setHandlerThrows(boolean flag) {
            Validator.checkIfNull(flag);
            this.doesHandlerThrow = flag;
            return this;
        }

        public LoggerBuilder setLogs(boolean flag) {
            Validator.checkIfNull(flag);
            this.doesLog = flag;
            this.doesCreateFiles = flag;
            return this;
        }

        public LoggerBuilder setCreatesFile(boolean flag) {
            Validator.checkIfNull(flag);
            this.doesCreateFiles = flag;
            return this;
        }

        public Logger build() {
            return new Logger(this);
        }

    }

    public Writer getLogWriter() {
        try {
            return Files.newBufferedWriter(getFullPath(), StandardCharsets.UTF_8, APPEND);
        } catch (IOException e) {
            if (doesHandlerThrow) {
                throw new LoggerException("Couldn't access log writer ", e);
            }
        }
        return new BufferedWriter(new OutputStreamWriter(System.out));
    }

    public void log(String msg, Writer writer) {
        if (!doesLog) {
            return;
        }
        createNonexistentLogFile();
        LogMessage logMessage = createLog(LocalDateTime.now(), msg);
        try (Writer logWriter = new BufferedWriter(writer)) {
            logWriter.write(logMessage.toString());
            logWriter.flush();
        } catch (IOException e) {
            if (doesHandlerThrow) {
                throw new LoggerException("An error occurred when trying to log: " + logMessage, e);
            }
        }
    }

    private LogMessage createLog(LocalDateTime timestamp, String msg) {
        Validator.checkIfNull(timestamp, msg);
        Validator.isBlank(msg);

        return new LogMessage(timestamp, msg);
    }

    private void createNonexistentLogFile() {
        Path path = getFullPath();
        if (!Files.exists(path) && doesCreateFiles) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                if (doesHandlerThrow) {
                    throw new LoggerException("Couldn't create file", e);
                }
            }
        }
    }

    private Path getFullPath() {
        return Path.of(logPath, fileName);
    }
}
