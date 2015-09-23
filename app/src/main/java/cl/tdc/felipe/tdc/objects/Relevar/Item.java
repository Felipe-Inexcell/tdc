package cl.tdc.felipe.tdc.objects.Relevar;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import cl.tdc.felipe.tdc.extras.Funciones;

public class Item {
    int nAerial;
    int id;
    String name;
    String type;
    ArrayList<String> values;
    String valor;
    View vista;
    ArrayList<CheckBox> checkBoxes;
    EditText description;
    ArrayList<Item> subItems;
    ArrayList<ArrayList<Item>> vistas = new ArrayList<>();

    public int getnAerial() {
        return nAerial;
    }

    public void setnAerial(int nAerial) {
        this.nAerial = nAerial;
    }

    public ArrayList<ArrayList<Item>> getVistas() {
        return vistas;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public void addArrayListViews (ArrayList<Item> Item){
        vistas.add(Item);
    }

    public ArrayList<Item> getSubItems() {
        return subItems;
    }

    public void setSubItems(ArrayList<Item> subItems) {
        this.subItems = subItems;
    }

    public View getVista() {
        return vista;
    }

    public void setVista(View vista) {
        this.vista = vista;
    }

    public Item() {
    }

    public EditText getDescription() {
        return description;
    }

    public void setDescription(EditText description) {
        this.description = description;
    }

    public String getValor(){
        if(this.type.equals("SELECT")){
            return ((Spinner)this.vista).getSelectedItem().toString();
        }else if(this.type.equals("CHECK")){
            return Funciones.getChecked(this.checkBoxes);
        }else if(this.type.equals("NUM") || this.type.equals("VARCHAR")){
            return ((EditText)this.vista).getText().toString();
        }else if(this.type.equals("COMPLEX")) {
            return this.valor;
        }else
        {
            return "";
        }
    }

    public ArrayList<CheckBox> getCheckBoxes() {
        return checkBoxes;
    }

    public void setCheckBoxes(ArrayList<CheckBox> checkBoxes) {
        this.checkBoxes = checkBoxes;
    }

    public int getId() {
        return id;
    }

    public void setId(String id) {
        if (id.equals(""))
            this.id = -1;
        else this.id = Integer.valueOf(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getValues() {
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }
}
