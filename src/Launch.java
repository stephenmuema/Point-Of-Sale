import Controllers.ShopControllers.ShopController;
import com.sun.istack.internal.NotNull;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import logging.LogClass;
import securityandtime.CheckConn;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import static securityandtime.config.*;

/**
 * @author Steve muema
 */
public class Launch extends Application {

    static Stage stage = null;


    public static void main(String[] args) {
        Launch.CallerMethod();
//the launcher main method
        launch(args);
    }

    private static void CallerMethod() {
        LogClass.getLogger().log(Level.INFO, "LAUNCH CLASS:::ONLY LOG NEGATIVE MESSAGES");
        new CheckConn();
        ExecutorService service = Executors.newFixedThreadPool(4);
        service.submit(() -> {
            if (CheckConn.pingHost(securityandtime.config.host, 443, 2000)) {
                LogClass.getLogger().log(Level.INFO, "CONNECTED");

            } else {
                LogClass.getLogger().log(Level.INFO, "NOT CONNECTED");

            }
        });

    }

    private static void createSqliteDb() {
        Connection connection = null;
        try {
//            create cartdb
//            todo remember to change path to db
            connection = DriverManager.getConnection(localCartDb);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            String heldTransactionsList = "CREATE TABLE IF NOT EXISTS heldTransactionList (" + "id INTEGER primary key autoincrement ,name TEXT ,transactionid text)";
            statement.executeUpdate(heldTransactionsList);

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS settings (" + "id INTEGER primary key autoincrement ,owner TEXT ,expirydate text,creationdate text)");


            Statement heldTransactionsDetails = connection.createStatement();
            String heldItems = "CREATE TABLE IF NOT EXISTS heldItems (id integer primary key autoincrement,itemname text,itemprice text,itemid text,code text,amount text,cumulativeprice text ,transactionid text)";
            heldTransactionsDetails.executeUpdate(heldItems);
            new ShopController().setTransID();

            Statement Cart = connection.createStatement();
            String cartItems = "CREATE TABLE IF NOT EXISTS cartItems (id integer primary key autoincrement,itemname text,itemprice text,itemid integer,code text,amount text,cumulativeprice text ,transactionid text,pic BLOB)";
            heldTransactionsDetails.executeUpdate(cartItems);
//            System.out.println("created");
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no securityandtime file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                e.printStackTrace();
            }
        }
    }

    @Override
    public void start(@NotNull Stage stage) throws Exception {
        createSqliteDb();

        File file = new File(licensepath);
        boolean exists = file.exists();

        if (exists) {
//            GO TO SPLASHSCREEN
            FileInputStream fileInputStream = new FileInputStream(licensepath);
            byte[] fileContent = new byte[(int) file.length()];

            int i = fileInputStream.read(fileContent);
            System.out.println("bytes read are " + i);
            StringBuilder builder = new StringBuilder();

            for (byte b : fileContent
            ) {
                builder.append((char) b);
                System.out.print((char) b);
            }



            long time = CheckConn.timelogin().getTime() / 1000;//get current time
            System.out.println(time + Long.parseLong(builder.toString().split(":::")[2]));
            if (time > Long.parseLong(builder.toString().split(":::")[2])) {
                Parent root = FXMLLoader.load(getClass().getResource("resourcefiles/AuthenticationFiles/licensingPanel.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                Media hit = new Media(getClass().getClassLoader().getResource("resourcefiles/sounds/notification.wav").toString());
                MediaPlayer mediaPlayer = new MediaPlayer(hit);
                mediaPlayer.play();
                stage.initStyle(StageStyle.DECORATED);
                stage.getIcons().add(new Image("resourcefiles/images/banner_hardware.png"));
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        Platform.exit();
                        System.exit(123);
                    }
                });
                stage.setMaxWidth(1024.0);
                stage.setMaxHeight(600.0);
//        APP TITLE
                stage.setTitle("Nanotech Softwares Point of Sale 2019  (v 1.1) Licensing");
                stage.setMaxWidth(1200.0);
                stage.setMaxHeight(700.0);
                stage.setMaximized(false);
                stage.setFullScreen(false);
                Launch.stage = stage;
                stage.show();
            } else {
                license.put("name", builder.toString().split(":::")[0]);
                license.put("email", builder.toString().split(":::")[1]);
                license.put("time", builder.toString().split(":::")[2]);

                fileInputStream.close();
                AnchorPane root = FXMLLoader.load(getClass().getResource("resourcefiles/AuthenticationFiles/SplashScreen.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                Media hit = new Media(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/sounds/notification.wav")).toString());
                MediaPlayer mediaPlayer = new MediaPlayer(hit);
                mediaPlayer.play();
                stage.initStyle(StageStyle.DECORATED);
                stage.getIcons().add(new Image("resourcefiles/images/banner_hardware.png"));
//        todo change title later
                stage.setTitle("Nanotech Softwares Point of Sale 2019  (v 1.1)");
                stage.setMaxWidth(1024.0);
                stage.setMaxHeight(600.0);
//        APP TITLE
                stage.setMaxWidth(1200.0);
                stage.setMaxHeight(700.0);
                stage.setWidth(1200.0);
                stage.setHeight(700.0);
                stage.setMaximized(false);
                stage.setFullScreen(false);
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        Platform.exit();
                        System.exit(123);
                    }
                });
                Launch.stage = stage;
                stage.show();
            }
//            todo distinguish admin account from cashier account
        } else {
//            GO TO LICENSING PANEL
            Parent root = FXMLLoader.load(getClass().getResource("resourcefiles/AuthenticationFiles/licensingPanel.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            Media hit = new Media(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/sounds/notification.wav")).toString());
            MediaPlayer mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.play();
            stage.initStyle(StageStyle.DECORATED);
            stage.getIcons().add(new Image("resourcefiles/images/banner_hardware.png"));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Platform.exit();
                    System.exit(123);
                }
            });
//            0700758591
            stage.setMaxWidth(1024.0);
            stage.setMaxHeight(600.0);
//        APP TITLE
            stage.setTitle("Nanotech Softwares Point of Sale 2019  (v 1.1) Licensing");
            stage.setMaxWidth(1200.0);
            stage.setMaxHeight(700.0);
            stage.setMaximized(false);
            stage.setFullScreen(false);
            Launch.stage = stage;
            stage.show();
        }


    }


}



