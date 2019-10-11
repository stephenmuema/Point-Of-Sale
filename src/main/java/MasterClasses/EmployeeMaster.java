package MasterClasses;

import javafx.beans.property.SimpleStringProperty;

public class EmployeeMaster {
    public SimpleStringProperty Name = new SimpleStringProperty();
    public SimpleStringProperty email = new SimpleStringProperty();
    public SimpleStringProperty number = new SimpleStringProperty();
    public SimpleStringProperty status = new SimpleStringProperty();
    private SimpleStringProperty Id = new SimpleStringProperty();
    private SimpleStringProperty storeLocation = new SimpleStringProperty();
    private SimpleStringProperty dateAdded = new SimpleStringProperty();
    private SimpleStringProperty activated = new SimpleStringProperty();

    public SimpleStringProperty idProperty() {
        return Id;
    }

    public SimpleStringProperty nameProperty() {
        return Name;
    }

    public SimpleStringProperty storeLocationProperty() {
        return storeLocation;
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public SimpleStringProperty dateAddedProperty() {
        return dateAdded;
    }

    public SimpleStringProperty numberProperty() {
        return number;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

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

    public String getActivated() {
        return activated.get();
    }

    public void setActivated(String activated) {
        this.activated.set(activated);
    }

    public SimpleStringProperty activatedProperty() {
        return activated;
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
