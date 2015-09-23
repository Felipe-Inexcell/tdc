package cl.tdc.felipe.tdc.objects.Seguimiento;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Dia {

    public Dia() {
        this.modify = false;
    }

    boolean modify;
    int dayNumber;
    String programmedAdvance;
    String realAdvance;
    String advanceToday;
    String date;
    String descriptionDay;
    ArrayList<Actividad> actividades;
    LinearLayout lContenido;
    ArrayList<CheckBox> checkBoxes;
    EditText fecha, observacion, avance;

    public LinearLayout getlContenido() {
        return lContenido;
    }

    public void setlContenido(LinearLayout lContenido) {
        this.lContenido = lContenido;
    }

    public boolean isModify() {
        return modify;
    }

    public void setModify(boolean modify) {
        this.modify = modify;
    }

    public String getAdvanceToday() {
        return advanceToday;
    }

    public void setAdvanceToday(String advanceToday) {
        this.advanceToday = advanceToday;
    }

    public ArrayList<CheckBox> getCheckBoxes() {
        return checkBoxes;
    }

    public void setCheckBoxes(ArrayList<CheckBox> checkBoxes) {
        this.checkBoxes = checkBoxes;
    }

    public EditText getFecha() {
        return fecha;
    }

    public void setFecha(EditText fecha) {
        this.fecha = fecha;
    }

    public EditText getObservacion() {
        return observacion;
    }

    public void setObservacion(EditText observacion) {
        this.observacion = observacion;
    }

    public EditText getAvance() {
        return avance;
    }

    public void setAvance(EditText avance) {
        this.avance = avance;
    }

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
