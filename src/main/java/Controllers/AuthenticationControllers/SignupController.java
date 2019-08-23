package Controllers.AuthenticationControllers;

import Controllers.UtilityClass;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import logging.LogClass;
import securityandtime.Security;

import java.awt.*;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;

import static securityandtime.config.site;

//end of imports
public class SignupController extends UtilityClass implements Initializable {
    public Label clock;
    @FXML
    private PasswordField password, passwordconfirmation;
    @FXML
    private TextField email, name, IDNUMBER, key;
    @FXML
    private Hyperlink link;
    @FXML
    private Button login1, signup1;
    @FXML
    private AnchorPane parent1;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
//
        timeMain(clock);
        link.setOnMousePressed(event -> {
            try {
//                    todo change when created website
                Desktop.getDesktop().browse(new URL(site).toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });


        listenEnter();
        login1.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                parent1.getChildren().removeAll();
                try {
                    parent1.getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml"))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        signup1.setOnMousePressed(event -> registerUser());
    }

    private void listenEnter() {
        password.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                registerUser();
            }
        });
        passwordconfirmation.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                registerUser();
            }
        });
        email.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                registerUser();
            }
        });
        name.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                registerUser();
            }
        });
        IDNUMBER.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                registerUser();
            }
        });
        key.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                registerUser();
            }
        });
    }

    private void registerUser() {

        if (password.getText().isEmpty() ||
                passwordconfirmation.getText().isEmpty() ||
                email.getText().isEmpty() || name.getText().isEmpty()
                || IDNUMBER.getText().isEmpty() ||
                key.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, parent1.getScene().getWindow(),
                    "Fill all the fields", "Please fill all the fields");
        } else {
            if (!password.getText().equals(passwordconfirmation.getText())) {
                showAlert(Alert.AlertType.INFORMATION, parent1.getScene().getWindow(),
                        "Your passwords don't match", "Please enter matching passwords");
            } else {
//                Connection snm = connectiondb.getConnect();
                try {
                    try {
                        insertion(email, name, IDNUMBER, key, password, passwordconfirmation);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }


                } catch (SQLException e) {
                    LogClass.getLogger().log(Level.SEVERE, " Registration not successful");
                    showAlert(Alert.AlertType.INFORMATION, parent1.getScene().getWindow(),
                            "Transaction unsuccessfull!!", "Registration not successful");
                    e.printStackTrace();
                }

            }
        }
    }

    private void insertion(TextField email, TextField name, TextField IDNUMBER, TextField key, PasswordField password, PasswordField passwordconfirmation) throws SQLException, NoSuchAlgorithmException {
        String str = email.getText() + name.getText() + password.getText() + IDNUMBER.getText() + new java.util.Date().toString();
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(str.getBytes(), 0, str.length());
        String hash = new BigInteger(1, messageDigest.digest()).toString(16);
        System.out.println("MD5: " + hash);
        Connection connection = getConnection();
        PreparedStatement statementemail = connection.prepareStatement("SELECT * FROM users WHERE email=? ");
        statementemail.setString(1, email.getText());
        ResultSet resultSetemail = statementemail.executeQuery();
        if (resultSetemail.isBeforeFirst()) {
            showAlert(Alert.AlertType.WARNING, parent1.getScene().getWindow(),
                    "EMAIL IN USE", "EMAIL IS IN USE");
//                System.out.println("resultSet lacks values");
            LogClass.getLogger().log(Level.SEVERE, " EMAIL IS IN USE");

        } else {

            PreparedStatement statementname = connection.prepareStatement("SELECT * FROM users WHERE employeename=? ");
            statementname.setString(1, name.getText());
            ResultSet resultSetname = statementname.executeQuery();
            if (resultSetname.isBeforeFirst()) {
                showAlert(Alert.AlertType.WARNING, parent1.getScene().getWindow(),
                        "NAME IN USE", "NAME IS IN USE");
//                System.out.println("resultSet lacks values");
                LogClass.getLogger().log(Level.SEVERE, " NAME IS IN USE");

            } else {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(employeename,email,password, employeeid,activated,hash,subscriberkey)VALUES(?,?,?,?,?,?,?)");

                preparedStatement.setString(1, name.getText());
                preparedStatement.setString(2, email.getText());
                preparedStatement.setString(3, Security.hashPassword(password.getText()));
                preparedStatement.setString(4, IDNUMBER.getText());
                preparedStatement.setBoolean(5, true);
                preparedStatement.setString(6, hash);
                preparedStatement.setString(7, key.getText());
                if (preparedStatement.executeUpdate() > 0) {
                    LogClass.getLogger().log(Level.CONFIG, " Registration successful");
                    showAlert(Alert.AlertType.INFORMATION, parent1.getScene().getWindow(),
                            "Transaction successfull!!", "Registration  successful");
                    parent1.getChildren().removeAll();
                    try {
                        parent1.getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml"))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    LogClass.getLogger().log(Level.SEVERE, " Registration not successful");
                    showAlert(Alert.AlertType.INFORMATION, parent1.getScene().getWindow(),
                            "Transaction unsuccessfull!!", "Registration not successful");
                }

            }
        }
//    Properties properties = System.getProperties();
//    properties.setProperty("mail.smtp.host", config.host);
//    Session session = Session.getDefaultInstance(properties);
//
//    //compose the message
//    try{
//        MimeMessage message = new MimeMessage(session);
//        message.setFrom(new InternetAddress(config.from));
//        message.addRecipient(Message.RecipientType.TO,new InternetAddress(email.getText()));
//        message.setSubject("Account activation for Nanosoft POS software");
//        message.setText("Hello, this is example of sending email");
////        todo change email body and change email properties
//
//        // Send message
//        Transport.send(message);
//        System.out.println("message sent successfully....");
//
//    }catch (MessagingException mex) {mex.printStackTrace();}
    }

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.showAndWait();
    }

    public Label getClock() {
        return clock;
    }

    public SignupController setClock(Label clock) {
        this.clock = clock;
        return this;
    }

    public PasswordField getPassword() {
        return password;
    }

    public SignupController setPassword(PasswordField password) {
        this.password = password;
        return this;
    }

    public PasswordField getPasswordconfirmation() {
        return passwordconfirmation;
    }

    public SignupController setPasswordconfirmation(PasswordField passwordconfirmation) {
        this.passwordconfirmation = passwordconfirmation;
        return this;
    }

    public TextField getEmail() {
        return email;
    }

    public SignupController setEmail(TextField email) {
        this.email = email;
        return this;
    }

    public TextField getName() {
        return name;
    }

    public SignupController setName(TextField name) {
        this.name = name;
        return this;
    }

    public TextField getIDNUMBER() {
        return IDNUMBER;
    }

    public SignupController setIDNUMBER(TextField IDNUMBER) {
        this.IDNUMBER = IDNUMBER;
        return this;
    }

    public TextField getKey() {
        return key;
    }

    public SignupController setKey(TextField key) {
        this.key = key;
        return this;
    }

    public Hyperlink getLink() {
        return link;
    }

    public SignupController setLink(Hyperlink link) {
        this.link = link;
        return this;
    }

    public Button getLogin1() {
        return login1;
    }

    public SignupController setLogin1(Button login1) {
        this.login1 = login1;
        return this;
    }

    public Button getSignup1() {
        return signup1;
    }

    public SignupController setSignup1(Button signup1) {
        this.signup1 = signup1;
        return this;
    }

    public AnchorPane getParent1() {
        return parent1;
    }

    public SignupController setParent1(AnchorPane parent1) {
        this.parent1 = parent1;
        return this;
    }

    @FXML
    private void close_app(MouseEvent event) {
        System.exit(0);
    }


}

