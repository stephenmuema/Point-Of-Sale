package Controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import javafx.util.Duration;
import org.apache.commons.io.FileUtils;
import securityandtime.AesCrypto;
import securityandtime.CheckConn;
import securityandtime.config;

import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

import static securityandtime.config.*;


public class UtilityClass extends FetchDbDetails {


    public static int prev = 0;
    public static String prevString = "PROFILE";
    public String path = fileSavePath + "backups";
    private Connection connection;
    private Connection connectionDbLocal;


    public UtilityClass() throws IOException {
        super();

        {
            try {
                connectionDbLocal = DriverManager.getConnection(localCartDb);

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                connection = DriverManager
                        .getConnection(FetchDbDetails.getDes()[2], FetchDbDetails.getDes()[0], FetchDbDetails.getDes()[1]);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

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

    public static void restart() throws RuntimeException, IOException {
        String restartCommand;
        String operatingSystem = System.getProperty("os.name");
        System.out.println(operatingSystem);
        if (operatingSystem.startsWith("Linux") || operatingSystem.startsWith("Mac")) {
            restartCommand = "shutdown -r now";
        } else if (operatingSystem.startsWith("Windows")) {
            restartCommand = "shutdown.exe -r -t 0";
        } else {
            throw new RuntimeException("Unsupported operating system.");
        }

        Runtime.getRuntime().exec(restartCommand);
        System.exit(0);
    }

    public void goToStaff(AnchorPane panel) {
        try {
            refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    panel.getChildren().removeAll();
                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/employees.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void goToStocks(AnchorPane panel) {
        try {
            refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    panel.getChildren().removeAll();
                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("shopFiles/stocks.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void goToCarwash(AnchorPane panel) {
        try {
            refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        panel.getChildren().removeAll();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("carwashFiles/carwash.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void goToSuppliers(AnchorPane panel) {
        try {
            refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Desktop.getDesktop().browse(new URL(supplierSite).toURI());
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void gotoAudits(AnchorPane panel) {
        try {
            refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    panel.getChildren().removeAll();
                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("shopFiles/audits.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void refresh() throws SQLException {
        ResultSet resultSet;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email=?");
        statement.setString(1, config.user.get("user"));
        resultSet = statement.executeQuery();
        if (resultSet.isBeforeFirst()) {
            while (resultSet.next()) {
                config.login.put("loggedinasadmin", true);
                config.user.put("userName", resultSet.getString("employeename"));

                config.user.put("user", resultSet.getString("email"));
                config.key.put("key", resultSet.getString("subscribername"));
                config.user.put("backupemail", resultSet.getString("backupemail"));
                config.user.put("backupemailpassword", AesCrypto.decrypt(encryptionkey, resultSet.getString("backupemailpassword")));
            }
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

    public UtilityClass setConnectionDbLocal(Connection connectionDbLocal) {
        this.connectionDbLocal = connectionDbLocal;
        return this;
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

    public void mailSend(String text, String subject, String to, String from, String type, String password) throws MessagingException {

        Session session = Session.getDefaultInstance(mailProp,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });
        session.setDebug(true);
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));

        InternetAddress[] parse = InternetAddress.parse(to, true);
        message.setRecipients(Message.RecipientType.TO, parse);
        message.setSubject(subject);
        message.setContent(text, type);
        Transport.send(message);

    }

    protected void checkSetPcName(Label label) throws SQLException {
        try (Connection connection = new UtilityClass().getConnectionDbLocal()) {
            PreparedStatement prep = null;
            prep = connection.prepareStatement("SELECT * FROM system_settings WHERE name=?");
            prep.setString(1, "pcname");
            ResultSet resultSet = prep.executeQuery();
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    label.setText(resultSet.getString("config"));

                }
            } else {

                label.setText(getComputerName());

            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public String getComputerName() {
        String pcName = null;
        try {
            PreparedStatement prep = connectionDbLocal.prepareStatement("SELECT * FROM system_settings WHERE name=?");
            prep.setString(1, "pcname");
            ResultSet rs = prep.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    pcName = rs.getString("config");
                }
            } else {
                Map<String, String> env = System.getenv();
                if (env.containsKey("COMPUTERNAME"))
                    pcName = env.get("COMPUTERNAME");
                else pcName = env.getOrDefault("HOSTNAME", "Unknown Computer");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pcName;
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
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(alertType);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.initOwner(owner);
                alert.showAndWait();
            }
        });

    }

    public void checkEmailAndPassword() throws SQLException, NullPointerException {

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = new UtilityClass().getConnection().prepareStatement("SELECT * FROM users where email=?");
        } catch (IOException e) {
            e.printStackTrace();
        }
        preparedStatement.setString(1, user.get("user"));
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            if (resultSet.getString("backupemailPassword") == null || resultSet.getString("backupemail") == null) {
//                    reportIssues.setDisable(true);
//                    reportIssuesMenu.setVisible(false);
//                    reportIssues.setText("NO EMAIL CONFIGURED");
//                    startDay.setDisable(true);
//                    startDayMenu.setDisable(true);
//                    endDay.setDisable(true);
//                    endDayMenu.setDisable(true);
//                    backup.setDisable(true);
//                    backupMenu.setDisable(true);
                showAlert(Alert.AlertType.INFORMATION, config.panel.get("panel").getScene().getWindow(), "SET UP EMAIL FOR BACKUPS", "YOU NEED TO CREATE A BACK UP EMAIL AND A PASSWORD FOR THAT EMAIL FOR ONLINE BACKUPS TO TAKE PLACE IN ACCOUNT SECTION");
            } else {
                if (resultSet.getString("backupemail").contains("gmail")) {
                    mailProp.put("mail.smtp.auth", true);
                    mailProp.put("mail.smtp.starttls.enable", "true");
                    mailProp.put("mail.smtp.host", "smtp.gmail.com");
                    mailProp.put("mail.smtp.port", "587");
                    mailProp.put("mail.smtp.ssl.trust", "smtp.gmail.com");
                } else if (resultSet.getString("backupemail").contains("outlook")) {
                    mailProp.put("mail.smtp.auth", "true");
                    mailProp.put("mail.smtp.starttls.enable", "true");
                    mailProp.put("mail.smtp.host", "smtp-mail.outlook.com");
                    mailProp.put("mail.smtp.port", "587");
                }
            }
        }
    }

    public void systemSettingsBackUp() throws SQLException {
        PreparedStatement preparedStatement1;
        preparedStatement1 = connection.prepareStatement("INSERT INTO systemsettings (name,type,value)VALUES (?,?,?)");
        preparedStatement1.setString(1, "backup");
        preparedStatement1.setString(2, "security");
        preparedStatement1.setString(3, "STARTUP BACKUP");
        preparedStatement1.executeUpdate();
//        preparedStatement1.close();

    }

    public void systemSettingsexportFormat() throws SQLException {
        PreparedStatement preparedStatement1;
        preparedStatement1 = connection.prepareStatement("INSERT INTO systemsettings (name,type,value)VALUES (?,?,?)");
        preparedStatement1.setString(1, "exportFormat");
        preparedStatement1.setString(2, "reporting");
        preparedStatement1.setString(3, "PDF");
        preparedStatement1.executeUpdate();
//        preparedStatement1.close();

    }

    private void takeSnapShot(Scene scene) {
        WritableImage writableImage =
                new WritableImage((int) scene.getWidth(), (int) scene.getHeight());
        scene.snapshot(writableImage);

        File file = new File(fileSavePath + "\\images\\" + new Date().toString() + ".png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            System.out.println("snapshot saved: " + file.getAbsolutePath());
        } catch (IOException ignore) {
//            Logger.getLogger(JavaFXSnapshot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clearDir() {
        try {
            FileUtils.cleanDirectory(new File(sysconfig.get("backUpLoc") + "\\unzippedFiles"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exit() {
        Platform.exit();
        System.exit(111);
    }

}




