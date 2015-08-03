package cl.tdc.felipe.tdc.objects.Seguimiento;

/**
 * Created by felip on 02/08/2015.
 */
public class Proyecto {
    public Proyecto() {
    }

    public Proyecto(String all) {
        String[] info = all.split(";");

        setId(info[0]);
        setNombre(info[1]);
        setFecha_inicio(info[2]);
        setFecha_final(info[3]);
        setDia(info[4]);
        setAtrasado(info[5]);
        setAvance_programado(info[6]);
        setAvance_real(info[7]);
    }

    @Override
    public String toString() {
        return id + ";" + nombre + ";" + fecha_inicio + ";" + fecha_final + ";" + dia + ";" + atrasado + ";" + avance_programado + ";" + avance_real;
    }

    int id;
    String nombre;
    String fecha_inicio;
    String fecha_final;
    int dia;
    int atrasado;
    String avance_programado;
    String avance_real;

    public int getId() {
        return id;
    }

    public void setId(String id) {
        this.id = Integer.parseInt(id);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getFecha_final() {
        return fecha_final;
    }

    public void setFecha_final(String fecha_final) {
        this.fecha_final = fecha_final;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = Integer.parseInt(dia);
    }

    public int getAtrasado() {
        return atrasado;
    }

    public void setAtrasado(String atrasado) {
        this.atrasado = Integer.parseInt(atrasado);
    }

    public String getAvance_programado() {
        return avance_programado;
    }

    public void setAvance_programado(String avance_programado) {
        this.avance_programado = avance_programado;
    }

    public String getAvance_real() {
        return avance_real;
    }

    public void setAvance_real(String avance_real) {
        if (avance_real.compareTo("") == 0)
            this.avance_real = "0";
        else
            this.avance_real = avance_real;
    }
}
