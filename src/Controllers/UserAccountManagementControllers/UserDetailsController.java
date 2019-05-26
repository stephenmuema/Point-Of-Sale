package Controllers.UserAccountManagementControllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import securityandtime.config;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static securityandtime.config.des;

public class UserDetailsController implements Initializable {
    public Button updateb;
    public Label name, email;
    String namestr, emailstr;
    Connection connection;

    {
        try {
            connection = DriverManager
                    .getConnection(des[2], des[0], des[1]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fetchDetails();


    }

    private void fetchDetails() {
        String identifier = config.user.get("user");
        System.out.println(identifier);
        String sql = "SELECT * FROM users WHERE  email=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, identifier);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
//                edit and view details
                emailstr = resultSet.getString("email");
                namestr = resultSet.getString("employeename");
                name.setText(resultSet.getString("employeename"));
                email.setText(resultSet.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Button getUpdateb() {
        return updateb;
    }

    public void setUpdateb(Button updateb) {
        this.updateb = updateb;
    }

    public Label getName() {
        return name;
    }

    public void setName(Label name) {
        this.name = name;
    }

    public Label getEmail() {
        return email;
    }

    public void setEmail(Label email) {
        this.email = email;
    }

    public String getNamestr() {
        return namestr;
    }

    public void setNamestr(String namestr) {
        this.namestr = namestr;
    }

    public String getEmailstr() {
        return emailstr;
    }

    public void setEmailstr(String emailstr) {
        this.emailstr = emailstr;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
