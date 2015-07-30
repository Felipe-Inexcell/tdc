package cl.tdc.felipe.tdc.objects;

import java.util.ArrayList;

/**
 * Created by Felipes on 02-07-2015.
 */
public class FormularioCheck {

    String code;
    String description;
    String maintenanceId;
    String date;
    ArrayList<FormSystem> system;

    public FormularioCheck() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(String maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<FormSystem> getSystem() {
        return system;
    }

    public void setSystem(ArrayList<FormSystem> system) {
        this.system = system;
    }
}
