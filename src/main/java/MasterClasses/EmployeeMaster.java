package MasterClasses;

import javafx.beans.property.SimpleStringProperty;

public class EmployeeMaster {
    public SimpleStringProperty Id = new SimpleStringProperty();
    public SimpleStringProperty Name = new SimpleStringProperty();
    public SimpleStringProperty storeLocation = new SimpleStringProperty();
    public SimpleStringProperty email = new SimpleStringProperty();
    public SimpleStringProperty dateAdded = new SimpleStringProperty();
    public SimpleStringProperty number = new SimpleStringProperty();

    public String getDateAdded() {
        return dateAdded.get();
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded.set(dateAdded);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getId() {
        return Id.get();
    }

    public void setId(String id) {
        this.Id.set(id);
    }

    public String getName() {
        return Name.get();
    }

    public void setName(String name) {
        this.Name.set(name);
    }

    public String getStoreLocation() {
        return storeLocation.get();
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation.set(storeLocation);
    }

    public String getNumber() {
        return number.get();
    }

    public void setNumber(String number) {
        this.number.set(number);
    }
}
