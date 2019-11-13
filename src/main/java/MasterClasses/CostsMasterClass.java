package MasterClasses;

import javafx.beans.property.SimpleStringProperty;

public class CostsMasterClass {
    private SimpleStringProperty id = new SimpleStringProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty category = new SimpleStringProperty();
    private SimpleStringProperty isactive = new SimpleStringProperty();
    private SimpleStringProperty dateadded = new SimpleStringProperty();
    private SimpleStringProperty amount = new SimpleStringProperty();

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public String getIsactive() {
        return isactive.get();
    }

    public void setIsactive(String isactive) {
        this.isactive.set(isactive);
    }

    public SimpleStringProperty isactiveProperty() {
        return isactive;
    }

    public String getDateadded() {
        return dateadded.get();
    }

    public void setDateadded(String dateadded) {
        this.dateadded.set(dateadded);
    }

    public SimpleStringProperty dateaddedProperty() {
        return dateadded;
    }

    public String getAmount() {
        return amount.get();
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }

    public SimpleStringProperty amountProperty() {
        return amount;
    }
}
