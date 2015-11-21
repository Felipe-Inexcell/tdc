package cl.tdc.felipe.tdc.objects.FormularioCierre;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
    Button button;
    EditText editText;
    ArrayList<Button> buttons;
    PHOTO photo;
    View view;
    ArrayList<CheckBox> checkBoxes;

    public ITEM() {
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public String getAnswer3G(){
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
    public String getAnswerFaena(){
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
        if(idType.equals(Constantes.HOUR)){
            Answer = editText.getText().toString();
        }

        if(idType.equals(Constantes.PHOTO)){
            if(photo != null) {

                File fot = new File(photo.getNamePhoto());
                if(fot.exists()) {
                    Answer = fot.getName();
                }else Answer = "";
            }
            else{
                Answer = "";
            }
        }

        return Answer;
    }


    public View generateView(final Context ctx) {
        if (idType.equals(Constantes.ADD)) {
            view = new TextView(ctx);
            ((TextView)view).setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            /*LinearLayout.LayoutParams buttonParam = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams numParam = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);

            buttonParam.weight = 3;
            numParam.weight = 2;

            view = new  LinearLayout(ctx);
            button = new Button(ctx);
            button.setText("Generar");
            button.setLayoutParams(buttonParam);

            editText = new EditText(ctx);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setLayoutParams(numParam);

            ((LinearLayout)view).setOrientation(LinearLayout.HORIZONTAL);
            ((LinearLayout) view).addView(editText);
            ((LinearLayout) view).addView(button);*/


        }
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
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                param.weight=1;
                CheckBox c = new CheckBox(ctx);
                c.setText(v.getNameValue());
                c.setLayoutParams(param);
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
            view.setBackgroundResource(R.drawable.fondo_edittext);
            if(idType.equals(Constantes.NUM)){
                ((TextView)view).setInputType(InputType.TYPE_CLASS_NUMBER);
            }
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

        if(idType.equals(Constantes.HOUR)){
            final Calendar myCalendar = Calendar.getInstance();
            final EditText fecha = new EditText(ctx);
            fecha.setBackgroundResource(R.drawable.fondo_edittext);
            fecha.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            fecha.setEnabled(false);
            fecha.setGravity(Gravity.CENTER_VERTICAL);

            ImageButton pick = new ImageButton(ctx);
            pick.setBackgroundResource(R.drawable.button_gray);
            pick.setImageResource(R.drawable.ic_reloj_white);

            LinearLayout.LayoutParams pButton = new LinearLayout.LayoutParams(0,(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, ctx.getResources().getDisplayMetrics()));
            LinearLayout.LayoutParams pText = new LinearLayout.LayoutParams(0, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, ctx.getResources().getDisplayMetrics()));
            pButton.weight=1;
            pText.weight=4;
            pText.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, ctx.getResources().getDisplayMetrics());


            final TimePickerDialog.OnTimeSetListener date = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    myCalendar.set(Calendar.HOUR_OF_DAY, hour);
                    myCalendar.set(Calendar.MINUTE, minute);

                    String myFormat = "HH:mm"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                    fecha.setText(sdf.format(myCalendar.getTime()));
                }
            };

            pick.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new TimePickerDialog(ctx, date, myCalendar
                            .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
                }
            });

            view = new LinearLayout(ctx);
            ((LinearLayout)view).setOrientation(LinearLayout.HORIZONTAL);
            ((LinearLayout)view).setGravity(Gravity.CENTER_VERTICAL);

            fecha.setLayoutParams(pText);
            pick.setLayoutParams(pButton);

            editText = fecha;

            ((LinearLayout)view).addView(pick);
            ((LinearLayout)view).addView(fecha);
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

    public ArrayList<Button> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<Button> buttons) {
        this.buttons = buttons;
    }

    public PHOTO getPhoto() {
        return photo;
    }

    public void setPhoto(PHOTO photo) {
        this.photo = photo;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
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
