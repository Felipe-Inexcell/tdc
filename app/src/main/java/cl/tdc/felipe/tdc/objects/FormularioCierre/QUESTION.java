package cl.tdc.felipe.tdc.objects.FormularioCierre;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.view.GravityCompat;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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
    ArrayList<EditText> editTexts;

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
                Answer = "";
        }
        if(idType.equals(Constantes.CHECK)){
            for(CheckBox c: checkBoxes){
                if(c.isChecked())
                    Answer = c.getText().toString();
                else
                    Answer = "";
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
                Answer = "";
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
                    Answer = "";
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

    public View generateView(final Context ctx) {
        if(idType.equals(Constantes.DIV)){
            view = new LinearLayout(ctx);
            ((LinearLayout)view).setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams pTEXT = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams pSEPARADOR = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            pTEXT.weight= 4;
            pSEPARADOR.weight = 1;
            pSEPARADOR.setMargins(20,4,0,4);

            EditText left = new EditText(ctx);
            left.setLayoutParams(pTEXT);
            left.setBackgroundResource(R.drawable.fondo_edittext);
            left.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

            EditText right = new EditText(ctx);
            right.setLayoutParams(pTEXT);
            right.setBackgroundResource(R.drawable.fondo_edittext);
            right.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

            TextView separador = new TextView(ctx);
            separador.setLayoutParams(pSEPARADOR);

            left.setInputType(InputType.TYPE_CLASS_NUMBER);
            right.setInputType(InputType.TYPE_CLASS_NUMBER);

            separador.setText("/");
            separador.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            editTexts = new ArrayList<>();
            editTexts.add(left);
            editTexts.add(right);

            ((LinearLayout)view).addView(left);
            ((LinearLayout)view).addView(separador);
            ((LinearLayout)view).addView(right);
        }
        if(idType.equals(Constantes.DATE)){
            final Calendar myCalendar = Calendar.getInstance();
            final EditText fecha = new EditText(ctx);
            fecha.setBackgroundResource(R.drawable.fondo_edittext);
            fecha.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            fecha.setEnabled(false);

            ImageButton pick = new ImageButton(ctx);
            pick.setBackgroundResource(R.drawable.button_gray);
            pick.setImageResource(R.drawable.ic_calendarwhite);

            LinearLayout.LayoutParams pButton = new LinearLayout.LayoutParams(0,(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, ctx.getResources().getDisplayMetrics()));
            LinearLayout.LayoutParams pText = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            pButton.weight=1;
            pText.weight=4;
            pText.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, ctx.getResources().getDisplayMetrics());

            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker picker, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String myFormat = "yyyy/MM/dd"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                    fecha.setText(sdf.format(myCalendar.getTime()));
                }

            };

            pick.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(ctx, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            view = new LinearLayout(ctx);
            ((LinearLayout)view).setOrientation(LinearLayout.HORIZONTAL);
            ((LinearLayout)view).setGravity(Gravity.CENTER_VERTICAL);

            fecha.setLayoutParams(pText);
            pick.setLayoutParams(pButton);

            editTexts = new ArrayList<>();
            editTexts.add(fecha);

            ((LinearLayout)view).addView(pick);
            ((LinearLayout)view).addView(fecha);

        }
        if (idType.equals(Constantes.RADIO)) {
            view = new RadioGroup(ctx);
            for (VALUE v : values) {
                RadioButton b = new RadioButton(ctx);
                b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                b.setText(v.getNameValue());
                ((RadioGroup)view).addView(b);
            }
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            p.leftMargin =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, ctx.getResources().getDisplayMetrics());
            ((RadioGroup) view).setGravity(Gravity.LEFT);
            view.setLayoutParams(p);
            ((RadioGroup) view).setOrientation(LinearLayout.HORIZONTAL);
            if(values.size() > 3)
                ((RadioGroup) view).setOrientation(LinearLayout.VERTICAL);
        }
        if (idType.equals(Constantes.CHECK)) {
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
                    if(checkBoxes.size() == values.size()){
                        ((LinearLayout) view).addView(tmp);
                    }
                }else if(count == 1){
                    tmp.addView(c);
                    ((LinearLayout) view).addView(tmp);
                    tmp = new LinearLayout(ctx);
                    tmp.setOrientation(LinearLayout.HORIZONTAL);
                    count = 0;
                }
            }
            /*view = new LinearLayout(ctx);
            ((LinearLayout)view).setOrientation(LinearLayout.VERTICAL);
            checkBoxes = new ArrayList<>();
            int count = 0;
            for (VALUE v : values) {
                CheckBox c = new CheckBox(ctx);
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
            }*/
        }
        if(idType.equals(Constantes.TEXT) || idType.equals(Constantes.NUM)){
            view = new EditText(ctx);
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
            take.setLayoutParams(left);
            take.setBackgroundResource(R.drawable.custom_button_blue_left);
            take.setTextColor(Color.WHITE);
            show.setText("Ver");
            show.setLayoutParams(right);
            show.setBackgroundResource(R.drawable.custom_button_blue_right);
            show.setTextColor(Color.WHITE);
            show.setEnabled(false);

            buttons.add(take);
            buttons.add(show);

            view = new LinearLayout(ctx);
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

    public ArrayList<EditText> getEditTexts() {
        return editTexts;
    }

    public void setEditTexts(ArrayList<EditText> editTexts) {
        this.editTexts = editTexts;
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
