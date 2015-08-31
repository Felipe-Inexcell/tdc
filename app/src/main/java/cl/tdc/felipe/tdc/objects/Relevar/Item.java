package cl.tdc.felipe.tdc.objects.Relevar;

import java.util.ArrayList;

/**
 * Created by felip on 31/08/2015.
 */
public class Item {
    int id;
    String name;
    String type;
    ArrayList<String> values;

    public Item() {
    }

    public int getId() {
        return id;
    }

    public void setId(String id) {
        if (id.equals(""))
            this.id = -1;
        else this.id = Integer.valueOf(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getValues() {
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }
}
