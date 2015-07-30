package cl.tdc.felipe.tdc.objects;

import java.util.ArrayList;

/**
 * Created by Felipes on 02-07-2015.
 */
public class FormSubSystemItem {

    int idItem;
    String nameItem;
    ArrayList<FormSubSystemItemAttribute> attributeList;

    public FormSubSystemItem() {
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public String getNameItem() {
        return nameItem;
    }

    public void setNameItem(String nameItem) {
        this.nameItem = nameItem;
    }

    public ArrayList<FormSubSystemItemAttribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(ArrayList<FormSubSystemItemAttribute> attributeList) {
        this.attributeList = attributeList;
    }
}
