package Controllers.ShopControllers;

import java.sql.Timestamp;

import static securityandtime.config.cartid;

public class CartIdGenerator {
    public String idcart;

    public CartIdGenerator() {
        String x = String.valueOf(new Timestamp(System.currentTimeMillis()));
        idcart = cartid.put("cartKey", x);
        System.out.println(cartid.isEmpty());
    }

    public String getIdcart() {
        return idcart;
    }

    public void setIdcart(String idcart) {
        this.idcart = idcart;
    }
}
