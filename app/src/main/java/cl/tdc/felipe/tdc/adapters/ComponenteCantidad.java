package cl.tdc.felipe.tdc.adapters;

/**
 * Created by Carlos on 18/05/2015.
 */
public class ComponenteCantidad {
    Componente componente;
    int cantidad;

    public ComponenteCantidad(Componente componente, int cantidad) {
        this.componente = componente;
        this.cantidad = cantidad;
    }

    public Componente getComponente() {
        return componente;
    }

    public void setComponente(Componente componente) {
        this.componente = componente;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
