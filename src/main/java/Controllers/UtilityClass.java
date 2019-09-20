package Controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import javafx.util.Duration;
import securityandtime.CheckConn;
import securityandtime.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Objects;

import static securityandtime.config.*;

public class UtilityClass {
    private Connection connection;
    private Connection connectionDbLocal;

    public UtilityClass() {
        Path path = Paths.get(fileSavePath);

        if (!Files.exists(path)) {

            try {
                Files.createDirectories(path);
                Files.createDirectories(Paths.get(fileSavePath + "\\licenses"));
                Files.createDirectories(Paths.get(fileSavePath + "\\dependencies"));
                Files.createDirectories(Paths.get(fileSavePath + "\\images"));
                Files.createDirectories(Paths.get(fileSavePath + "\\files"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    {
        try {
            connectionDbLocal = DriverManager.getConnection(localCartDb);

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            connection = DriverManager
                    .getConnection(des[2], des[0], des[1]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public UtilityClass setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }

    public Connection getConnectionDbLocal() {
        return connectionDbLocal;
    }

    public void logout(AnchorPane panel) {

        config.pricegot.clear();
        config.key.clear();
        config.cartid.clear();
        config.throwables.clear();
        config.license.clear();
        config.action.clear();
        config.panel.clear();
        config.networkConnectionMap.clear();
        config.login.put("loggedout", true);
        config.user.clear();
        try {
            panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void shutdown() throws RuntimeException, IOException {
        String shutdownCommand;
        String operatingSystem = System.getProperty("os.name");
        System.out.println(operatingSystem);
        if (operatingSystem.startsWith("Linux") || operatingSystem.startsWith("Mac")) {
            shutdownCommand = "shutdown -h now";
        } else if (operatingSystem.startsWith("Windows")) {
            shutdownCommand = "shutdown.exe -s -t 0";
        } else {
            throw new RuntimeException("Unsupported operating system.");
        }

        Runtime.getRuntime().exec(shutdownCommand);
        System.exit(0);
    }

    public UtilityClass setConnectionDbLocal(Connection connectionDbLocal) {
        this.connectionDbLocal = connectionDbLocal;
        return this;
    }

    public void time(Label clock) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            String mins = null, hrs = null, secs = null, pmam = null;
            try {
                int minutes = Integer.parseInt(String.valueOf(CheckConn.timelogin().getMinutes()));
                int seconds = Integer.parseInt(String.valueOf(CheckConn.timelogin().getSeconds()));
                int hours = Integer.parseInt(String.valueOf(CheckConn.timelogin().getHours()));

                if (hours >= 12) {
                    pmam = "PM";
                } else {
                    pmam = "AM";

                }
                if (minutes > 9) {
                    mins = String.valueOf(minutes);
                } else {
                    mins = "0" + minutes;

                }
                if (seconds > 9) {
                    secs = String.valueOf(seconds);
                } else {
                    secs = "0" + seconds;

                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
//                if (networkConnectionMap.containsKey("server") || networkConnectionMap.containsKey("Internet")) {
//                    if (!networkConnectionMap.get("server")) {
////                    no connection to server
//                        showAlert(Alert.AlertType.ERROR, panel.get("panel").getScene().getWindow(), "ERROR", "NETWORK CONNECTION LOST");
//                    } else {
//                        if (!networkConnectionMap.get("Internet")) {
//                            showToast();//about connection to the internet
//                        }
//                    }
//
//                }
                clock.setText(CheckConn.timelogin().getHours() + ":" + (mins) + ":" + (secs) + " " + pmam);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void showToast() {

    }


    protected void timeMain(Label clock) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            clock.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss a").format(Calendar.getInstance().getTime()));
        }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.showAndWait();
    }

    public static void restart() throws RuntimeException, IOException {
        String shutdownCommand;
        String operatingSystem = System.getProperty("os.name");
        System.out.println(operatingSystem);
        if (operatingSystem.startsWith("Linux") || operatingSystem.startsWith("Mac")) {
            shutdownCommand = "shutdown -r now";
        } else if (operatingSystem.startsWith("Windows")) {
            shutdownCommand = "shutdown.exe -r -t 0";
        } else {
            throw new RuntimeException("Unsupported operating system.");
        }

        Runtime.getRuntime().exec(shutdownCommand);
        System.exit(0);
    }

    public void exit() {
        Platform.exit();
        System.exit(111);
    }

}



