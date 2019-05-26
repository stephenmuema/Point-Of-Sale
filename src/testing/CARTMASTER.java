package testing;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;

public class CARTMASTER {
    private SimpleListProperty<String> itemName = new SimpleListProperty<>();
    private SimpleListProperty<Integer> itemId = new SimpleListProperty<>();
//    private SimpleListProperty<String> itemName =new SimpleListProperty<>();
//    private SimpleListProperty<String> itemName =new SimpleListProperty<>();
//    private SimpleListProperty<String> itemName =new SimpleListProperty<>();
//    private SimpleListProperty<String> itemName =new SimpleListProperty<>();

    public ObservableList<String> getItemName() {
        return itemName.get();
    }

    public void setItemName(String itemName) {
        this.itemName.add(itemName);
    }

    public SimpleListProperty<String> itemNameProperty() {
        return itemName;
    }

    public ObservableList<Integer> getItemId() {
        return itemId.get();
    }

    public void setItemId(int itemId) {
        this.itemId.add(itemId);
    }

    public SimpleListProperty<Integer> itemIdProperty() {
        return itemId;
    }
}
