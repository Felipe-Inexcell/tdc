package cl.tdc.felipe.tdc.objects.MaintChecklist;

import java.util.ArrayList;

public class Modulo {

    int id;
    String name;
    ArrayList<SubModulo> subModulos;

    public Modulo() {
    }

    public int getId() {
        return id;
    }

    public void setId(String id) {
        if (id.compareTo("") == 0)
            this.id = -1;
        else
            this.id = Integer.parseInt(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<SubModulo> getSubModulos() {
        return subModulos;
    }

    public void setSubModulos(ArrayList<SubModulo> subModulos) {
        this.subModulos = subModulos;
    }
}
