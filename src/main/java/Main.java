import Controllers.ShopControllers.ShopController;
import Controllers.UtilityClass;
import com.sun.istack.internal.NotNull;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logging.LogClass;
import securityandtime.AesCrypto;
import securityandtime.CheckConn;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import static securityandtime.config.*;

/**
 * @author Steve muema
 */
public class Main extends Application {

    static Stage stage = null;
    private String licenseId;

    public static void main(String[] args) throws InterruptedException {
        Runnable target;
        Thread thread = new Thread(new NetworkCheck());
        thread.join();
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Path path = Paths.get(fileSavePath);

        if (!Files.exists(path)) {

            try {
                Files.createDirectories(path);
                Files.createDirectories(Paths.get(fileSavePath + "\\licenses"));
                Files.createDirectories(Paths.get(fileSavePath + "\\dependencies"));
                Files.createDirectories(Paths.get(fileSavePath + "\\images"));
                Files.createDirectories(Paths.get(fileSavePath + "\\files"));
                new File(fileSavePath + "files\\shoppingLocal.db");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        Main.CallerMethod();
        launch(args);
    }

    private static void CallerMethod() {
        new CheckConn();
        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(() -> {
            if (CheckConn.pingHost(securityandtime.config.host, 443, 2000)) {
                LogClass.getLogger().log(Level.INFO, "CONNECTED");

            } else {
                LogClass.getLogger().log(Level.WARNING, "NOT CONNECTED");

            }
        });

    }

    private static void createSqliteDb() {
        Connection connection = null;
        UtilityClass utilityClass = new UtilityClass();

        try {
            connection = utilityClass.getConnectionDbLocal();
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            String heldTransactionsList = "CREATE TABLE IF NOT EXISTS heldTransactionList (" + "id INTEGER primary key autoincrement ,name TEXT ,transactionid text)";
            statement.executeUpdate(heldTransactionsList);
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS settings (" + "id INTEGER primary key autoincrement ,owner TEXT ,expirydate text,creationdate text,type text)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS system_settings (" + "id INTEGER primary key autoincrement ,name TEXT ,config text)");


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
//            System.err.println(e.getMessage());
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
            if (!file.isHidden()) {
                Path path = Paths.get(fileSavePath + "licenses");

                //set hidden attribute
                Files.setAttribute(path, "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);

                File fl = new File(fileSavePath + "licenses");
                File[] files = fl.listFiles(new FileFilter() {
                    public boolean accept(File file) {
                        return file.isFile();
                    }
                });
                long lastMod = Long.MIN_VALUE;
                File choice = null;
                assert files != null;
                for (File f : files) {
                    if (!f.isHidden()) {
                        Path pathf = Paths.get(f.getAbsolutePath());

                        Files.setAttribute(pathf, "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);

                    }
                }


            }

            PreparedStatement pre = new UtilityClass().getConnection().prepareStatement("SELECT * FROM systemsettings WHERE name=?");
            pre.setString(1, "licenseId");
            ResultSet resultSet = pre.executeQuery();
            if (resultSet.isBeforeFirst()) {
                //exists
                while (resultSet.next()) {
                    licenseId = resultSet.getString("value");
                }
            }

            FileInputStream fileInputStream = new FileInputStream(licensepath);

            byte[] fileContent = new byte[(int) file.length()];

            int i = fileInputStream.read(fileContent);
//            System.out.println("bytes read are " + i);
            StringBuilder builderEnc = new StringBuilder();
            StringBuilder builder = new StringBuilder();

            for (byte b : fileContent
            ) {
                builderEnc.append((char) b);
            }
            String decrypt = AesCrypto.decrypt(encryptionkey, builderEnc.toString());
            if (throwables.containsKey("invalidlicense")) {
                Parent root = FXMLLoader.load(getClass().getResource("AuthenticationFiles/licensingPanel.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
//                Media hit = new Media(getClass().getClassLoader().getResource("sounds/notification.wav").toString());
//                MediaPlayer mediaPlayer = new MediaPlayer(hit);
//                mediaPlayer.play();
                System.out.println("Expired license");
                stage.initStyle(StageStyle.DECORATED);
                stage.getIcons().add(image);
                stage.setOnCloseRequest(event -> {
                    Platform.exit();
                    System.exit(123);
                });

//        APP TITLE
                stage.setTitle(company + year + version + " Licensing" + "       CLIENT ID        " + licenseId);

                stage.setMaximized(true);

                stage.setFullScreen(true);
                Main.stage = stage;
                stage.show();
            } else {
                System.out.println(decrypt);
                builder.append(decrypt);


                long time = CheckConn.timelogin().getTime() / 1000;//get current time
//            //System.out.println(time + Long.parseLong(builder.toString().split(":::")[2]));
                if (time > Long.parseLong(builder.toString().split(":::")[2])) {
                    Parent root = FXMLLoader.load(getClass().getResource("AuthenticationFiles/licensingPanel.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);

                    System.out.println("Expired license");
                    stage.initStyle(StageStyle.DECORATED);
                    stage.getIcons().add(image);
                    stage.setOnCloseRequest(event -> {
                        Platform.exit();
                        System.exit(123);
                    });

//        APP TITLE
                    stage.setTitle(company + year + version + " Licensing" + "       CLIENT ID       " + licenseId);


//                    stage.setFullScreen(true);
                    Main.stage = stage;
                    stage.show();
                } else {
                    license.put("name", builder.toString().split(":::")[0]);
                    license.put("email", builder.toString().split(":::")[1]);
                    license.put("time", builder.toString().split(":::")[2]);
                    license.put("id", builder.toString().split(":::")[4]);
                    license.put("number", builder.toString().split(":::")[5]);
                    license.put("pkg", builder.toString().split(":::")[6]);
                    license.put("clientId", licenseId);
                    if (builder.toString().split(":::")[6].equalsIgnoreCase("pos") || builder.toString().split(":::")[6].equalsIgnoreCase("all") || builder.toString().split(":::")[6].contains("pos") || builder.toString().split(":::")[6].contains("POS")) {
                        fileInputStream.close();
                        AnchorPane root = FXMLLoader.load(getClass().getResource("AuthenticationFiles/SplashScreen.fxml"));
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.initStyle(StageStyle.DECORATED);

                        stage.getIcons().add(image);
                        stage.setTitle(company + year + version + "      CLIENT ID        " + licenseId);//TITLE
                        stage.setOnCloseRequest(event -> {
                            Platform.exit();
                            System.exit(123);
                        });
                        Main.stage = stage;
                        stage.show();
                    } else {
                        throwables.put("INVALID KEY", new InvalidObjectException("invalid license key generated"));
                        new UtilityClass().mailSend("INVALID LICENSE FOR THE POS PACKAGE.CONTACT THE SYSTEM ADMINISTRATORS TO GET A NEW LICENSE", "ERROR REPORT", license.get("email"), from, "text/plain", mailPassword);
                        fileInputStream.close();
                        AnchorPane root = FXMLLoader.load(getClass().getResource("AuthenticationFiles/SplashScreen.fxml"));
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.initStyle(StageStyle.DECORATED);

                        stage.getIcons().add(image);
                        stage.setTitle(company + year + version + "     CLIENT ID       " + licenseId);//TITLE
                        stage.setOnCloseRequest(event -> {
                            Platform.exit();
                            System.exit(123);
                        });
                        Main.stage = stage;
                        stage.show();
                    }

                }
            }
        } else {
//            GO TO LICENSING PANEL
            Parent root = FXMLLoader.load(getClass().getResource("AuthenticationFiles/licensingPanel.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.DECORATED);
            stage.getIcons().add(image);
            stage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(123);
            });

//        APP TITLE
            stage.setTitle(company + year + version + " Licensing" + "       CLIENT ID       " + licenseId);
            Main.stage = stage;
            stage.show();
        }

    }
}



