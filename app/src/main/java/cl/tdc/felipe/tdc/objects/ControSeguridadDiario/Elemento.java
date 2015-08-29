package cl.tdc.felipe.tdc.objects.ControSeguridadDiario;

import android.graphics.Bitmap;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;

import cl.tdc.felipe.tdc.extras.Funciones;

/**
 * Created by felip on 04/08/2015.
 */
public class Elemento {

    int id;
    String name;
    String type;
    ArrayList<String> values;
    Bitmap firma;
    ArrayList<CheckBox> checkBoxes;
    String FileName;

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getValue(){
        if(this.type.compareTo("TEXT")==0){
            return this.editText.getText().toString();
        }
        if(this.type.compareTo("CHECK")==0){
            return getCheckboxSelected();
        }
        if(this.type.compareTo("FIRMA")==0){
            return FileName;
        }

        return "";
    }

    private String getCheckboxSelected(){
        for(CheckBox c: this.checkBoxes){
            if(c.isChecked()){
                return c.getText().toString();
            }
        }
        return "";
    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    EditText editText;

    public ArrayList<CheckBox> getCheckBoxes() {
        return checkBoxes;
    }

    public void setCheckBoxes(ArrayList<CheckBox> checkBoxes) {
        this.checkBoxes = checkBoxes;
    }

    public Bitmap getFirma() {
        return firma;
    }

    public void setFirma(Bitmap firma) {
        this.firma = firma;
    }

    public Elemento() {
    }

    public int getId() {
        return id;
    }

    public void setId(String id) {
        if (id.compareTo("") == 0)
            this.id = -1;
        else
            this.id = Integer.parseInt(id);
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
