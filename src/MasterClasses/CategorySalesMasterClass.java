package MasterClasses;

import javafx.beans.property.SimpleStringProperty;

public class CategorySalesMasterClass {
    SimpleStringProperty categorysalesid = new SimpleStringProperty();
    SimpleStringProperty categorysalespayout = new SimpleStringProperty();
    SimpleStringProperty categorysalessalesperday = new SimpleStringProperty();

    SimpleStringProperty categorysalesname = new SimpleStringProperty();

    public String getCategorysalesid() {
        return categorysalesid.get();
    }

    public SimpleStringProperty categorysalesidProperty() {
        return categorysalesid;
    }

    public void setCategorysalesid(String categorysalesid) {
        this.categorysalesid.set(categorysalesid);
    }

    public String getCategorysalespayout() {
        return categorysalespayout.get();
    }

    public SimpleStringProperty categorysalespayoutProperty() {
        return categorysalespayout;
    }

    public void setCategorysalespayout(String categorysalespayout) {
        this.categorysalespayout.set(categorysalespayout);
    }

    public String getCategorysalessalesperday() {
        return categorysalessalesperday.get();
    }

    public SimpleStringProperty categorysalessalesperdayProperty() {
        return categorysalessalesperday;
    }

    public void setCategorysalessalesperday(String categorysalessalesperday) {
        this.categorysalessalesperday.set(categorysalessalesperday);
    }

    public String getCategorysalesname() {
        return categorysalesname.get();
    }

    public SimpleStringProperty categorysalesnameProperty() {
        return categorysalesname;
    }

    public void setCategorysalesname(String categorysalesname) {
        this.categorysalesname.set(categorysalesname);
    }
}
