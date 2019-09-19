package securityandtime;

import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public interface config {//host,user,password,des,port
    String environment = "development";
    //    String environment = "production";
    String[] dbdetails = {"127.0.0.1/", "root", "", "jdbc:mysql://", "nanotechsoftwarespos?zeroDateTimeBehavior=convertToNull", "3306"};
    String[] des = {dbdetails[1], dbdetails[2], dbdetails[3] + dbdetails[0] + dbdetails[4]};
    String host = "localhost";
    String from = "muemasnyamai@gmail.com";
    String backupmail = "muemasnyamai@gmail.com";
    HashMap<String, Boolean> login = new HashMap<>();
    HashMap<String, String> user = new HashMap<>();
    String mailPassword = "tpgkhylqyxiypqld";
    IdentityHashMap<String, String> key = new IdentityHashMap<>();
    HashMap<String, String> cartid = new HashMap<String, String>();
    HashMap<String, Integer> pricegot = new HashMap<String, Integer>();
    HashMap<String, Throwable> throwables = new HashMap<>();
    HashMap<String, String> license = new HashMap<>();
    String site = "https://nanotechsoftwares.co.ke";
    String siteLicensing = "https://nanotechsoftwares.co.ke";
    String sitedocs = "https://nanotechsoftwares.co.ke";
    String supplierSite = "http://activationsite/";
    HashMap<String, String> action = new HashMap<>();
    HashMap<String, AnchorPane> panel = new HashMap<>();
    String google = "http://www.google.com";
    String encryptionkey = "0123456789abcdef";
    String initVector = "abcdef9876543210";
    String company = "MEDICA PHARMACY SYSTEM";
    String year = " 2019 ";
    String version = " (V 1.1) ";
    String fileSavePath = "D:\\NANOTECHSOFTWARES\\nanotechPOS\\";
    String licensepath = fileSavePath + "licenses\\license.dll";
    String localCartDb = "jdbc:sqlite:" + fileSavePath + "files\\shoppingLocal.db";
    String aesKey = "26kozQaKwRuNJ24t26kozQaKwRuNJ24t";
    Map<String, Boolean> networkConnectionMap = new HashMap<>();
    File file = new File(fileSavePath + "\\images\\logo.png");
    Image image = new Image(file.toURI().toString());



}
