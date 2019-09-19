package Controllers.UserAccountManagementControllers;

import Controllers.UtilityClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import securityandtime.AesCrypto;
import securityandtime.Security;
import securityandtime.config;

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

import static securityandtime.config.encryptionkey;
import static securityandtime.config.initVector;

public class SettingsController extends UtilityClass implements Initializable {
    public AnchorPane panel;
    public Label userAccountNameLabel;
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
    private Connection connection = getConnection();
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

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userAccountHintChange.setVisible(false);
        userAccountHintSet.setVisible(false);
        try {
            initializer();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        config.panel.put("panel", panel);
        periodical.setToggleGroup(tg);
        startUp.setToggleGroup(tg);
        endDay.setToggleGroup(tg);
        buttonListeners();
        menuListeners();
        tg.selectedToggleProperty().addListener((ob, o, n) -> {

            RadioButton rb = (RadioButton) tg.getSelectedToggle();

            if (rb != null) {
                String s = rb.getText();
//change backup in mysql
                try {
                    preparedStatement = connection.prepareStatement("UPDATE systemsettings SET value=? WHERE name=?");
                    preparedStatement.setString(1, s);
                    preparedStatement.setString(2, "backup");
                    preparedStatement.executeUpdate();

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
    }

    private void initialiseRadioButtons() throws SQLException {
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
            System.out.println("errrrr");
        }
    }


    private void reload() {
        try {
            panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/Settings.fxml")))));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initializer() throws SQLException, IOException {

        if (!config.login.containsKey("loggedinasadmin")) {
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
        preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email=? ");
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            if (resultSet.getString("backupemail") == null) {

                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert1.setTitle("BACK UP EMAIL SETUP PROCEDURE");
                alert1.setHeaderText(null);
                alert1.setContentText("HI...YOU NEED TO SET UP THE BACK UP EMAIL FOR ONLINE BACKUPS");
                alert1.initOwner(config.panel.get("panel").getScene().getWindow());
                Optional<ButtonType> option = alert1.showAndWait();
                if (option.isPresent() && option.get() == ButtonType.OK) {
                    try {
                        changeColumn("users", "backupemail");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "DEFAULT EMAIL", "THIS OPERATION HAS MADE YOUR ACCOUNT YOUR BACK UP EMAIL");
                    PreparedStatement insertDefaultBackupEmail = connection.prepareStatement("UPDATE users SET backupemail=? WHERE id=? ");
                    insertDefaultBackupEmail.setString(1, email);
                    insertDefaultBackupEmail.setInt(2, id);
                    insertDefaultBackupEmail.execute();
                    reload();
                }

//                reload();
            }
            if (resultSet.getString("backupemailPassword") == null) {
                showAlert(Alert.AlertType.INFORMATION, config.panel.get("panel").getScene().getWindow(), "SET PASSWORD FOR BACKUPS", "YOU NEED TO CREATE A BACK UP EMAIL AND A PASSWORD FOR THAT EMAIL FOR ONLINE BACKUPS TO TAKE PLACE");
                changeColumn("users", "backupemailPassword");

            }
            if (resultSet.getString("passwordhint").isEmpty()) {
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
        resultSet.close();

    }

    private void menuListeners() {
//        menu listeners
    }

    private void dialogBoxHint(String title, String text) throws SQLException {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(text);

// Traditional way to get the response value.
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
                dialogBoxHint("SET HINT", "PLEASE INPUT YOUR NEW LOGIN HINT(MAX 30)");
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });
        userAccountHintSet.setOnAction(event -> {
                    try {
                        dialogBoxHint("SET HINT", "PLEASE INPUT YOUR LOGIN HINT(MAX 30)");
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
            reload();
        });
    }

    private void changeColumn(String table, String column) throws SQLException {
        if (column.equals("backupemail")) {
//            user.get("backupemail"));
//                properties.setProperty(MysqlExportService.EMAIL_PASSWORD,user.get("backupemailpassword"));
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
                    showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "DATABASE HAS BEEN UPDATED SUCCESSFULLY");
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
//        Security.hashPassword(password.getText()
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
