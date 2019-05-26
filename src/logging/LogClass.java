package logging;

import java.io.IOException;
import java.util.logging.*;

public class LogClass {
    private static final Logger logger = Logger.getLogger(LogClass.class.getName());
    private Handler filehandler = null;
    private Handler consolehandler = null;

    public Handler getFilehandler() {
        return filehandler;
    }

    public void setFilehandler(Handler filehandler) {
        this.filehandler = filehandler;
    }

    public Handler getConsolehandler() {
        return consolehandler;
    }

    public void setConsolehandler(Handler consolehandler) {
        this.consolehandler = consolehandler;
    }

    LogClass() {
        try {
            consolehandler = new ConsoleHandler();
            filehandler = new FileHandler("./nanotechPOS.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert filehandler != null;
        logger.addHandler(filehandler);
        logger.addHandler(consolehandler);
        logger.setLevel(Level.ALL);
        logger.config("configuration level is set");
        logger.removeHandler(consolehandler);
    }

    public static Logger getLogger() {
        return logger;
    }
}
