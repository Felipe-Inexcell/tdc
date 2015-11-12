package cl.tdc.felipe.tdc.objects.FormularioCierre;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import cl.tdc.felipe.tdc.R;
import cl.tdc.felipe.tdc.extras.Constantes;


public class QUESTION {
    String idQuestion;
    String nameQuestion;
    String nameType;
    String photo;
    String numberPhoto;
    String idType;
    PHOTO foto;
    ArrayList<VALUE> values;
    ArrayList<PHOTO> fotos;

    View view;
    TextView title;
    ArrayList<CheckBox> checkBoxes;
    ArrayList<Button> buttons;

    public QUESTION() {
    }

    public String getAswerIDEN(){
        String Answer = "";

        if(idType.equals(Constantes.RADIO)){
            RadioGroup rg = (RadioGroup) view;
            int id = rg.getCheckedRadioButtonId();
            if(id != -1) {
                RadioButton rb = (RadioButton) rg.findViewById(id);
                Answer = rb.getText().toString();
            }else
                Answer = "Sin selección";
        }
        if(idType.equals(Constantes.CHECK)){
            for(CheckBox c: checkBoxes){
                if(c.isChecked())
                    Answer = c.getText().toString();
                else
                    Answer = "Sin Selección";
            }
        }
        if(idType.equals(Constantes.TEXT) || idType.equals(Constantes.NUM)){
            Answer = ((EditText)view).getText().toString();
        }

        return Answer;
    }

    public String getAswer3G(){
        String Answer = "";

        if(idType.equals(Constantes.RADIO)){
            RadioGroup rg = (RadioGroup) view;
            int id = rg.getCheckedRadioButtonId();
            if(id != -1) {
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

    public View getView(){
         return view;
    }

    public View generateView(Context ctx) {
        Random r = new Random();
        if (idType.equals(Constantes.RADIO)) {
            view = new RadioGroup(ctx);
            view.setId(r.nextInt(999999 - 10000) + 10000);
            for (VALUE v : values) {
                RadioButton b = new RadioButton(ctx);
                b.setId(r.nextInt(999999 - 10000) + 10000);
                b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                b.setText(v.getNameValue());
                ((RadioGroup)view).addView(b);
            }
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            p.leftMargin =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, ctx.getResources().getDisplayMetrics());
            ((RadioGroup) view).setGravity(Gravity.LEFT);
            view.setLayoutParams(p);
            ((RadioGroup) view).setOrientation(LinearLayout.HORIZONTAL);
        }
        if (idType.equals(Constantes.CHECK)) {
            view = new LinearLayout(ctx);
            ((LinearLayout)view).setOrientation(LinearLayout.VERTICAL);
            view.setId(r.nextInt(999999 - 10000) + 10000);
            checkBoxes = new ArrayList<>();
            int count = 0;
            for (VALUE v : values) {
                CheckBox c = new CheckBox(ctx);
                c.setId(r.nextInt(999999 - 10000) + 10000);
                c.setText(v.getNameValue());
                c.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                checkBoxes.add(c);
                if(values.size() >= 3) {
                    LinearLayout tmp = new LinearLayout(ctx);
                    tmp.setOrientation(LinearLayout.HORIZONTAL);
                    if (count < 2) {
                        tmp.addView(c);
                        count++;
                    } else {
                        ((LinearLayout) view).addView(tmp);
                        count = 0;
                    }
                }else{
                    ((LinearLayout) view).addView(c);
                }
            }
        }
        if(idType.equals(Constantes.TEXT) || idType.equals(Constantes.NUM)){
            view = new EditText(ctx);
            view.setId(r.nextInt(999999 - 10000) + 10000);
            ((TextView)view).setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
            view.setBackgroundResource(R.drawable.fondo_edittext);
            view.setPadding(10,5,10,5);
            if(idType.equals(Constantes.NUM)){
                ((TextView)view).setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            if(idType.equals(Constantes.TEXT)){
                ((TextView)view).setLines(4);
                ((TextView)view).setGravity(Gravity.LEFT|Gravity.TOP);
            }

        }
        if(idType.equals(Constantes.PHOTO)){
            LinearLayout.LayoutParams left = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams right = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            left.weight = 3;
            right.weight = 2;
            buttons = new ArrayList<>();
            Button take = new Button(ctx);
            Button show = new Button(ctx);

            take.setText("Tomar Foto");
            take.setId(r.nextInt(999999 - 10000) + 10000);
            take.setLayoutParams(left);
            take.setBackgroundResource(R.drawable.custom_button_blue_left);
            take.setTextColor(Color.WHITE);
            show.setText("Ver");
            show.setId(r.nextInt(999999 - 10000) + 10000);
            show.setLayoutParams(right);
            show.setBackgroundResource(R.drawable.custom_button_blue_right);
            show.setTextColor(Color.WHITE);
            show.setEnabled(false);

            buttons.add(take);
            buttons.add(show);

            view = new LinearLayout(ctx);
            view.setId(r.nextInt(999999 - 10000) + 10000);
            ((LinearLayout)view).setOrientation(LinearLayout.HORIZONTAL);
            ((LinearLayout)view).addView(take);
            ((LinearLayout)view).addView(show);
        }

        return view;
    }
    public TextView getTitle(Context ctx) {
        title = new TextView(ctx);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        p.leftMargin =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, ctx.getResources().getDisplayMetrics());
        title.setLayoutParams(p);
        title.setText(this.getNameQuestion());
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        return title;
    }

    public PHOTO getFoto() {
        return foto;
    }

    public void setFoto(PHOTO foto) {
        this.foto = foto;
    }

    public void addFoto(PHOTO p){
        if(fotos == null)
            fotos = new ArrayList<>();

        fotos.add(p);
    }

    public boolean removeFoto(PHOTO p){
        if(fotos != null)
            return fotos.remove(p);
        else return false;
    }

    public ArrayList<PHOTO> getFotos() {
        return fotos;
    }

    public ArrayList<CheckBox> getCheckBoxes() {
        return checkBoxes;
    }

    public void setCheckBoxes(ArrayList<CheckBox> checkBoxes) {
        this.checkBoxes = checkBoxes;
    }

    public void setFotos(ArrayList<PHOTO> fotos) {
        this.fotos = fotos;
    }

    public ArrayList<Button> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<Button> buttons) {
        this.buttons = buttons;
    }

    public String getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getNameQuestion() {
        return nameQuestion;
    }

    public void setNameQuestion(String nameQuestion) {
        this.nameQuestion = nameQuestion;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNumberPhoto() {
        return numberPhoto;
    }

    public void setNumberPhoto(String numberPhoto) {
        this.numberPhoto = numberPhoto;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public ArrayList<VALUE> getValues() {
        return values;
    }

    public void setValues(ArrayList<VALUE> values) {
        this.values = values;
    }
}
