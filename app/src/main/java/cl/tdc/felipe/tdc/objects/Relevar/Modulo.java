package cl.tdc.felipe.tdc.objects.Relevar;

import java.util.ArrayList;

/**
 * Created by felip on 31/08/2015.
 */
public class Modulo {
    int id;
    String name;
    ArrayList<Item> items;
    ArrayList<Modulo> subModulo;

    public ArrayList<Modulo> getSubModulo() {
        return subModulo;
    }

    public void setSubModulo(ArrayList<Modulo> subModulo) {
        this.subModulo = subModulo;
    }

    public Modulo() {
    }

    public int getId() {
        return id;
    }

    public void setId(String id) {
        if (id.equals("")) this.id = -1;
        else this.id = Integer.valueOf(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}
