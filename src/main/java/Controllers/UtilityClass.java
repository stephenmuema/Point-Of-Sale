package Controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import javafx.util.Duration;
import securityandtime.CheckConn;
import securityandtime.config;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Objects;

import static securityandtime.config.des;
import static securityandtime.config.localCartDb;

public class UtilityClass {
    private Connection connection;
    private Connection connectionDbLocal;


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
        config.login.put("loggedout", true);
        config.user.clear();
        try {
//                System.out.println("logging out");
            panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
//                    hrs= "0"+String.valueOf(hours-12);
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


    protected void timeMain(Label clock) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            //                int minutes = Integer.parseInt(String.valueOf(CheckConn.timelogin().getMinutes()));
//                int seconds = Integer.parseInt(String.valueOf(CheckConn.timelogin().getSeconds()));
//                int hours = Integer.parseInt(String.valueOf(CheckConn.timelogin().getHours()));

            //                    hrs= "0"+String.valueOf(hours-12);
            //            display time
            clock.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss a").format(Calendar.getInstance().getTime()));
            //                clock.setText(CheckConn.timelogin().getHours() + ":" + (mins) +":" + (secs)+ " " + pmam);
        }),
                new KeyFrame(Duration.seconds(1))
        );
//        refresh every one second
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


}


class GetNetworkAddress {

    public static String GetAddress(String addressType) {
        String address = "";
        InetAddress lanIp = null;
        try {

            String ipAddress = null;
            Enumeration<NetworkInterface> net = null;
            net = NetworkInterface.getNetworkInterfaces();

            while (net.hasMoreElements()) {
                NetworkInterface element = net.nextElement();
                Enumeration<InetAddress> addresses = element.getInetAddresses();

                while (addresses.hasMoreElements() && element.getHardwareAddress().length > 0 && !isVMMac(element.getHardwareAddress())) {
                    InetAddress ip = addresses.nextElement();
                    if (ip instanceof Inet4Address) {

                        if (ip.isSiteLocalAddress()) {
                            ipAddress = ip.getHostAddress();
                            lanIp = InetAddress.getByName(ipAddress);
                        }

                    }

                }
            }

            if (lanIp == null)
                return null;

            if (addressType.equals("ip")) {

                address = lanIp.toString().replaceAll("^/+", "");

            } else if (addressType.equals("mac")) {

                address = getMacAddress(lanIp);

            } else {

                throw new Exception("Specify \"ip\" or \"mac\"");

            }

        } catch (Exception ex) {

            ex.printStackTrace();

        }

        return address;

    }

    private static String getMacAddress(InetAddress ip) {
        String address = null;
        try {

            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            address = sb.toString();

        } catch (SocketException ex) {

            ex.printStackTrace();

        }

        return address;
    }

    private static boolean isVMMac(byte[] mac) {
        if (null == mac) return false;
        byte[][] invalidMacs = {
                {0x00, 0x05, 0x69},             //VMWare
                {0x00, 0x1C, 0x14},             //VMWare
                {0x00, 0x0C, 0x29},             //VMWare
                {0x00, 0x50, 0x56},             //VMWare
                {0x08, 0x00, 0x27},             //Virtualbox
                {0x0A, 0x00, 0x27},             //Virtualbox
                {0x00, 0x03, (byte) 0xFF},       //Virtual-PC
                {0x00, 0x15, 0x5D}              //Hyper-V
        };

        for (byte[] invalid : invalidMacs) {
            if (invalid[0] == mac[0] && invalid[1] == mac[1] && invalid[2] == mac[2]) return true;
        }

        return false;
    }

}

