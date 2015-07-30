package cl.tdc.felipe.tdc.objects;

import java.util.ArrayList;

/**
 * Created by Felipes on 02-07-2015.
 */
public class FormSubSystemItemAttribute {
    String nameAttribute;
    ArrayList<FormSubSystemItemAttributeValues> valuesList;

    public FormSubSystemItemAttribute() {
    }

    public String getNameAttribute() {
        return nameAttribute;
    }

    public void setNameAttribute(String nameAttribute) {
        this.nameAttribute = nameAttribute;
    }

    public ArrayList<FormSubSystemItemAttributeValues> getValuesList() {
        return valuesList;
    }

    public void setValuesList(ArrayList<FormSubSystemItemAttributeValues> valuesList) {
        this.valuesList = valuesList;
    }
}
