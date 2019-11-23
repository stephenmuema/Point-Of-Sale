package MasterClasses;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class StockAlertsMasterClass {
    //    id,name,date,remaining,rate,category
    private SimpleIntegerProperty id = new SimpleIntegerProperty(1);
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty date = new SimpleStringProperty();
    private SimpleStringProperty remaining = new SimpleStringProperty();
    private SimpleStringProperty rate = new SimpleStringProperty();
    private SimpleStringProperty category = new SimpleStringProperty();

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public SimpleIntegerProperty idProperty() {
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

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public String getRemaining() {
        return remaining.get();
    }

    public void setRemaining(String remaining) {
        this.remaining.set(remaining);
    }

    public SimpleStringProperty remainingProperty() {
        return remaining;
    }

    public String getRate() {
        return rate.get();
    }

    public void setRate(String rate) {
        this.rate.set(rate);
    }

    public SimpleStringProperty rateProperty() {
        return rate;
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
}
