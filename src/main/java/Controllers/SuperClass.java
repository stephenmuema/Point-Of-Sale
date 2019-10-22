package Controllers;

import java.io.IOException;

import static securityandtime.config.pricegot;

public class SuperClass extends UtilityClass {
    public SuperClass() throws IOException {
    }

    public void completetransaction() {
// todo complete transaction and continue from here
//     unset price variables
        pricegot.clear();
//    print receipt

    }
}
