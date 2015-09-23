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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cl.tdc.felipe.tdc.extras.Funciones;
import cl.tdc.felipe.tdc.objects.FormImage;
import cl.tdc.felipe.tdc.objects.FormSubSystem;
import cl.tdc.felipe.tdc.objects.FormSubSystemItem;
import cl.tdc.felipe.tdc.objects.FormSubSystemItemAttribute;
import cl.tdc.felipe.tdc.objects.FormSubSystemItemAttributeValues;
import cl.tdc.felipe.tdc.objects.FormSystem;
import cl.tdc.felipe.tdc.objects.FormularioCheck;
import cl.tdc.felipe.tdc.objects.Seguimiento.ImagenDia;
import cl.tdc.felipe.tdc.preferences.FormCheckReg;
import cl.tdc.felipe.tdc.preferences.MaintenanceReg;
import cl.tdc.felipe.tdc.preferences.PreferencesTDC;
import cl.tdc.felipe.tdc.webservice.SoapRequest;
import cl.tdc.felipe.tdc.webservice.UploadImage;
import cl.tdc.felipe.tdc.webservice.XMLParser;
import cl.tdc.felipe.tdc.webservice.dummy;

public class FormCheckActivity extends Activity {
    Context mContext;
    String Response;
    public static Activity actividad;
    FormularioCheck formulario;
    ScrollView scrollViewMain;
    ArrayList<String> idsActividades;
    Bitmap b;
    ArrayList<FormImage> imagenes;
    FormImage imageTmp;
    int idMantenimiento;

    ArrayList<View> vistas = new ArrayList<>();

    FormCheckReg reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formcheck);

        mContext = this;
        actividad = this;
        reg = new FormCheckReg(this, "ACTIVITYREG");

        imagenes = new ArrayList<>();
        scrollViewMain = (ScrollView) findViewById(R.id.cerca_content);
        Response = getIntent().getStringExtra("RESPONSE");
        idsActividades = getIntent().getStringArrayListExtra("ACTIVIDADES");
        idMantenimiento = getIntent().getIntExtra("MANTENIMIENTO", -1);
        ObtenerFormulario init = new ObtenerFormulario(this);
        init.execute();
    }

    public void onClick_apagar(View v) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage("¿Seguro que desea salir del CheckList?");
        b.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveData();
                actividad.finish();
                if (AgendaActivity.actividad != null)
                    AgendaActivity.actividad.finish();
                if (MainActivity.actividad != null)
                    MainActivity.actividad.finish();
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
        //super.onBackPressed();
        onClick_back(null);
    }


    public void onClick_back(View v) {
        InputMethodManager imm = (InputMethodManager) actividad.getSystemService(Context.INPUT_METHOD_SERVICE);
        View foco = actividad.getCurrentFocus();
        if (foco == null || !imm.hideSoftInputFromWindow(foco.getWindowToken(), 0)){
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setMessage("¿Seguro que desea salir del CheckList?");
            b.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
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

    }


    private void makeOnlyOneCheckable(final List<CheckBox> cbs) {
        final List<CheckBox> copy = cbs;

        for (int i = 0; i < cbs.size(); i++) {
            final CheckBox check = cbs.get(i);
            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        for (int j = 0; j < cbs.size(); j++) {
                            if (copy.get(j) != check) {
                                copy.get(j).setChecked(false);
                            }

                        }
                    }
                }
            });

        }
    }


    /*
    TODO DIBUJAR VISTA
     */
    private void dibujarVista(FormularioCheck formulario) {
        LinearLayout contenido = (LinearLayout) this.findViewById(R.id.contenido);
        final List<LinearLayout> subcontenidos = new ArrayList<>();

        for (final FormSystem System : formulario.getSystem()) {

            Button buttonSystem = new Button(this);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            buttonSystem.setText(System.getNameSystem());

            if (formulario.getSystem().indexOf(System) == 0) {
                buttonSystem.setBackgroundResource(R.drawable.accordion_top);
            } else {
                buttonSystem.setBackgroundResource(R.drawable.accordion_topc);
                param.setMargins(0, 10, 0, 0);
            }
            buttonSystem.setLayoutParams(param);
            buttonSystem.setClickable(true);
            contenido.addView(buttonSystem);


            final LinearLayout bottom = new LinearLayout(this);
            bottom.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            bottom.setOrientation(LinearLayout.VERTICAL);
            bottom.setBackgroundResource(R.drawable.accordion_bot);
            bottom.setVisibility(View.GONE);

            for (final FormSubSystem subSystem : System.getSubSystemList()) {
                TextView nameSubSystem = new TextView(this);
                nameSubSystem.setText(subSystem.getNameSubSystem());
                Log.d("TEST", nameSubSystem.getText().toString());
                nameSubSystem.setTextSize(16);
                nameSubSystem.setGravity(Gravity.CENTER_HORIZONTAL);
                nameSubSystem.setPadding(0, 15, 0, 10);

                //contenido.addView(nameSubSystem);
                bottom.addView(nameSubSystem);

                for (int i = 0; i < subSystem.getItemList().size(); i++) {
                    FormSubSystemItem item = subSystem.getItemList().get(i);
                    LinearLayout itemLayout = new LinearLayout(this);

                    /*if (i == 0)
                        itemLayout.setBackgroundResource(R.drawable.fondo_general1_top);
                    else if (i == (subSystem.getItemList().size() - 1))
                        itemLayout.setBackgroundResource(R.drawable.fondo_general1_bottom);
                    else
                        itemLayout.setBackgroundResource(R.drawable.fondo_general1_center);
                        */

                    itemLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                    itemLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    itemLayout.setPadding(16, 5, 16, 5);
                    itemLayout.setOrientation(LinearLayout.VERTICAL);


                    TextView titulo = new TextView(this);
                    titulo.setText(item.getNameItem());
                    Log.d("TEST", titulo.getText().toString() + "*");
                    titulo.setTextSize(14);
                    titulo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    titulo.setGravity(Gravity.CENTER_HORIZONTAL);
                    titulo.setBackgroundResource(R.drawable.fondo_general_top);
                    titulo.setTextColor(Color.WHITE);
                    titulo.setPadding(10, 2, 10, 2);


                    itemLayout.addView(titulo);

                    LinearLayout atributosLayout = new LinearLayout(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    atributosLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                    atributosLayout.setOrientation(LinearLayout.VERTICAL);
                    atributosLayout.setPadding(10, 10, 10, 10);
                    atributosLayout.setBackgroundResource(R.drawable.fondo_general_bottom);

                    for (FormSubSystemItemAttribute attribute : item.getAttributeList()) {
                        LinearLayout valuesLayout = new LinearLayout(this);
                        valuesLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        valuesLayout.setOrientation(LinearLayout.VERTICAL);

                        TextView atributo = new TextView(this);
                        atributo.setText(attribute.getNameAttribute());
                        //Log.d("TEST", atributo.getText().toString());
                        atributo.setTextSize(14);
                        atributo.setTextColor(Color.BLUE);
                        atributo.setPadding(0, 8, 0, 0);
                        atributo.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        atributo.setGravity(Gravity.CENTER_HORIZONTAL);
                        if (attribute.getNameAttribute().compareTo("") == 0)
                            atributo.setVisibility(View.GONE);


                        for (FormSubSystemItemAttributeValues values : attribute.getValuesList()) {
                            if (values.getValueState() != null && values.getTypeValue().compareTo("CHECK") == 0) {
                                List<CheckBox> checkBoxes = new ArrayList<>();

                                LinearLayout checkboxLayout = new LinearLayout(this);
                                checkboxLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                checkboxLayout.setOrientation(LinearLayout.VERTICAL);

                                /*
                                Este WHILE lo que hace es dibujar a los mas 2 checkbox por linea, para evitar que queden
                                elementos fuera de la vista.
                                 */

                                int count = 0;
                                while (count < values.getValueState().size()) {
                                    LinearLayout dump = new LinearLayout(this);
                                    dump.setOrientation(LinearLayout.HORIZONTAL);
                                    dump.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    for (int p = 0; p < 2; p++) {
                                        if (count < values.getValueState().size()) {
                                            String state = values.getValueState().get(count);
                                            CheckBox cb = new CheckBox(this);
                                            String id = System.getIdSystem()+subSystem.getIdSubSystem()+item.getIdItem()+atributo.getId()+values.getValueState().get(count);
                                            cb.setId(Funciones.str2int(id));
                                            cb.setChecked(reg.getBoolean("CHECK"+cb.getId()));
                                            cb.setText(state);
                                            checkBoxes.add(cb);
                                            dump.addView(cb);
                                            count++;
                                            vistas.add(cb);
                                        }
                                    }
                                    checkboxLayout.addView(dump);

                                }
                                makeOnlyOneCheckable(checkBoxes);
                                values.setCheckBoxes(checkBoxes);
                                valuesLayout.addView(atributo);
                                valuesLayout.addView(checkboxLayout);

                            }
                            if (values.getTypeValue().compareTo("TEXT") == 0) {
                                EditText campo = new EditText(this);
                                String id = System.getIdSystem()+subSystem.getIdSubSystem()+item.getIdItem()+atributo.getId()+"";
                                campo.setId(Funciones.str2int(id));
                                campo.setText(reg.getString("TEXT" + campo.getId()));
                                campo.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                campo.setBackgroundResource(R.drawable.fondo_edittext);
                                valuesLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                valuesLayout.addView(atributo);
                                valuesLayout.addView(campo);
                                values.setEditText(campo);
                                vistas.add(campo);
                            }

                        }
                        atributosLayout.addView(valuesLayout);
                    }
                    atributosLayout.setLayoutParams(layoutParams);
                    itemLayout.addView(atributosLayout);

                    //contenido.addView(itemLayout);
                    bottom.addView(itemLayout);
                }

                LinearLayout horizontal = new LinearLayout(this);
                horizontal.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                LinearLayout.LayoutParams left = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams right = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                left.weight = 2;
                right.weight = 1;
                right.setMargins(1,0,0,0);

                Button foto = new Button(this);
                Button verfoto = new Button(this);

                foto.setText("Agregar Foto");
                foto.setBackgroundResource(R.drawable.custom_button_blue_left);
                foto.setTextColor(Color.WHITE);
                foto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imageTmp = new FormImage(System.getIdSystem(), subSystem.getIdSubSystem(), null, null);
                        tomarFoto();
                    }
                });


                verfoto.setText("Ver Foto");
                verfoto.setBackgroundResource(R.drawable.custom_button_blue_right);
                verfoto.setTextColor(Color.WHITE);
                verfoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FormImage tmp = findImg(System.getIdSystem(), subSystem.getIdSubSystem());
                        if (tmp == null) {
                            Toast.makeText(mContext, "No hay imagen registrada", Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog.Builder bi = new AlertDialog.Builder(mContext);
                            bi.setNeutralButton("Cerrar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            ImageView iv = new ImageView(mContext);
                            iv.setImageBitmap(tmp.getImage());

                            bi.setView(iv);
                            bi.show();
                        }
                    }
                });

                horizontal.addView(foto, left);
                horizontal.addView(verfoto, right);
                bottom.addView(horizontal);

                loadData(System.getIdSystem(), subSystem.getIdSubSystem());
            }
            subcontenidos.add(bottom);
            contenido.addView(bottom);
            System.setContenido(bottom);

            buttonSystem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bottom.getVisibility() == View.GONE) {
                        bottom.setVisibility(View.VISIBLE);
                        for (LinearLayout l : subcontenidos) {
                            if (l != bottom)
                                l.setVisibility(View.GONE);
                        }
                        scrollViewMain.smoothScrollTo(0, bottom.getTop());
                    } else {
                        bottom.setVisibility(View.GONE);
                    }
                }
            });


        }
    }

    private void loadData(int system, int subSystem){
        String id1 = "FOTOBITMAP"+system+subSystem;
        String id2 = "FOTONAME"+system+subSystem;
        String id3 = "FOTOCOMMENT"+system+subSystem;
        FormImage tmp = new FormImage(system, subSystem, reg.getString(id3),reg.getString(id2));

        if(reg.getString(id1).equals(""))
            return;

        tmp.setImage(Funciones.decodeBase64(reg.getString(id1)));
        imagenes.add(tmp);
    }

    private void saveData(){
        for(FormImage img: imagenes){
            if(img.getImage() != null) {
                String id1 = "FOTOBITMAP" + img.getIdSystem() + img.getIdSubSystem();
                String id2 = "FOTONAME" + img.getIdSystem() + img.getIdSubSystem();
                String id3 = "FOTOCOMMENT" + img.getIdSystem() + img.getIdSubSystem();

                reg.addValue(id1, Funciones.encodeTobase64(img.getImage()));
                reg.addValue(id2, img.getName());
                reg.addValue(id3, img.getComment());
            }
        }
        for(View v: vistas){
            if(v instanceof CheckBox ){
                reg.addValue("CHECK" + v.getId(), ((CheckBox) v).isChecked());
            }
            if(v instanceof EditText){
                reg.addValue("TEXT" + v.getId(), ((EditText) v).getText().toString());
            }
        }
    }

    private FormImage findImg(int idSystem, int idSubSystem) {
        for(FormImage img: imagenes){
            if(img.getIdSystem() == idSystem && img.getIdSubSystem() == idSubSystem)
                return img;
        }
        return null;
    }


    /*
    TODO Obtener Formulario
     */
    private class ObtenerFormulario extends AsyncTask<String, String, FormularioCheck> {
        private final String ASYNCTAG = "OBTENERFORMULARIO";
        Context context;
        ProgressDialog dialog;

        public ObtenerFormulario(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage("Obteniendo Formulario...");
            dialog.show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected FormularioCheck doInBackground(String... params) {
            try {
                Log.w("FORMCHECK", Response);
                FormularioCheck parse = XMLParser.getForm(Response);
                return parse;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(ASYNCTAG, e.getMessage() + ": " + e.getCause());
                return null;
            }
        }

        @Override
        protected void onPostExecute(FormularioCheck response) {
            if (response != null) {
                if (response.getCode().compareTo("0") == 0) {
                    dibujarVista(response);
                    formulario = response;
                } else {
                    Toast.makeText(context, response.getDescription(), Toast.LENGTH_LONG).show();
                    FormCheckActivity.this.finish();
                }
            } else {
                Toast.makeText(context, "Ha ocurrido un error, por favor reintente", Toast.LENGTH_LONG).show();
                FormCheckActivity.this.finish();
            }
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

    /*
   TODO Enviar Formulario
    */
    private class Enviar extends AsyncTask<String, String, ArrayList<String>> {
        private final String ASYNCTAG = "OBTENERFORMULARIO";
        Context context;
        ProgressDialog dialog;

        public Enviar(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage("Enviando Formulario...");
            dialog.show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            try {
                String request = SoapRequest.FormSave(formulario, imagenes);
                Log.d("ENVIANDOFORM", request);
                ArrayList<String> parse = XMLParser.getReturnCode1(request);
                return parse;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(ASYNCTAG, e.getMessage() + ": " + e.getCause());
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<String> response) {
            if (response != null) {
                if (response.get(0).compareTo("0") == 0) {
                    Toast.makeText(context, response.get(1), Toast.LENGTH_LONG).show();

                    MaintenanceReg m = new MaintenanceReg(getApplicationContext());
                    m.clearPreferences();
                    reg.clearPreferences();

                    if (AgendaActivity.actividad != null) {
                        AgendaActivity.actividad.finish();
                    }
                    FormCheckActivity.this.finish();

                } else {
                    Toast.makeText(context, response.get(1), Toast.LENGTH_LONG).show();
                    FormCheckActivity.this.finish();
                }
            } else {
                Toast.makeText(context, "Ha ocurrido un error, por favor reintente", Toast.LENGTH_LONG).show();

            }
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

    private void tomarFoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        int code = 0;
        imageTmp.newName(idMantenimiento);
        Uri output = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/TDC@/" + imageTmp.getName()));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        startActivityForResult(intent, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ONRESULT", "Req" + requestCode + " Res" + resultCode);
        if (requestCode == 0) {
            if (resultCode == -1) {
                if (data != null) {
                    if (data.hasExtra("data")) {
                        imageTmp.setImage(Bitmap.createScaledBitmap((Bitmap) data.getParcelableExtra("data"), 640, 480, true));
                    }
                } else {
                    imageTmp.setImage(Bitmap.createScaledBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/TDC@/" + imageTmp.getName()), 640, 480, true));

                }
                View v = LayoutInflater.from(this).inflate(R.layout.view_fotocomment, null, false);
                final EditText comment = (EditText) v.findViewById(R.id.comentario);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(v);

                builder.setIcon(R.drawable.ic_camera);
                builder.setTitle("Comentario");
                builder.setPositiveButton("Guardar", null);
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        imageTmp = null;
                        dialogInterface.dismiss();
                    }
                });
                builder.setCancelable(false);

                final AlertDialog alert = builder.create();
                alert.setCanceledOnTouchOutside(false);
                alert.setCancelable(false);
                alert.show();
                alert.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (comment.getText().toString().length() == 0) {
                            Toast.makeText(getApplicationContext(), "Ingrese un comentario", Toast.LENGTH_LONG).show();
                            return;
                        }
                        imageTmp.setComment(comment.getText().toString());
                        imagenes.add(imageTmp);
                        imageTmp = null;
                        alert.dismiss();
                    }
                });
            }
        } else if (requestCode == 1) {
            Uri selectedImage = data.getData();
            InputStream is;
            try {
                is = getContentResolver().openInputStream(selectedImage);
                BufferedInputStream bis = new BufferedInputStream(is);
                b = BitmapFactory.decodeStream(bis);

            } catch (FileNotFoundException e) {
            }
        }

    }

    public void enviar_form(View v) {
        String TAG = "ENVIARFORM";
        for (FormSystem fs : formulario.getSystem()) {
            for (FormSubSystem fss : fs.getSubSystemList()) {
                for (FormSubSystemItem fssi : fss.getItemList()) {
                    for (FormSubSystemItemAttribute attribute : fssi.getAttributeList()) {
                        for (FormSubSystemItemAttributeValues value : attribute.getValuesList()) {
                            if (value.getTypeValue().compareTo("CHECK") == 0) {

                            }
                            if (value.getTypeValue().compareTo("TEXT") == 0) {
                                EditText campo = value.getEditText();
                                if (campo.getText().toString().compareTo("") == 0) {
                                    fs.getContenido().setVisibility(View.VISIBLE);
                                    campo.requestFocus();
                                    InputMethodManager lManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    lManager.showSoftInput(campo, 0);
                                    Toast.makeText(this, "Este campo es obligatorio.", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                        }
                    }
                }
            }

        }

        UploadImage up = new UploadImage(this);
        up.execute();


    }

    private class UploadImage extends AsyncTask<String, String, String> {

        private Context mContext;
        ProgressDialog dialog;

        public UploadImage(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            dialog.setMessage(values[0]);
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";

            for (FormImage img : imagenes) {
                try {
                    String fileName = img.getName();

                    publishProgress(fileName);
                    Log.i("ENVIANDO", fileName);
                    Log.i("ENVIANDO", "comentario: " + img.getComment());
                    Log.i("ENVIANDO", "system: " + img.getIdSystem());
                    Log.i("ENVIANDO", "subsystem: " + img.getIdSubSystem());
                    HttpURLConnection conn;
                    DataOutputStream dos;
                    String lineEnd = "\r\n";
                    String twoHyphens = "--";
                    String boundary = "*****";
                    int bytesRead, bytesAvailable, bufferSize;

                    File done = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName);
                    done.createNewFile();

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    img.getImage().compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();

                    FileOutputStream fos = new FileOutputStream(done);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();

                    if (!done.isFile())
                        Log.e("DownloadManager", "no existe");
                    else {
                        FileInputStream fileInputStream = new FileInputStream(done);
                        URL url = new URL(dummy.URL_UPLOAD_IMG_MAINTENANCE);

                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.setUseCaches(false);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                        conn.setRequestProperty("uploaded_file", fileName);

                        dos = new DataOutputStream(conn.getOutputStream());

                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);
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

                        if (response.contains("<MESSAGE>OK</MESSAGE>")) {
                            Log.d("ENVIANDO", "OK");
                            img.setSend(true);
                        } else {
                            Log.d("ENVIANDO", "NOK");
                        }
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
            dialog.setTitle("Subiendo imagenes");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            if (dialog.isShowing())
                dialog.dismiss();
            Enviar task = new Enviar(mContext);
            task.execute();
            super.onPostExecute(s);
        }


    }


}
