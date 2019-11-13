package Controllers.ShopControllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCategoryController implements Initializable {
    public AnchorPane panel;
    public TextField name;
    public TextArea comment;
    public Button submit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        submit.setOnAction(event -> {

        });
    }
}
