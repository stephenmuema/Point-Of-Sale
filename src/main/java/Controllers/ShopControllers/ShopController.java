package Controllers.ShopControllers;

import Controllers.SuperClass;
import Controllers.UserAccountManagementControllers.IdleMonitor;
import Controllers.UtilityClass;
import MasterClasses.CartMaster;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;
import scanhandler.BarcodeScanner;
import securityandtime.CheckConn;
import securityandtime.config;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static Controllers.UtilityClass.shutdown;
import static securityandtime.config.*;

//made by steve
public class ShopController extends CartIdGenerator implements Initializable {
    public AnchorPane shopPanel;
    public Label clock;
    public Label username;
    public Button logoutb;
    public ImageView logoimage;
    public TextField barcodetext;
    public Button searchb;
    public TableView<CartMaster> cart;
    public TableColumn<CartMaster, String> name;
    public TableColumn<CartMaster, String> code;
    public TableColumn<CartMaster, String> price;
    public TableColumn<CartMaster, String> amount;
    public TableColumn<CartMaster, Integer> cumulativeprice;
    public Button deletefromcartb;
    public Button onlinepayments;
    public Button paycash;
    public Button holduserdatab;
    public Button panel;
    public Button clearb;
    public Label totalprice;
    public TableView<CartMaster> listViewHeldItems;
    public TableColumn<CartMaster, String> heldname, heldid;
    public Button loyaltiesB;
    //    public TextField searchname;
    public MenuItem logoutMenu;
    public MenuItem exitMenu;
    public MenuItem accountdetailsMenu;
    public MenuItem CreatorsMenu;
    public MenuItem helpMenu;
    public MenuItem stores;
    public MenuItem stocks;
    public Button clearheld;
    public Button endDay;
    private ObservableList<CartMaster> data;
    private int counter = 0;
    ArrayList<CartMaster> arrayList = new ArrayList<CartMaster>();
    private String transID;
    private SuperClass superClass = new SuperClass();

    private Connection connectionDbLocal, connection;
    private UtilityClass utilityClass = new UtilityClass();

    private Statement statementLocal = null;

    {
        connection = superClass.getConnection();
        connectionDbLocal = superClass.getConnectionDbLocal();
    }

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
                        shopPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml")))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, true);
        config.panel.put("panel", shopPanel);

        idleMonitor.register(panel, Event.ANY);
        getLogo();
        menuclick();
        buttonListeners();
        setName();
        time();
        tableLoad();
        setListViewHeldItems();
        editTable();
        checkCart();
        transID = cartid.get("cartKey");
        barcodetext.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                searchButton();
            }
        });


    }



    private void setListViewHeldItems() {
        try {
            data = FXCollections.observableArrayList();
            ResultSet resultSet = statementLocal.executeQuery("SELECT * FROM heldTransactionList");
            while (resultSet.next()) {
                CartMaster cartMaster = new CartMaster();
                cartMaster.setTransactionId(resultSet.getString("transactionid"));
                cartMaster.setItemName(resultSet.getString("name"));
                cartMaster.setItemId(Integer.parseInt(resultSet.getString("id")));
                data.add(cartMaster);
            }
            listViewHeldItems.setItems(data);

            assert listViewHeldItems != null : "fx:id=\"listViewHeldItems\" was not injected: check your FXML ";
            heldname.setCellValueFactory(
                    new PropertyValueFactory<CartMaster, String>("itemName"));
            heldid.setCellValueFactory(
                    new PropertyValueFactory<CartMaster, String>("itemId"));

            listViewHeldItems.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //restore to cart
        listViewHeldItems.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        restore();
                    }
                }
            }

            private void restore() {
//            todo check if cart is empty during restoration,if empty restore,otherwise save existing cart then restore
                try {
                    ResultSet resultSet = statementLocal.executeQuery("SELECT * FROM cartItems");
                    if (resultSet.isBeforeFirst()) {
//                    exists
                        String name;
                        TextInputDialog dialog = new TextInputDialog("");
                        dialog.setTitle("TRANSACTION IN CART");
                        dialog.setHeaderText(null);
                        dialog.setContentText("Save existing items by entering something that uniquely identifies the transaction(eg customer name):");

// Traditional way to get the response value.
                        Optional<String> result = dialog.showAndWait();
                        name = result.orElse(null);
                        holdUserData(name);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                CartMaster store = listViewHeldItems.getSelectionModel().getSelectedItem();
                String transid = store.getTransactionId();
                String insert = "INSERT INTO cartItems(itemname, itemprice, itemid, code, amount, cumulativeprice, transactionid)SELECT itemname,itemprice,itemid,code,amount,cumulativeprice,transactionid from heldItems where transactionid='" + transid + "'";
                try {
                    statementLocal.execute(insert);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    PreparedStatement preparedStatement = connectionDbLocal.prepareStatement("DELETE FROM heldItems where transactionid=?");
                    preparedStatement.setString(1, transid);
                    preparedStatement.executeUpdate();
                    PreparedStatement preparedStatement1 = connectionDbLocal.prepareStatement("DELETE FROM heldTransactionList where transactionid=?");
                    preparedStatement1.setString(1, transid);
                    preparedStatement1.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
//            tableLoad();
//            setListViewHeldItems();
                try {
                    shopPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("shopFiles/shop.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                setTransID();
                System.out.println(getTransID());
            }
        });


    }

    private void reload() {
        tableLoad();
    }

    private String getTransID() {
        return transID;
    }

    public void setTransID() {
        transID = new CartIdGenerator().getIdcart();
    }

    private void checkCart() {
        try {
            if (statementLocal.execute("SELECT  * FROM cartItems where transactionid not null ")) {
                ResultSet rs = statementLocal.executeQuery("SELECT  * FROM cartItems where transactionid is not null ");
                if (rs.isBeforeFirst()) {
//                    new CartIdGenerator().idcart = rs.getString("transactionid");
                    cartid.put("cartKey", rs.getString("transactionid"));
                    statementLocal.close();

                }
            } else {
                String x = new Timestamp(System.currentTimeMillis()).toString();
                new CartIdGenerator().setIdcart(x);
                idcart = cartid.put("cartKey", x);
                transID = idcart;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private void editTable() {
//        edit amount and cumulative price
        cart.setEditable(true);
        amount.setCellFactory(TextFieldTableCell.forTableColumn());
        amount.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<CartMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<CartMaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setItemNumber(t.getNewValue());
                        String newval = t.getNewValue();

                        String price = String.valueOf(Integer.parseInt(newval) * Integer.parseInt(t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).getItemPrice()));

                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setItemCumulativeCost(Integer.parseInt(price));

                        System.out.println("price=" + price);
                        try {
                            statementLocal.executeUpdate("UPDATE cartItems set amount=" + newval + " ,cumulativeprice=" + price + " WHERE itemid=" + t.getTableView().getItems().get(
                                    t.getTablePosition().getRow()).getItemId());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
//                        update total price
                        try {
                            totalprice.setText(String.valueOf(countTotalPrice()));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }


                    }
                }
        );
    }
    private void tableLoad() {
/*
       1 create a sqlite database
       2 create a transaction id each time a new transaction is started
       3  insert items and transaction id into sqlite database
       4  fetch items from sqlite  database and display them to user
       5  on delete,remove item from sqlite database
*/

        /*
         *  scan for reading in barcode
         *  else enter manually
         *
         * */
        String zcode = null;
        String itemname = null;
        BarcodeScanner barcodeScanner = new BarcodeScanner();
        StringBuffer stringBuffer = barcodeScanner.getBarcode();
        if (!stringBuffer.toString().equals("")) {
//            if bar code reads
            if (stringBuffer.toString().length() < 8) {
                showAlert(Alert.AlertType.ERROR, shopPanel.getScene().getWindow(), "ERROR", "ERROR READING CODE");
            } else {
                zcode = stringBuffer.toString();
                barcodetext.setText(zcode);
            }
//
//  todo to test later when with bar code scanner
        } else {
            zcode = barcodetext.getText();
//            itemname = searchname.getText();
        }
//        searchname.clear();
        barcodetext.clear();
        data = FXCollections.observableArrayList();
        Connection connection = null;
        try {
            connection = DriverManager
                    .getConnection(des[2], des[0], des[1]);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        //            for local storage
        try {
            assert connectionDbLocal != null;
            statementLocal = connectionDbLocal.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            assert statementLocal != null;
            statementLocal.setQueryTimeout(30); // set timeout to 30 sec.
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
//                        insert into sqlite
//                        String cartItems = "CREATE TABLE IF NOT EXISTS cartItems
//                        (itemname text,itemprice text,
//                        itemid text,code text,amount text,cumulativeprice text ,transactionid text)";
            if (connection != null) {

                PreparedStatement statement = connection.prepareStatement("SELECT * FROM stocks where itemcode=?");
                statement.setString(1, zcode);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {

                    CartMaster cartmaster = new CartMaster();
                    cartmaster.setItemId(resultSet.getInt("id"));
                    cartmaster.setItemName(resultSet.getString("name"));
                    cartmaster.setItemNumber("1");
                    cartmaster.setItemPrice(resultSet.getString("price"));
                    cartmaster.setItemBarCode(zcode);
                    cartmaster.setItemCumulativeCost(Integer.parseInt(resultSet.getString("price")) * Integer.parseInt(cartmaster.getItemNumber()));

                    ResultSet rs = statementLocal.executeQuery("SELECT  * FROM cartItems where itemid=" + cartmaster.getItemId());
                    if (rs.isBeforeFirst()) {
//                        String updateCount=;
                        statementLocal.executeUpdate("UPDATE cartItems set amount=amount+1,cumulativeprice=(amount+1)*itemprice where itemid=" + cartmaster.getItemId());
                        statementLocal.close();
                    } else {
                        String insert = "INSERT INTO cartItems(itemname, itemprice, itemid, code, amount, cumulativeprice, transactionid)VALUES ('" + cartmaster.getItemName() + "','" + cartmaster.getItemPrice() + "','" +
                                cartmaster.getItemId() + "','" + cartmaster.getItemBarCode() + "','" + cartmaster.getItemNumber() + "','" +
                                cartmaster.getItemCumulativeCost() + "','" + getTransID() + "')";
                        int i = statementLocal.executeUpdate(insert);
                        statementLocal.close();
                    }

                }

                ResultSet rs = statementLocal.executeQuery("SELECT  * FROM cartItems");
                while (rs.next()) {
                    CartMaster cartMaster = new CartMaster();
//                        totalprice.setText(String.valueOf(totalpricetext));

//                        totalpricetext+=Integer.parseInt(rs.getString("cumulativeprice"));
                    cartMaster.setItemId(rs.getInt("itemid"));
                    cartMaster.setItemName(rs.getString("itemname"));
                    cartMaster.setItemNumber(rs.getString("amount"));
                    cartMaster.setItemPrice(rs.getString("itemprice"));
                    cartMaster.setItemBarCode(rs.getString("code"));
                    cartMaster.setItemCumulativeCost(Integer.parseInt(rs.getString("cumulativeprice")));
                    cartMaster.setTransactionId(rs.getString("transactionid"));

                    data.add(cartMaster);
                }
                statementLocal.close();

                totalprice.setText(String.valueOf(countTotalPrice()));
                cart.setItems(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert cart != null : "fx:id=\"cart\" was not injected: check your FXML ";
        name.setCellValueFactory(
                new PropertyValueFactory<CartMaster, String>("itemName"));
        code.setCellValueFactory(
                new PropertyValueFactory<CartMaster, String>("itemBarCode"));
        price.setCellValueFactory(
                new PropertyValueFactory<CartMaster, String>("itemPrice"));
        cumulativeprice.setCellValueFactory(
                new PropertyValueFactory<CartMaster, Integer>("itemCumulativeCost"));
        amount.setCellValueFactory(new PropertyValueFactory<CartMaster, String>("itemNumber"));
        cart.refresh();
    }

    private int countTotalPrice() throws SQLException {
        int price = 0;
        ResultSet rs = statementLocal.executeQuery("SELECT  * FROM cartItems");
        while (rs.next()) {
            price += Integer.parseInt(rs.getString("cumulativeprice"));
        }
        statementLocal.close();

        return price;
    }

    private void getLogo() {
//        Image image = new Image("images/banner_hardware.png");
//        logoimage.setImage(image);
    }

    private void setName() {
        String name = config.user.get("user");
        username.setText(name);
        System.out.println();
    }

    private void menuclick() {


        helpMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Desktop.getDesktop().browse(new URL(sitedocs).toURI());
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
        CreatorsMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Desktop.getDesktop().browse(new URL(site).toURI());
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
        exitMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
                System.exit(1);
            }
        });
        logoutMenu.setOnAction(event -> {
            config.login.put("loggedout", true);

            try {
//                System.out.println("logging out");
                shopPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void deletefromcart() {
        CartMaster store = cart.getSelectionModel().getSelectedItem();
        if (store == null) {
            showAlert(Alert.AlertType.WARNING, shopPanel.getScene().getWindow(), "NO ITEM SELECTED", "SELECT ITEM TO REMOVE");

        } else {
            try {

                PreparedStatement preparedStatement = connectionDbLocal.prepareStatement("DELETE FROM cartItems WHERE itemid=?");

                preparedStatement.setString(1, String.valueOf(store.getItemId()));
                int updated = preparedStatement.executeUpdate();
                if (updated > 0) {
                    tableLoad();
                } else {
                    showAlert(Alert.AlertType.WARNING, shopPanel.getScene().getWindow(), "ERROR", "EROR REMOVING ITEM");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.WARNING, shopPanel.getScene().getWindow(), "CONNECTION ERROR", "EROR REMOVING ITEM.CHECK YOUR CONNECTION TO THE SERVER");

            }
        }
    }

    private void buttonListeners() {
        clearheld.setOnAction(event -> {
            try {
                statementLocal.execute("DELETE FROM heldItems");
                statementLocal.execute("DELETE FROM heldTransactionList");
                try {
                    shopPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("shopFiles/shop.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        endDay.setOnAction(event -> {
            try {
                shutdown();
                endShift();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        onlinepayments.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                pricegot.put("price", Integer.valueOf(totalprice.getText()));
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("shopFiles/onlinepayments.fxml"));
                try {
                    Parent parent = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(parent));
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        deletefromcartb.setOnMouseClicked(event -> deletefromcart());

        searchb.setOnMouseClicked(event -> {

            searchButton();
        });
        paycash.setOnMouseClicked(event -> {
//pricegot= Integer.parseInt(totalprice.getText());
            pricegot.put("price", Integer.valueOf(totalprice.getText()));
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("shopFiles/showpriceexitcash.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(parent));
                stage.initStyle(StageStyle.UNDECORATED);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
//        checkoutb.setOnMouseClicked(event -> {
//            transID = new CartIdGenerator().idcart;
//
//            ShopController shopController = new ShopController();
//            //                shopController.setTransID(CheckConn.timelogin().toString());
//        });
        clearb.setOnMouseClicked(event -> {
            clearCart();
            showAlert(Alert.AlertType.INFORMATION, shopPanel.getScene().getWindow(), "CART CLEARED", "YOU HAVE CLEARED YOUR CART");
        });
        logoutb.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //logout button destroy session variables
                config.login.put("loggedout", true);
                try {
                    shopPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml")))));
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
                    shopPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/panel.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        holduserdatab.setOnMouseClicked(event -> {
            String name;
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("TRANSACTION IDENTIFIER");
            dialog.setHeaderText(null);
            dialog.setContentText("Please enter something that identifies the transaction(eg customer name):");

// Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
            name = result.orElse(null);

            holdUserData(name);
        });
    }

    private void endShift() {
        //todo end shift
    }

    private void holdUserData(String name) {


        try {

            String id = tableCartTransfer("heldItems", "cartItems");
            statementLocal.execute("INSERT INTO heldTransactionList(name, transactionid) VALUES ('" + name + "','" + id + "')");
            statementLocal.execute("DELETE FROM cartItems");

            checkCart();
            tableLoad();

            try {
                shopPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("shopFiles/shop.fxml")))));
                setTransID();
                System.out.println(getTransID());
            } catch (IOException e) {
                e.printStackTrace();
            }
            showAlert(Alert.AlertType.INFORMATION, shopPanel.getScene().getWindow(), "SUCCESS", "HOLDING SUCCESSFULL");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //fixme channge how unique transaction id is created after a sale or hold
    private String tableCartTransfer(String to, String from) {
        AtomicReference<String> id = new AtomicReference<>("");
        try {

            if (connectionDbLocal != null) {
                String insert = "INSERT INTO " + to + "(itemname, itemprice, itemid, code, amount, cumulativeprice, transactionid)SELECT itemname,itemprice,itemid,code,amount,cumulativeprice,transactionid from " + from + "";
                int i = statementLocal.executeUpdate(insert);
                ResultSet rs = statementLocal.executeQuery("SELECT transactionid from " + from + "");
                if (rs.isBeforeFirst()) id.set(rs.getString("transactionid"));

            }
            statementLocal.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id.get();
    }

    private void clearCart() {
        try {
            statementLocal.execute("DROP TABLE cartItems");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Statement Cart = connectionDbLocal.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String cartItems = "CREATE TABLE IF NOT EXISTS cartItems (id integer primary key autoincrement,itemname text,itemprice text,itemid integer,code text,amount text,cumulativeprice text ,transactionid text)";
        try {
            statementLocal.executeUpdate(cartItems);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableLoad();
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
                    mins = "0" + minutes;

                }
                if (seconds > 9) {
                    secs = String.valueOf(seconds);
                } else {
                    secs = "0" + seconds;

                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (Objects.equals(action.get("shopcontroller"), "reload")) {
                reload();
                action.remove("shopcontroller", "reload");
            }

            try {
                if (networkConnectionMap.containsKey("server") || networkConnectionMap.containsKey("Internet")) {
                    if (!networkConnectionMap.get("server")) {
//                    no connection to server
                        showAlert(Alert.AlertType.ERROR, config.panel.get("panel").getScene().getWindow(), "ERROR", "NETWORK CONNECTION LOST");
                    } else {
                        if (!networkConnectionMap.get("Internet")) {
                            utilityClass.showToast();//about connection to the internet
                        }
                    }

                }
                clock.setText(String.format("%d:%s:%s %s", CheckConn.timelogin().getHours(), mins, secs, pmam));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void searchButton() {

        if (counter == 0) {
            try {
                String uniqueid = CheckConn.timelogin().getTime() + user.get("user");
                System.out.println(uniqueid);

            } catch (IOException e) {
                e.printStackTrace();
            }
            counter++;
        }
        tableLoad();
    }

    //method to show an alert
    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.showAndWait();
    }


}
