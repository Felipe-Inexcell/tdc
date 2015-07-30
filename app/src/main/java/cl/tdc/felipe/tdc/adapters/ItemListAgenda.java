package cl.tdc.felipe.tdc.adapters;

/**
 * Created by Felipe on 14/02/2015.
 */
public class ItemListAgenda {

    private int urgencia; //1 urgente 0 tranquilo
    private String when;
    private String where;
    private String what;

    public ItemListAgenda(int urgencia, String when, String where, String what) {
        this.urgencia = urgencia;
        this.when = when;
        this.where = where;
        this.what = what;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public int getUrgencia() {
        return urgencia;
    }

    public void setUrgencia(int urgencia) {
        this.urgencia = urgencia;
    }



}
