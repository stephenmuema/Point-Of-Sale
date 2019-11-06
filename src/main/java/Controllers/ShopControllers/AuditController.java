package Controllers.ShopControllers;

import Controllers.IdleMonitor;
import Controllers.UtilityClass;
import MasterClasses.EmployeeMaster;
import MasterClasses.SalesMaster;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
import java.time.LocalDate;
import java.util.*;

import static securityandtime.config.site;

//made by steve
public class AuditController extends UtilityClass implements Initializable {
    public TableView<SalesMasterClassCatOrIndividual> categorysalestable;
    public TableColumn<SalesMasterClassCatOrIndividual, String> categorysalesid;
    public TableColumn<SalesMasterClassCatOrIndividual, String> categorysalesname;
    public TableColumn<SalesMasterClassCatOrIndividual, String> categorysalespayout;
    public TableColumn<SalesMasterClassCatOrIndividual, String> categorysalessalesperday;
    public TableView<SalesMasterClassCatOrIndividual> itemsalestable;
    public TableColumn<SalesMasterClassCatOrIndividual, String> itemsalesid;
    public TableColumn<SalesMasterClassCatOrIndividual, String> itemsalesname;
    public TableColumn<SalesMasterClassCatOrIndividual, String> itemsalespayout;
    public TableColumn<SalesMasterClassCatOrIndividual, String> itemsalessalesperday;
    public TableColumn<SalesMasterClassCatOrIndividual, String> itemsalessalesremainingstock;
    public RadioButton indSalesRad;
    public RadioButton catSalesRad;
    public Button getSales;
    String radSelected;
    ////////////////////new tab
    @FXML
    private MenuItem details;
    @FXML
    private MenuItem menulogout;
    @FXML
    private TabPane maintabpane;//maintabpane
    @FXML
    private Tab tabemployeeaudits;//employee audits tab

    //table of employees
    @FXML
    private TableView<EmployeeMaster> tableemployeelist;
    @FXML
    private TableColumn<EmployeeMaster, String> employeeid;
    @FXML
    private TableColumn<EmployeeMaster, String> employeename;
    @FXML
    private TableColumn<EmployeeMaster, String> employeeemail;

//    table of sales for each selected employee
@FXML
private TableView<SalesMaster> tableemployeesales;
    @FXML
    private TableColumn<SalesMaster, String> employeetransid;
    @FXML
    private TableColumn<SalesMaster, String> transprice;
    @FXML
    private TableColumn<SalesMaster, String> transpaid;
    @FXML
    private TableColumn<SalesMaster, String> transmethod;
    @FXML
    private TableColumn<SalesMaster, String> transbalance;
    @FXML
    private TableColumn<SalesMaster, String> transcompletion;
    //
//done
//    end of table of sales per employee

    @FXML
    private Tab tabsalesaudits;//tab of sales

    @FXML
    private Tab tabstockalerts;
    //tab of alerts
    @FXML
    private TableView stockalerttable;


    @FXML
    private TableColumn stockalerttableid;
    @FXML
    private TableColumn stockalerttablename;
    @FXML
    private TableColumn stockalerttabledate;
    @FXML
    private TableColumn stockalerttablemarkasread;
    @FXML
    private Tab taballaudits;
    //tab of all audits for exports or graphical viewing
    @FXML
    private Button exportfullreport;
    @FXML
    private Button exportcategoryreport;
    @FXML
    private Button exportemployeereport;
    @FXML
    private Button getdetailedgraph;
    @FXML
    private Button getcateegorygraph;
    @FXML
    private Button getemployeegraph;
    @FXML
    private Label clock;
    @FXML
    private AnchorPane panel;
    @FXML
    private Button topanelbutton;
    @FXML
    private Button tocarwashbutton;
    @FXML
    private Button toemployeesbutton;
    @FXML
    private Button logoutbutton;
    @FXML
    private Button tosupplierbutton;
    @FXML
    private Button showempperformancegraph;
    @FXML
    private Button exportfirstempreport;
    @FXML
    private Button showEmpReport;

    @FXML
    private MenuItem endDayMenu;
    @FXML
    private MenuItem reportIssuesMenu;
    @FXML
    private MenuItem restartServerMenu;
    @FXML
    private MenuItem troubleShootMenu;

    @FXML
    private MenuItem abtMenu;
    @FXML
    private MenuItem termsMenu;
    @FXML
    private MenuItem checkUpdatesMenu;
    @FXML
    private MenuItem reachUsMenu;
    @FXML
    private MenuItem generateReportsMenu;
    @FXML
    private MenuItem documentationMenu;
    @FXML
    private MenuItem menuQuit;

    @FXML
    private MenuItem staffMenu;
    @FXML
    private MenuItem carWashMenu;
    @FXML
    private MenuItem inventoryMenu;
    @FXML
    private MenuItem mrMenu;
    @FXML
    private MenuItem auditsMenu;
    @FXML
    private MenuItem menuShutDown;
    @FXML
    private MenuItem menuRestart;

    //query report of one employee
    @FXML
    private DatePicker startDateEmployeeSales;
    @FXML
    private DatePicker endDateEmployeeSales;
    @FXML
    private Button queryEmpTimeReport;
    //db connection
    private Connection connection;
    private ObservableList<EmployeeMaster> employeeMasterObservableList = FXCollections.observableArrayList();
    private ObservableList<SalesMaster> salesMasterObservableList = FXCollections.observableArrayList();
    private ObservableList<SalesMasterClassCatOrIndividual> salesMasterClassCatOrIndividuals = FXCollections.observableArrayList();
    private String sellerEmail;

    public AuditController() throws IOException {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            IdleMonitor idleMonitor = new IdleMonitor(Duration.seconds(900),
                    () -> {
                        try {
                            config.login.put("loggedout", true);

                            panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml")))));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ToggleGroup toggleGroup = new ToggleGroup();
        catSalesRad.setToggleGroup(toggleGroup);
        indSalesRad.setToggleGroup(toggleGroup);
        indSalesRad.setSelected(true);
        radSelected = indSalesRad.getText();
        toggleGroup.selectedToggleProperty().addListener((ob, o, n) -> {
//            UtilityClass utilityClass = new UtilityClass();
            Connection connection = null;
            try {
                connection = new UtilityClass().getConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            RadioButton rb = (RadioButton) toggleGroup.getSelectedToggle();

            if (rb != null) {
                radSelected = rb.getText();
                loadCategoricalOrIndividualSalesTable();// changes in the radio button selected
            }
        });
        loadCategoricalOrIndividualSalesTable();//no changes in the radio button selected
        config.panel.put("panel", panel);
        startDateEmployeeSales.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) > 0);
            }
        });
        endDateEmployeeSales.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) > 0);
            }
        });
        menuListeners();
        buttonListeners();
        navigatoryButtonListeners();
        loadTables();
        time(clock);
    }

    private void loadCategoricalOrIndividualSalesTable() {
        Set<String> names = new LinkedHashSet<>();
        Set<String> categoryNames = new LinkedHashSet<>();

        try {
            connection = new UtilityClass().getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (radSelected.contains("individual") || radSelected.contains("INDIVIDUAL")) {
            //SHOW INDIVIDUAL SALES
            salesMasterClassCatOrIndividuals.clear();

            categorysalestable.setVisible(false);
            itemsalestable.setVisible(true);
            //select item sales by name and display the total price sold in the specified time
            //show the number of items remaining in the db adn show the rate of sales per day
            try {
                PreparedStatement preparedStatementFetchItemNames = connection.prepareStatement("SELECT * FROM solditems ");
                ResultSet resultSetFetchItemNames = preparedStatementFetchItemNames.executeQuery();
                if (resultSetFetchItemNames.isBeforeFirst()) {
                    while (resultSetFetchItemNames.next()) {
//                        System.out.println(resultSetFetchItemNames.getString("name"));
                        names.add(resultSetFetchItemNames.getString("name"));
                    }
                }//fetch items from sold items whose name=current value in set

                //loop through set
                for (String name : names
                ) {
                    SalesMasterClassCatOrIndividual classIndividual = new SalesMasterClassCatOrIndividual();
                    int totalprice = 0, totalsold = 0;
//                    System.out.println(name);
                    PreparedStatement fetchSoldItems = connection.prepareStatement("SELECT * FROM solditems where name =?");
                    fetchSoldItems.setString(1, name);
                    ResultSet resultSet = fetchSoldItems.executeQuery();
                    if (resultSet.isBeforeFirst()) {
                        while (resultSet.next()) {
                            //loop through all sales with current name,,,
                            // add the price and quantity sold to divide by time
//                        get quantity remaining from database of items
                            totalprice += Integer.parseInt(resultSet.getString("price")) * Integer.parseInt(resultSet.getString("quantitysold"));
                            totalsold += Integer.parseInt(resultSet.getString("quantitysold"));
                        }
                    }
                    classIndividual.setId(classIndividual.getId() + 1);
                    classIndividual.setName(name);
                    classIndividual.setPayout(String.valueOf(totalprice));
                    classIndividual.setRateOfSales(String.valueOf(totalsold));
                    preparedStatementFetchItemNames = connection.prepareStatement("SELECT * FROM stocks WHERE name=?");
                    preparedStatementFetchItemNames.setString(1, name);
                    resultSet = preparedStatementFetchItemNames.executeQuery();
                    if (resultSet.isBeforeFirst()) {
                        while (resultSet.next()) {
                            classIndividual.setRemaining(resultSet.getString("amount"));
                        }
                    }
                    salesMasterClassCatOrIndividuals.add(classIndividual);
                }
                itemsalestable.setItems(salesMasterClassCatOrIndividuals);

                assert itemsalestable != null : "fx:id=\"tableemployeesales\" was not injected: check your FXML ";
                itemsalesname.setCellValueFactory(
                        new PropertyValueFactory<>("name"));
                itemsalesid.setCellValueFactory(
                        new PropertyValueFactory<>("id"));
                itemsalespayout.setCellValueFactory(
                        new PropertyValueFactory<>("payout"));
                itemsalessalesperday.setCellValueFactory(new PropertyValueFactory<>("rateOfSales"));
                itemsalessalesremainingstock.setCellValueFactory(new PropertyValueFactory<>("remaining"));
                itemsalestable.refresh();

            } catch (SQLException e) {
                e.printStackTrace();
            }


        } else {
            salesMasterClassCatOrIndividuals.clear();

            //SHOW INDIVIDUAL SALES
            categorysalestable.setVisible(false);
            itemsalestable.setVisible(true);
            //select item sales by name and display the total price sold in the specified time
            //show the number of items remaining in the db adn show the rate of sales per day
            try {
                PreparedStatement preparedStatementFetchItemNames = connection.prepareStatement("SELECT * FROM solditems ");
                ResultSet resultSetFetchItemNames = preparedStatementFetchItemNames.executeQuery();
                if (resultSetFetchItemNames.isBeforeFirst()) {
                    while (resultSetFetchItemNames.next()) {
//                        System.out.println(resultSetFetchItemNames.getString("category"));
                        categoryNames.add(resultSetFetchItemNames.getString("category"));
                    }
                }//fetch items from sold items whose name=current value in set
                //loop through set
                for (String name : categoryNames
                ) {
                    SalesMasterClassCatOrIndividual classCategoricalSales = new SalesMasterClassCatOrIndividual();
                    int totalprice = 0, totalsold = 0;
//                    System.out.println(name);
                    PreparedStatement fetchSoldItems = connection.prepareStatement("SELECT * FROM solditems where category =?");
                    fetchSoldItems.setString(1, name);
                    ResultSet resultSet = fetchSoldItems.executeQuery();
                    if (resultSet.isBeforeFirst()) {
                        while (resultSet.next()) {
                            //loop through all sales with current name,,,
                            // add the price and quantity sold to divide by time
//                        get quantity remaining from database of items
                            totalprice += Integer.parseInt(resultSet.getString("price")) * Integer.parseInt(resultSet.getString("quantitysold"));
                            totalsold += Integer.parseInt(resultSet.getString("quantitysold"));
                        }
                    }
                    classCategoricalSales.setId(classCategoricalSales.getId() + 1);
                    classCategoricalSales.setName(name);
                    classCategoricalSales.setPayout(String.valueOf(totalprice));
                    classCategoricalSales.setRateOfSales(String.valueOf(totalsold));

                    salesMasterClassCatOrIndividuals.add(classCategoricalSales);
                }
                categorysalestable.setItems(salesMasterClassCatOrIndividuals);

                assert categorysalestable != null : "fx:id=\"tableemployeesales\" was not injected: check your FXML ";
                categorysalesname.setCellValueFactory(
                        new PropertyValueFactory<>("name"));
                categorysalesid.setCellValueFactory(
                        new PropertyValueFactory<>("id"));
                categorysalespayout.setCellValueFactory(
                        new PropertyValueFactory<>("payout"));
                categorysalessalesperday.setCellValueFactory(new PropertyValueFactory<>("rateOfSales"));
                itemsalestable.refresh();

            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
    }

    private void navigatoryButtonListeners() {
        topanelbutton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/panelAdmin.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        tocarwashbutton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("carwashFiles/carwash.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        toemployeesbutton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    panel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/employees.fxml")))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        logoutbutton.setOnMouseClicked(event -> logout());
        tosupplierbutton.setOnMouseClicked(event -> {
            try {
                //todo change link to supplier site
                Desktop.getDesktop().browse(new URL(site).toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }

    private void menuListeners() {

        carWashMenu.setOnAction(event -> {
            goToCarwash(panel);
        });
        auditsMenu.setOnAction(event -> {
            gotoAudits(panel);
        });
        staffMenu.setOnAction(event -> {
            goToStaff(panel);
        });
        inventoryMenu.setOnAction(event -> {
            goToStocks(panel);
        });

        menuShutDown.setOnAction(event -> {
            try {
                shutdown();
            } catch (IOException e) {
                showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "UNSUPPORTED OS", "YOUR OS IS UNSUPPORTED BY THIS ACTION");
            }
        });
        menuRestart.setOnAction(event -> {
            try {
                restart();
            } catch (IOException e) {
                showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "UNSUPPORTED OS", "YOUR OS IS UNSUPPORTED BY THIS ACTION");
            }
        });
        menuQuit.setOnAction(event -> exit());
        details.setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("UserAccountManagementFiles/adminSettings.fxml"));
            try {
                Parent parent = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(parent));
                stage.initStyle(StageStyle.UTILITY);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        menulogout.setOnAction(event -> logout(panel));

    }

    private void logout() {
        logout(panel);
    }

    private void buttonListeners() {
        queryEmpTimeReport.setOnAction(event -> {
//      public DatePicker startDateEmployeeSales;
//    public DatePicker endDateEmployeeSales;
//    public Button queryEmpTimeReport;
            String start = null;
            try {
                start = startDateEmployeeSales.getValue().toString();
            } catch (NullPointerException e) {
//    start=null;
            }
            String end = null;

            try {
                end = endDateEmployeeSales.getValue().toString();
            } catch (NullPointerException e) {
//    end=null;
            }
            if (start == null && end == null) {
                loadCashierSalesTable();
            } else if (start == null && end != null) {
                loadCashierSalesTableEnd(endDateEmployeeSales.getValue().toString());

            } else if (start != null && end == null) {
                loadCashierSalesTableStart(startDateEmployeeSales.getValue().toString());

            } else if (start != null && end != null) {
                loadCashierSalesTableStartEnd(startDateEmployeeSales.getValue().toString(), endDateEmployeeSales.getValue().toString());
            } else {
                loadCashierSalesTable();
            }

        });
    }

    private void loadCashierSalesTableStartEnd(String start, String end) {
        try {
            connection = new UtilityClass().getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tableemployeelist.setOnMouseClicked(event -> {
            salesMasterObservableList.clear();
            EmployeeMaster selectedEmployee = tableemployeelist.getSelectionModel().getSelectedItem();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT  * FROM sales WHERE seller=? AND time between ? and ?");
                preparedStatement.setString(1, selectedEmployee.getEmail());
                preparedStatement.setString(2, start);
                preparedStatement.setString(3, end);
                ResultSet salesResultSet = preparedStatement.executeQuery();
                while (salesResultSet.next()) {


                    SalesMaster salesMaster = new SalesMaster();
                    salesMaster.setEmployeetransid(salesResultSet.getString("transactionid"));
                    salesMaster.setTransbalance(salesResultSet.getString("balance"));
                    salesMaster.setTranscompletion(salesResultSet.getString("completed"));
                    salesMaster.setDateCompleted(salesResultSet.getString("time"));
                    salesMaster.setTransmethod(salesResultSet.getString("method"));
                    salesMaster.setTranspaid(salesResultSet.getString("moneypaid"));
                    salesMaster.setTransprice(salesResultSet.getString("cash"));
                    salesMasterObservableList.add(salesMaster);
                }
                tableemployeesales.setItems(salesMasterObservableList);

                assert tableemployeesales != null : "fx:id=\"tableemployeesales\" was not injected: check your FXML ";
                transprice.setCellValueFactory(
                        new PropertyValueFactory<>("transprice"));
                employeetransid.setCellValueFactory(
                        new PropertyValueFactory<>("employeetransid"));
                transpaid.setCellValueFactory(new PropertyValueFactory<>("transpaid"));
                transmethod.setCellValueFactory(new PropertyValueFactory<>("transmethod"));
                transbalance.setCellValueFactory(new PropertyValueFactory<>("transbalance"));
                transcompletion.setCellValueFactory(new PropertyValueFactory<>("dateCompleted"));
                tableemployeelist.refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });


    }

    private void loadTables() {
        loadCashiersTable();
        loadCashierSalesTable();
//        loadSpecificItemsTable();// todo v1.2
//        loadCategoricalSalesTable();// todo v1.2
//        costsTableAndInput();// todo v1.2
    }


    private void loadCashiersTable() {
        try {
            try {
                connection = new UtilityClass().getConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
//                        DISPLAYING EMPLOYEES
            if (connection != null) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE admin=? and status=? and activated=?");

                statement.setBoolean(1, false);
                statement.setString(2, "active");
                statement.setInt(3, 1);
//                                statement.setBoolean(2, false);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    EmployeeMaster employeeMaster = new EmployeeMaster();
                    employeeMaster.setName(resultSet.getString("employeename"));
                    employeeMaster.setEmail(resultSet.getString("email"));
                    employeeMaster.setId(resultSet.getString("id"));
                    employeeMasterObservableList.add(employeeMaster);
                }
                tableemployeelist.setItems(employeeMasterObservableList);


                assert tableemployeelist != null : "fx:id=\"tab\" was not injected: check your FXML ";
                employeename.setCellValueFactory(
                        new PropertyValueFactory<>("Name"));
                employeeid.setCellValueFactory(
                        new PropertyValueFactory<>("Id"));
                employeeemail.setCellValueFactory(new PropertyValueFactory<>("email"));
                tableemployeelist.refresh();

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCashierSalesTableEnd(String end) {
        try {
            connection = new UtilityClass().getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tableemployeelist.setOnMouseClicked(event -> {
            salesMasterObservableList.clear();
            EmployeeMaster selectedEmployee = tableemployeelist.getSelectionModel().getSelectedItem();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT  * FROM sales WHERE seller=? AND time<?");
                preparedStatement.setString(1, selectedEmployee.getEmail());
                preparedStatement.setString(2, end);
                ResultSet salesResultSet = preparedStatement.executeQuery();
                while (salesResultSet.next()) {


                    SalesMaster salesMaster = new SalesMaster();
                    salesMaster.setEmployeetransid(salesResultSet.getString("transactionid"));
                    salesMaster.setTransbalance(salesResultSet.getString("balance"));
                    salesMaster.setTranscompletion(salesResultSet.getString("completed"));
                    salesMaster.setDateCompleted(salesResultSet.getString("time"));
                    salesMaster.setTransmethod(salesResultSet.getString("method"));
                    salesMaster.setTranspaid(salesResultSet.getString("moneypaid"));
                    salesMaster.setTransprice(salesResultSet.getString("cash"));
                    salesMasterObservableList.add(salesMaster);
                }
                tableemployeesales.setItems(salesMasterObservableList);

                assert tableemployeesales != null : "fx:id=\"tableemployeesales\" was not injected: check your FXML ";
                transprice.setCellValueFactory(
                        new PropertyValueFactory<>("transprice"));
                employeetransid.setCellValueFactory(
                        new PropertyValueFactory<>("employeetransid"));
                transpaid.setCellValueFactory(new PropertyValueFactory<>("transpaid"));
                transmethod.setCellValueFactory(new PropertyValueFactory<>("transmethod"));
                transbalance.setCellValueFactory(new PropertyValueFactory<>("transbalance"));
                transcompletion.setCellValueFactory(new PropertyValueFactory<>("dateCompleted"));
                tableemployeelist.refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });


    }

    private void loadCashierSalesTableStart(String start) {
        try {
            connection = new UtilityClass().getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tableemployeelist.setOnMouseClicked(event -> {
            salesMasterObservableList.clear();
            EmployeeMaster selectedEmployee = tableemployeelist.getSelectionModel().getSelectedItem();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT  * FROM sales WHERE seller=? AND time>?");
                preparedStatement.setString(1, selectedEmployee.getEmail());
                preparedStatement.setString(2, start);
                ResultSet salesResultSet = preparedStatement.executeQuery();
                while (salesResultSet.next()) {


                    SalesMaster salesMaster = new SalesMaster();
                    salesMaster.setEmployeetransid(salesResultSet.getString("transactionid"));
                    salesMaster.setTransbalance(salesResultSet.getString("balance"));
                    salesMaster.setTranscompletion(salesResultSet.getString("completed"));
                    salesMaster.setDateCompleted(salesResultSet.getString("time"));
                    salesMaster.setTransmethod(salesResultSet.getString("method"));
                    salesMaster.setTranspaid(salesResultSet.getString("moneypaid"));
                    salesMaster.setTransprice(salesResultSet.getString("cash"));
                    salesMasterObservableList.add(salesMaster);
                }
                tableemployeesales.setItems(salesMasterObservableList);

                assert tableemployeesales != null : "fx:id=\"tableemployeesales\" was not injected: check your FXML ";
                transprice.setCellValueFactory(
                        new PropertyValueFactory<>("transprice"));
                employeetransid.setCellValueFactory(
                        new PropertyValueFactory<>("employeetransid"));
                transpaid.setCellValueFactory(new PropertyValueFactory<>("transpaid"));
                transmethod.setCellValueFactory(new PropertyValueFactory<>("transmethod"));
                transbalance.setCellValueFactory(new PropertyValueFactory<>("transbalance"));
                transcompletion.setCellValueFactory(new PropertyValueFactory<>("dateCompleted"));
                tableemployeelist.refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });


    }

    private void loadCashierSalesTable(String start, String end) {
        try {
            connection = new UtilityClass().getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tableemployeelist.setOnMouseClicked(event -> {
            salesMasterObservableList.clear();
            EmployeeMaster selectedEmployee = tableemployeelist.getSelectionModel().getSelectedItem();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT  * FROM sales WHERE seller=? AND transactionid BETWEEN ? AND ?");
                preparedStatement.setString(1, selectedEmployee.getEmail());
                preparedStatement.setString(2, start);
                preparedStatement.setString(3, end);
                ResultSet salesResultSet = preparedStatement.executeQuery();
                while (salesResultSet.next()) {


                    SalesMaster salesMaster = new SalesMaster();
                    salesMaster.setEmployeetransid(salesResultSet.getString("transactionid"));
                    salesMaster.setTransbalance(salesResultSet.getString("balance"));
                    salesMaster.setTranscompletion(salesResultSet.getString("completed"));
                    salesMaster.setDateCompleted(salesResultSet.getString("time"));
                    salesMaster.setTransmethod(salesResultSet.getString("method"));
                    salesMaster.setTranspaid(salesResultSet.getString("moneypaid"));
                    salesMaster.setTransprice(salesResultSet.getString("cash"));
                    salesMasterObservableList.add(salesMaster);
                }
                tableemployeesales.setItems(salesMasterObservableList);

                assert tableemployeesales != null : "fx:id=\"tableemployeesales\" was not injected: check your FXML ";
                transprice.setCellValueFactory(
                        new PropertyValueFactory<>("transprice"));
                employeetransid.setCellValueFactory(
                        new PropertyValueFactory<>("employeetransid"));
                transpaid.setCellValueFactory(new PropertyValueFactory<>("transpaid"));
                transmethod.setCellValueFactory(new PropertyValueFactory<>("transmethod"));
                transbalance.setCellValueFactory(new PropertyValueFactory<>("transbalance"));
                transcompletion.setCellValueFactory(new PropertyValueFactory<>("dateCompleted"));
                tableemployeelist.refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });


    }

    private void loadCashierSalesTable() {
        try {
            connection = new UtilityClass().getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tableemployeelist.setOnMouseClicked(event -> {
            salesMasterObservableList.clear();
            EmployeeMaster selectedEmployee = tableemployeelist.getSelectionModel().getSelectedItem();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT  * FROM sales WHERE seller=?");
                preparedStatement.setString(1, selectedEmployee.getEmail());
                ResultSet salesResultSet = preparedStatement.executeQuery();
                while (salesResultSet.next()) {


                    SalesMaster salesMaster = new SalesMaster();
                    salesMaster.setEmployeetransid(salesResultSet.getString("transactionid"));
                    salesMaster.setTransbalance(salesResultSet.getString("balance"));
                    salesMaster.setTranscompletion(salesResultSet.getString("completed"));
                    salesMaster.setDateCompleted(salesResultSet.getString("time"));
                    salesMaster.setTransmethod(salesResultSet.getString("method"));
                    salesMaster.setTranspaid(salesResultSet.getString("moneypaid"));
                    salesMaster.setTransprice(salesResultSet.getString("cash"));
                    salesMasterObservableList.add(salesMaster);
                }
                tableemployeesales.setItems(salesMasterObservableList);

                assert tableemployeesales != null : "fx:id=\"tableemployeesales\" was not injected: check your FXML ";
                transprice.setCellValueFactory(
                        new PropertyValueFactory<>("transprice"));
                employeetransid.setCellValueFactory(
                        new PropertyValueFactory<>("employeetransid"));
                transpaid.setCellValueFactory(new PropertyValueFactory<>("transpaid"));
                transmethod.setCellValueFactory(new PropertyValueFactory<>("transmethod"));
                transbalance.setCellValueFactory(new PropertyValueFactory<>("transbalance"));
                transcompletion.setCellValueFactory(new PropertyValueFactory<>("dateCompleted"));
                tableemployeelist.refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });


    }

    //    fixme template pdf generator


    public TabPane getMaintabpane() {
        return maintabpane;
    }

    public void setMaintabpane(TabPane maintabpane) {
        this.maintabpane = maintabpane;
    }

    public Tab getTabemployeeaudits() {
        return tabemployeeaudits;
    }

    public void setTabemployeeaudits(Tab tabemployeeaudits) {
        this.tabemployeeaudits = tabemployeeaudits;
    }

    public TableView<EmployeeMaster> getTableemployeelist() {
        return tableemployeelist;
    }

    public void setTableemployeelist(TableView<EmployeeMaster> tableemployeelist) {
        this.tableemployeelist = tableemployeelist;
    }

    public TableColumn<EmployeeMaster, String> getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(TableColumn<EmployeeMaster, String> employeeid) {
        this.employeeid = employeeid;
    }

    public TableColumn<EmployeeMaster, String> getEmployeename() {
        return employeename;
    }

    public void setEmployeename(TableColumn<EmployeeMaster, String> employeename) {
        this.employeename = employeename;
    }

    public TableColumn<EmployeeMaster, String> getEmployeeemail() {
        return employeeemail;
    }

    public void setEmployeeemail(TableColumn<EmployeeMaster, String> employeeemail) {
        this.employeeemail = employeeemail;
    }

    public ObservableList<EmployeeMaster> getEmployeeMasterObservableList() {
        return employeeMasterObservableList;
    }

    public void setEmployeeMasterObservableList(ObservableList<EmployeeMaster> employeeMasterObservableList) {
        this.employeeMasterObservableList = employeeMasterObservableList;
    }



    public TableView<SalesMaster> getTableemployeesales() {
        return tableemployeesales;
    }

    public void setTableemployeesales(TableView<SalesMaster> tableemployeesales) {
        this.tableemployeesales = tableemployeesales;
    }

    public TableColumn<SalesMaster, String> getEmployeetransid() {
        return employeetransid;
    }

    public void setEmployeetransid(TableColumn<SalesMaster, String> employeetransid) {
        this.employeetransid = employeetransid;
    }

    public TableColumn<SalesMaster, String> getTransprice() {
        return transprice;
    }

    public void setTransprice(TableColumn<SalesMaster, String> transprice) {
        this.transprice = transprice;
    }

    public TableColumn<SalesMaster, String> getTranspaid() {
        return transpaid;
    }

    public void setTranspaid(TableColumn<SalesMaster, String> transpaid) {
        this.transpaid = transpaid;
    }

    public TableColumn<SalesMaster, String> getTransmethod() {
        return transmethod;
    }

    public void setTransmethod(TableColumn<SalesMaster, String> transmethod) {
        this.transmethod = transmethod;
    }

    public TableColumn<SalesMaster, String> getTransbalance() {
        return transbalance;
    }

    public void setTransbalance(TableColumn<SalesMaster, String> transbalance) {
        this.transbalance = transbalance;
    }

    public TableColumn<SalesMaster, String> getTranscompletion() {
        return transcompletion;
    }

    public void setTranscompletion(TableColumn<SalesMaster, String> transcompletion) {
        this.transcompletion = transcompletion;
    }

    public ObservableList<SalesMaster> getSalesMasterObservableList() {
        return salesMasterObservableList;
    }

    public void setSalesMasterObservableList(ObservableList<SalesMaster> salesMasterObservableList) {
        this.salesMasterObservableList = salesMasterObservableList;
    }


    public Tab getTabsalesaudits() {
        return tabsalesaudits;
    }

    public void setTabsalesaudits(Tab tabsalesaudits) {
        this.tabsalesaudits = tabsalesaudits;
    }

    public Tab getTabstockalerts() {
        return tabstockalerts;
    }

    public void setTabstockalerts(Tab tabstockalerts) {
        this.tabstockalerts = tabstockalerts;
    }

    public TableView getStockalerttable() {
        return stockalerttable;
    }

    public void setStockalerttable(TableView stockalerttable) {
        this.stockalerttable = stockalerttable;
    }

    public TableColumn getStockalerttableid() {
        return stockalerttableid;
    }

    public void setStockalerttableid(TableColumn stockalerttableid) {
        this.stockalerttableid = stockalerttableid;
    }

    public TableColumn getStockalerttablename() {
        return stockalerttablename;
    }

    public void setStockalerttablename(TableColumn stockalerttablename) {
        this.stockalerttablename = stockalerttablename;
    }

    public TableColumn getStockalerttabledate() {
        return stockalerttabledate;
    }

    public void setStockalerttabledate(TableColumn stockalerttabledate) {
        this.stockalerttabledate = stockalerttabledate;
    }

    public TableColumn getStockalerttablemarkasread() {
        return stockalerttablemarkasread;
    }

    public void setStockalerttablemarkasread(TableColumn stockalerttablemarkasread) {
        this.stockalerttablemarkasread = stockalerttablemarkasread;
    }

    public Tab getTaballaudits() {
        return taballaudits;
    }

    public void setTaballaudits(Tab taballaudits) {
        this.taballaudits = taballaudits;
    }

    public Button getExportfullreport() {
        return exportfullreport;
    }

    public void setExportfullreport(Button exportfullreport) {
        this.exportfullreport = exportfullreport;
    }

    public Button getExportcategoryreport() {
        return exportcategoryreport;
    }

    public void setExportcategoryreport(Button exportcategoryreport) {
        this.exportcategoryreport = exportcategoryreport;
    }

    public Button getExportemployeereport() {
        return exportemployeereport;
    }

    public void setExportemployeereport(Button exportemployeereport) {
        this.exportemployeereport = exportemployeereport;
    }

    public Button getGetdetailedgraph() {
        return getdetailedgraph;
    }

    public void setGetdetailedgraph(Button getdetailedgraph) {
        this.getdetailedgraph = getdetailedgraph;
    }

    public Button getGetcateegorygraph() {
        return getcateegorygraph;
    }

    public void setGetcateegorygraph(Button getcateegorygraph) {
        this.getcateegorygraph = getcateegorygraph;
    }

    public Button getGetemployeegraph() {
        return getemployeegraph;
    }

    public void setGetemployeegraph(Button getemployeegraph) {
        this.getemployeegraph = getemployeegraph;
    }

    public Label getClock() {
        return clock;
    }

    public void setClock(Label clock) {
        this.clock = clock;
    }


    public AnchorPane getPanel() {
        return panel;
    }

    public void setPanel(AnchorPane panel) {
        this.panel = panel;
    }

    public Button getTopanelbutton() {
        return topanelbutton;
    }

    public void setTopanelbutton(Button topanelbutton) {
        this.topanelbutton = topanelbutton;
    }

    public Button getTocarwashbutton() {
        return tocarwashbutton;
    }

    public void setTocarwashbutton(Button tocarwashbutton) {
        this.tocarwashbutton = tocarwashbutton;
    }

    public Button getToemployeesbutton() {
        return toemployeesbutton;
    }

    public void setToemployeesbutton(Button toemployeesbutton) {
        this.toemployeesbutton = toemployeesbutton;
    }

    public Button getLogoutbutton() {
        return logoutbutton;
    }

    public void setLogoutbutton(Button logoutbutton) {
        this.logoutbutton = logoutbutton;
    }

    public Button getTosupplierbutton() {
        return tosupplierbutton;
    }

    public void setTosupplierbutton(Button tosupplierbutton) {
        this.tosupplierbutton = tosupplierbutton;
    }


    @Override
    public AuditController setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }
}
