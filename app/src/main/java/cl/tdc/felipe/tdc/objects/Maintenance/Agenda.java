package cl.tdc.felipe.tdc.objects.Maintenance;

import java.util.ArrayList;

/**
 * Created by Felipes on 08-07-2015.
 */
public class Agenda {
    String code;
    String description;
    String flag;
    String element;
    String operationType;
    ArrayList<Maintenance> maintenanceList;

    public Agenda() {
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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public ArrayList<Maintenance> getMaintenanceList() {
        return maintenanceList;
    }

    public void setMaintenanceList(ArrayList<Maintenance> maintenanceList) {
        this.maintenanceList = maintenanceList;
    }
}
