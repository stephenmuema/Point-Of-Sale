package Controllers.ShopControllers;

import Controllers.UserAccountManagementControllers.IdleMonitor;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import securityandtime.config;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import static securityandtime.config.pricegot;

public class OnlinePaymentsController implements Initializable {
    public Button mpesa;
    public Button kcb;
    public Button back;
    public AnchorPane panel;
    public Label price;
    public Button goofflinepayments;
    private Integer pricevalue;


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
        IdleMonitor idleMonitor = new IdleMonitor(Duration.seconds(3600),
                () -> {
                    try {
                        config.login.put("loggedout", true);
                        panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/AuthenticationFiles/Login.fxml")))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, true);
        idleMonitor.register(panel, Event.ANY);
        buttonListeners();
        utilities();
    }

    private void utilities() {
        pricevalue = pricegot.get("price");
        price.setText(String.valueOf(pricegot.get("price")));
    }

    private void buttonListeners() {
        //button listeners
        goofflinepayments.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/shopFiles/showpriceexitcash.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        back.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("BACK TO PANEL");
            alert.setHeaderText(null);
            alert.setContentText("GO BACK TO PANEL?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Stage stage = (Stage) back.getScene().getWindow();
                stage.close();

            }
        });
    }
}
