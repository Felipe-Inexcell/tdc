package cl.tdc.felipe.tdc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import cl.tdc.felipe.tdc.extras.Constantes;
import cl.tdc.felipe.tdc.extras.Funciones;
import cl.tdc.felipe.tdc.objects.FormularioCierre.AREA;
import cl.tdc.felipe.tdc.objects.FormularioCierre.ITEM;
import cl.tdc.felipe.tdc.objects.FormularioCierre.PHOTO;
import cl.tdc.felipe.tdc.objects.FormularioCierre.QUESTION;
import cl.tdc.felipe.tdc.objects.FormularioCierre.SET;
import cl.tdc.felipe.tdc.objects.FormularioCierre.SYSTEM;
import cl.tdc.felipe.tdc.objects.FormularioCierre.VALUE;
import cl.tdc.felipe.tdc.objects.Seguimiento.ImagenDia;
import cl.tdc.felipe.tdc.preferences.FormCierreReg;
import cl.tdc.felipe.tdc.webservice.SoapRequestTDC;
import cl.tdc.felipe.tdc.webservice.XMLParser;
import cl.tdc.felipe.tdc.webservice.XMLParserTDC;
import cl.tdc.felipe.tdc.webservice.dummy;

public class ActividadCierreFormActivity extends Activity {
    private static int TAKE_PICTURE = 1;
    private static int TAKE_PICTURES = 2;
    FormCierreReg REG;
    private static String TITLE;
    String QUERY, IDMAIN;
    TextView PAGETITLE;
    public static Activity actividad;
    Context mContext;
    LinearLayout CONTENIDO;

    private String name;
    private String imgName;
    PHOTO photoTMP;
    QUESTION questionTMP;
    Button buttonTMP;
    ArrayList<SYSTEM> SYSTEMS;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cierre_actividad_form);
        actividad = this;
        mContext = this;
        CONTENIDO = (LinearLayout) this.findViewById(R.id.contenido);
        TITLE = getIntent().getStringExtra("TITULO");
        QUERY = getIntent().getStringExtra("XML");
        IDMAIN = getIntent().getStringExtra("ID");

        REG = new FormCierreReg(mContext, TITLE);

        PAGETITLE = (TextView) this.findViewById(R.id.header_actual);
        PAGETITLE.setText(TITLE);

        name = Environment.getExternalStorageDirectory() + "/TDC@/" + TITLE + "/";
        File dir = new File(name);
        if (!dir.exists())
            if (dir.mkdirs()) {
                Log.d(TITLE, "Se creo el directorio " + name);
            } else
                Log.d(TITLE, "No se pudo crear el directorio " + name);

        init();
    }

    public void onClick_apagar(View v) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage("¿Seguro que desea salir de TDC?");
        b.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveData();
                if (ActividadCierreActivity.actividad != null)
                    ActividadCierreActivity.actividad.finish();
                if (AgendaActivity.actividad != null)
                    AgendaActivity.actividad.finish();
                if (MainActivity.actividad != null)
                    MainActivity.actividad.finish();
                actividad.finish();
            }
        });
        b.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        b.show();
    }

    @Override
    public void onBackPressed() {
        onClick_back(null);
    }

    public void onClick_back(View v) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage("¿Seguro que desea salir del Formulario?");
        b.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveData();
                actividad.finish();
            }
        });
        b.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        b.show();
    }

    private void init() {
        /**
         * Se dibuja el checklist
         */


        try {
            SYSTEMS = XMLParserTDC.parseFormulario(QUERY);

            for (SYSTEM S : SYSTEMS) {
                /**      CABECERA SYSTEMS  **/
                CONTENIDO.addView(S.generateView(mContext));

                for (AREA A : S.getAreas()) {
                    /**         CABECERA AREAS **/
                    CONTENIDO.addView(A.generateView(mContext));

                    for (final ITEM I : A.getItems()) {
                        /**         CABECERA ITEMS **/
                        CONTENIDO.addView(I.getTitle(mContext));

                        LinearLayout itemLayout = new LinearLayout(mContext);
                        LinearLayout.LayoutParams itemLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        itemLayoutParam.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getResources().getDisplayMetrics());
                        itemLayoutParam.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getResources().getDisplayMetrics());
                        itemLayoutParam.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                        itemLayout.setBackgroundResource(R.drawable.fondo_spinner1);
                        itemLayout.setLayoutParams(itemLayoutParam);
                        itemLayout.setOrientation(LinearLayout.VERTICAL);
                        itemLayout.setPadding(6, 6, 6, 6);

                        View v = I.generateView(mContext);
                            if (v != null) {
                            itemLayout.addView(v);
                        }
                        if (I.getQuestions() != null) {

                            for (final QUESTION Q : I.getQuestions()) {
                                /**         CABECERA QUESTION  **/
                                LinearLayout.LayoutParams questionLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                questionLayoutParam.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());
                                questionLayoutParam.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                                LinearLayout layquest = new LinearLayout(mContext);
                                layquest.setLayoutParams(questionLayoutParam);
                                layquest.setOrientation(LinearLayout.HORIZONTAL);
                                if (Q.getPhoto().equals("OK")) {


                                    ImageButton photo = new ImageButton(mContext);
                                    photo.setLayoutParams(new LinearLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics()),
                                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics())));
                                    photo.setImageResource(R.drawable.ic_camerawhite);
                                    photo.setPadding(10, 10, 10, 10);
                                    photo.setBackgroundResource(R.drawable.button_gray_rounded);

                                    photo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            questionTMP = Q;

                                            AlertDialog.Builder b = new AlertDialog.Builder(actividad);
                                            final ArrayList<PHOTO> fotos = Q.getFotos();
                                            int n_fotos = 0;
                                            if (fotos != null) {
                                                n_fotos = fotos.size();
                                            }
                                            b.setTitle("Actualmente tiene " + n_fotos + " fotos");
                                            b.setItems(new CharSequence[]{"Tomar Fotografía", "Ver Fotografías"}, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    if (i == 0) {
                                                        photoTMP = new PHOTO();
                                                        tomarFotos();
                                                    } else {
                                                        if (fotos != null && fotos.size() > 0)
                                                            verFotos();
                                                        else
                                                            Toast.makeText(mContext, "No tiene fotografías", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                            b.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            });
                                            b.show();
                                        }
                                    });


                                    layquest.addView(photo);


                                }
                                layquest.addView(Q.getTitle(mContext));
                                layquest.setGravity(Gravity.CENTER_VERTICAL);

                                View question = Q.generateView(mContext);
                                if (question != null) {
                                    String tag = S.getIdSystem() + "-" + A.getIdArea() + "-" + I.getIdItem() + "-" + Q.getIdQuestion() + "-" + Q.getNameQuestion();

                                    if (Q.getPhoto().equals("OK")) {
                                        int as = 0;
                                        ArrayList<PHOTO> fotos = new ArrayList<>();
                                        String name;
                                        while (!(name = REG.getString("PHOTONAME" + tag + as)).equals("")) {
                                            File tmp = new File(name);
                                            if(tmp.exists()) {
                                                PHOTO f = new PHOTO();
                                                f.setNamePhoto(REG.getString("PHOTONAME" + tag + as));
                                                f.setTitlePhoto(REG.getString("PHOTOTITLE" + tag + as));
                                                f.setDateTime(REG.getString("PHOTODATE" + tag + as));
                                                f.setCoordX(REG.getString("PHOTOCOORDX" + tag + as));
                                                f.setCoordY(REG.getString("PHOTOCOORDY" + tag + as));

                                                fotos.add(f);
                                            }
                                            as++;

                                        }

                                        if (fotos.size() > 0) Q.setFotos(fotos);
                                    }


                                    /*if (Q.getIdType().equals(Constantes.PHOTO)) {
                                        final ArrayList<Button> buttons = Q.getButtons();
                                        String name = REG.getString("PHOTONAME" + tag);
                                        if (!name.equals("")) {
                                            File tmp = new File(name);
                                            if (tmp.exists()) {
                                                PHOTO p = new PHOTO();
                                                String title = REG.getString("PHOTOTITLE" + tag);
                                                String date = REG.getString("PHOTODATE" + tag);
                                                String lon = REG.getString("PHOTOCOORDX" + tag);
                                                String lat = REG.getString("PHOTOCOORDY" + tag);

                                                p.setNamePhoto(name);
                                                p.setTitlePhoto(title);
                                                p.setDateTime(date);
                                                p.setCoordX(lon);
                                                p.setCoordY(lat);
                                                Q.setFoto(p);
                                            }
                                        }

                                        if (Q.getFoto() != null) {
                                            buttons.get(1).setEnabled(true);
                                        }


                                        buttons.get(0).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                questionTMP = Q;
                                                photoTMP = new PHOTO();
                                                buttonTMP = buttons.get(1);
                                                tomarFoto();
                                            }
                                        });

                                        buttons.get(1).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if (Q.getFoto() != null) {
                                                    AlertDialog.Builder b = new AlertDialog.Builder(actividad);
                                                    b.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.dismiss();
                                                        }
                                                    });
                                                    b.setNegativeButton("Borrar", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            File delete = new File(Q.getFoto().getNamePhoto());
                                                            if (delete.exists())
                                                                if (delete.delete()) {
                                                                    Log.d("FOTO", "Imagen eliminada");
                                                                }
                                                            Q.setFoto(null);
                                                            Toast.makeText(mContext, "Imagen eliminada", Toast.LENGTH_SHORT).show();
                                                            buttons.get(1).setEnabled(false);
                                                            dialogInterface.dismiss();
                                                        }
                                                    });
                                                    ImageView joto = new ImageView(mContext);
                                                    File foto = new File(Q.getFoto().getNamePhoto());
                                                    if (foto.exists()) {
                                                        Bitmap tmp = BitmapFactory.decodeFile(Q.getFoto().getNamePhoto());
                                                        joto.setImageBitmap(tmp);
                                                        b.setView(joto);
                                                        b.setTitle(Q.getFoto().getTitlePhoto());
                                                        b.show();
                                                    }
                                                }
                                            }
                                        });
                                    }*/
                                    if (Q.getIdType().equals(Constantes.RADIO)) {
                                        int pos = REG.getInt("RADIO" + tag);
                                        if (pos != -100) {
                                            ((RadioButton) ((RadioGroup) Q.getView()).getChildAt(pos)).setChecked(true);
                                        }
                                    }
                                    if (Q.getIdType().equals(Constantes.NUM)) {
                                        String text = REG.getString("NUM" + tag);
                                        ((TextView) Q.getView()).setText(text);
                                    }
                                    if (Q.getIdType().equals(Constantes.TEXT)) {
                                        String text = REG.getString("TEXT" + tag);
                                        ((TextView) Q.getView()).setText(text);

                                    }
                                    if (Q.getIdType().equals(Constantes.CHECK)) {
                                        ArrayList<CheckBox> ch = Q.getCheckBoxes();

                                        for (int j = 0; j < ch.size(); j++) {
                                            Boolean check = REG.getBoolean("CHECK" + tag + j);
                                            ch.get(j).setChecked(check);
                                        }

                                    }

                                    itemLayout.addView(layquest);
                                    if(!Q.getIdType().equals(Constantes.PHOTO))
                                        itemLayout.addView(question);

                                }

                            }
                        }

                        if (I.getSetArrayList() != null) {
                            /** Generar vista del IteamRepeat **/
                            final ArrayList<View> repeatContentList = new ArrayList<>();
                            final ArrayList<Button> repeatButtontList = new ArrayList<>();
                            LinearLayout.LayoutParams repeatLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            //repeatLayoutParam.bottomMargin =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());

                            LinearLayout repeatLayout = new LinearLayout(mContext);
                            repeatLayout.setLayoutParams(repeatLayoutParam);
                            repeatLayout.setOrientation(LinearLayout.VERTICAL);

                            if (I.getIdType().equals(Constantes.TABLE)) {
                                //Preparamos la tabla o lo que sea
                                for (int x = 0; x < I.getValues().size(); x++) {
                                    VALUE value = I.getValues().get(x);
                                    Button boton = new Button(mContext);
                                    boton.setText(value.getNameValue());

                                    final LinearLayout contentSetLayout = new LinearLayout(mContext);
                                    contentSetLayout.setOrientation(LinearLayout.VERTICAL);

                                    ArrayList<SET> listaAuxSet = new ArrayList<>();
                                    for (SET set : I.getSetArrayList()) {

                                        SET setAux = new SET();
                                        setAux.setIdSet(set.getIdSet());
                                        setAux.setNameSet(set.getNameSet());
                                        setAux.setValueSet(set.getValueSet());
                                        //setAux.setQuestions(set.getQuestions());
                                        LinearLayout.LayoutParams questionLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        questionLayoutParam.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
                                        questionLayoutParam.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());


                                        LinearLayout setLayout = new LinearLayout(mContext);
                                        setLayout.setLayoutParams(questionLayoutParam);
                                        setLayout.setOrientation(LinearLayout.VERTICAL);
                                        setLayout.setBackgroundResource(R.drawable.fondo_general);
                                        setLayout.setPadding(10, 10, 10, 10);

                                        if (set.getQuestions() != null) {
                                            ArrayList<QUESTION> listadoQ = new ArrayList<>();

                                            for (final QUESTION Q : set.getQuestions()) {
                                                final QUESTION qAux = new QUESTION();
                                                qAux.setIdQuestion(Q.getIdQuestion());
                                                qAux.setPhoto(Q.getPhoto());
                                                qAux.setNumberPhoto(Q.getNumberPhoto());
                                                qAux.setNameType(Q.getNameType());
                                                qAux.setNameQuestion(Q.getNameQuestion());
                                                qAux.setIdType(Q.getIdType());
                                                qAux.setValues(Q.getValues());

                                                /**         CABECERA QUESTION  **/


                                                LinearLayout questTitle = new LinearLayout(mContext);
                                                questTitle.setLayoutParams(questionLayoutParam);
                                                questTitle.setOrientation(LinearLayout.HORIZONTAL);

                                                if (Q.getPhoto().equals("OK")) {

                                                    final ImageButton photo = new ImageButton(mContext);
                                                    photo.setLayoutParams(new LinearLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics()),
                                                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics())));
                                                    photo.setImageResource(R.drawable.ic_camerawhite);
                                                    photo.setPadding(10, 10, 10, 10);
                                                    photo.setBackgroundResource(R.drawable.button_gray_rounded);

                                                    photo.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            questionTMP = qAux;

                                                            AlertDialog.Builder b = new AlertDialog.Builder(actividad);
                                                            final ArrayList<PHOTO> fotos = qAux.getFotos();
                                                            int n_fotos = 0;
                                                            if (fotos != null) {
                                                                n_fotos = fotos.size();
                                                            }
                                                            b.setTitle("Actualmente tiene " + n_fotos + " fotos");
                                                            b.setItems(new CharSequence[]{"Tomar Fotografía", "Ver Fotografías"}, new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    if (i == 0) {
                                                                        photoTMP = new PHOTO();
                                                                        tomarFotos();
                                                                    } else {
                                                                        if (fotos != null && fotos.size() > 0)
                                                                            verFotos();
                                                                        else
                                                                            Toast.makeText(mContext, "No tiene fotografías", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                            b.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    dialogInterface.dismiss();
                                                                }
                                                            });
                                                            b.show();
                                                        }
                                                    });

                                                    questTitle.addView(photo);
                                                }
                                                questTitle.addView(qAux.getTitle(mContext));
                                                questTitle.setGravity(Gravity.CENTER_VERTICAL);

                                                View question = qAux.generateView(mContext);
                                                if (question != null) {
                                                    String tag = S.getIdSystem() + "-" + A.getIdArea() + "-" + I.getIdItem() + "-" + value.getIdValue() + value.getNameValue() + "-" + setAux.getIdSet() + setAux.getNameSet() + "-" + Q.getIdQuestion() + "-" + Q.getNameQuestion();

                                                    if (Q.getPhoto().equals("OK")) {
                                                        int as = 0;
                                                        ArrayList<PHOTO> fotos = new ArrayList<>();

                                                        String name;
                                                        while (!(name = REG.getString("PHOTONAME" + tag + as)).equals("")) {
                                                            File tmp = new File(name);
                                                            if(tmp.exists()) {
                                                                PHOTO f = new PHOTO();
                                                                f.setNamePhoto(REG.getString("PHOTONAME" + tag + as));
                                                                f.setTitlePhoto(REG.getString("PHOTOTITLE" + tag + as));
                                                                f.setDateTime(REG.getString("PHOTODATE" + tag + as));
                                                                f.setCoordX(REG.getString("PHOTOCOORDX" + tag + as));
                                                                f.setCoordY(REG.getString("PHOTOCOORDY" + tag + as));
                                                                //f.setBitmap(Funciones.decodeBase64(bmp));

                                                                fotos.add(f);
                                                            }
                                                            as++;

                                                        }

                                                        if (fotos.size() > 0) Q.setFotos(fotos);
                                                    }

                                                    /*if (Q.getIdType().equals(Constantes.PHOTO)) {
                                                        final ArrayList<Button> buttons = qAux.getButtons();
                                                        String name = REG.getString("PHOTONAME" + tag);
                                                        if (!name.equals("")) {
                                                            File tmp = new File(name);
                                                            if(tmp.exists()) {
                                                                PHOTO p = new PHOTO();
                                                                String title = REG.getString("PHOTOTITLE" + tag);
                                                                String date = REG.getString("PHOTODATE" + tag);
                                                                String lon = REG.getString("PHOTOCOORDX" + tag);
                                                                String lat = REG.getString("PHOTOCOORDY" + tag);

                                                                p.setNamePhoto(name);
                                                                p.setTitlePhoto(title);
                                                                p.setDateTime(date);
                                                                p.setCoordX(lon);
                                                                p.setCoordY(lat);
                                                                qAux.setFoto(p);
                                                            }
                                                        }

                                                        if (qAux.getFoto() != null) {
                                                            buttons.get(1).setEnabled(true);
                                                        }

                                                        buttons.get(0).setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                questionTMP = qAux;
                                                                photoTMP = new PHOTO();
                                                                buttonTMP = buttons.get(1);
                                                                tomarFoto();
                                                            }
                                                        });

                                                        buttons.get(1).setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                if (qAux.getFoto() != null) {
                                                                    AlertDialog.Builder b = new AlertDialog.Builder(actividad);
                                                                    b.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            dialogInterface.dismiss();
                                                                        }
                                                                    });
                                                                    b.setNegativeButton("Borrar", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            File delete = new File(qAux.getFoto().getNamePhoto());
                                                                            if (delete.exists())
                                                                                if (delete.delete()) {
                                                                                    Log.d("FOTO", "Imagen eliminada");
                                                                                }
                                                                            qAux.setFoto(null);
                                                                            Toast.makeText(mContext, "Imagen eliminada", Toast.LENGTH_SHORT).show();
                                                                            buttons.get(1).setEnabled(false);
                                                                            dialogInterface.dismiss();
                                                                        }
                                                                    });
                                                                    ImageView joto = new ImageView(mContext);
                                                                    File foto = new File(qAux.getFoto().getNamePhoto());
                                                                    if (foto.exists()) {

                                                                        Bitmap tmp = BitmapFactory.decodeFile(qAux.getFoto().getNamePhoto());
                                                                        joto.setImageBitmap(tmp);
                                                                        b.setView(joto);
                                                                        b.setTitle(qAux.getFoto().getTitlePhoto());
                                                                        b.show();
                                                                    }
                                                                }
                                                            }
                                                        });
                                                    }*/
                                                    if (Q.getIdType().equals(Constantes.RADIO)) {
                                                        int pos = REG.getInt("RADIO" + tag);
                                                        if (pos != -100) {
                                                            ((RadioButton) ((RadioGroup) qAux.getView()).getChildAt(pos)).setChecked(true);
                                                        }
                                                    }
                                                    if (Q.getIdType().equals(Constantes.NUM)) {
                                                        String text = REG.getString("NUM" + tag);
                                                        ((TextView) qAux.getView()).setText(text);
                                                    }
                                                    if (Q.getIdType().equals(Constantes.TEXT)) {
                                                        String text = REG.getString("TEXT" + tag);
                                                        ((TextView) qAux.getView()).setText(text);

                                                    }
                                                    if (Q.getIdType().equals(Constantes.CHECK)) {
                                                        ArrayList<CheckBox> ch = qAux.getCheckBoxes();

                                                        for (int j = 0; j < ch.size(); j++) {
                                                            Boolean check = REG.getBoolean("CHECK" + tag + j);
                                                            ch.get(j).setChecked(check);
                                                        }

                                                    }


                                                    setLayout.addView(questTitle);
                                                    if(!Q.getIdType().equals(Constantes.PHOTO))
                                                        setLayout.addView(question);
                                                }
                                                listadoQ.add(qAux);


                                            }
                                            setAux.setQuestions(listadoQ);
                                            contentSetLayout.addView(setAux.getTitle(mContext));
                                            contentSetLayout.addView(setLayout);
                                        }


                                        listaAuxSet.add(setAux);
                                    }
                                    I.addListSet(listaAuxSet);

                                    LinearLayout.LayoutParams botonparam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    botonparam.setMargins(
                                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()),   //left
                                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics()),   //top
                                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()),   //right
                                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics())    //bottom
                                    );

                                    boton.setBackgroundResource(R.drawable.button_gray);
                                    boton.setLayoutParams(botonparam);
                                    boton.setTextColor(Color.WHITE);
                                    boton.setTypeface(Typeface.DEFAULT_BOLD);


                                    boton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (contentSetLayout.getVisibility() == View.GONE) {
                                                contentSetLayout.setVisibility(View.VISIBLE);
                                                for (View layu : repeatContentList) {
                                                    if (!layu.equals(contentSetLayout)) {
                                                        layu.setVisibility(View.GONE);
                                                    }
                                                }
                                            } else {
                                                contentSetLayout.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                    boton.setVisibility(View.GONE);
                                    contentSetLayout.setVisibility(View.GONE);
                                    repeatContentList.add(contentSetLayout);//para que al mostrar uno se oculten los demas
                                    repeatButtontList.add(boton);       //para que al mostrar uno se oculten los demas
                                    repeatLayout.addView(boton);
                                    repeatLayout.addView(contentSetLayout);

                                }
                                itemLayout.addView(repeatLayout);

                                for (final CheckBox c : I.getCheckBoxes()) {
                                    final int pos = I.getCheckBoxes().indexOf(c);
                                    c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                            repeatContentList.get(pos).setVisibility(View.GONE);
                                            if (b) {
                                                repeatButtontList.get(pos).setVisibility(View.VISIBLE);
                                            } else {
                                                repeatButtontList.get(pos).setVisibility(View.GONE);
                                            }
                                        }
                                    });

                                    boolean check = REG.getBoolean("TABLE" + S.getIdSystem() + "-" + A.getIdArea() + "-" + I.getIdItem() + pos);
                                    c.setChecked(check);
                                }


                            }

                            if (I.getIdType().equals(Constantes.RADIO)) {
                                for (int x = 0; x < I.getValues().size(); x++) {
                                    VALUE value = I.getValues().get(x);

                                    Button boton = new Button(mContext);
                                    boton.setText(value.getNameValue());

                                    final LinearLayout contentSetLayout = new LinearLayout(mContext);
                                    contentSetLayout.setOrientation(LinearLayout.VERTICAL);

                                    ArrayList<SET> listaAuxSet = new ArrayList<>();
                                    for (SET set : I.getSetArrayList()) {

                                        SET setAux = new SET();
                                        setAux.setIdSet(set.getIdSet());
                                        setAux.setNameSet(set.getNameSet());
                                        setAux.setValueSet(set.getValueSet());
                                        //setAux.setQuestions(set.getQuestions());
                                        LinearLayout.LayoutParams questionLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                        questionLayoutParam.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
                                        questionLayoutParam.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());


                                        LinearLayout setLayout = new LinearLayout(mContext);
                                        setLayout.setLayoutParams(questionLayoutParam);
                                        setLayout.setOrientation(LinearLayout.VERTICAL);
                                        setLayout.setBackgroundResource(R.drawable.fondo_general);
                                        setLayout.setPadding(10, 10, 10, 10);

                                        if (set.getQuestions() != null) {
                                            ArrayList<QUESTION> listadoQ = new ArrayList<>();

                                            for (final QUESTION Q : set.getQuestions()) {
                                                QUESTION qAux = new QUESTION();
                                                qAux.setIdQuestion(Q.getIdQuestion());
                                                qAux.setPhoto(Q.getPhoto());
                                                qAux.setNumberPhoto(Q.getNumberPhoto());
                                                qAux.setNameType(Q.getNameType());
                                                qAux.setNameQuestion(Q.getNameQuestion());
                                                qAux.setIdType(Q.getIdType());
                                                qAux.setValues(Q.getValues());

                                                /**         CABECERA QUESTION  **/


                                                LinearLayout questTitle = new LinearLayout(mContext);
                                                questTitle.setLayoutParams(questionLayoutParam);
                                                questTitle.setOrientation(LinearLayout.HORIZONTAL);

                                                if (Q.getPhoto().equals("OK")) {

                                                    final ImageButton photo = new ImageButton(mContext);
                                                    photo.setLayoutParams(new LinearLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics()),
                                                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics())));
                                                    photo.setImageResource(R.drawable.ic_camerawhite);
                                                    photo.setPadding(10, 10, 10, 10);
                                                    photo.setBackgroundResource(R.drawable.button_gray_rounded);

                                                    photo.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            questionTMP = Q;

                                                            AlertDialog.Builder b = new AlertDialog.Builder(actividad);
                                                            final ArrayList<PHOTO> fotos = Q.getFotos();
                                                            int n_fotos = 0;
                                                            if (fotos != null) {
                                                                n_fotos = fotos.size();
                                                            }
                                                            b.setTitle("Actualmente tiene " + n_fotos + " fotos");
                                                            b.setItems(new CharSequence[]{"Tomar Fotografía", "Ver Fotografías"}, new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    if (i == 0) {
                                                                        photoTMP = new PHOTO();
                                                                        tomarFotos();
                                                                    } else {
                                                                        if (fotos != null && fotos.size() > 0)
                                                                            verFotos();
                                                                        else
                                                                            Toast.makeText(mContext, "No tiene fotografías", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                            b.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    dialogInterface.dismiss();
                                                                }
                                                            });
                                                            b.show();
                                                        }
                                                    });

                                                    questTitle.addView(photo);
                                                }
                                                questTitle.addView(qAux.getTitle(mContext));
                                                questTitle.setGravity(Gravity.CENTER_VERTICAL);

                                                View question = qAux.generateView(mContext);
                                                if (question != null) {
                                                    String tag = S.getIdSystem() + "-" + A.getIdArea() + "-" + I.getIdItem() + "-" + value.getIdValue() + value.getNameValue() + "-" + setAux.getIdSet() + setAux.getNameSet() + "-" + Q.getIdQuestion() + "-" + Q.getNameQuestion();

                                                    if (Q.getPhoto().equals("OK")) {
                                                        int as = 0;
                                                        ArrayList<PHOTO> fotos = new ArrayList<>();

                                                        //TODO ver si la imagen existe
                                                        String fotoname = REG.getString("PHOTONAME" + tag + as);
                                                        File joto = new File(fotoname);
                                                        while (joto.exists()) {
                                                            PHOTO f = new PHOTO();
                                                            f.setNamePhoto(REG.getString("PHOTONAME" + tag + as));
                                                            f.setTitlePhoto(REG.getString("PHOTOTITLE" + tag + as));
                                                            f.setDateTime(REG.getString("PHOTODATE" + tag + as));
                                                            f.setCoordX(REG.getString("PHOTOCOORDX" + tag + as));
                                                            f.setCoordY(REG.getString("PHOTOCOORDY" + tag + as));
                                                            fotos.add(f);
                                                            as++;

                                                            fotoname = REG.getString("PHOTONAME" + tag + as);
                                                            joto = new File(fotoname);

                                                        }

                                                        if (fotos.size() > 0) Q.setFotos(fotos);
                                                    }

                                                    /*if (Q.getIdType().equals(Constantes.PHOTO)) {
                                                        final ArrayList<Button> buttons = qAux.getButtons();

                                                        String fotoname = REG.getString("PHOTONAME" + tag);
                                                        File joto = new File(fotoname);
                                                        if (joto.exists()) {
                                                            PHOTO p = new PHOTO();
                                                            String name = REG.getString("PHOTONAME" + tag);
                                                            String title = REG.getString("PHOTOTITLE" + tag);
                                                            String date = REG.getString("PHOTODATE" + tag);
                                                            String lon = REG.getString("PHOTOCOORDX" + tag);
                                                            String lat = REG.getString("PHOTOCOORDY" + tag);

                                                            p.setNamePhoto(name);
                                                            p.setTitlePhoto(title);
                                                            p.setDateTime(date);
                                                            p.setCoordX(lon);
                                                            p.setCoordY(lat);
                                                            qAux.setFoto(p);
                                                        }

                                                        if (qAux.getFoto() != null) {
                                                            buttons.get(1).setEnabled(true);
                                                        }

                                                        buttons.get(0).setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                questionTMP = Q;
                                                                photoTMP = new PHOTO();
                                                                buttonTMP = buttons.get(1);
                                                                tomarFoto();
                                                            }
                                                        });

                                                        buttons.get(1).setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                if (Q.getFoto() != null) {
                                                                    AlertDialog.Builder b = new AlertDialog.Builder(actividad);
                                                                    b.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            dialogInterface.dismiss();
                                                                        }
                                                                    });
                                                                    b.setNegativeButton("Borrar", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            File delete = new File(Q.getFoto().getNamePhoto());
                                                                            if (delete.exists())
                                                                                if (delete.delete()) {
                                                                                    Log.d("FOTO", "Imagen eliminada");
                                                                                }
                                                                            Q.setPhoto(null);
                                                                            Toast.makeText(mContext, "Imagen eliminada", Toast.LENGTH_SHORT).show();
                                                                            buttons.get(1).setEnabled(false);
                                                                            dialogInterface.dismiss();
                                                                        }
                                                                    });
                                                                    ImageView joto = new ImageView(mContext);
                                                                    File foto = new File(Q.getFoto().getNamePhoto());
                                                                    if (foto.exists()) {
                                                                        Bitmap tmp = BitmapFactory.decodeFile(Q.getFoto().getNamePhoto());
                                                                        joto.setImageBitmap(tmp);
                                                                        b.setView(joto);
                                                                        b.setTitle(Q.getFoto().getTitlePhoto());
                                                                        b.show();
                                                                    }
                                                                }
                                                            }
                                                        });
                                                    }*/
                                                    if (Q.getIdType().equals(Constantes.RADIO)) {
                                                        int pos = REG.getInt("RADIO" + tag);
                                                        if (pos != -100) {
                                                            ((RadioButton) ((RadioGroup) qAux.getView()).getChildAt(pos)).setChecked(true);
                                                        }
                                                    }
                                                    if (Q.getIdType().equals(Constantes.NUM)) {
                                                        String text = REG.getString("NUM" + tag);
                                                        ((TextView) qAux.getView()).setText(text);
                                                    }
                                                    if (Q.getIdType().equals(Constantes.TEXT)) {
                                                        String text = REG.getString("TEXT" + tag);
                                                        ((TextView) qAux.getView()).setText(text);

                                                    }
                                                    if (Q.getIdType().equals(Constantes.CHECK)) {
                                                        ArrayList<CheckBox> ch = qAux.getCheckBoxes();

                                                        for (int j = 0; j < ch.size(); j++) {
                                                            Boolean check = REG.getBoolean("CHECK" + tag + j);
                                                            ch.get(j).setChecked(check);
                                                        }

                                                    }


                                                    setLayout.addView(questTitle);
                                                    if(!qAux.getIdType().equals(Constantes.PHOTO))
                                                        setLayout.addView(question);
                                                }
                                                listadoQ.add(qAux);


                                            }
                                            setAux.setQuestions(listadoQ);
                                            contentSetLayout.addView(setAux.getTitle(mContext));
                                            contentSetLayout.addView(setLayout);
                                        }


                                        listaAuxSet.add(setAux);
                                    }
                                    I.addListSet(listaAuxSet);

                                    LinearLayout.LayoutParams botonparam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    botonparam.setMargins(
                                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()),   //left
                                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics()),   //top
                                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()),   //right
                                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics())    //bottom
                                    );

                                    boton.setBackgroundResource(R.drawable.button_gray);
                                    boton.setLayoutParams(botonparam);
                                    boton.setTextColor(Color.WHITE);
                                    boton.setTypeface(Typeface.DEFAULT_BOLD);


                                    boton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (contentSetLayout.getVisibility() == View.GONE) {
                                                contentSetLayout.setVisibility(View.VISIBLE);
                                                for (View layu : repeatContentList) {
                                                    if (!layu.equals(contentSetLayout)) {
                                                        layu.setVisibility(View.GONE);
                                                    }
                                                }
                                            } else {
                                                contentSetLayout.setVisibility(View.GONE);
                                            }
                                        }
                                    });

                                    contentSetLayout.setVisibility(View.GONE);

                                    boton.setVisibility(View.GONE);

                                    repeatContentList.add(contentSetLayout);//para que al mostrar uno se oculten los demas
                                    repeatButtontList.add(boton);
                                    repeatLayout.addView(boton);
                                    repeatLayout.addView(contentSetLayout);

                                }

                                RadioGroup group = (RadioGroup) I.getView();

                                group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(RadioGroup rg, int id) {
                                        for (View l : repeatContentList) {
                                            l.setVisibility(View.GONE);
                                        }
                                        RadioButton btn = (RadioButton) rg.findViewById(id);
                                        int position = rg.indexOfChild(btn) + 1;
                                        for (Button b : repeatButtontList) {
                                            b.setVisibility(View.GONE);
                                        }
                                        for (int i = 0; i < position; i++) {
                                            repeatButtontList.get(i).setVisibility(View.VISIBLE);
                                        }

                                    }
                                });


                                int checked = REG.getInt("RADIO" + S.getIdSystem() + "-" + A.getIdArea() + "-" + I.getIdItem());
                                if (checked != -1)
                                    group.check(checked);


                                itemLayout.addView(repeatLayout);
                            }


                        } else {
                            if (I.getIdType().equals(Constantes.RADIO)) {
                                RadioGroup group = (RadioGroup) I.getView();
                                int checked = REG.getInt("RADIO" + S.getIdSystem() + "-" + A.getIdArea() + "-" + I.getIdItem());
                                if (checked != -1)
                                    group.check(checked);
                            }
                        }

                        CONTENIDO.addView(itemLayout);
                    }
                }
            }

        } catch (ParserConfigurationException | SAXException | XPathExpressionException | IOException e) {
            e.printStackTrace();
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("Error en XML");
            b.setIcon(android.R.drawable.ic_dialog_alert);
            b.setMessage("No se pudo generar el formulario");

            b.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            b.show();

        }
    }

    //TODO INICIO SAVEDATA
    private void saveData() {

        for (SYSTEM S : SYSTEMS) {
            for (AREA A : S.getAreas())
                for (ITEM I : A.getItems()) {
                    String preId = S.getIdSystem() + "-" + A.getIdArea() + "-" + I.getIdItem();
                    if (I.getIdType().equals("")) {
                        Log.d("GUARDANDO", "ITEM NORMAL");
                        for (QUESTION Q : I.getQuestions()) {
                            String tagid = preId + "-" + Q.getIdQuestion() + "-" + Q.getNameQuestion();

                            if (Q.getPhoto().equals("OK")) {
                                ArrayList<PHOTO> fotos = Q.getFotos();
                                if (fotos != null) {
                                    for (int as = 0; as < fotos.size(); as++) {
                                        PHOTO f = fotos.get(as);
                                        Log.d("GUARDANDO", "foto: " + f.getTitlePhoto());
                                        REG.addValue("PHOTONAME" + tagid + as, f.getNamePhoto());
                                        REG.addValue("PHOTOTITLE" + tagid + as, f.getTitlePhoto());
                                        REG.addValue("PHOTODATE" + tagid + as, f.getDateTime());
                                        REG.addValue("PHOTOCOORDX" + tagid + as, f.getCoordX());
                                        REG.addValue("PHOTOCOORDY" + tagid + as, f.getCoordY());
                                        //REG.addValue("PHOTOBMP" + tagid + as, Funciones.encodeTobase64(f.getBitmap()));


                                    }
                                }
                            }

                            if (Q.getIdType().equals(Constantes.RADIO)) {
                                int id = ((RadioGroup) Q.getView()).getCheckedRadioButtonId();
                                if (id != -1) {
                                    RadioButton b = (RadioButton) Q.getView().findViewById(id);
                                    int pos = ((RadioGroup) Q.getView()).indexOfChild(b);
                                    REG.addValue("RADIO" + tagid, pos);
                                    Log.d("GUARDANDO", "Seleccionado-> " + pos);
                                } else {
                                    Log.d("GUARDANDO", "nada seleccionado");
                                }
                            }
                            if (Q.getIdType().equals(Constantes.PHOTO)) {
                                PHOTO p = Q.getFoto();
                                if (p != null) {
                                    Log.d("GUARDANDO", "foto: " + p.getTitlePhoto());

                                    REG.addValue("PHOTONAME" + tagid, p.getNamePhoto());
                                    REG.addValue("PHOTOTITLE" + tagid, p.getTitlePhoto());
                                    REG.addValue("PHOTODATE" + tagid, p.getDateTime());
                                    REG.addValue("PHOTOCOORDX" + tagid, p.getCoordX());
                                    REG.addValue("PHOTOCOORDY" + tagid, p.getCoordY());
                                    //REG.addValue("PHOTOBMP" + tagid, Funciones.encodeTobase64(p.getBitmap()));

                                }

                            }
                            if (Q.getIdType().equals(Constantes.NUM)) {
                                String text = ((TextView) Q.getView()).getText().toString();
                                Log.d("GUARDANDO", text);
                                if (text.length() > 0) {
                                    REG.addValue("NUM" + tagid, text);
                                }
                            }
                            if (Q.getIdType().equals(Constantes.TEXT)) {
                                String text = ((TextView) Q.getView()).getText().toString();
                                Log.d("GUARDANDO", text);
                                if (text.length() > 0) {
                                    REG.addValue("TEXT" + tagid, text);
                                }
                            }
                            if (Q.getIdType().equals(Constantes.CHECK)) {
                                ArrayList<CheckBox> ch = Q.getCheckBoxes();
                                for (int j = 0; j < ch.size(); j++) {
                                    if (ch.get(j).isChecked()) {
                                        REG.addValue("CHECK" + tagid + j, true);
                                    } else {
                                        REG.addValue("CHECK" + tagid + j, false);
                                    }
                                }

                            }
                        }
                    } else {
                        if (I.getIdType().equals(Constantes.TABLE)) {
                            for (int i = 0; i < I.getValues().size(); i++) {
                                CheckBox c = I.getCheckBoxes().get(i);

                                REG.addValue("TABLE" + S.getIdSystem() + "-" + A.getIdArea() + "-" + I.getIdItem() + i, c.isChecked()); //GUARDAMOS LA SELECCION DE SECTORES
                                ArrayList<SET> list_i = I.getSetlistArrayList().get(i);
                                VALUE valor = I.getValues().get(i);
                                for (SET SeT : list_i) {
                                    for (QUESTION Q : SeT.getQuestions()) {
                                        String tagid = preId + "-" + valor.getIdValue() + valor.getNameValue() + "-" + SeT.getIdSet() + SeT.getNameSet() + "-" + Q.getIdQuestion() + "-" + Q.getNameQuestion();

                                        if (Q.getPhoto().equals("OK")) {
                                            ArrayList<PHOTO> fotos = Q.getFotos();
                                            if (fotos != null) {
                                                for (int as = 0; as < fotos.size(); as++) {
                                                    PHOTO f = fotos.get(as);
                                                    Log.d("GUARDANDO", "foto: " + f.getTitlePhoto());
                                                    REG.addValue("PHOTONAME" + tagid + as, f.getNamePhoto());
                                                    REG.addValue("PHOTOTITLE" + tagid + as, f.getTitlePhoto());
                                                    REG.addValue("PHOTODATE" + tagid + as, f.getDateTime());
                                                    REG.addValue("PHOTOCOORDX" + tagid + as, f.getCoordX());
                                                    REG.addValue("PHOTOCOORDY" + tagid + as, f.getCoordY());
                                                    //REG.addValue("PHOTOBMP" + tagid + as, Funciones.encodeTobase64(f.getBitmap()));


                                                }
                                            }
                                        }

                                        if (Q.getIdType().equals(Constantes.RADIO)) {
                                            int id = ((RadioGroup) Q.getView()).getCheckedRadioButtonId();
                                            if (id != -1) {
                                                RadioButton b = (RadioButton) Q.getView().findViewById(id);
                                                int pos = ((RadioGroup) Q.getView()).indexOfChild(b);
                                                REG.addValue("RADIO" + tagid, pos);
                                                Log.d("GUARDANDO", "Seleccionado-> " + pos);
                                            } else {
                                                Log.d("GUARDANDO", "nada seleccionado");
                                            }
                                        }
                                        if (Q.getIdType().equals(Constantes.PHOTO)) {
                                            PHOTO p = Q.getFoto();
                                            if (p != null) {
                                                Log.d("GUARDANDO", "foto: " + p.getTitlePhoto());
                                                REG.addValue("PHOTONAME" + tagid, p.getNamePhoto());
                                                REG.addValue("PHOTOTITLE" + tagid, p.getTitlePhoto());
                                                REG.addValue("PHOTODATE" + tagid, p.getDateTime());
                                                REG.addValue("PHOTOCOORDX" + tagid, p.getCoordX());
                                                REG.addValue("PHOTOCOORDY" + tagid, p.getCoordY());
                                                //REG.addValue("PHOTOBMP" + tagid, Funciones.encodeTobase64(p.getBitmap()));
                                            }

                                        }
                                        if (Q.getIdType().equals(Constantes.NUM)) {
                                            String text = ((TextView) Q.getView()).getText().toString();
                                            Log.d("GUARDANDO", text);
                                            if (text.length() > 0) {
                                                REG.addValue("NUM" + tagid, text);
                                            }
                                        }
                                        if (Q.getIdType().equals(Constantes.TEXT)) {
                                            String text = ((TextView) Q.getView()).getText().toString();
                                            Log.d("GUARDANDO", text);
                                            if (text.length() > 0) {
                                                REG.addValue("TEXT" + tagid, text);
                                            }
                                        }
                                        if (Q.getIdType().equals(Constantes.CHECK)) {
                                            ArrayList<CheckBox> ch = Q.getCheckBoxes();

                                            for (int j = 0; j < ch.size(); j++) {
                                                if (ch.get(j).isChecked()) {
                                                    REG.addValue("CHECK" + tagid + j, true);
                                                } else {
                                                    REG.addValue("CHECK" + tagid + j, false);
                                                }
                                            }

                                        }
                                    }


                                }
                            }
                        }

                        if (I.getIdType().equals(Constantes.RADIO)) {
                            ArrayList<VALUE> values = I.getValues();
                            RadioGroup radioGroup = (RadioGroup) I.getView();
                            RadioButton btn = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                            int position = radioGroup.indexOfChild(btn) + 1;

                            REG.addValue("RADIO" + S.getIdSystem() + "-" + A.getIdArea() + "-" + I.getIdItem(), radioGroup.getCheckedRadioButtonId()); //GUARDAMOS LA SELECCION DE SECTORES

                            if (I.getSetArrayList() != null) {
                                for (int i = 0; i < position; i++) {

                                    ArrayList<SET> list_i = I.getSetlistArrayList().get(i);
                                    VALUE valor = values.get(i);
                                    for (SET SeT : list_i) {
                                        for (QUESTION Q : SeT.getQuestions()) {
                                            String tagid = preId + "-" + valor.getIdValue() + valor.getNameValue() + "-" + SeT.getIdSet() + SeT.getNameSet() + "-" + Q.getIdQuestion() + "-" + Q.getNameQuestion();

                                            if (Q.getPhoto().equals("OK")) {
                                                ArrayList<PHOTO> fotos = Q.getFotos();
                                                if (fotos != null) {
                                                    for (int as = 0; as < fotos.size(); as++) {
                                                        PHOTO f = fotos.get(as);
                                                        Log.d("GUARDANDO", "foto: " + f.getTitlePhoto());
                                                        REG.addValue("PHOTONAME" + tagid + as, f.getNamePhoto());
                                                        REG.addValue("PHOTOTITLE" + tagid + as, f.getTitlePhoto());
                                                        REG.addValue("PHOTODATE" + tagid + as, f.getDateTime());
                                                        REG.addValue("PHOTOCOORDX" + tagid + as, f.getCoordX());
                                                        REG.addValue("PHOTOCOORDY" + tagid + as, f.getCoordY());
                                                        //REG.addValue("PHOTOBMP" + tagid + as, Funciones.encodeTobase64(f.getBitmap()));


                                                    }
                                                }
                                            }

                                            if (Q.getIdType().equals(Constantes.RADIO)) {
                                                int id = ((RadioGroup) Q.getView()).getCheckedRadioButtonId();
                                                if (id != -1) {
                                                    RadioButton b = (RadioButton) Q.getView().findViewById(id);
                                                    int pos = ((RadioGroup) Q.getView()).indexOfChild(b);
                                                    REG.addValue("RADIO" + tagid, pos);
                                                    Log.d("GUARDANDO", "Seleccionado-> " + pos);
                                                } else {
                                                    Log.d("GUARDANDO", "nada seleccionado");
                                                }
                                            }
                                            if (Q.getIdType().equals(Constantes.PHOTO)) {
                                                PHOTO p = Q.getFoto();
                                                if (p != null) {
                                                    Log.d("GUARDANDO", "foto: " + p.getTitlePhoto());
                                                    REG.addValue("PHOTONAME" + tagid, p.getNamePhoto());
                                                    REG.addValue("PHOTOTITLE" + tagid, p.getTitlePhoto());
                                                    REG.addValue("PHOTODATE" + tagid, p.getDateTime());
                                                    REG.addValue("PHOTOCOORDX" + tagid, p.getCoordX());
                                                    REG.addValue("PHOTOCOORDY" + tagid, p.getCoordY());
                                                    //REG.addValue("PHOTOBMP" + tagid, Funciones.encodeTobase64(p.getBitmap()));
                                                }

                                            }
                                            if (Q.getIdType().equals(Constantes.NUM)) {
                                                String text = ((TextView) Q.getView()).getText().toString();
                                                Log.d("GUARDANDO", text);
                                                if (text.length() > 0) {
                                                    REG.addValue("NUM" + tagid, text);
                                                }
                                            }
                                            if (Q.getIdType().equals(Constantes.TEXT)) {
                                                String text = ((TextView) Q.getView()).getText().toString();
                                                Log.d("GUARDANDO", text);
                                                if (text.length() > 0) {
                                                    REG.addValue("TEXT" + tagid, text);
                                                }
                                            }
                                            if (Q.getIdType().equals(Constantes.CHECK)) {
                                                ArrayList<CheckBox> ch = Q.getCheckBoxes();

                                                for (int j = 0; j < ch.size(); j++) {
                                                    if (ch.get(j).isChecked()) {
                                                        REG.addValue("CHECK" + tagid + j, true);
                                                    } else {
                                                        REG.addValue("CHECK" + tagid + j, false);
                                                    }
                                                }

                                            }
                                        }


                                    }
                                }
                            }
                        }

                    }
                }
        }
    }
    //TODO FIN SAVEDATA


    public void enviar(View v) {

        if (TITLE.equals("IDEN")) {
            EnviarIden e = new EnviarIden();
            e.execute();
        }
        if(TITLE.equals("3G")){
            Enviar3G e = new Enviar3G();
            e.execute();

        }

    }

    private void verFotos() {
        AlertDialog.Builder b = new AlertDialog.Builder(actividad);
        final ArrayList<PHOTO> fotos = questionTMP.getFotos();
        ArrayList<String> list = new ArrayList<>();
        for (PHOTO p : fotos) {
            list.add(p.getTitlePhoto());
        }

        b.setItems(list.toArray(new CharSequence[list.size()]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int i) {
                ImageView joto = new ImageView(mContext);
                final PHOTO f = fotos.get(i);
                joto.setImageBitmap(BitmapFactory.decodeFile(f.getNamePhoto()));
                AlertDialog.Builder b = new AlertDialog.Builder(actividad);
                b.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                b.setNegativeButton("Borrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {
                        File delete = new File(f.getNamePhoto());
                        if (delete.exists())
                            if (delete.delete()) {
                                Log.d("FOTO", "Imagen eliminada");
                            }
                        fotos.remove(f);
                        Toast.makeText(mContext, "Imagen eliminada", Toast.LENGTH_SHORT).show();

                        dialogInterface.dismiss();
                    }
                });

                b.setView(joto);
                b.setTitle(fotos.get(i).getTitlePhoto());
                b.show();
            }
        });
        b.setTitle("Listado de Imágenes");
        b.show();

    }

    private void tomarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        int code = TAKE_PICTURE;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        photoTMP.setDateTime(timeStamp);
        imgName = name + questionTMP.getIdQuestion() + questionTMP.getNameQuestion() + "_" + timeStamp + ".jpg";
        Uri output = Uri.fromFile(new File(imgName));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        startActivityForResult(intent, code);
    }

    private void tomarFotos() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        int code = TAKE_PICTURES;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        photoTMP.setDateTime(timeStamp);
        imgName = name + questionTMP.getIdQuestion() + "_" + timeStamp + ".jpg";
        Uri output = Uri.fromFile(new File(imgName));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        startActivityForResult(intent, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("CODE", "resultcode"+resultCode);
        if(resultCode == -1) {
            if (requestCode == TAKE_PICTURE) {
            /*if (data != null) {
                if (data.hasExtra("data")) {
                    photoTMP.setBitmap((Bitmap) data.getParcelableExtra("data"));
                }
            } else {
                photoTMP.setBitmap(BitmapFactory.decodeFile(name));

            }
            photoTMP.setBitmap(Bitmap.createScaledBitmap(photoTMP.getBitmap(), (int) (photoTMP.getBitmap().getWidth() * 0.5), (int) (photoTMP.getBitmap().getHeight() * 0.5), true));
*/
                AlertDialog.Builder b = new AlertDialog.Builder(actividad);
                final EditText titulo = new EditText(this);
                b.setCancelable(false);
                titulo.setHint("Título");
                b.setTitle("Información Fotografía");
                b.setView(titulo);
                b.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        photoTMP.setTitlePhoto(titulo.getText().toString());
                        photoTMP.setCoordX("1");
                        photoTMP.setCoordY("1");
                        photoTMP.setNamePhoto(imgName);
                        questionTMP.setFoto(photoTMP);
                        photoTMP = null;
                        buttonTMP.setEnabled(true);
                        buttonTMP = null;
                        dialogInterface.dismiss();
                    }
                });
                b.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        photoTMP = null;
                        dialogInterface.dismiss();
                    }
                });
                b.show();


            } else if (requestCode == TAKE_PICTURES) {
                //photoTMP = new PHOTO();
            /*if (data != null) {
                if (data.hasExtra("data")) {
                    photoTMP.setBitmap((Bitmap) data.getParcelableExtra("data"));
                }
            } else {
                photoTMP.setBitmap(BitmapFactory.decodeFile(name));

            }
            photoTMP.setBitmap(Bitmap.createScaledBitmap(photoTMP.getBitmap(), (int) (photoTMP.getBitmap().getWidth() * 0.5), (int) (photoTMP.getBitmap().getHeight() * 0.5), true));
*/
                AlertDialog.Builder b = new AlertDialog.Builder(actividad);
                final EditText titulo = new EditText(this);

                b.setCancelable(false);
                titulo.setHint("Título");
                b.setTitle("Información Fotografía");
                b.setView(titulo);
                b.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        photoTMP.setTitlePhoto(titulo.getText().toString());
                        photoTMP.setCoordX("1");
                        photoTMP.setCoordY("1");
                        photoTMP.setNamePhoto(imgName);
                        questionTMP.addFoto(photoTMP);
                        dialogInterface.dismiss();
                    }
                });
                b.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        photoTMP = null;
                        dialogInterface.dismiss();
                    }
                });
                b.show();

            }
        }


    }

    private class EnviarIden extends AsyncTask<String, String, String> {
        ProgressDialog dialog;
        boolean ok = false;

        private EnviarIden() {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage("Enviando formulario...");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String response = SoapRequestTDC.sendAnswerIDEN(telephonyManager.getDeviceId(), IDMAIN, SYSTEMS);
                ArrayList<String> parse = XMLParser.getReturnCode2(response);
                if (parse.get(0).equals("0")) {
                    ok = true;
                    return parse.get(1);
                } else {
                    return "Error Code:" + parse.get(0) + "\n" + parse.get(1);
                }
            } catch (IOException e) {
                return "Se agotó el tiempo de conexión.";
            } catch (ParserConfigurationException | SAXException | XPathExpressionException e) {
                return "Error al leer XML";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (dialog.isShowing()) dialog.dismiss();

            if (ok) {
                subir_fotos(s);
            } else {
                AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                b.setMessage(s);
                b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                b.show();
            }

        }
    }

    private class Enviar3G extends AsyncTask<String, String, String> {
        ProgressDialog dialog;
        boolean ok = false;

        private Enviar3G() {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage("Enviando formulario...");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String response = SoapRequestTDC.sendAnswer3G(telephonyManager.getDeviceId(), IDMAIN, SYSTEMS);
                ArrayList<String> parse = XMLParser.getReturnCode2(response);
                if (parse.get(0).equals("0")) {
                    ok = true;
                    return parse.get(1);
                } else {
                    return "Error Code:" + parse.get(0) + "\n" + parse.get(1);
                }
            } catch (IOException e) {
                return "Se agotó el tiempo de conexión.";
            } catch (ParserConfigurationException | SAXException | XPathExpressionException e) {
                return "Error al leer XML";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (dialog.isShowing()) dialog.dismiss();

            if (ok) {
                subir_fotos(s);
            } else {
                AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                b.setMessage(s);
                b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                b.show();
            }

        }
    }


    public void subir_fotos(String mensaje){
        AlertDialog.Builder b = new AlertDialog.Builder(mContext);
        b.setMessage(mensaje);
        b.setCancelable(false);
        ArrayList<PHOTO> p = new ArrayList<>();
        for (SYSTEM S : SYSTEMS) {
            for (AREA A : S.getAreas()) {
                for (ITEM I : A.getItems()) {
                    if (I.getQuestions() != null) {
                        for (QUESTION Q : I.getQuestions()) {
                            if (Q.getFoto() != null) {
                                p.add(Q.getFoto());
                            }
                            if (Q.getFotos() != null) {
                                for (PHOTO P : Q.getFotos()) {
                                    p.add(P);
                                }
                            }
                        }
                    }
                    if (I.getSetlistArrayList() != null && I.getValues() != null) {
                        for (CheckBox c : I.getCheckBoxes()) {
                            if (c.isChecked()) {
                                for (SET Set : I.getSetlistArrayList().get(I.getCheckBoxes().indexOf(c))) {
                                    if (Set.getQuestions() != null) {
                                        for (QUESTION Q : Set.getQuestions()) {
                                            if (Q.getFoto() != null) {
                                                p.add(Q.getFoto());
                                            }
                                            if (Q.getFotos() != null) {
                                                for (PHOTO P : Q.getFotos()) {
                                                    p.add(P);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (p.size() > 0) {
            UploadImage up = new UploadImage(p, mensaje);
            up.execute(dummy.URL_UPLOAD_IMG_MAINTENANCE);
        }else{
            b = new AlertDialog.Builder(actividad);
            b.setMessage(mensaje);
            b.setCancelable(false);
            b.setPositiveButton("SALIR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    REG.clearPreferences();
                    setResult(RESULT_OK);
                    actividad.finish();

                }
            });
            b.show();
        }
    }

    //TODO UPLOAD PHOTOS
    private class UploadImage extends AsyncTask<String, String, String> {

        ProgressDialog dialog;
        ArrayList<PHOTO> allPhotos;
        String mensaje;

        public UploadImage(ArrayList<PHOTO> ps, String msj) {
            this.allPhotos = ps;
            this.mensaje = msj;

        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";

            DateFormat timestamp_name = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

            for (PHOTO p : allPhotos) {
                try {
                    String fileName = p.getNamePhoto();

                    Log.i("ENVIANDO", fileName);
                    HttpURLConnection conn;
                    DataOutputStream dos;
                    String lineEnd = "\r\n";
                    String twoHyphens = "--";
                    String boundary = "*****";
                    int bytesRead, bytesAvailable, bufferSize;

                    File done = new File(fileName);
                    /*

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    img.getBitmap().compress(Bitmap.CompressFormat.PNG, 0, bos);
                    byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
                    FileOutputStream fos = new FileOutputStream(done);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();*/


                    if (!done.isFile())
                        Log.e("DownloadManager", "no existe");
                    else {
                        FileInputStream fileInputStream = new FileInputStream(done);
                        URL url = new URL(params[0]);

                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.setUseCaches(false);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                        conn.setRequestProperty("uploaded_file", done.getName());

                        dos = new DataOutputStream(conn.getOutputStream());

                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + done.getName() + "\"" + lineEnd);
                        dos.writeBytes(lineEnd);

                        bytesAvailable = fileInputStream.available();

                        bufferSize = Math.min(bytesAvailable, 1 * 1024 * 1024);
                        byte[] buf = new byte[bufferSize];

                        bytesRead = fileInputStream.read(buf, 0, bufferSize);

                        while (bytesRead > 0) {

                            dos.write(buf, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math.min(bytesAvailable, 1 * 1024 * 1024);
                            bytesRead = fileInputStream.read(buf, 0, bufferSize);

                        }

                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                        int serverResponseCode = conn.getResponseCode();
                        String serverResponseMessage = conn.getResponseMessage();


                        Log.i("UploadManager", "HTTP response is: " + serverResponseMessage + ": " + serverResponseCode);

                        fileInputStream.close();
                        dos.flush();
                        dos.close();

                        InputStream responseStream = new BufferedInputStream(conn.getInputStream());

                        BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                        String line = "";
                        StringBuilder stringBuilder = new StringBuilder();
                        while ((line = responseStreamReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        responseStreamReader.close();

                        response = stringBuilder.toString();

                        Log.d("IMAGENES", p.getNamePhoto() + "   \n" + response);
                    }


                } catch (Exception e) {
                    Log.d("TAG", "Error: " + e.getMessage());
                    response = "ERROR";
                }
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage("Subiendo imagenes...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            if (dialog.isShowing())
                dialog.dismiss();
            AlertDialog.Builder b = new AlertDialog.Builder(actividad);
            b.setMessage(mensaje);
            b.setCancelable(false);
            b.setPositiveButton("SALIR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    REG.clearPreferences();
                    setResult(RESULT_OK);
                    actividad.finish();

                }
            });
            b.show();
            super.onPostExecute(s);
        }

    }
}