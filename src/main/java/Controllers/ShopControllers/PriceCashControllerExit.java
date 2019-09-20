package Controllers.ShopControllers;

import Controllers.SuperClass;
import Controllers.UserAccountManagementControllers.IdleMonitor;
import Controllers.UtilityClass;
import MasterClasses.ReceiptMasterClass;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import securityandtime.config;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import static securityandtime.config.*;

public class PriceCashControllerExit extends UtilityClass implements Initializable {
    public Label price;
    public Button complete;
    public Button done;
    public Label balance;
    public Button goonlinepayments;
    public Button back;
    public AnchorPane panel;
    public TextField cash;
    Connection connection = getConnection();
    private int pricevalue;
    private boolean completedPayment;
    private Connection connectionDbLocal = getConnectionDbLocal();
    private int bal;
    private Statement statementLocal;

//    {
//
//        try {
//            connection = DriverManager
//                    .getConnection(des[2], des[0], des[1]);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            connectionDbLocal = DriverManager.getConnection(localCartDb);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    {
        try {
            assert false;
            statementLocal = connectionDbLocal.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * made by steve
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
                        panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml")))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, true);
        config.panel.put("panel", panel);

        idleMonitor.register(panel, Event.ANY);
        buttonListeners();
        load();
        setPrice();
    }

    private void setPrice() {
        AtomicInteger x = new AtomicInteger();
        x.set(Integer.parseInt(String.valueOf(pricevalue)));
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            if (!cash.getText().isEmpty()) {
                x.set(Integer.parseInt(cash.getText()) - pricevalue);
                if (x.get() >= 0) {
                    balance.setText(String.valueOf(x.get()));
                    completedPayment = true;
                } else {
                    balance.setText("0.00");
                    completedPayment = false;
                }
                bal = x.get();
            }
        }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void load() {
        cash.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
//                complete transaction
                completetransaction();
            }
        });
        pricevalue = pricegot.get("price");
        price.setText(String.valueOf(pricevalue));
    }

    private void completetransaction() {
        SuperClass superClass = new SuperClass();
        superClass.completetransaction();


    }


    private void buttonListeners() {
        complete.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (completedPayment) {
                    payment();
                } else {

                    showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "THE AMOUNT PAID IS NOT COMPLETE");
                }
            }

            private void payment() {
                // complete the transaction by first removing from db

                try {
                    ResultSet resultset = statementLocal.executeQuery("SELECT * FROM cartItems");
                    String transid = "";
                    while (resultset.next()) {
                        String code = resultset.getString("code");
                        String amount = resultset.getString("amount");
//    transid=user.get("user")+" "+resultset.getString("transactionid");
                        transid = resultset.getString("transactionid");
                        String priceCumulative = resultset.getString("cumulativeprice");

                        PreparedStatement statement = connection.prepareStatement("SELECT * FROM stocks where itemcode=?");
                        statement.setString(1, code);
                        ResultSet resultSetStocks = statement.executeQuery();
                        if (resultSetStocks.isBeforeFirst()) {
                            while (resultSetStocks.next()) {
                                PreparedStatement statementUpdate = connection.prepareStatement("UPDATE stocks SET amount=? where itemcode=?");
                                statementUpdate.setString(1, String.valueOf(Integer.parseInt(resultSetStocks.getString("amount")) - Integer.parseInt(amount)));
                                statementUpdate.setString(2, code);
                                statementUpdate.execute();
                                PreparedStatement insertIntoSales = connection.prepareStatement("INSERT INTO solditems(name,price,quantitysold,transactionid,category)VALUES (?,?,?,?,?)");
                                insertIntoSales.setString(1, resultset.getString("itemname"));
                                insertIntoSales.setString(2, resultset.getString("itemprice"));
                                insertIntoSales.setString(3, resultset.getString("amount"));
                                insertIntoSales.setString(4, resultset.getString("transactionid"));
                                insertIntoSales.setString(5, resultSetStocks.getString("category"));
                                insertIntoSales.executeUpdate();
//                                    check if category exists,if exists update its records,elseinsert records
//                                todo categorical audits v1.2
//                                PreparedStatement checkIfExists=connection.prepareStatement("SELECT * FROM salespercategory where category=?");
//                                checkIfExists.setString(1,resultSetStocks.getString("category"));
//                                ResultSet rs=checkIfExists.executeQuery();
//                                if(rs.isBeforeFirst()){
//                                    //exists
//                                    PreparedStatement updateCategories=connection.prepareStatement("UPDATE salespercategory set price=?, sales=? where category=?");
//
//                                }else{
//                                    //does not exist
//                                    PreparedStatement insertCategories=connection.prepareStatement("INSERT INTO salespercategory(category,price,sales)VALUES (?,?,?)");
//                                    insertCategories.setString(1,resultSetStocks.getString("category"));
//                                    insertCategories.setString(2,);
//                                        insertCategories.setString(3,amount);
////
//                                }


                            }
                        }

                    }
                    PreparedStatement statement = connection.prepareStatement("insert into sales(transactionid,balance,seller,cash,moneypaid,method,completed)values (?,?,?,?,?,?,?)");
                    statement.setString(1, transid);
                    statement.setInt(2, Integer.parseInt(balance.getText()));
                    statement.setString(3, user.get("user"));
                    statement.setString(4, String.valueOf(pricevalue));
                    statement.setString(5, cash.getText());
                    statement.setString(6, "CASH");
                    statement.setBoolean(7, true);

                    statement.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                completedPayment = false;
                //todo print a receipt to customer
                new ReceiptMasterClass();


                pricevalue = 0;

//    delete from sqlite table
                try {
                    statementLocal.execute("DELETE FROM cartItems");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                action.put("shopcontroller", "reload");
                Stage stage = (Stage) complete.getScene().getWindow();
                stage.close();


                //todo add a way of supporting multiple ways of payment eg start with cash complete with online payment

            }
        });
        goonlinepayments.setOnMouseClicked(event -> {
            try {
                panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("shopFiles/onlinepayments.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
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


    public Label getPrice() {
        return price;
    }

    public PriceCashControllerExit setPrice(Label price) {
        this.price = price;
        return this;
    }

    public Button getComplete() {
        return complete;
    }

    public PriceCashControllerExit setComplete(Button complete) {
        this.complete = complete;
        return this;
    }

    public Button getDone() {
        return done;
    }

    public PriceCashControllerExit setDone(Button done) {
        this.done = done;
        return this;
    }

    public Label getBalance() {
        return balance;
    }

    public PriceCashControllerExit setBalance(Label balance) {
        this.balance = balance;
        return this;
    }

    public Button getGoonlinepayments() {
        return goonlinepayments;
    }

    public PriceCashControllerExit setGoonlinepayments(Button goonlinepayments) {
        this.goonlinepayments = goonlinepayments;
        return this;
    }

    public Button getBack() {
        return back;
    }

    public PriceCashControllerExit setBack(Button back) {
        this.back = back;
        return this;
    }

    public AnchorPane getPanel() {
        return panel;
    }

    public PriceCashControllerExit setPanel(AnchorPane panel) {
        this.panel = panel;
        return this;
    }

    public TextField getCash() {
        return cash;
    }

    public PriceCashControllerExit setCash(TextField cash) {
        this.cash = cash;
        return this;
    }


    public int getPricevalue() {
        return pricevalue;
    }

    public PriceCashControllerExit setPricevalue(int pricevalue) {
        this.pricevalue = pricevalue;
        return this;
    }

    public boolean isCompletedPayment() {
        return completedPayment;
    }

    public PriceCashControllerExit setCompletedPayment(boolean completedPayment) {
        this.completedPayment = completedPayment;
        return this;
    }


    public PriceCashControllerExit setConnectionDbLocal(Connection connectionDbLocal) {
        this.connectionDbLocal = connectionDbLocal;
        return this;
    }

    public int getBal() {
        return bal;
    }

    public PriceCashControllerExit setBal(int bal) {
        this.bal = bal;
        return this;
    }


}
