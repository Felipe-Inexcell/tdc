package cl.tdc.felipe.tdc.objects;

import java.util.ArrayList;

/**
 * Created by Felipes on 02-07-2015.
 */
public class FormSubSystem {

    int idSubSystem;
    String nameSubSystem;
    ArrayList<FormSubSystemItem> itemList;

    public FormSubSystem() {
    }

    public int getIdSubSystem() {
        return idSubSystem;
    }

    public void setIdSubSystem(int idSubSystem) {
        this.idSubSystem = idSubSystem;
    }

    public String getNameSubSystem() {
        return nameSubSystem;
    }

    public void setNameSubSystem(String nameSubSystem) {
        this.nameSubSystem = nameSubSystem;
    }

    public ArrayList<FormSubSystemItem> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<FormSubSystemItem> itemList) {
        this.itemList = itemList;
    }
}
