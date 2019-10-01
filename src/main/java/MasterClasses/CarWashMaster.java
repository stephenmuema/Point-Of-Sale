package MasterClasses;

import javafx.beans.property.SimpleStringProperty;

public class CarWashMaster {
    public SimpleStringProperty Id = new SimpleStringProperty();
    public SimpleStringProperty idnum = new SimpleStringProperty();
    public SimpleStringProperty Name = new SimpleStringProperty();
    public SimpleStringProperty regno = new SimpleStringProperty();
    private SimpleStringProperty contact = new SimpleStringProperty();
    public SimpleStringProperty date = new SimpleStringProperty();
    public SimpleStringProperty status = new SimpleStringProperty();
    public SimpleStringProperty operator = new SimpleStringProperty();
    public SimpleStringProperty cash = new SimpleStringProperty();

    public String getCash() {
        return cash.get();
    }

    public void setCash(String cash) {
        this.cash.set(cash);
    }

    public SimpleStringProperty cashProperty() {
        return cash;
    }

    public String getIdnum() {
        return idnum.get();
    }

    public void setIdnum(String idnum) {
        this.idnum.set(idnum);
    }

    public SimpleStringProperty idnumProperty() {
        return idnum;
    }

    public String getId() {
        return Id.get();
    }

    public void setId(String id) {
        this.Id.set(id);
    }

    public SimpleStringProperty idProperty() {
        return Id;
    }

    public String getName() {
        return Name.get();
    }

    public void setName(String name) {
        this.Name.set(name);
    }

    public SimpleStringProperty nameProperty() {
        return Name;
    }

    public String getRegno() {
        return regno.get();
    }

    public void setRegno(String regno) {
        this.regno.set(regno);
    }

    public SimpleStringProperty regnoProperty() {
        return regno;
    }

    public String getContact() {
        return contact.get();
    }

    public void setContact(String contact) {
        this.contact.set(contact);
    }

    public SimpleStringProperty contactProperty() {
        return contact;
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public SimpleStringProperty dateProperty() {
        return date;
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

    public String getOperator() {
        return operator.get();
    }

    public void setOperator(String operator) {
        this.operator.set(operator);
    }

    public SimpleStringProperty operatorProperty() {
        return operator;
    }


}
