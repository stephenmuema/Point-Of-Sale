package Controllers.CarWashControllers;
//deals with cr wash cashiers

import Controllers.IdleMonitor;
import Controllers.UtilityClass;
import MasterClasses.CarWashMaster;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import securityandtime.config;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Objects;
import java.util.ResourceBundle;

import static securityandtime.config.site;
import static securityandtime.config.sitedocs;

public class CarwashSalesController extends UtilityClass implements Initializable {
    public static int lastSelectedTabIndex = 0;
    public AnchorPane panel;
    public Tab clients;
    public TableView<CarWashMaster> tab;
    public TableColumn<CarWashMaster, String> Name;
    public TableColumn<CarWashMaster, String> reg;
    public TableColumn<CarWashMaster, String> id;
    public TableColumn<CarWashMaster, String> status;
    public TableColumn<CarWashMaster, String> operator;
    public TableColumn<CarWashMaster, String> payout;
    public Label clock;
    public Button home;
    public TabPane tabpane;
    public TextField name;
    public TextField registration;
    public TextField contact;
    public TextField identification;
    public Button submit;
    public Tab newclients;
    public MenuItem logoutMenu;
    public MenuItem exitMenu;
    public MenuItem accountdetailsMenu;
    public MenuItem CreatorsMenu;
    public MenuItem helpMenu;
    private double tabWidth = 415.0;
    private ObservableList<CarWashMaster> data;

    public static int getLastSelectedTabIndex() {
        return lastSelectedTabIndex;
    }

    public static void setLastSelectedTabIndex(int lastSelectedTabIndex) {
        CarwashSalesController.lastSelectedTabIndex = lastSelectedTabIndex;
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
        time(clock);
        config.panel.put("panel", panel);

        editable();
        buttonListeners();
        IdleMonitor idleMonitor = new IdleMonitor(Duration.seconds(3600),
                () -> {
                    try {
                        config.login.put("loggedout", true);
                        panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml")))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, true);
        idleMonitor.register(panel, Event.ANY);
        clients.setOnSelectionChanged(event -> {
            data = FXCollections.observableArrayList();
            if (clients.isSelected()) {
                loadTab();
            }
        });
        menuclick();
    }

    private void menuclick() {


        helpMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Desktop.getDesktop().browse(new URL(sitedocs).toURI());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
        CreatorsMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Desktop.getDesktop().browse(new URL(site).toURI());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
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
                panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    private void tabpaneStyles() {
        tabpane.setSide(Side.TOP);
        tabpane.setTabMinWidth(tabWidth);
        tabpane.setTabMaxWidth(tabWidth);
        tabpane.setTabMinHeight(tabWidth - 380.0);
        tabpane.setTabMaxHeight(tabWidth - 380.0);
        tabpane.setRotateGraphic(true);

        configureTab(clients, "Existing Jobs");
        configureTab(newclients, "New Jobs");
    }


    private void configureTab(Tab tab, String title) {
        double imageWidth = 40.0;


        Label label = new Label(title);
        label.setMaxWidth(tabWidth - 20);
        label.setPadding(new Insets(5, 0, 0, 0));
        label.setStyle("-fx-text-fill: black; -fx-font-size: 8pt; -fx-font-weight: normal;");
        label.setTextAlignment(TextAlignment.CENTER);

        BorderPane tabPane = new BorderPane();
        tabPane.setRotate(90.0);
        tabPane.setMaxWidth(tabWidth);
        tabPane.setBottom(label);

        /// 6.
        tab.setText("");
        tab.setGraphic(tabPane);
    }


    private void buttonListeners() {

        submit.setOnMouseClicked(event -> {
            UtilityClass utilityClass = new UtilityClass();
            Connection connection = utilityClass.getConnection();
            String ownername = name.getText().toUpperCase();
            String numberplate = registration.getText().toUpperCase();
            String idnum = identification.getText().toUpperCase();
            String contactnumber = contact.getText().toUpperCase();

//
//
//
            if (ownername.isEmpty() || numberplate.isEmpty() || idnum.isEmpty() || contactnumber.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "FILL ALL FIELDS", "ALL FIELDS SHOULD BE FILLED TO PROCEED");
            } else {
                PreparedStatement preparedStatement = null;

                try {
                    assert connection != null;
                    preparedStatement = connection.prepareStatement("INSERT INTO carwash(`ownername`,registration,idnumber,contact,status)VALUES(?,?,?,?,?)");
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    if (preparedStatement != null) {
                        preparedStatement.setString(1, ownername);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (preparedStatement != null) {
                        preparedStatement.setString(2, numberplate);
                        //                System.out.println("user name=="+user);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (preparedStatement != null) {
                        preparedStatement.setString(3, idnum);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (preparedStatement != null) {
                        preparedStatement.setString(4, contactnumber);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (preparedStatement != null) {
                        preparedStatement.setString(5, "PENDING");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    //executequery
                    if (preparedStatement != null) {
                        int rows = preparedStatement.executeUpdate();
                        if (rows > 0) {
                            //System.out.println(rows);
                            showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS ", "YOUR ITEM WAS ADDED SUCCESSFULLY");
                            name.clear();
                            registration.clear();
                            identification.clear();
                            contact.clear();
                        } else {
                            showAlert(Alert.AlertType.WARNING, panel.getScene().getWindow(), "  FAILURE", "ERROR WHEN INSERTING ITEMS");

                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        });
        home.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/panel.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private void loadTab() {
        data = FXCollections.observableArrayList();

        UtilityClass utilityClass = new UtilityClass();
        Connection connection = utilityClass.getConnection();
        try {
//                        DISPLAYING CLIENTS
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM carwash where status=?");
                statement.setString(1, "PENDING");
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    CarWashMaster carWashMaster1 = new CarWashMaster();
                    carWashMaster1.Name.set(resultSet.getString("ownername"));
                    carWashMaster1.regno.set(resultSet.getString("registration"));
                    carWashMaster1.Id.set(resultSet.getString("id"));
                    carWashMaster1.idnum.set(resultSet.getString("idnumber"));
                    carWashMaster1.status.set(resultSet.getString("status"));
                    carWashMaster1.operator.set(resultSet.getString("washedby"));
                    carWashMaster1.cash.set(resultSet.getString("cashpaid"));

                    data.add(carWashMaster1);
                }
                tab.setItems(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assert tab != null : "fx:id=\"tab\" was not injected: check your FXML ";
        Name.setCellValueFactory(
                new PropertyValueFactory<>("Name"));
        id.setCellValueFactory(
                new PropertyValueFactory<>("idnum"));
        reg.setCellValueFactory(new PropertyValueFactory<>("regno"));
        status.setCellValueFactory(
                new PropertyValueFactory<>("status"));
        operator.setCellValueFactory(new PropertyValueFactory<>("operator"));
        payout.setCellValueFactory(new PropertyValueFactory<>("cash"));

        tab.refresh();
    }

    private void editable() {
        tab.setEditable(true);
        UtilityClass utilityClass = new UtilityClass();
        Connection connection = utilityClass.getConnection();
        Name.setCellFactory(TextFieldTableCell.forTableColumn());
        Name.setOnEditCommit(
                t -> {
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setName(t.getNewValue());
                    String newval = t.getNewValue();
                    PreparedStatement preparedStatement = null;
                    try {

                        CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                        String id = carWashMaster.getId();
                        assert connection != null;
                        preparedStatement = connection.prepareStatement("UPDATE carwash set `ownername`=? where id=?");
                        preparedStatement.setString(1, newval);
                        preparedStatement.setString(2, id);
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

//                        preparedStatement.setString(1, name.getText());
                }
        );
        reg.setCellFactory(TextFieldTableCell.forTableColumn());
        reg.setOnEditCommit(
                t -> {
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setName(t.getNewValue());
                    String newval = t.getNewValue();
                    PreparedStatement preparedStatement = null;
                    try {
                        CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                        String id = carWashMaster.getId();
                        preparedStatement = connection.prepareStatement("UPDATE carwash set registration=? where id=?");
                        preparedStatement.setString(1, newval);
                        preparedStatement.setString(2, id);
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

//                        preparedStatement.setString(1, name.getText());
                }
        );
        id.setCellFactory(TextFieldTableCell.forTableColumn());
        id.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<CarWashMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<CarWashMaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                            String id = carWashMaster.getId();
                            preparedStatement = connection.prepareStatement("UPDATE carwash set idnumber=? where id=?");
                            preparedStatement.setString(1, newval);
                            preparedStatement.setString(2, id);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

//                        preparedStatement.setString(1, name.getText());
                    }
                }
        );
        status.setCellFactory(TextFieldTableCell.forTableColumn());
        status.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<CarWashMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<CarWashMaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                            String id = carWashMaster.getId();
                            preparedStatement = connection.prepareStatement("UPDATE carwash set status=? where id=?");
                            preparedStatement.setString(1, newval);
                            preparedStatement.setString(2, id);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

//                        preparedStatement.setString(1, name.getText());
                    }
                }
        );
        operator.setCellFactory(TextFieldTableCell.forTableColumn());
        operator.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<CarWashMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<CarWashMaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                            String id = carWashMaster.getId();
                            preparedStatement = connection.prepareStatement("UPDATE carwash set washedby=? where id=?");
                            preparedStatement.setString(1, newval);
                            preparedStatement.setString(2, id);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

//                        preparedStatement.setString(1, name.getText());
                    }
                }
        );

        payout.setCellFactory(TextFieldTableCell.forTableColumn());
        payout.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<CarWashMaster, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<CarWashMaster, String> t) {
                        t.getTableView().getItems().get(
                                t.getTablePosition().getRow()).setName(t.getNewValue());
                        String newval = t.getNewValue();
                        PreparedStatement preparedStatement = null;
                        try {
                            CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                            String id = carWashMaster.getId();
                            preparedStatement = connection.prepareStatement("UPDATE carwash set cashpaid=? where id=?");
                            preparedStatement.setString(1, newval);
                            preparedStatement.setString(2, id);
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

//                        preparedStatement.setString(1, name.getText());
                    }
                }
        );
    }


    private void compete() {
        UtilityClass utilityClass = new UtilityClass();
        Connection connection = utilityClass.getConnection();
        tab.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (event.getClickCount() == 2) {
                        CarWashMaster carWashMaster = tab.getSelectionModel().getSelectedItem();
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("carwashFiles/carwashprice.fxml"));
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
                }
            }
        });
    }

    public AnchorPane getCarWash() {
        return panel;
    }

    public CarwashSalesController setCarWash(AnchorPane carWash) {
        this.panel = carWash;
        return this;
    }

    public Tab getClients() {
        return clients;
    }

    public CarwashSalesController setClients(Tab clients) {
        this.clients = clients;
        return this;
    }

    public TableView<CarWashMaster> getTab() {
        return tab;
    }

    public CarwashSalesController setTab(TableView<CarWashMaster> tab) {
        this.tab = tab;
        return this;
    }

    public TableColumn<CarWashMaster, String> getName() {
        return Name;
    }

    public CarwashSalesController setName(TextField name) {
        this.name = name;
        return this;
    }

    public CarwashSalesController setName(TableColumn<CarWashMaster, String> name) {
        Name = name;
        return this;
    }

    public TextField getRegistration() {
        return registration;
    }

    public CarwashSalesController setRegistration(TextField registration) {
        this.registration = registration;
        return this;
    }

    public TextField getContact() {
        return contact;
    }

    public CarwashSalesController setContact(TextField contact) {
        this.contact = contact;
        return this;
    }

    public TextField getIdentification() {
        return identification;
    }

    public CarwashSalesController setIdentification(TextField identification) {
        this.identification = identification;
        return this;
    }

    public Button getSubmit() {
        return submit;
    }

    public CarwashSalesController setSubmit(Button submit) {
        this.submit = submit;
        return this;
    }

    public Tab getNewclients() {
        return newclients;
    }

    public CarwashSalesController setNewclients(Tab newclients) {
        this.newclients = newclients;
        return this;
    }

    public MenuItem getLogoutMenu() {
        return logoutMenu;
    }

    public CarwashSalesController setLogoutMenu(MenuItem logoutMenu) {
        this.logoutMenu = logoutMenu;
        return this;
    }

    public MenuItem getExitMenu() {
        return exitMenu;
    }

    public CarwashSalesController setExitMenu(MenuItem exitMenu) {
        this.exitMenu = exitMenu;
        return this;
    }

    public MenuItem getAccountdetailsMenu() {
        return accountdetailsMenu;
    }

    public CarwashSalesController setAccountdetailsMenu(MenuItem accountdetailsMenu) {
        this.accountdetailsMenu = accountdetailsMenu;
        return this;
    }

    public MenuItem getCreatorsMenu() {
        return CreatorsMenu;
    }

    public CarwashSalesController setCreatorsMenu(MenuItem creatorsMenu) {
        CreatorsMenu = creatorsMenu;
        return this;
    }

    public MenuItem getHelpMenu() {
        return helpMenu;
    }

    public CarwashSalesController setHelpMenu(MenuItem helpMenu) {
        this.helpMenu = helpMenu;
        return this;
    }

    public double getTabWidth() {
        return tabWidth;
    }

    public CarwashSalesController setTabWidth(double tabWidth) {
        this.tabWidth = tabWidth;
        return this;
    }

    public ObservableList<CarWashMaster> getData() {
        return data;
    }

    public CarwashSalesController setData(ObservableList<CarWashMaster> data) {
        this.data = data;
        return this;
    }

    public TableColumn<CarWashMaster, String> getReg() {
        return reg;
    }

    public CarwashSalesController setReg(TableColumn<CarWashMaster, String> reg) {
        this.reg = reg;
        return this;
    }

    public TableColumn<CarWashMaster, String> getId() {
        return id;
    }

    public CarwashSalesController setId(TableColumn<CarWashMaster, String> id) {
        this.id = id;
        return this;
    }

    public TableColumn<CarWashMaster, String> getStatus() {
        return status;
    }

    public CarwashSalesController setStatus(TableColumn<CarWashMaster, String> status) {
        this.status = status;
        return this;
    }

    public TableColumn<CarWashMaster, String> getOperator() {
        return operator;
    }

    public CarwashSalesController setOperator(TableColumn<CarWashMaster, String> operator) {
        this.operator = operator;
        return this;
    }

    public TableColumn<CarWashMaster, String> getPayout() {
        return payout;
    }

    public CarwashSalesController setPayout(TableColumn<CarWashMaster, String> payout) {
        this.payout = payout;
        return this;
    }

    public Label getClock() {
        return clock;
    }

    public CarwashSalesController setClock(Label clock) {
        this.clock = clock;
        return this;
    }

    public Button getHome() {
        return home;
    }

    public CarwashSalesController setHome(Button home) {
        this.home = home;
        return this;
    }

    public TabPane getTabpane() {
        return tabpane;
    }

    public CarwashSalesController setTabpane(TabPane tabpane) {
        this.tabpane = tabpane;
        return this;
    }
}
