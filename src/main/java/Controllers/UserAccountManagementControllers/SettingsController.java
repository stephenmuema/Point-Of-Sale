package Controllers.UserAccountManagementControllers;

import Controllers.UtilityClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import securityandtime.config;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Objects;
import java.util.ResourceBundle;

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
    private CheckBox duringStartup;
    @FXML
    private CheckBox duringEndDay;
    @FXML
    private Button changeBackUpLocation;
    private Connection connection = getConnection();
    @FXML
    private Button userAccountHintChange;
    private PreparedStatement preparedStatement;

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
        try {
            initializer();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        config.panel.put("panel", panel);

        buttonListeners();
        menuListeners();
    }

    private void initializer() throws SQLException, IOException {
        String email = config.user.get("user");

        if (!config.login.containsKey("loggedinasadmin")) {

            backupEmailChangeButton.setVisible(false);
            backupEmail.setVisible(false);
            backupEmailLabel.setVisible(false);
            changeBackUpLocation.setVisible(false);
            currentBackUpLocation.setVisible(false);
            duringStartup.setVisible(false);
            duringEndDay.setVisible(false);

        }
        preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email=? ");
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            if (resultSet.getString("backupemail") == null) {
                PreparedStatement insertDefaultBackupEmail = connection.prepareStatement("UPDATE users SET backupemail=? WHERE id=? ");
                insertDefaultBackupEmail.setString(1, email);
                insertDefaultBackupEmail.setInt(2, id);
                insertDefaultBackupEmail.execute();
                panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/Settings.fxml")))));

            }
            userAccountName.setText(resultSet.getString("employeename"));
            userAccountEmail.setText(resultSet.getString("email"));
            if (config.login.containsKey("loggedinasadmin")) {
                backupEmail.setText(resultSet.getString("backupemail"));
            }
        }
        resultSet.close();

    }

    private void menuListeners() {
//        menu listeners
    }

    private void buttonListeners() {
        userAccountEmailChange.setOnAction(event -> {
//            update db change email
        });
        userAccountNameChange.setOnAction(event -> {
            //            update db change name

        });
        userAccountPasswordChange.setOnAction(event -> {
            //            update db change password

        });
        userAccountHintChange.setOnAction(event -> {
            //            update db change hint

        });
        backupEmailChangeButton.setOnAction(event -> {
            //            update db change backup email

        });
        changeBackUpLocation.setOnAction(event -> {
            //            update db change backup location

        });
    }
}
