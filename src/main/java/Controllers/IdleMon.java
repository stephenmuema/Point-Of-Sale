package Controllers;

import javafx.event.Event;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import securityandtime.config;

import java.io.IOException;

public class IdleMon {
    public IdleMon(AnchorPane panel) {
        IdleMonitor idleMonitor = null;
        try {
            idleMonitor = new IdleMonitor(Duration.seconds(config.timeou.get("value")),
                    () -> {
                        try {
                            new UtilityClass().logout(panel);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        idleMonitor.register(panel, Event.ANY);

    }
}
