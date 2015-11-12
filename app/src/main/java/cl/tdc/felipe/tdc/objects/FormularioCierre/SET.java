package cl.tdc.felipe.tdc.objects.FormularioCierre;

import android.content.Context;
import android.util.TypedValue;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by felip on 05/11/2015.
 */
public class SET {
    String idSet;
    String nameSet;
    String valueSet;
    ArrayList<QUESTION> questions;
    ArrayList<VALUE> values;

    TextView title;

    public SET() {
    }

    public TextView getTitle(Context ctx) {
        title = new TextView(ctx);
        title.setText(this.getNameSet());
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        return title;
    }

    public String getIdSet() {
        return idSet;
    }

    public void setIdSet(String idSet) {
        this.idSet = idSet;
    }

    public String getNameSet() {
        return nameSet;
    }

    public void setNameSet(String nameSet) {
        this.nameSet = nameSet;
    }

    public String getValueSet() {
        return valueSet;
    }

    public void setValueSet(String valueSet) {
        this.valueSet = valueSet;
    }

    public ArrayList<QUESTION> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<QUESTION> questions) {
        this.questions = questions;
    }
}
