package cl.tdc.felipe.tdc.objects.Seguimiento;

import java.util.ArrayList;

/**
 * Created by felip on 02/08/2015.
 */
public class Dia {

    public Dia() {
    }

    int dayNumber;
    String programmedAdvance;
    String realAdvance;
    String date;
    String descriptionDay;
    ArrayList<Actividad> actividades;

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(String dayNumber) {
        this.dayNumber = Integer.valueOf(dayNumber);
    }

    public String getProgrammedAdvance() {
        return programmedAdvance;
    }

    public void setProgrammedAdvance(String programmedAdvance) {
        this.programmedAdvance = programmedAdvance;
    }

    public String getRealAdvance() {
        return realAdvance;
    }

    public void setRealAdvance(String realAdvance) {
        if (realAdvance.compareTo("") == 0) this.realAdvance = "0";
        else this.realAdvance = realAdvance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescriptionDay() {
        return descriptionDay;
    }

    public void setDescriptionDay(String descriptionDay) {
        this.descriptionDay = descriptionDay;
    }

    public ArrayList<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(ArrayList<Actividad> actividades) {
        this.actividades = actividades;
    }
}
