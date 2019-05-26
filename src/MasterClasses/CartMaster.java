package MasterClasses;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class CartMaster {
    private int counter = 0;
    private SimpleIntegerProperty itemCount = new SimpleIntegerProperty();
    private SimpleStringProperty number = new SimpleStringProperty();
    private SimpleIntegerProperty itemId = new SimpleIntegerProperty();
    private SimpleStringProperty itemName = new SimpleStringProperty();
    private SimpleStringProperty itemBarCode = new SimpleStringProperty();
    private SimpleStringProperty itemPrice = new SimpleStringProperty();
    private SimpleStringProperty itemNumber = new SimpleStringProperty();
    private SimpleIntegerProperty itemCumulativeCost = new SimpleIntegerProperty();
    private SimpleStringProperty transactionId = new SimpleStringProperty();
    private SimpleIntegerProperty totalprice = new SimpleIntegerProperty();

    public int getTotalprice() {
        return totalprice.get();
    }

    public SimpleIntegerProperty totalpriceProperty() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice.set(totalprice);
    }

    private ObjectProperty storePic = new SimpleObjectProperty();

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getItemCount() {
        return itemCount.get();
    }

    public void setItemCount(int itemCount) {
        this.itemCount.set(itemCount);
    }

    public SimpleIntegerProperty itemCountProperty() {
        return itemCount;
    }

    public String getNumber() {
        return number.get();
    }

    public void setNumber(String number) {
        this.number.set(number);
    }

    public SimpleStringProperty numberProperty() {
        return number;
    }

    public Object getStorePic() {
        return storePic.get();
    }

    public void setStorePic(Object storePic) {
        this.storePic.set(storePic);
    }

    public ObjectProperty storePicProperty() {
        return storePic;
    }

    public String getTransactionId() {
        return transactionId.get();
    }

    public void setTransactionId(String transactionId) {
        this.transactionId.set(transactionId);
    }

    public SimpleStringProperty transactionIdProperty() {
        return transactionId;
    }

    public int getItemId() {
        return itemId.get();
    }

    public void setItemId(int itemId) {
        counter++;
        this.itemId.set(itemId);
    }

    public SimpleIntegerProperty itemIdProperty() {
        return itemId;
    }

    public String getItemName() {
        return itemName.get();
    }

    public void setItemName(String itemName) {
        this.itemName.set(itemName);
    }

    public SimpleStringProperty itemNameProperty() {
        return itemName;
    }

    public String getItemBarCode() {
        return itemBarCode.get();
    }

    public void setItemBarCode(String itemBarCode) {
        this.itemBarCode.set(itemBarCode);
    }

    public SimpleStringProperty itemBarCodeProperty() {
        return itemBarCode;
    }

    public String getItemPrice() {
        return itemPrice.get();
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice.set(itemPrice);
    }

    public SimpleStringProperty itemPriceProperty() {
        return itemPrice;
    }

    public String getItemNumber() {
        return itemNumber.get();
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber.set(itemNumber);
    }

    public SimpleStringProperty itemNumberProperty() {
        return itemNumber;
    }

    public Integer getItemCumulativeCost() {
        return itemCumulativeCost.get();
    }

    public void setItemCumulativeCost(int itemCumulativeCost) {
        this.itemCumulativeCost.set(itemCumulativeCost);
    }

    public SimpleIntegerProperty itemCumulativeCostProperty() {
        return itemCumulativeCost;
    }


}
