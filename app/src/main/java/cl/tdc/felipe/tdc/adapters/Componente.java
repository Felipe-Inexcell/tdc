package cl.tdc.felipe.tdc.adapters;

public class Componente {
    int storeId;
    String store;
    int componentId;
    String tipo;
    String nombre;
    String codigo;

    public Componente (int componentId, String nombre, String tipo, String codigo, int idBodega, String bodega){
        this.store = bodega;
        this.storeId = idBodega;
        this.componentId = componentId;
        this. nombre = nombre;
        this.tipo = tipo;
        this.codigo = codigo;
    }

    public int getStoreId() {
        return storeId;
    }

    public String getStore() {
        return store;
    }

    public int getComponentId() {
        return componentId;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodigo() {
        return codigo;
    }
}
