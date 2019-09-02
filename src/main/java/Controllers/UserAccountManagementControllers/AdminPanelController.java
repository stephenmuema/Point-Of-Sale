package Controllers.UserAccountManagementControllers;

import Controllers.UtilityClass;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.util.Duration;
import securityandtime.config;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Objects;
import java.util.ResourceBundle;

import static securityandtime.config.dbdetails;
import static securityandtime.config.supplierSite;

public class AdminPanelController extends UtilityClass implements Initializable {
    public MenuItem menulogout;
    public Button addshop;
    public MenuItem details;
    public Menu branches;
    public MenuItem license;
    public MenuItem feedback;
    public Label clock;
    public Font x1;
    public Button employees;
    public Button stockspanel;
    public Button carwashpanel;
    public Button visitSuppliers;
    public Button backup;
    public Button audits;
    @FXML
    private AnchorPane AdminPanel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuClick();
        buttonClick();
        time(clock);


        IdleMonitor idleMonitor = new IdleMonitor(Duration.seconds(900),
                () -> {
                    try {
                        config.login.put("loggedout", true);

                        AdminPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("AuthenticationFiles/Login.fxml")))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, true);
        idleMonitor.register(AdminPanel, Event.ANY);
    }


    public void menuClick() {
        menulogout.setOnAction(event -> {
            logout(AdminPanel);

        });
    }



    private void buttonClick() {

        backup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String[] tables = {"audits", "carwash", "chats", "costs", "payments", "sales", "salespercategory", "solditems"
                        , "stocks", "stores", "subscribers", "suppliers", "timers", "users"};
                for (String tablename : tables) {
                    try {
                        System.out.println(" Table " + tablename + backup("127.0.0.1", dbdetails[5], dbdetails[1], dbdetails[2], dbdetails[4], tablename));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            //todo continue from backing up database
            private String backup(String host, String port, String user, String password, String db, String table) {

                //an "C:/xampp/mysql/bin/mysqldump" ---- location ito han mysqldump

                Process run = null;
                try {
                    run = Runtime.getRuntime().exec(
                            "C:/xampp/mysql/bin/mysqldump --host=" + host + " --port=" + port +
                                    " --user=" + user + " --password=" + password +
                                    " --compact --databases --add-drop-table --complete-insert --extended-insert " +
                                    "--skip-comments --skip-triggers " + db + " --tables " + table);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                InputStream in = run.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                StringBuffer temp = new StringBuffer();
                int count;
                char[] cbuf = new char[40];

                while (true) {
                    try {
                        if (!((count = br.read(cbuf, 0, 40)) != -1)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                temp.append(cbuf, 0, count);

                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return temp.toString();
            }

        });
        audits.setOnMouseClicked(event -> {
            try {
                AdminPanel.getChildren().removeAll();
                AdminPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("shopFiles/audits.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        visitSuppliers.setOnMouseClicked(event -> {
            try {
//                    todo change when created website
                Desktop.getDesktop().browse(new URL(supplierSite).toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
        carwashpanel.setOnAction(event -> {
            AdminPanel.getChildren().removeAll();
            try {
                AdminPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("carwashFiles/carwash.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }


        });

        stockspanel.setOnMousePressed(event -> {

            try {
                AdminPanel.getChildren().removeAll();
                AdminPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("shopFiles/stocks.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });


        employees.setOnMousePressed(event -> {
            try {
                AdminPanel.getChildren().removeAll();
                AdminPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("UserAccountManagementFiles/employees.fxml")))));
            } catch (IOException e) {
                e.printStackTrace();
            }
//            showAlert(Alert.AlertType.INFORMATION, AdminPanel.getScene().getWindow(), "coming soon", "Feature not yet supported");
        });
//        addshop.setOnMousePressed(event -> {
//            try {
//                AdminPanel.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("addshop.fxml")))));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        });
    }


    public MenuItem getMenulogout() {
        return menulogout;
    }

    public void setMenulogout(MenuItem menulogout) {
        this.menulogout = menulogout;
    }

    public Button getAddshop() {
        return addshop;
    }

    public void setAddshop(Button addshop) {
        this.addshop = addshop;
    }

    public MenuItem getDetails() {
        return details;
    }

    public void setDetails(MenuItem details) {
        this.details = details;
    }

    public Menu getBranches() {
        return branches;
    }

    public void setBranches(Menu branches) {
        this.branches = branches;
    }

    public MenuItem getLicense() {
        return license;
    }

    public void setLicense(MenuItem license) {
        this.license = license;
    }

    public MenuItem getFeedback() {
        return feedback;
    }

    public void setFeedback(MenuItem feedback) {
        this.feedback = feedback;
    }

    public Label getClock() {
        return clock;
    }

    public void setClock(Label clock) {
        this.clock = clock;
    }

    public Font getX1() {
        return x1;
    }

    public void setX1(Font x1) {
        this.x1 = x1;
    }

    public Button getEmployees() {
        return employees;
    }

    public void setEmployees(Button employees) {
        this.employees = employees;
    }

    public Button getStockspanel() {
        return stockspanel;
    }

    public void setStockspanel(Button stockspanel) {
        this.stockspanel = stockspanel;
    }

    public Button getCarwashpanel() {
        return carwashpanel;
    }

    public void setCarwashpanel(Button carwashpanel) {
        this.carwashpanel = carwashpanel;
    }

    public Button getVisitSuppliers() {
        return visitSuppliers;
    }

    public void setVisitSuppliers(Button visitSuppliers) {
        this.visitSuppliers = visitSuppliers;
    }

    public Button getBackup() {
        return backup;
    }

    public void setBackup(Button backup) {
        this.backup = backup;
    }

    public Button getAudits() {
        return audits;
    }

    public void setAudits(Button audits) {
        this.audits = audits;
    }

    public AnchorPane getAdminPanel() {
        return AdminPanel;
    }

    public void setAdminPanel(AnchorPane adminPanel) {
        AdminPanel = adminPanel;
    }

}