package MasterClasses;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class StockMaster {
    public ObjectProperty itemPic = new SimpleObjectProperty();
    public SimpleStringProperty amount = new SimpleStringProperty();
    private SimpleIntegerProperty Id = new SimpleIntegerProperty();
    private ObjectProperty Pic = new SimpleObjectProperty();
    private SimpleStringProperty Name = new SimpleStringProperty();
    private SimpleStringProperty barcode = new SimpleStringProperty();
    private SimpleStringProperty price = new SimpleStringProperty();
    private SimpleStringProperty dateAdded = new SimpleStringProperty();
    private SimpleStringProperty category = new SimpleStringProperty();
    private SimpleStringProperty quantity = new SimpleStringProperty();

    public Object getItemPic() {
        return itemPic.get();
    }

    public void setItemPic(Object itemPic) {
        this.itemPic.set(itemPic);
    }

    public ObjectProperty itemPicProperty() {
        return itemPic;
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

    public String getQuantity() {
        return quantity.get();
    }

    public void setQuantity(String quantity) {
        this.quantity.set(quantity);
    }

    public SimpleStringProperty quantityProperty() {
        return quantity;
    }

    public Integer getId() {
        return Id.get();
    }

    public void setId(Integer id) {
        this.Id.set(id);
    }

    public SimpleIntegerProperty idProperty() {
        return Id;
    }

    public Object getPic() {
        return Pic.get();
    }

    public void setPic(Object pic) {
        this.Pic.set(pic);
    }

    public ObjectProperty picProperty() {
        return Pic;
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

    public String getBarcode() {
        return barcode.get();
    }

    public void setBarcode(String barcode) {
        this.barcode.set(barcode);
    }

    public SimpleStringProperty barcodeProperty() {
        return barcode;
    }

    public String getPrice() {
        return price.get();
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public SimpleStringProperty priceProperty() {
        return price;
    }

    public String getDateAdded() {
        return dateAdded.get();
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded.set(dateAdded);
    }

    public SimpleStringProperty dateAddedProperty() {
        return dateAdded;
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
