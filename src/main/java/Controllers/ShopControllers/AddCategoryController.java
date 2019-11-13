package Controllers.ShopControllers;

import Controllers.UtilityClass;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddCategoryController extends UtilityClass implements Initializable {
    public AnchorPane panel;
    public TextField name;
    public TextArea comment;
    public Button submit;

    public AddCategoryController() throws IOException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        submit.setOnAction(event -> {
            if (name.getText().isEmpty() || comment.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "FILL ALL FIELDS BEFORE SUBMITTING THIS FORM");
            } else {
                Connection connection = null;
                try {
                    connection = new UtilityClass().getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM cost_category WHERE category_name=?");
                    preparedStatement.setString(1, name.getText());
                    ResultSet rs = preparedStatement.executeQuery();
                    if (rs.isBeforeFirst()) {
                        showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "SUCH A CATEGORY EXISTS ALREADY");
                    } else {
//insert category into table
                        preparedStatement = connection.prepareStatement("INSERT INTO cost_category(category_name, description) VALUES (?,?)");
                        preparedStatement.setString(1, name.getText());
                        preparedStatement.setString(2, comment.getText());
                        if (preparedStatement.executeUpdate() == 0) {
                            showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "ERROR...CHECK DATABASE");
                        } else {
                            showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "CATEGORY ADDED SUCCESSFULLY");
                            name.clear();
                            comment.clear();
                        }
                    }
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
