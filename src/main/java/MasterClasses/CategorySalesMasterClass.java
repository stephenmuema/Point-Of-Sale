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

    public void setCategorysalesid(String categorysalesid) {
        this.categorysalesid.set(categorysalesid);
    }

    public SimpleStringProperty categorysalesidProperty() {
        return categorysalesid;
    }

    public String getCategorysalespayout() {
        return categorysalespayout.get();
    }

    public void setCategorysalespayout(String categorysalespayout) {
        this.categorysalespayout.set(categorysalespayout);
    }

    public SimpleStringProperty categorysalespayoutProperty() {
        return categorysalespayout;
    }

    public String getCategorysalessalesperday() {
        return categorysalessalesperday.get();
    }

    public void setCategorysalessalesperday(String categorysalessalesperday) {
        this.categorysalessalesperday.set(categorysalessalesperday);
    }

    public SimpleStringProperty categorysalessalesperdayProperty() {
        return categorysalessalesperday;
    }

    public String getCategorysalesname() {
        return categorysalesname.get();
    }

    public void setCategorysalesname(String categorysalesname) {
        this.categorysalesname.set(categorysalesname);
    }

    public SimpleStringProperty categorysalesnameProperty() {
        return categorysalesname;
    }
}
