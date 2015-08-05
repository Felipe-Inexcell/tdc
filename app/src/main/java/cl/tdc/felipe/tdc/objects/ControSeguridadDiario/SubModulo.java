package cl.tdc.felipe.tdc.objects.ControSeguridadDiario;

import java.util.ArrayList;

/**
 * Created by felip on 04/08/2015.
 */
public class SubModulo {

    int id;
    String name;
    ArrayList<Elemento> elementos;

    public ArrayList<Elemento> getElementos() {
        return elementos;
    }

    public void setElementos(ArrayList<Elemento> elementos) {
        this.elementos = elementos;
    }

    public SubModulo() {
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
}
