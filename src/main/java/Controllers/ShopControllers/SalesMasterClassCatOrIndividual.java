package Controllers.ShopControllers;

import javafx.beans.property.SimpleStringProperty;

public class SalesMasterClassCatOrIndividual {
    private SimpleStringProperty id = new SimpleStringProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty payout = new SimpleStringProperty();
    private SimpleStringProperty rateOfSales = new SimpleStringProperty();
    private SimpleStringProperty remaining = new SimpleStringProperty();

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

    public String getPayout() {
        return payout.get();
    }

    public void setPayout(String payout) {
        this.payout.set(payout);
    }

    public SimpleStringProperty payoutProperty() {
        return payout;
    }

    public String getRateOfSales() {
        return rateOfSales.get();
    }

    public void setRateOfSales(String rateOfSales) {
        this.rateOfSales.set(rateOfSales);
    }

    public SimpleStringProperty rateOfSalesProperty() {
        return rateOfSales;
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
}
