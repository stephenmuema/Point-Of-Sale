package testing;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import securityandtime.CheckConn;
import securityandtime.config;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Collections;
import java.util.Objects;
import java.util.ResourceBundle;

import static securityandtime.config.des;

public class cartcontroller implements Initializable {
    public VBox shopPanel;
    public MenuItem logout;
    public Label clock;
    public Label username;
    public Button logoutb;
    public TextField barcodetext;
    public Button searchb;
    public TableView<CARTMASTER> cart;
    public TableColumn<CARTMASTER, String> name;
    public TableColumn<CARTMASTER, String> code;
//    public TableColumn <CartMaster,String> price;
//    public TableColumn <CartMaster,String> amount;
//    public TableColumn <CartMaster,String> cumulativeprice;

    public Button panel;
    private ObservableList<CARTMASTER> data;

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
        buttonListeners();
        setName();
//        editTable();
    }

    /*

    private void editTable() {
    //        edit amount and cumulative price
    cart.setEditable(true);
    amount.setCellFactory(TextFieldTableCell.forTableColumn());
    amount.setOnEditCommit(
    new EventHandler<TableColumn.CellEditEvent<CartMaster, String>>() {
    @Override
    public void handle(TableColumn.CellEditEvent<CartMaster, String> t) {
    ((CartMaster) t.getTableView().getItems().get(
    t.getTablePosition().getRow())
    ).setItemNumber(t.getNewValue());
    String newval = t.getNewValue();
    ((CartMaster) t.getTableView().getItems().get(
    t.getTablePosition().getRow())
    ).setItemCumulativeCost(String.valueOf(Integer.parseInt(t.getNewValue())*Integer.parseInt(((CartMaster) t.getTableView().getItems().get(
    t.getTablePosition().getRow())
    ).getItemPrice())));

    }
    }
    );
    }
    */
    private void tableLoad(String zcode) {
        data = FXCollections.observableArrayList();
        Connection connection = null;
        try {
            connection = DriverManager
                    .getConnection(des[2], des[0], des[1]);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM stocks WHERE itemcode=?");
                statement.setString(1, zcode);
                ResultSet resultSet = statement.executeQuery();


                while (resultSet.next()) {
                    CARTMASTER cartMaster = new CARTMASTER();
/*
System.out.println(resultSet.getString("name"));
ArrayList<CartMaster> list=new ArrayList<>();
list.add(cartMaster);
for (int i = 0; i < list.size(); i++) {
//                        System.out.println(crunchifyList.get(i));
list.get(i).setItemName(resultSet.getString("name"));
list.get(i).setItemId(resultSet.getInt("id"));
list.get(i).setItemBarCode(resultSet.getString("itemcode"));
list.get(i).setItemPrice(resultSet.getString("price"));
list.get(i).setItemNumber("1");
list.get(i).setItemCumulativeCost(String.valueOf(Integer.parseInt(resultSet.getString("price"))*Integer.parseInt(cartMaster.getItemNumber())));
data.add(list.get(i));
}
*/

                    cartMaster.setItemName(resultSet.getString("name"));
                    cartMaster.setItemId(resultSet.getInt("id"));
/*
cartMaster.setItemBarCode(resultSet.getString("itemcode"));
cartMaster.setItemPrice(resultSet.getString("price"));
cartMaster.setItemNumber("1");
cartMaster.setStorePic(Objects.requireNonNull(resultSet).getBinaryStream("image"));
cartMaster.setItemCumulativeCost(String.valueOf(Integer.parseInt(resultSet.getString("price"))*Integer.parseInt(cartMaster.getItemNumber())));
*/
                    data.add(cartMaster);
                }
                cart.setItems(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assert cart != null : "fx:id=\"cart\" was not injected: check your FXML ";
        name.setCellValueFactory(
                new PropertyValueFactory<>("itemName"));
        code.setCellValueFactory(
                new PropertyValueFactory<>("itemId"));
/*
price.setCellValueFactory(
new PropertyValueFactory<CartMaster, String>("itemPrice"));
cumulativeprice.setCellValueFactory(
new PropertyValueFactory<CartMaster, String>("itemCumulativeCost"));
amount.setCellValueFactory(new PropertyValueFactory<CartMaster,String>("itemNumber"));
*/
        cart.refresh();
    }

    private void setName() {
        String name = config.user.get("user");
        username.setText(name);
        System.out.println();
    }


    private void buttonListeners() {
        searchb.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                tableLoad(barcodetext.getText());
            }
        });
        logoutb.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //logout button destroy session variables
                config.login.put("loggedout", true);
                try {
                    shopPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/login.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        panel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
//                    shopPanel.getChildren().removeAll();
                    shopPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/UserAccountManagementFiles/panel.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void time() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            String mins = null, hrs = null, secs = null, pmam = null;
            try {
                int minutes = Integer.parseInt(String.valueOf(CheckConn.timelogin().getMinutes()));
                int seconds = Integer.parseInt(String.valueOf(CheckConn.timelogin().getSeconds()));
                int hours = Integer.parseInt(String.valueOf(CheckConn.timelogin().getHours()));

                if (hours >= 12) {
//                    hrs= "0"+String.valueOf(hours-12);
                    pmam = "PM";
                } else {
                    pmam = "AM";

                }
                if (minutes > 9) {
                    mins = String.valueOf(minutes);
                } else {
                    mins = "0" + String.valueOf(minutes);

                }
                if (seconds > 9) {
                    secs = String.valueOf(seconds);
                } else {
                    secs = "0" + String.valueOf(seconds);

                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                clock.setText(CheckConn.timelogin().getHours() + ":" + (mins) + ":" + (secs) + " " + pmam);

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


}
