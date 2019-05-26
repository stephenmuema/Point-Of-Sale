package securityandtime;

import java.util.HashMap;
import java.util.IdentityHashMap;

public interface config {

    String[] des = {"root", "", "jdbc:mysql://127.0.0.1/nanotechsoftwarespos"};
    String host = "localhost";
    String from = "muemasn@outlook.com";
    HashMap<String, Boolean> login = new HashMap<>();
    HashMap<String, String> user = new HashMap<>();
    IdentityHashMap<String, String> key = new IdentityHashMap<>();
    public String localCartDb = "jdbc:sqlite:shoppingCartDb.db";
    HashMap<String, String> cartid = new HashMap<String, String>();
    HashMap<String, Integer> pricegot = new HashMap<String, Integer>();
    String licensepath = "D:\\license.npos";
    HashMap<String, Throwable> throwables = new HashMap<>();
    HashMap<String, String> license = new HashMap<>();
    String site = "https://nanotechsoftwares.co.ke";
    String sitedocs = "https://nanotechsoftwares.co.ke";

    HashMap<String, String> action = new HashMap<>();

}
