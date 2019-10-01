package MasterClasses;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Storemaster {
    private SimpleStringProperty storeId = new SimpleStringProperty();
    private SimpleStringProperty storeName = new SimpleStringProperty();
    private SimpleStringProperty storeLocation = new SimpleStringProperty();
    private SimpleStringProperty employeeNumber = new SimpleStringProperty();
    private ObjectProperty storePic = new SimpleObjectProperty();
    private SimpleStringProperty storeDescription = new SimpleStringProperty();
    private SimpleStringProperty dateAdded = new SimpleStringProperty();

    public String getStoreId() {
        return storeId.get();
    }

    public void setStoreId(String storeId) {
        this.storeId.set(storeId);
    }

    public Object getStorePic() {
        return storePic.get();
    }

    public String getStoreName() {
        return storeName.get();
    }

    public void setStoreName(String storeName) {
        this.storeName.set(storeName);
    }

    public String getStoreLocation() {
        return storeLocation.get();
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation.set(storeLocation);
    }

    public String getStoreDescription() {
        return storeDescription.get();
    }

    public void setStoreDescription(String storeDescription) {
        this.storeDescription.set(storeDescription);
    }

    public String getDateAdded() {
        return dateAdded.get();
    }

    public String getEmployeeNumber() {
        return employeeNumber.get();
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber.set(employeeNumber);
    }

}
