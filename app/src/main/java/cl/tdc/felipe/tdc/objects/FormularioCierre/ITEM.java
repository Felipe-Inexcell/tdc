package cl.tdc.felipe.tdc.objects.FormularioCierre;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cl.tdc.felipe.tdc.R;
import cl.tdc.felipe.tdc.extras.Constantes;

public class ITEM {
    String idItem;
    String idType;
    String nameType;
    String nameItem;
    String answer;
    ArrayList<QUESTION> questions;
    ArrayList<VALUE> values;
    ArrayList<SET> setArrayList;

    ArrayList<ArrayList<SET>> setlistArrayList;

    TextView title;

    View view;
    ArrayList<CheckBox> checkBoxes;

    public ITEM() {
    }

    public String getAnswer3G(){
        String Answer = "";
        if(idType.equals(Constantes.RADIO)){
            RadioGroup rg = (RadioGroup) view;
            int id = rg.getCheckedRadioButtonId();
            if(id != -100) {
                RadioButton rb = (RadioButton) rg.findViewById(id);
                Answer = rb.getText().toString();
            }else
                Answer = "Sin selección";
        }
        if(idType.equals(Constantes.CHECK)){
            int count= 0;
            for(CheckBox c: checkBoxes){

                if(c.isChecked()) {
                    if(checkBoxes.indexOf(c)==0){
                        Answer += c.getText().toString();
                    }else{
                        Answer += ";"+c.getText().toString();
                    }
                    count++;
                }
                if(count==0)
                    Answer = "Sin Selección";
            }
        }
        if(idType.equals(Constantes.TEXT) || idType.equals(Constantes.NUM)){
            Answer = ((EditText)view).getText().toString();
        }

        return Answer;
    }


    public View generateView(Context ctx) {
        if (idType.equals(Constantes.RADIO)) {
            view = new RadioGroup(ctx);
            for (VALUE v : values) {
                RadioButton b = new RadioButton(ctx);
                b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                b.setText(v.getNameValue());
                ((RadioGroup)view).addView(b);
            }
            ((RadioGroup) view).setGravity(Gravity.CENTER_HORIZONTAL);
            ((RadioGroup) view).setOrientation(LinearLayout.HORIZONTAL);
        }
        if (idType.equals(Constantes.CHECK) || idType.equals(Constantes.TABLE) || idType.equals(Constantes.CHECK_PHOTO)) {
            view = new LinearLayout(ctx);
            ((LinearLayout) view).setOrientation(LinearLayout.VERTICAL);
            checkBoxes = new ArrayList<>();
            int count = 0;

            LinearLayout tmp ;
            tmp = new LinearLayout(ctx);
            tmp.setOrientation(LinearLayout.HORIZONTAL);
            for (VALUE v : values) {
                CheckBox c = new CheckBox(ctx);
                c.setText(v.getNameValue());
                c.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                checkBoxes.add(c);
                if(count == 0){
                    tmp.addView(c);
                    count++;
                }else if(count == 1){
                    tmp.addView(c);
                    ((LinearLayout) view).addView(tmp);
                    tmp = new LinearLayout(ctx);
                    tmp.setOrientation(LinearLayout.HORIZONTAL);
                    count = 0;
                }
            }
        }
        if(idType.equals(Constantes.TEXT) || idType.equals(Constantes.NUM)){
            view = new EditText(ctx);
            ((TextView)view).setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
            if(idType.equals(Constantes.NUM)){
                ((TextView)view).setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        }


        return view;
    }

    public TextView getTitle(Context ctx) {
        title = new TextView(ctx);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        p.leftMargin =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, ctx.getResources().getDisplayMetrics());
        title.setLayoutParams(p);
        title.setText(this.getNameItem());
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        return title;
    }

    public void addListSet(ArrayList<SET> sets){
        if(this.setlistArrayList == null)
            setlistArrayList = new ArrayList<>();
        setlistArrayList.add(sets);
    }

    public ArrayList<CheckBox> getCheckBoxes() {
        return checkBoxes;
    }

    public void setCheckBoxes(ArrayList<CheckBox> checkBoxes) {
        this.checkBoxes = checkBoxes;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public ArrayList<ArrayList<SET>> getSetlistArrayList() {
        return setlistArrayList;
    }

    public void setSetlistArrayList(ArrayList<ArrayList<SET>> setlistArrayList) {
        this.setlistArrayList = setlistArrayList;
    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public String getNameItem() {
        return nameItem;
    }

    public void setNameItem(String nameItem) {
        this.nameItem = nameItem;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public ArrayList<QUESTION> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<QUESTION> questions) {
        this.questions = questions;
    }

    public ArrayList<VALUE> getValues() {
        return values;
    }

    public void setValues(ArrayList<VALUE> values) {
        this.values = values;
    }

    public ArrayList<SET> getSetArrayList() {
        return setArrayList;
    }

    public void setSetArrayList(ArrayList<SET> setArrayList) {
        this.setArrayList = setArrayList;
    }
}
