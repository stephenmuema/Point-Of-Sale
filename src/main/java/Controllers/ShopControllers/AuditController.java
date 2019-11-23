package Controllers.ShopControllers;

import Controllers.IdleMonitor;
import Controllers.UtilityClass;
import MasterClasses.CostsMasterClass;
import MasterClasses.EmployeeMaster;
import MasterClasses.SalesMaster;
import MasterClasses.StockAlertsMasterClass;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
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
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

import static securityandtime.config.site;

//made by steve
public class AuditController extends UtilityClass implements Initializable {

    @FXML
    private TableView<SalesMasterClassCatOrIndividual> categorysalestable;
    @FXML
    private TableColumn<SalesMasterClassCatOrIndividual, String> categorysalesid;
    @FXML
    private TableColumn<SalesMasterClassCatOrIndividual, String> categorysalesname;
    @FXML
    private TableColumn<SalesMasterClassCatOrIndividual, String> categorysalespayout;
    @FXML
    private TableColumn<SalesMasterClassCatOrIndividual, String> categorysalessalesperday;
    @FXML
    private TableView<SalesMasterClassCatOrIndividual> itemsalestable;
    @FXML
    private TableColumn<SalesMasterClassCatOrIndividual, String> itemsalesid;
    @FXML
    private TableColumn<SalesMasterClassCatOrIndividual, String> itemsalesname;
    @FXML
    private TableColumn<SalesMasterClassCatOrIndividual, String> itemsalespayout;
    @FXML
    private TableColumn<SalesMasterClassCatOrIndividual, String> itemsalessalesperday;
    @FXML
    private TableColumn<SalesMasterClassCatOrIndividual, String> itemsalessalesremainingstock;
    @FXML
    private RadioButton indSalesRad;
    @FXML
    private RadioButton catSalesRad;
    @FXML
    private Button getSales;
    @FXML
    private DatePicker startDateCatOrIndSales;
    @FXML
    private DatePicker endDateCatOrIndSales;
    @FXML
    private Label tblLblMsg;

    private ArrayList<DatePicker> showPastDatesArrayList = new ArrayList<>();
    ////////////////////new tab


    ///////////////////new costs tab
    @FXML
    private Tab tabcapandcost;
    @FXML
    private TabPane tapaneinnercapitalandcost;
    @FXML
    private Tab newcosts;
    @FXML
    private DatePicker newcostsdatecreated;
    @FXML
    private TextField newcostsamount;
    @FXML
    private TextField newcostsname;
    @FXML
    private Button newcostssubmit;
    @FXML
    private TextArea newcostdescription;
    @FXML
    private TextField showcurrentuser;
    @FXML
    private ComboBox<String> combocategory;
    @FXML
    private Button createCostsCat;
    @FXML
    private CheckBox activatecostchkbox;
    @FXML
    private Tab pastcosts;
    @FXML
    private TableView<CostsMasterClass> pastcoststable;
    @FXML
    private TableColumn<CostsMasterClass, String> pastcoststableid;
    @FXML
    private TableColumn<CostsMasterClass, String> pastcoststablename;
    @FXML
    private TableColumn<CostsMasterClass, String> pastcoststabledateadded;
    @FXML
    private TableColumn<CostsMasterClass, String> pastcoststableamount;
    @FXML
    private TableColumn<CostsMasterClass, String> pastcoststableactiveinactivestatus;


    ////end of costs tab
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
    private TableView<StockAlertsMasterClass> stockalerttable;


    @FXML
    private TableColumn<StockAlertsMasterClass, String> stockalerttableid;
    @FXML
    private TableColumn<StockAlertsMasterClass, String> stockalerttablename;
    @FXML
    private TableColumn<StockAlertsMasterClass, String> stockalerttabledate;

    @FXML
    private TableColumn<StockAlertsMasterClass, String> amountRemaining;
    @FXML
    private TableColumn<StockAlertsMasterClass, String> salesperday;
    @FXML
    private TableColumn<StockAlertsMasterClass, String> itemCategory;
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
    private ObservableList<CostsMasterClass> costsMasterClassObservableList = FXCollections.observableArrayList();
    private ObservableList<StockAlertsMasterClass> stockAlertsMasterClassObservableList = FXCollections.observableArrayList();
    private String sellerEmail;
    private String radSelected;
    public AuditController() throws IOException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initScene();
    }

    public static void checkStocks() {
        try {
//            adding new stock alerts
            String id = null;
            Connection connection = new UtilityClass().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT *FROM stocks WHERE amount < ?");
            preparedStatement.setString(1, "500");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    id = resultSet.getString("id");
                }
            }
            preparedStatement = connection.prepareStatement("SELECT * FROM stockalerts where itemid=?");
            preparedStatement.setString(1, id);
            if (resultSet.isBeforeFirst()) {
                preparedStatement = connection.prepareStatement("INSERT INTO stockalerts (itemid) VALUES (?)");
                preparedStatement.setString(1, id);
                preparedStatement.executeUpdate();
            }

//            removing items tat have not reached alert level
            //            adding new stock alerts
            preparedStatement = connection.prepareStatement("SELECT *FROM stockalerts");
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    id = resultSet.getString("itemid");
                }
            }
            preparedStatement = connection.prepareStatement("SELECT * FROM stocks where id=?");
            preparedStatement.setString(1, id);
            if (resultSet.isBeforeFirst()) {
                if (Integer.parseInt(resultSet.getString("amount")) >= 500) {
                    preparedStatement = connection.prepareStatement("DELETE FROM stockalerts WHERE itemid=?");
                    preparedStatement.setString(1, id);
                    preparedStatement.executeUpdate();
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void initScene() {

        tabstockalerts.setOnSelectionChanged(event -> {
            stockAlertsMasterClassObservableList.clear();
            if (tabstockalerts.isSelected()) {
                checkStocks();
                loadAlerts();
            }
        });
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
            tblLblMsg.setText(null);
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
        initDatePickers();
        menuListeners();
        buttonListeners();
        setTableEditableProperty();
        navigatoryButtonListeners();
        loadTables();
        time(clock);
        //load combo box data
        ObservableList<String> combodata = FXCollections.observableArrayList();
        try {
            Statement stmt = new UtilityClass().getConnection().createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM cost_category");
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    combodata.add(resultSet.getString("category_name"));
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        combocategory.setItems(combodata);
        showcurrentuser.setText(config.user.get("userName"));
        pastcosts.setOnSelectionChanged(event -> {
            costsMasterClassObservableList.clear();
            if (pastcosts.isSelected()) {
//load table of past costs
                try {
                    connection = new UtilityClass().getConnection();
                    Statement statement = connection.createStatement();
                    ResultSet resultSetCosts = statement.executeQuery("SELECT * FROM costs");
                    if (resultSetCosts.isBeforeFirst()) {
                        while (resultSetCosts.next()) {
                            CostsMasterClass costsMasterClass = new CostsMasterClass();
                            costsMasterClass.setId(resultSetCosts.getString("id"));
                            costsMasterClass.setAmount(resultSetCosts.getString("amount"));
                            costsMasterClass.setDateadded(resultSetCosts.getString("datecreated"));
                            costsMasterClass.setName(resultSetCosts.getString("name"));
                            costsMasterClass.setIsactive(resultSetCosts.getString("status"));
                            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM cost_category WHERE id=?");
                            preparedStatement.setString(1, costsMasterClass.getId());
                            ResultSet resultSet = preparedStatement.executeQuery();
                            if (resultSet.isBeforeFirst()) {
                                while (resultSet.next()) {
                                    costsMasterClass.setCategory(resultSet.getString("category_name"));
                                }
                            }
                            costsMasterClassObservableList.add(costsMasterClass);
                        }
                    }

                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }


                pastcoststable.setItems(costsMasterClassObservableList);


                assert pastcoststable != null : "fx:id=\"pastcoststable\" was not injected: check your FXML ";
                pastcoststableid.setCellValueFactory(
                        new PropertyValueFactory<>("id"));
                pastcoststablename.setCellValueFactory(
                        new PropertyValueFactory<>("name"));
                pastcoststabledateadded.setCellValueFactory(new PropertyValueFactory<>("dateadded"));
                pastcoststableamount.setCellValueFactory(new PropertyValueFactory<>("amount"));
                pastcoststableactiveinactivestatus.setCellValueFactory(new PropertyValueFactory<>("isactive"));
                pastcoststable.refresh();
            }
        });
        //add table past costs context menu
        ContextMenu cm = new ContextMenu();
        MenuItem contextToggleStatus = new MenuItem("TOGGLE STATUS");
        cm.getItems().add(contextToggleStatus);
        pastcoststable.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
            if (t.getButton() == MouseButton.SECONDARY) {
                cm.show(pastcoststable, t.getScreenX(), t.getScreenY());
            }
        });
        contextToggleStatus.setOnAction(event -> {
            CostsMasterClass costsMasterClass = pastcoststable.getSelectionModel().getSelectedItem();
            if (costsMasterClass != null) {
                String status = costsMasterClass.getIsactive();
                if (status.equalsIgnoreCase("active")) {
                    //set status to inactive
                    try {
                        connection = new UtilityClass().getConnection();
                        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE costs SET status=? WHERE id=?");
                        preparedStatement.setString(1, "inactive".toUpperCase());
                        preparedStatement.setString(2, costsMasterClass.getId());
                        preparedStatement.executeUpdate();
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    //set status to active if it is currently inactive
                    try {
                        connection = new UtilityClass().getConnection();
                        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE costs SET status=? WHERE id=?");
                        preparedStatement.setString(1, "active".toUpperCase());
                        preparedStatement.setString(2, costsMasterClass.getId());
                        preparedStatement.executeUpdate();
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                }
                loadCosts();
            }
        });
    }

    private void loadAlerts() {
        String query = "SELECT * FROM stockalerts ";
        try {
            connection = new UtilityClass().getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSetFetchAlerts = preparedStatement.executeQuery();
            if (resultSetFetchAlerts.isBeforeFirst()) {
                while (resultSetFetchAlerts.next()) {
                    StockAlertsMasterClass stockAlertsMasterClass = new StockAlertsMasterClass();

                    stockAlertsMasterClass.setDate(resultSetFetchAlerts.getString("date"));
                    preparedStatement = connection.prepareStatement("SELECT * FROM stocks where id=?");
                    preparedStatement.setString(1, resultSetFetchAlerts.getString("itemid"));
                    resultSetFetchAlerts = preparedStatement.executeQuery();
                    if (resultSetFetchAlerts.isBeforeFirst()) {
                        while (resultSetFetchAlerts.next()) {
                            stockAlertsMasterClass.setId(Integer.parseInt(resultSetFetchAlerts.getString("id")));
                            stockAlertsMasterClass.setCategory(resultSetFetchAlerts.getString("category"));
                            stockAlertsMasterClass.setName(resultSetFetchAlerts.getString("name"));
                            stockAlertsMasterClass.setRemaining(resultSetFetchAlerts.getString("amount"));
                        }
                    }
                    stockAlertsMasterClassObservableList.add(stockAlertsMasterClass);
                }
                stockalerttable.setItems(stockAlertsMasterClassObservableList);
                assert stockalerttable != null : "fx:id=\"stockalerttable\" was not injected: check your FXML ";

                stockalerttableid.setCellValueFactory(
                        new PropertyValueFactory<>("id"));
                stockalerttablename.setCellValueFactory(
                        new PropertyValueFactory<>("name"));
                stockalerttabledate.setCellValueFactory(new PropertyValueFactory<>("date"));
                amountRemaining.setCellValueFactory(new PropertyValueFactory<>("remaining"));
                salesperday.setCellValueFactory(new PropertyValueFactory<>("rate"));
                itemCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
                stockalerttable.refresh();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setTableEditableProperty() {
        pastcoststable.setEditable(true);

        pastcoststablename.setCellFactory(TextFieldTableCell.forTableColumn());
        pastcoststableamount.setCellFactory(TextFieldTableCell.forTableColumn());
        pastcoststableamount.setOnEditCommit(
                t -> {
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setName(t.getNewValue());
                    String newval = t.getNewValue();
                    PreparedStatement preparedStatement = null;
                    try {
                        try {
                            connection = new UtilityClass().getConnection();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        CostsMasterClass costsMasterClass = pastcoststable.getSelectionModel().getSelectedItem();
                        String id = costsMasterClass.getId();
                        preparedStatement = connection.prepareStatement("UPDATE costs set amount=? where id=?");
                        preparedStatement.setString(1, newval.toUpperCase());
                        preparedStatement.setString(2, id);
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

//                        preparedStatement.setString(1, name.getText());
                }
        );
        pastcoststablename.setOnEditCommit(
                t -> {
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setName(t.getNewValue());
                    String newval = t.getNewValue();
                    PreparedStatement preparedStatement = null;
                    try {
                        try {
                            connection = new UtilityClass().getConnection();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        CostsMasterClass costsMasterClass = pastcoststable.getSelectionModel().getSelectedItem();
                        String id = costsMasterClass.getId();
                        preparedStatement = connection.prepareStatement("UPDATE costs set name=? where id=?");
                        preparedStatement.setString(1, newval.toUpperCase());
                        preparedStatement.setString(2, id);
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

//                        preparedStatement.setString(1, name.getText());
                }
        );
        // when character or numbers pressed it will start edit in editable
    }


    private void initDatePickers() {
        showPastDatesArrayList.add(endDateCatOrIndSales);
        showPastDatesArrayList.add(newcostsdatecreated);
        showPastDatesArrayList.add(startDateCatOrIndSales);
        showPastDatesArrayList.add(endDateEmployeeSales);
        showPastDatesArrayList.add(startDateEmployeeSales);
        datePickerShowPastOnly(showPastDatesArrayList);
    }

    private void datePickerShowPastOnly(ArrayList<DatePicker> datePickerArrayList) {
        for (DatePicker datepicker :
                datePickerArrayList) {
            datepicker.setDayCellFactory(picker -> new DateCell() {
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate today = LocalDate.now();

                    setDisable(empty || date.compareTo(today) > 0);
                }
            });
        }

    }

    //done with this method
    private void loadCategoricalOrIndividualSalesTable() {
        tblLblMsg.setText("****ALL SALES EVER DONE****");
        itemsalessalesperday.setText("TOTAL SALES");

        try {
            connection = new UtilityClass().getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (radSelected.contains("individual") || radSelected.contains("INDIVIDUAL")) {
            //SHOW INDIVIDUAL SALES

            categorysalestable.setVisible(false);
            itemsalestable.setVisible(true);


        } else {

            //SHOW CATEGORICAL SALES
            categorysalestable.setVisible(true);
            itemsalestable.setVisible(false);
            //select item sales by name and display the total price sold in the specified time
            //show the number of items remaining in the db adn show the rate of sales per day

        }
    }

    private void loadCategoricalInitially() {
        salesMasterClassCatOrIndividuals.clear();

        Set<String> categoryNames = new LinkedHashSet<>();

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

    private void loadIndividualInitially() {
        salesMasterClassCatOrIndividuals.clear();
        itemsalessalesremainingstock.setVisible(false);
        Set<String> names = new LinkedHashSet<>();

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
                        String amt = resultSet.getString("amount");
                        classIndividual.setRemaining(amt);
                        System.out.println(amt);
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
    }

    private void loadCategoricalOrIndividualSalesTableWithTime(String start, String end) {
        tblLblMsg.setText("***SALES DONE BETWEEN " + start + " and " + end + "***");
        Set<String> names = new LinkedHashSet<>();
        Set<String> categoryNames = new LinkedHashSet<>();
        itemsalessalesremainingstock.setVisible(true);

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
                PreparedStatement preparedStatementFetchItemNames = connection.prepareStatement("SELECT * FROM solditems where timeSold BETWEEN ? and ?");
                preparedStatementFetchItemNames.setString(1, start);
                preparedStatementFetchItemNames.setString(2, end);
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
                    PreparedStatement fetchSoldItems = connection.prepareStatement("SELECT * FROM solditems where name =? and timeSold BETWEEN ? and ?");
                    fetchSoldItems.setString(1, name);
                    fetchSoldItems.setString(2, start);
                    fetchSoldItems.setString(3, end);
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

            //SHOW CATEGORICAL SALES
            categorysalestable.setVisible(true);
            itemsalestable.setVisible(false);
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
                }//fetch items from sold items whose category=current value in set
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
                    preparedStatementFetchItemNames = connection.prepareStatement("SELECT * FROM stocks WHERE category=?");
                    preparedStatementFetchItemNames.setString(1, name);
                    resultSet = preparedStatementFetchItemNames.executeQuery();
                    int remainingCategoryAmount = 0;
                    if (resultSet.isBeforeFirst()) {
                        while (resultSet.next()) {
                            classCategoricalSales.setRemaining(resultSet.getString("amount"));
                            remainingCategoryAmount += Integer.parseInt(resultSet.getString("amount"));
                        }
                    }
                    classCategoricalSales.setRemaining(String.valueOf(remainingCategoryAmount));
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
//                categorysalessalesperday.setCellValueFactory(new PropertyValueFactory<>("rateOfSales"));
                categorysalestable.refresh();

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
        newcostssubmit.setOnAction(event -> {
            try {
                connection = new UtilityClass().getConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String date = newcostsdatecreated.getValue().toString();
            String newCostAmt = newcostsamount.getText();
            String newCostsName = newcostsname.getText();
            String newCostsDesc = newcostdescription.getText();
            String user = showcurrentuser.getText();
            String category = combocategory.getValue();
            String activated = activatecostchkbox.isSelected() ? "active" : "inactive";
            validateAddCostsMenu(date, newCostAmt, newCostsName, newCostsDesc, category);
            PreparedStatement preparedStatement;
            try {
                preparedStatement = connection.prepareStatement("SELECT * FROM cost_category WHERE category_name=?");
                preparedStatement.setString(1, category);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.isBeforeFirst()) {
                    while (resultSet.next()) {
                        category = resultSet.getString("id");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                preparedStatement = connection.prepareStatement("SELECT * FROM costs WHERE name=?");
                preparedStatement.setString(1, newCostsName);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.isBeforeFirst()) {
                    showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "A PRICE WITH A SIMILAR NAME ALREADY EXISTS");
                } else {
                    preparedStatement = connection.prepareStatement("INSERT INTO costs (name, category, description, owner, status,datecreated,amount) VALUES (?,?,?,?,?,?,?)");
                    preparedStatement.setString(1, newCostsName.toUpperCase());
                    preparedStatement.setString(2, category.toUpperCase());
                    preparedStatement.setString(3, newCostsDesc.toUpperCase());
                    preparedStatement.setString(4, user);
                    preparedStatement.setString(5, activated.toUpperCase());
                    preparedStatement.setString(6, date.toUpperCase());
                    preparedStatement.setString(7, newCostAmt.toUpperCase());
                    if (preparedStatement.executeUpdate() > 0) {
                        newcostsdatecreated.setValue(null);
                        newcostsamount.clear();
                        newcostsname.clear();
                        newcostdescription.clear();
                        activatecostchkbox.setSelected(false);
                        showAlert(Alert.AlertType.INFORMATION, panel.getScene().getWindow(), "SUCCESS", "YOUR DATA HAS BEEN ADDED SUCCESSFULLY");

                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        createCostsCat.setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("shopFiles/categoriesAdd.fxml"));
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
        getSales.setOnAction(event -> {
            String endDate;
            String startDate = null;
            try {
                startDate = startDateCatOrIndSales.getValue().toString();
            } catch (NullPointerException e) {
                showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR!!", "specify a strting time for this action to be carried out!!");
            }


            try {
                endDate = endDateCatOrIndSales.getValue().toString();
            } catch (NullPointerException e) {
                endDate = LocalDate.now().toString();
            }


            loadCategoricalOrIndividualSalesTableWithTime(startDate, endDate);
        });
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

    private void validateAddCostsMenu(String date, String newCostAmt, String newCostsName, String newCostsDesc, String category) {
        if (date.isEmpty() || newCostAmt.isEmpty() || newCostsName.isEmpty() || newCostsDesc.isEmpty() || category.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, panel.getScene().getWindow(), "ERROR", "FILL ALL THE FIELDS BEFOR SUBMITTING THE FORM");
        }
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
        loadIndividualInitially();
        loadCategoricalInitially();
    }

    private void loadCosts() {
        costsMasterClassObservableList.clear();
        try {
            connection = new UtilityClass().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSetCosts = statement.executeQuery("SELECT * FROM costs");
            if (resultSetCosts.isBeforeFirst()) {
                while (resultSetCosts.next()) {
                    CostsMasterClass costsMasterClass1 = new CostsMasterClass();
                    costsMasterClass1.setId(resultSetCosts.getString("id"));
                    costsMasterClass1.setAmount(resultSetCosts.getString("amount"));
                    costsMasterClass1.setDateadded(resultSetCosts.getString("datecreated"));
                    costsMasterClass1.setName(resultSetCosts.getString("name"));
                    costsMasterClass1.setIsactive(resultSetCosts.getString("status"));
                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM cost_category WHERE id=?");
                    preparedStatement.setString(1, costsMasterClass1.getId());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.isBeforeFirst()) {
                        while (resultSet.next()) {
                            costsMasterClass1.setCategory(resultSet.getString("category_name"));
                        }
                    }
                    costsMasterClassObservableList.add(costsMasterClass1);
                }
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        pastcoststable.setItems(costsMasterClassObservableList);


        assert pastcoststable != null : "fx:id=\"pastcoststable\" was not injected: check your FXML ";
        pastcoststableid.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        pastcoststablename.setCellValueFactory(
                new PropertyValueFactory<>("name"));
        pastcoststabledateadded.setCellValueFactory(new PropertyValueFactory<>("dateadded"));
        pastcoststableamount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        pastcoststableactiveinactivestatus.setCellValueFactory(new PropertyValueFactory<>("isactive"));
        pastcoststable.refresh();
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
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT  * FROM sales WHERE seller=? AND time<=?");
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
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT  * FROM sales WHERE seller=? AND time>=?");
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

    public TableView<SalesMasterClassCatOrIndividual> getCategorysalestable() {
        return categorysalestable;
    }

    public void setCategorysalestable(TableView<SalesMasterClassCatOrIndividual> categorysalestable) {
        this.categorysalestable = categorysalestable;
    }

    public TableColumn<SalesMasterClassCatOrIndividual, String> getCategorysalesid() {
        return categorysalesid;
    }

    public void setCategorysalesid(TableColumn<SalesMasterClassCatOrIndividual, String> categorysalesid) {
        this.categorysalesid = categorysalesid;
    }

    public TableColumn<SalesMasterClassCatOrIndividual, String> getCategorysalesname() {
        return categorysalesname;
    }

    public void setCategorysalesname(TableColumn<SalesMasterClassCatOrIndividual, String> categorysalesname) {
        this.categorysalesname = categorysalesname;
    }

    public TableColumn<SalesMasterClassCatOrIndividual, String> getCategorysalespayout() {
        return categorysalespayout;
    }

    public void setCategorysalespayout(TableColumn<SalesMasterClassCatOrIndividual, String> categorysalespayout) {
        this.categorysalespayout = categorysalespayout;
    }

    public TableColumn<SalesMasterClassCatOrIndividual, String> getCategorysalessalesperday() {
        return categorysalessalesperday;
    }

    public void setCategorysalessalesperday(TableColumn<SalesMasterClassCatOrIndividual, String> categorysalessalesperday) {
        this.categorysalessalesperday = categorysalessalesperday;
    }

    public TableView<SalesMasterClassCatOrIndividual> getItemsalestable() {
        return itemsalestable;
    }

    public void setItemsalestable(TableView<SalesMasterClassCatOrIndividual> itemsalestable) {
        this.itemsalestable = itemsalestable;
    }

    public TableColumn<SalesMasterClassCatOrIndividual, String> getItemsalesid() {
        return itemsalesid;
    }

    public void setItemsalesid(TableColumn<SalesMasterClassCatOrIndividual, String> itemsalesid) {
        this.itemsalesid = itemsalesid;
    }

    public TableColumn<SalesMasterClassCatOrIndividual, String> getItemsalesname() {
        return itemsalesname;
    }

    public void setItemsalesname(TableColumn<SalesMasterClassCatOrIndividual, String> itemsalesname) {
        this.itemsalesname = itemsalesname;
    }

    public TableColumn<SalesMasterClassCatOrIndividual, String> getItemsalespayout() {
        return itemsalespayout;
    }

    public void setItemsalespayout(TableColumn<SalesMasterClassCatOrIndividual, String> itemsalespayout) {
        this.itemsalespayout = itemsalespayout;
    }

    public TableColumn<SalesMasterClassCatOrIndividual, String> getItemsalessalesperday() {
        return itemsalessalesperday;
    }

    public void setItemsalessalesperday(TableColumn<SalesMasterClassCatOrIndividual, String> itemsalessalesperday) {
        this.itemsalessalesperday = itemsalessalesperday;
    }

    public TableColumn<SalesMasterClassCatOrIndividual, String> getItemsalessalesremainingstock() {
        return itemsalessalesremainingstock;
    }

    public void setItemsalessalesremainingstock(TableColumn<SalesMasterClassCatOrIndividual, String> itemsalessalesremainingstock) {
        this.itemsalessalesremainingstock = itemsalessalesremainingstock;
    }

    public RadioButton getIndSalesRad() {
        return indSalesRad;
    }

    public void setIndSalesRad(RadioButton indSalesRad) {
        this.indSalesRad = indSalesRad;
    }

    public RadioButton getCatSalesRad() {
        return catSalesRad;
    }

    public void setCatSalesRad(RadioButton catSalesRad) {
        this.catSalesRad = catSalesRad;
    }

    public Button getGetSales() {
        return getSales;
    }

    public void setGetSales(Button getSales) {
        this.getSales = getSales;
    }

    public DatePicker getStartDateCatOrIndSales() {
        return startDateCatOrIndSales;
    }

    public void setStartDateCatOrIndSales(DatePicker startDateCatOrIndSales) {
        this.startDateCatOrIndSales = startDateCatOrIndSales;
    }

    public DatePicker getEndDateCatOrIndSales() {
        return endDateCatOrIndSales;
    }

    public void setEndDateCatOrIndSales(DatePicker endDateCatOrIndSales) {
        this.endDateCatOrIndSales = endDateCatOrIndSales;
    }

    public Label getTblLblMsg() {
        return tblLblMsg;
    }

    public void setTblLblMsg(Label tblLblMsg) {
        this.tblLblMsg = tblLblMsg;
    }

    public ArrayList<DatePicker> getShowPastDatesArrayList() {
        return showPastDatesArrayList;
    }

    public void setShowPastDatesArrayList(ArrayList<DatePicker> showPastDatesArrayList) {
        this.showPastDatesArrayList = showPastDatesArrayList;
    }

    public Tab getTabcapandcost() {
        return tabcapandcost;
    }

    public void setTabcapandcost(Tab tabcapandcost) {
        this.tabcapandcost = tabcapandcost;
    }

    public TabPane getTapaneinnercapitalandcost() {
        return tapaneinnercapitalandcost;
    }

    public void setTapaneinnercapitalandcost(TabPane tapaneinnercapitalandcost) {
        this.tapaneinnercapitalandcost = tapaneinnercapitalandcost;
    }

    public Tab getNewcosts() {
        return newcosts;
    }

    public void setNewcosts(Tab newcosts) {
        this.newcosts = newcosts;
    }

    public DatePicker getNewcostsdatecreated() {
        return newcostsdatecreated;
    }

    public void setNewcostsdatecreated(DatePicker newcostsdatecreated) {
        this.newcostsdatecreated = newcostsdatecreated;
    }

    public TextField getNewcostsamount() {
        return newcostsamount;
    }

    public void setNewcostsamount(TextField newcostsamount) {
        this.newcostsamount = newcostsamount;
    }

    public TextField getNewcostsname() {
        return newcostsname;
    }

    public void setNewcostsname(TextField newcostsname) {
        this.newcostsname = newcostsname;
    }

    public Button getNewcostssubmit() {
        return newcostssubmit;
    }

    public void setNewcostssubmit(Button newcostssubmit) {
        this.newcostssubmit = newcostssubmit;
    }

    public TextArea getNewcostdescription() {
        return newcostdescription;
    }

    public void setNewcostdescription(TextArea newcostdescription) {
        this.newcostdescription = newcostdescription;
    }

    public TextField getShowcurrentuser() {
        return showcurrentuser;
    }

    public void setShowcurrentuser(TextField showcurrentuser) {
        this.showcurrentuser = showcurrentuser;
    }

    public ComboBox<String> getCombocategory() {
        return combocategory;
    }

    public void setCombocategory(ComboBox<String> combocategory) {
        this.combocategory = combocategory;
    }

    public Button getCreateCostsCat() {
        return createCostsCat;
    }

    public void setCreateCostsCat(Button createCostsCat) {
        this.createCostsCat = createCostsCat;
    }

    public CheckBox getActivatecostchkbox() {
        return activatecostchkbox;
    }

    public void setActivatecostchkbox(CheckBox activatecostchkbox) {
        this.activatecostchkbox = activatecostchkbox;
    }

    public Tab getPastcosts() {
        return pastcosts;
    }

    public void setPastcosts(Tab pastcosts) {
        this.pastcosts = pastcosts;
    }

    public TableView<CostsMasterClass> getPastcoststable() {
        return pastcoststable;
    }

    public void setPastcoststable(TableView<CostsMasterClass> pastcoststable) {
        this.pastcoststable = pastcoststable;
    }

    public TableColumn<CostsMasterClass, String> getPastcoststableid() {
        return pastcoststableid;
    }

    public void setPastcoststableid(TableColumn<CostsMasterClass, String> pastcoststableid) {
        this.pastcoststableid = pastcoststableid;
    }

    public TableColumn<CostsMasterClass, String> getPastcoststablename() {
        return pastcoststablename;
    }

    public void setPastcoststablename(TableColumn<CostsMasterClass, String> pastcoststablename) {
        this.pastcoststablename = pastcoststablename;
    }

    public TableColumn<CostsMasterClass, String> getPastcoststabledateadded() {
        return pastcoststabledateadded;
    }

    public void setPastcoststabledateadded(TableColumn<CostsMasterClass, String> pastcoststabledateadded) {
        this.pastcoststabledateadded = pastcoststabledateadded;
    }

    public TableColumn<CostsMasterClass, String> getPastcoststableamount() {
        return pastcoststableamount;
    }

    public void setPastcoststableamount(TableColumn<CostsMasterClass, String> pastcoststableamount) {
        this.pastcoststableamount = pastcoststableamount;
    }

    public TableColumn<CostsMasterClass, String> getPastcoststableactiveinactivestatus() {
        return pastcoststableactiveinactivestatus;
    }

    public void setPastcoststableactiveinactivestatus(TableColumn<CostsMasterClass, String> pastcoststableactiveinactivestatus) {
        this.pastcoststableactiveinactivestatus = pastcoststableactiveinactivestatus;
    }

    public MenuItem getDetails() {
        return details;
    }

    public void setDetails(MenuItem details) {
        this.details = details;
    }

    public MenuItem getMenulogout() {
        return menulogout;
    }

    public void setMenulogout(MenuItem menulogout) {
        this.menulogout = menulogout;
    }

    public Button getShowempperformancegraph() {
        return showempperformancegraph;
    }

    public void setShowempperformancegraph(Button showempperformancegraph) {
        this.showempperformancegraph = showempperformancegraph;
    }

    public Button getExportfirstempreport() {
        return exportfirstempreport;
    }

    public void setExportfirstempreport(Button exportfirstempreport) {
        this.exportfirstempreport = exportfirstempreport;
    }

    public Button getShowEmpReport() {
        return showEmpReport;
    }

    public void setShowEmpReport(Button showEmpReport) {
        this.showEmpReport = showEmpReport;
    }

    public MenuItem getEndDayMenu() {
        return endDayMenu;
    }

    public void setEndDayMenu(MenuItem endDayMenu) {
        this.endDayMenu = endDayMenu;
    }

    public MenuItem getReportIssuesMenu() {
        return reportIssuesMenu;
    }

    public void setReportIssuesMenu(MenuItem reportIssuesMenu) {
        this.reportIssuesMenu = reportIssuesMenu;
    }

    public MenuItem getRestartServerMenu() {
        return restartServerMenu;
    }

    public void setRestartServerMenu(MenuItem restartServerMenu) {
        this.restartServerMenu = restartServerMenu;
    }

    public MenuItem getTroubleShootMenu() {
        return troubleShootMenu;
    }

    public void setTroubleShootMenu(MenuItem troubleShootMenu) {
        this.troubleShootMenu = troubleShootMenu;
    }

    public MenuItem getAbtMenu() {
        return abtMenu;
    }

    public void setAbtMenu(MenuItem abtMenu) {
        this.abtMenu = abtMenu;
    }

    public MenuItem getTermsMenu() {
        return termsMenu;
    }

    public void setTermsMenu(MenuItem termsMenu) {
        this.termsMenu = termsMenu;
    }

    public MenuItem getCheckUpdatesMenu() {
        return checkUpdatesMenu;
    }

    public void setCheckUpdatesMenu(MenuItem checkUpdatesMenu) {
        this.checkUpdatesMenu = checkUpdatesMenu;
    }

    public MenuItem getReachUsMenu() {
        return reachUsMenu;
    }

    public void setReachUsMenu(MenuItem reachUsMenu) {
        this.reachUsMenu = reachUsMenu;
    }

    public MenuItem getGenerateReportsMenu() {
        return generateReportsMenu;
    }

    public void setGenerateReportsMenu(MenuItem generateReportsMenu) {
        this.generateReportsMenu = generateReportsMenu;
    }

    public MenuItem getDocumentationMenu() {
        return documentationMenu;
    }

    public void setDocumentationMenu(MenuItem documentationMenu) {
        this.documentationMenu = documentationMenu;
    }

    public MenuItem getMenuQuit() {
        return menuQuit;
    }

    public void setMenuQuit(MenuItem menuQuit) {
        this.menuQuit = menuQuit;
    }

    public MenuItem getStaffMenu() {
        return staffMenu;
    }

    public void setStaffMenu(MenuItem staffMenu) {
        this.staffMenu = staffMenu;
    }

    public MenuItem getCarWashMenu() {
        return carWashMenu;
    }

    public void setCarWashMenu(MenuItem carWashMenu) {
        this.carWashMenu = carWashMenu;
    }

    public MenuItem getInventoryMenu() {
        return inventoryMenu;
    }

    public void setInventoryMenu(MenuItem inventoryMenu) {
        this.inventoryMenu = inventoryMenu;
    }

    public MenuItem getMrMenu() {
        return mrMenu;
    }

    public void setMrMenu(MenuItem mrMenu) {
        this.mrMenu = mrMenu;
    }

    public MenuItem getAuditsMenu() {
        return auditsMenu;
    }

    public void setAuditsMenu(MenuItem auditsMenu) {
        this.auditsMenu = auditsMenu;
    }

    public MenuItem getMenuShutDown() {
        return menuShutDown;
    }

    public void setMenuShutDown(MenuItem menuShutDown) {
        this.menuShutDown = menuShutDown;
    }

    public MenuItem getMenuRestart() {
        return menuRestart;
    }

    public void setMenuRestart(MenuItem menuRestart) {
        this.menuRestart = menuRestart;
    }

    public DatePicker getStartDateEmployeeSales() {
        return startDateEmployeeSales;
    }

    public void setStartDateEmployeeSales(DatePicker startDateEmployeeSales) {
        this.startDateEmployeeSales = startDateEmployeeSales;
    }

    public DatePicker getEndDateEmployeeSales() {
        return endDateEmployeeSales;
    }

    public void setEndDateEmployeeSales(DatePicker endDateEmployeeSales) {
        this.endDateEmployeeSales = endDateEmployeeSales;
    }

    public Button getQueryEmpTimeReport() {
        return queryEmpTimeReport;
    }

    public void setQueryEmpTimeReport(Button queryEmpTimeReport) {
        this.queryEmpTimeReport = queryEmpTimeReport;
    }


    public ObservableList<SalesMasterClassCatOrIndividual> getSalesMasterClassCatOrIndividuals() {
        return salesMasterClassCatOrIndividuals;
    }

    public void setSalesMasterClassCatOrIndividuals(ObservableList<SalesMasterClassCatOrIndividual> salesMasterClassCatOrIndividuals) {
        this.salesMasterClassCatOrIndividuals = salesMasterClassCatOrIndividuals;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getRadSelected() {
        return radSelected;
    }

    public void setRadSelected(String radSelected) {
        this.radSelected = radSelected;
    }
}
