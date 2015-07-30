package cl.tdc.felipe.tdc.objects;

import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felipes on 02-07-2015.
 */
public class FormSubSystemItemAttributeValues {
    String typeValue;
    ArrayList<String> valueState;
    List<CheckBox> checkBoxes;
    EditText editText;

    public FormSubSystemItemAttributeValues() {
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public ArrayList<String> getValueState() {
        return valueState;
    }

    public void setValueState(ArrayList<String> valueState) {
        this.valueState = valueState;
    }

    public List<CheckBox> getCheckBoxes() {
        return checkBoxes;
    }

    public void setCheckBoxes(List<CheckBox> checkBoxes) {
        this.editText = null;
        this.checkBoxes = checkBoxes;
    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.checkBoxes = null;
        this.editText = editText;
    }
}
