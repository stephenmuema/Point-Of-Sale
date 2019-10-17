package Controllers.UserAccountManagementControllers;

import Controllers.IdleMonitor;
import Controllers.UtilityClass;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;
import securityandtime.AesCrypto;
import securityandtime.Security;
import securityandtime.config;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import static securityandtime.config.*;

public class SettingsController extends UtilityClass implements Initializable {
    @FXML
    private AnchorPane panel;
    @FXML
    private Label userAccountNameLabel;
    @FXML
    private Label stationName;
    @FXML
    private Button changeStationName;
    @FXML
    private Button userAccountNameChange;
    @FXML
    private Label userAccountName;
    @FXML
    private Label userAccountEmailLabel;
    @FXML
    private Label userAccountEmail;
    @FXML
    private Button userAccountEmailChange;
    @FXML
    private Button userAccountPasswordChange;
    @FXML
    private Label backupEmailLabel;
    @FXML
    private Label backupEmail;
    @FXML
    private Button backupEmailChangeButton;
    @FXML
    private Label currentBackUpLocation;

    @FXML
    private Button changeBackUpLocation;
    @FXML
    private Button userAccountHintChange;
    private PreparedStatement preparedStatement;
    @FXML
    private Button userAccountHintSet;
    private String email = config.user.get("user");
    @FXML
    private RadioButton endDay;
    @FXML
    private RadioButton startUp;
    @FXML
    private RadioButton periodical;
    private ToggleGroup tg = new ToggleGroup();
    @FXML
    private Button backupEmailChangePassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userAccountHintChange.setVisible(false);
        userAccountHintSet.setVisible(false);
        try {
            initializer();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        config.panel.put("panel", panel);
        periodical.setToggleGroup(tg);
        startUp.setToggleGroup(tg);
        endDay.setToggleGroup(tg);
        buttonListeners();
        menuListeners();
        tg.selectedToggleProperty().addListener((ob, o, n) -> {
            UtilityClass utilityClass = new UtilityClass();
            Connection connection = utilityClass.getConnection();
            RadioButton rb = (RadioButton) tg.getSelectedToggle();

            if (rb != null) {
                String s = rb.getText();
//change backup in mysql
                try {
                    PreparedStatement prep = connection.prepareStatement("SELECT * FROM systemsettings WHERE name=?");
                    prep.setString(1, "backup");
                    ResultSet rs = prep.executeQuery();
                    if (!rs.isBeforeFirst()) {
////                      rs.close();
////                      prep.close();
                        systemSettingsBackUp();

                    } else {
                        preparedStatement = connection.prepareStatement("UPDATE systemsettings SET value=? WHERE name=?");
                        preparedStatement.setString(1, s);
                        preparedStatement.setString(2, "backup");
                        preparedStatement.executeUpdate();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            initialiseRadioButtons();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        IdleMonitor idleMonitor = new IdleMonitor(Duration.seconds(3600),
                () -> {
                    try {
                        config.login.put("loggedout", true);

                        panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml")))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, true);
        idleMonitor.register(panel, Event.ANY);
    }

    private void initialiseRadioButtons() throws SQLException {
        UtilityClass utilityClass = new UtilityClass();
        Connection connection = utilityClass.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM systemsettings WHERE name=?");
        preparedStatement.setString(1, "backup");
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.isBeforeFirst()) {
            while (rs.next()) {
                String buttonVal = rs.getString("value");
                if (buttonVal.contains("STARTUP BACKUP")) {
                    startUp.setSelected(true);
                } else if (buttonVal.contains("END DAY BACK UP")) {
                    endDay.setSelected(true);
                } else {
                    periodical.setSelected(true);
                }

            }
        } else {
            systemSettingsBackUp();
            initialiseRadioButtons();
        }
    }


    private void reload() {
        try {
            panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/adminSettings.fxml")))));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initializer() throws SQLException {
        changeStationName.setVisible(false);
        stationName.setVisible(false);
        if (!config.login.containsKey("loggedinasadmin")) {
            changeStationName.setVisible(true);
            stationName.setVisible(true);
            backupEmailChangePassword.setVisible(false);
            backupEmailChangeButton.setVisible(false);
            backupEmail.setVisible(false);
            backupEmailLabel.setVisible(false);
            changeBackUpLocation.setVisible(false);
            currentBackUpLocation.setVisible(false);
            endDay.setVisible(false);
            startUp.setVisible(false);
            periodical.setVisible(false);

        }
        checkSetPcName(stationName);
        UtilityClass utilityClass = new UtilityClass();
        Connection connection = utilityClass.getConnection();
        PreparedStatement prep;
        try {
            prep = connection.prepareStatement("SELECT * FROM systemsettings WHERE name=?");
            prep.setString(1, "backupLocation");
            ResultSet resultSet = prep.executeQuery();
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    currentBackUpLocation.setText(resultSet.getString("value"));
                    sysconfig.put("backUpLoc", resultSet.getString("value"));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email=? ");
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");

            if (resultSet.getString("backupemail") == null && config.login.containsKey("loggedinasadmin")) {
                backupEmailChangeButton.setText("SET BACKUP EMAIL");


            } else if ((resultSet.getString("backupemail") == null || resultSet.getString("backupemail").isEmpty()) && config.login.containsKey("loggedinasadmin")) {
                backupEmailChangeButton.setText("SET BACKUP EMAIL");

            }


            if (resultSet.getString("backupemailPassword") == null && config.login.containsKey("loggedinasadmin")) {
                backupEmailChangePassword.setText("SET BACKUP EMAIL PASSWORD");
            } else if ((resultSet.getString("backupemailPassword") == null || resultSet.getString("backupemailPassword").isEmpty()) && config.login.containsKey("loggedinasadmin")) {
                backupEmailChangePassword.setText("SET BACKUP EMAIL PASSWORD");

            }

            if (resultSet.getString("passwordhint") == null) {
                userAccountHintSet.setVisible(true);
            } else if (resultSet.getString("passwordhint").isEmpty()) {
                userAccountHintSet.setVisible(true);
            } else {
                userAccountHintChange.setVisible(true);
            }
            userAccountName.setText(resultSet.getString("employeename"));
            userAccountEmail.setText(resultSet.getString("email"));
            if (config.login.containsKey("loggedinasadmin")) {
                backupEmail.setText(resultSet.getString("backupemail"));
            }
            if (!resultSet.getBoolean("admin")) {
                userAccountEmailChange.setText("ADMIN PRIVILEDGES REQUIRED");
                userAccountNameChange.setText("ADMIN PRIVILEDGES REQUIRED");
                userAccountNameChange.setDisable(true);
                userAccountEmailChange.setDisable(true);
            }
        }

    }


    private void menuListeners() {
//        menu listeners
    }

    private void dialogBoxHint(String text) throws SQLException {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("SET HINT");
        dialog.setHeaderText(null);
        dialog.setContentText(text);
        UtilityClass utilityClass = new UtilityClass();
        Connection connection = utilityClass.getConnection();
        Optional<String> result = dialog.showAndWait();
        preparedStatement = connection.prepareStatement("UPDATE users SET passwordhint=? WHERE email=?");
        String value = result.orElse(null);
        preparedStatement.setString(1, value);
        preparedStatement.setString(2, email);
        if (preparedStatement.executeUpdate() > 0) {
            showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "HINT HAS BEEN SET");
        } else {
            showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "NETWORK ERROR");

        }
        reload();
    }

    private void buttonListeners() {
        changeStationName.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("STATION NAME CHANGING ");
            alert.setHeaderText(null);
            alert.setContentText("CHANGING THE STATION NAME IS IRREVERSIBLE.ARE YOU SURE YOU WANT TO CONTINUE?");
            alert.initOwner(panel.getScene().getWindow());
            Optional<ButtonType> option = alert.showAndWait();
            if (option.isPresent() && option.get() == ButtonType.OK) {
                try {
                    changeColumnLocal("system_settings", "config");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            reload();
        });
        backupEmailChangePassword.setOnAction(event -> {
            try {
                changeColumn("users", "backupemailPassword");
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });
        userAccountEmailChange.setOnAction(event -> {
//            update db change email
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("ACCOUNT EMAIL CHANGING ");
            alert.setHeaderText(null);
            alert.setContentText("CHANGING YOUR ACCOUNT EMAIL IS IRREVERSIBLE.ARE YOU SURE YOU WANT TO CONTINUE?");
            alert.initOwner(panel.getScene().getWindow());
            Optional<ButtonType> option = alert.showAndWait();
            if (option.isPresent() && option.get() == ButtonType.OK) {
                try {
                    changeColumn("users", "email");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            reload();
        });
        userAccountNameChange.setOnAction(event -> {
            //            update db change name
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("ACCOUNT NAME CHANGING ");
            alert.setHeaderText(null);
            alert.setContentText("CHANGING YOUR ACCOUNT NAME IS IRREVERSIBLE.ARE YOU SURE YOU WANT TO CONTINUE?");
            alert.initOwner(panel.getScene().getWindow());
            Optional<ButtonType> option = alert.showAndWait();
            if (option.isPresent() && option.get() == ButtonType.OK) {
                try {
                    changeColumn("users", "employeename");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            reload();
        });
        userAccountPasswordChange.setOnAction(event -> {
            //            update db change password
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("IRREVERSABLE PROCEDURE");
            alert.setHeaderText(null);
            alert.setContentText("CHANGING YOUR PASSWORD IS IRREVERSIBLE.ARE YOU SURE YOU WANT TO CONTINUE?");
            alert.initOwner(panel.getScene().getWindow());
            Optional<ButtonType> option = alert.showAndWait();
            if (option.isPresent() && option.get() == ButtonType.OK) {
                try {
                    changePassword();
                } catch (SQLException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
        userAccountHintChange.setOnAction(event -> {
            //            update db change hint
            try {
                dialogBoxHint("PLEASE INPUT YOUR NEW LOGIN HINT(MAX 30)");
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });
        userAccountHintSet.setOnAction(event -> {
                    try {
                        dialogBoxHint("PLEASE INPUT YOUR LOGIN HINT(MAX 30)");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
        );
        backupEmailChangeButton.setOnAction(event -> {
            //            update db change backup email
            {
                //            update db change password
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("BACK UP EMAIL CHANGING ");
                alert.setHeaderText(null);
                alert.setContentText("CHANGING YOUR BACK UP EMAIL IS IRREVERSIBLE.ARE YOU SURE YOU WANT TO CONTINUE?");
                alert.initOwner(panel.getScene().getWindow());
                Optional<ButtonType> option = alert.showAndWait();
                if (option.isPresent() && option.get() == ButtonType.OK) {
                    try {
                        changeColumn("users", "backupemail");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            reload();
        });
        changeBackUpLocation.setOnAction(event -> {
            //            update db change backup location
            // get the file selected
            UtilityClass utilityClass = new UtilityClass();
            Connection connection = utilityClass.getConnection();
            DirectoryChooser dir_chooser = new DirectoryChooser();
            File file = dir_chooser.showDialog(panel.getScene().getWindow());

            if (file != null) {
                PreparedStatement prep = null;
                try {
                    prep = connection.prepareStatement("SELECT * FROM systemsettings WHERE name=?");
                    prep.setString(1, "backupLocation");
                    ResultSet resultSet = prep.executeQuery();
                    if (resultSet.isBeforeFirst()) {
//                       prep.close();
                        prep = connection.prepareStatement("UPDATE systemsettings SET value=? WHERE name=?");
                        prep.setString(1, file.getAbsolutePath());
                        prep.setString(2, "backupLocation");
                        sysconfig.put("backUpLoc", file.getAbsolutePath());

                        prep.executeUpdate();
//                        prep.close();

                    } else {
                        try {
                            prep = connection.prepareStatement("INSERT INTO systemsettings (name,type,value)VALUES (?,?,?)");
                            prep.setString(1, "backupLocation");
                            prep.setString(2, "security");
                            prep.setString(3, file.getAbsolutePath());
                            prep.executeUpdate();
//                            prep.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
////resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            } else {
                showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "CANCELLATION", "THE OPERATION WAS CANCELLED");
            }


            reload();
        });
    }

    private void changeColumn(String table, String column) throws SQLException {
        UtilityClass utilityClass = new UtilityClass();
        Connection connection = utilityClass.getConnection();
        if (column.equals("backupemail")) {

            String columnValue = dialogBoxCredentials(" CHANGE DATABASE BACK UP EMAIL", "INPUT YOUR NEW BACK UP EMAIL");
            if (columnValue == null) {
                PreparedStatement insertDefaultBackupEmail = connection.prepareStatement("UPDATE users SET backupemail=? WHERE email=? ");
                insertDefaultBackupEmail.setString(1, email);
                insertDefaultBackupEmail.setString(2, email);
                insertDefaultBackupEmail.execute();
                showAlert(Alert.AlertType.INFORMATION, config.panel.get("panel").getScene().getWindow(), "DEFAULT", "SELECTING NULL VALUE WILL RESULT IN A DEFAULT EMAIL BEING USED AS A BACKUP EMAIL");
            } else {
                preparedStatement = connection.prepareStatement("UPDATE  " + table + " SET  " + column + "=? WHERE email=?");
                preparedStatement.setString(1, columnValue);
                preparedStatement.setString(2, email);
                if (preparedStatement.executeUpdate() > 0) {
                    showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "DATABASE HAS BEEN UPDATED SUCCESSFULLY");
                } else {
                    showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "DATABASE HAS NOT BEEN UPDATED SUCCESSFULLY");

                }
            }
        } else if (column.equals("backupemailPassword")) {
            String columnValue = dialogBoxCredentials(" CHANGE BACK UP EMAIL PASSWORD INFORMATION", "INPUT YOUR NEW VALUES");
            if (columnValue == null) {
                showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "PASSWORD CANNOT BE NULL");

            } else {
                preparedStatement = connection.prepareStatement("UPDATE  " + table + " SET  " + column + "=? WHERE email=?");
                preparedStatement.setString(1, AesCrypto.encrypt(encryptionkey, initVector, columnValue));
                preparedStatement.setString(2, email);
                if (preparedStatement.executeUpdate() > 0) {
                    showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "DATABASE HAS BEEN UPDATED SUCCESSFULLY.PASSWORD HAS BEEN ENCRYPTED TO " + AesCrypto.encrypt(encryptionkey, initVector, columnValue));
                } else {
                    showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "DATABASE HAS NOT BEEN UPDATED SUCCESSFULLY");

                }
            }
        } else {
            String columnValue = dialogBoxCredentials(" CHANGE DATABASE INFORMATION", "INPUT YOUR NEW VALUES");
            preparedStatement = connection.prepareStatement("UPDATE  " + table + " SET  " + column + "=? WHERE email=?");
            preparedStatement.setString(1, columnValue);
            preparedStatement.setString(2, email);
            if (preparedStatement.executeUpdate() > 0) {
                showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "DATABASE HAS BEEN UPDATED SUCCESSFULLY");
            } else {
                showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "DATABASE HAS NOT BEEN UPDATED SUCCESSFULLY");

            }
            if (column.equals("email")) {
                config.user.put("user", columnValue);
            }
            if (column.equals("employeename")) {
                config.user.put("userName", columnValue);
            }
        }


    }

    private void changeColumnLocal(String table, String column) throws SQLException {

        UtilityClass utilityClass = new UtilityClass();
        Connection connection = utilityClass.getConnectionDbLocal();
        PreparedStatement prep = connection.prepareStatement("SELECT * FROM system_settings WHERE name=?");
        prep.setString(1, "pcname");
        ResultSet rs = prep.executeQuery();
        String columnValue = dialogBoxCredentials("PC NAME", " INPUT YOUR NEW PC NAME");

        if (!rs.isBeforeFirst()) {
            prep = connection.prepareStatement("INSERT INTO system_settings(name, config) VALUES (?,?)");
            prep.setString(1, "pcname");
            prep.setString(2, columnValue);
            prep.executeUpdate();
            connection.close();
        } else {
            prep = connection.prepareStatement("UPDATE  " + table + " SET  " + column + "=? WHERE name=?");
            prep.setString(1, columnValue);
            prep.setString(2, "pcname");
            if (prep.executeUpdate() > 0) {
                showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "DATABASE HAS BEEN UPDATED SUCCESSFULLY");
            } else {
                showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "DATABASE HAS NOT BEEN UPDATED SUCCESSFULLY");

            }
            connection.close();
        }
    }

    private String dialogBoxCredentials(String title, String text) throws SQLException {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(text);

// Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    private void changePassword() throws SQLException, NoSuchAlgorithmException {
        UtilityClass utilityClass = new UtilityClass();
        Connection connection = utilityClass.getConnection();
        String password = dialogBoxCredentials("PASSWORD CHANGE", "INPUT YOUR NEW PASSWORD");
        preparedStatement = connection.prepareStatement("UPDATE  users SET password=? WHERE email=?");
        System.out.println(password);
        preparedStatement.setString(1, Security.hashPassword(password));
        preparedStatement.setString(2, email);
        if (preparedStatement.executeUpdate() > 0) {
            showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "PASSWORD HAS BEEN UPDATED SUCCESSFULLY");
        } else {
            showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "PASSWORD HAS NOT BEEN UPDATED SUCCESSFULLY");

        }
    }
}

