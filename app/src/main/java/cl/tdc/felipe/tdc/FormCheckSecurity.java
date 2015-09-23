package cl.tdc.felipe.tdc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
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

import com.github.gcacace.signaturepad.views.SignaturePad;

import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import cl.tdc.felipe.tdc.extras.Funciones;
import cl.tdc.felipe.tdc.objects.ControSeguridadDiario.Elemento;
import cl.tdc.felipe.tdc.objects.ControSeguridadDiario.Modulo;
import cl.tdc.felipe.tdc.objects.ControSeguridadDiario.SubModulo;
import cl.tdc.felipe.tdc.preferences.FormCheckReg;
import cl.tdc.felipe.tdc.preferences.FormCheckSecurityReg;
import cl.tdc.felipe.tdc.webservice.SoapRequestCheckLists;
import cl.tdc.felipe.tdc.webservice.XMLParserChecklists;
import cl.tdc.felipe.tdc.webservice.dummy;

public class FormCheckSecurity extends Activity {
    Context mContext;
    String Response;
    ArrayList<Modulo> formulario;
    ArrayList<Modulo> modulos;
    ScrollView scrollViewMain;
    Bitmap firma;
    public static Activity actividad;
    Bundle savedInstances;

    FormCheckReg reg;

    ArrayList<View> vistas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formchecksecurity);

        reg = new FormCheckReg(this,"SECURITYREG");

        actividad = this;

        if(savedInstanceState != null){
            savedInstances = savedInstanceState;
        }else{
            savedInstances = null;
        }
        mContext = this;
        scrollViewMain = (ScrollView)findViewById(R.id.cerca_content);
        Response = getIntent().getStringExtra("RESPONSE");
        ObtenerFormulario init = new ObtenerFormulario(this);
        init.execute();
    }

    public void onClick_apagar(View v) {AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage("¿Seguro que desea salir del CheckList?");
        b.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveData();
                ((Activity)mContext).finish();
                if(AgendaActivity.actividad!=null)
                    AgendaActivity.actividad.finish();
                if(MainActivity.actividad!= null)
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

    }

    public void enviar_form(View v){
        if(firma == null) {
            Toast.makeText(mContext, "Por favor registre su firma.", Toast.LENGTH_LONG).show();
            return;
        }

        Enviar task = new Enviar(this);
        task.execute();

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
    private void dibujarVista(ArrayList<Modulo> form) {
        LinearLayout contenido = (LinearLayout) this.findViewById(R.id.contenido);
        final List<LinearLayout> subcontenidos = new ArrayList<>();

        for(Modulo modulo : form){
            LinearLayout lModulotitle = new LinearLayout(this);
            lModulotitle.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            lModulotitle.setOrientation(LinearLayout.VERTICAL);

            LinearLayout lModulo = new LinearLayout(this);
            lModulo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            lModulo.setOrientation(LinearLayout.VERTICAL);
            lModulo.setBackgroundResource(R.drawable.fondo_general_bottom);
            lModulo.setPadding(10,6,10,6);

            TextView tModulo = new TextView(this);
            tModulo.setBackgroundResource(R.drawable.titlebar);
            tModulo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tModulo.setGravity(Gravity.CENTER_HORIZONTAL);
            tModulo.setTextAppearance(this, android.R.style.TextAppearance_Medium);
            tModulo.setTextColor(getResources().getColor(android.R.color.white));
            tModulo.setText(modulo.getName());
            lModulotitle.addView(tModulo);

            for(SubModulo subModulo: modulo.getSubModulos()){

                LinearLayout lSubModulo = new LinearLayout(this);
                lSubModulo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                lSubModulo.setOrientation(LinearLayout.VERTICAL);
                lSubModulo.setGravity(Gravity.CENTER_HORIZONTAL);
                lSubModulo.setBackgroundResource(R.drawable.fondo_1);

                TextView tSubModulo = new TextView(this);
                tSubModulo.setText(subModulo.getName());
                tSubModulo.setBackgroundColor(Color.parseColor("#226666"));
                tSubModulo.setTextAppearance(this, android.R.style.TextAppearance_Small);
                tSubModulo.setTextColor(getResources().getColor(android.R.color.white));
                tSubModulo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                tSubModulo.setGravity(Gravity.CENTER_HORIZONTAL);
                lSubModulo.addView(tSubModulo);



                for(final Elemento elemento : subModulo.getElementos()){

                    LinearLayout lElemento = new LinearLayout(this);
                    lElemento.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    lElemento.setOrientation(LinearLayout.VERTICAL);
                    lElemento.setGravity(Gravity.CENTER_HORIZONTAL);
                    lElemento.setPadding(24,8,24,8);

                    TextView tElemento = new TextView(this);
                    tElemento.setText(elemento.getName());
                    tElemento.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    tElemento.setGravity(Gravity.CENTER_HORIZONTAL);
                    lElemento.addView(tElemento);

                    if(elemento.getType().compareTo("CHECK") == 0){
                        LinearLayout checkboxLayout = new LinearLayout(this);
                        checkboxLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        checkboxLayout.setOrientation(LinearLayout.VERTICAL);

                        ArrayList<CheckBox> checkBoxes = new ArrayList<>();
                        int count = 0;
                        while(count < elemento.getValues().size()){
                            LinearLayout dump = new LinearLayout(this);
                            dump.setOrientation(LinearLayout.HORIZONTAL);
                            dump.setGravity(Gravity.CENTER_HORIZONTAL);
                            dump.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            for(int p = 0; p < 3; p++){
                                if(count < elemento.getValues().size()) {

                                    String state = elemento.getValues().get(count);
                                    CheckBox cb = new CheckBox(this);
                                    String id = modulo.getId()+subModulo.getId()+elemento.getId()+elemento.getName()+elemento.getValues().get(count);
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
                        lElemento.addView(checkboxLayout);

                        elemento.setCheckBoxes(checkBoxes);
                    }
                    if(elemento.getType().compareTo("TEXT") == 0){
                        EditText campo = new EditText(this);
                        String id = modulo.getId()+subModulo.getId()+elemento.getId()+elemento.getName();
                        campo.setId(Funciones.str2int(id));
                        campo.setText(reg.getString("TEXT"+campo.getId()));
                        campo.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        campo.setBackgroundResource(R.drawable.fondo_edittext);
                        lElemento.addView(campo);
                        elemento.setEditText(campo);
                        vistas.add(campo);
                    }
                    if(elemento.getType().compareTo("FIRMA")== 0){
                        LinearLayout firmaLayout = new LinearLayout(this);
                        firmaLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        firmaLayout.setOrientation(LinearLayout.HORIZONTAL);
                        firmaLayout.setGravity(Gravity.CENTER_HORIZONTAL);

                        Button firmar = new Button(this);
                        final Button verFirma = new Button(this);


                        firmar.setBackgroundResource(R.drawable.custom_button_rounded_green);
                        verFirma.setBackgroundResource(R.drawable.custom_button_rounded_green);

                        firmar.setText("Firmar");
                        verFirma.setText("Ver Firma");

                        verFirma.setEnabled(false);

                        firmar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                View v = LayoutInflater.from(FormCheckSecurity.this).inflate(R.layout.view_signature, null, false);

                                final SignaturePad pad = (SignaturePad) v.findViewById(R.id.signature_pad);

                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                                builder.setView(v);
                                builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        DateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                                        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                                        firma = pad.getSignatureBitmap();
                                        elemento.setFileName(telephonyManager.getDeviceId()+"_"+format.format(new Date())+".png");
                                        elemento.setFirma(firma);
                                        verFirma.setEnabled(true);
                                    }
                                });
                                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.setNeutralButton("Borrar", null);
                                final AlertDialog dialog = builder.create();
                                dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                                    @Override
                                    public void onShow(DialogInterface d) {

                                        Button b = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                                        b.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View view) {
                                                pad.clear();
                                            }
                                        });
                                    }
                                });

                                dialog.show();
                            }
                        });

                        verFirma.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                                b.setNeutralButton("Cerrar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                b.setNegativeButton("Borrar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        firma = null;
                                        elemento.setFirma(null);
                                        elemento.setFileName(null);
                                        verFirma.setEnabled(false);
                                        Toast.makeText(mContext, " Se  ha eliminado la firma.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                View v = LayoutInflater.from(mContext).inflate(R.layout.view_signature_preview, null, false);
                                ImageView IVpreview = (ImageView) v.findViewById(R.id.preview);
                                IVpreview.setImageBitmap(firma);
                                b.setView(v);
                                b.setTitle("Vista Previa Firma");
                                b.show();
                            }
                        });

                        String tmp = reg.getString("FIRMA");
                        if(!tmp.equals("")){
                            firma = Funciones.decodeBase64(tmp);
                            verFirma.setEnabled(true);
                        }
                        firmaLayout.addView(firmar);
                        firmaLayout.addView(verFirma);



                        lElemento.addView(firmaLayout);
                    }

                    lSubModulo.addView(lElemento);

                }

                lModulo.addView(lSubModulo);
            }

            contenido.addView(lModulotitle);
            contenido.addView(lModulo);
        }


    }



    private View addItem(String type) {
        LinearLayout l = new LinearLayout(this);
        switch (type){
            case "CHECK":
                break;
            case "TEXT":
                break;
            case "FIRMA":
                break;
        }

        return l;

    }



    /*
    TODO Obtener Formulario
     */
    private class ObtenerFormulario extends AsyncTask<String, String, ArrayList<Modulo>> {
        private final String ASYNCTAG = "OBTENERFORMULARIO";
        Context context;
        ProgressDialog dialog;
        String mensaje = "";

        public ObtenerFormulario(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage("Obteniendo Checklist de Seguridad...");
            dialog.show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<Modulo> doInBackground(String... params) {
            try {
                Log.w("FORMCHECK", Response);
                ArrayList<Modulo> m = XMLParserChecklists.getChecklistDaily(Response);
                return m;
            } catch (IOException e) {
                e.printStackTrace();
                mensaje = dummy.ERROR_CONNECTION;
                Log.e(ASYNCTAG, e.getMessage() + ": " + e.getCause());
            } catch (ParserConfigurationException | SAXException | XPathExpressionException e) {
                mensaje = dummy.ERROR_PARSE;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Modulo> response) {
            if (response != null) {
                    dibujarVista(response);
                    formulario = response;
                    modulos = response;
            } else {
                Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
                FormCheckSecurity.this.finish();
            }
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

    /*
   TODO Enviar Formulario
    */
    private class Enviar extends AsyncTask<String, String, String> {
        private final String ASYNCTAG = "ENVIARFORMULARIO";
        Context context;
        ProgressDialog dialog;
        boolean ok = false;

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
        protected String doInBackground(String... params) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String request = SoapRequestCheckLists.sendDailyActivities(telephonyManager.getDeviceId(), formulario);
                Log.d("ENVIANDOFORM", request);
                String[] parse = XMLParserChecklists.getResultCode(request).split(";");

                if(parse[0].compareTo("0")==0){
                    ok = true;
                    return parse[1];
                }else{
                    ok = false;
                    return parse[1];
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(ASYNCTAG, e.getMessage() + ": " + e.getCause());
                return e.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(mContext, response, Toast.LENGTH_LONG).show();
            if(ok){
                for(Modulo m: formulario){
                    for(SubModulo sm: m.getSubModulos()){
                        for(Elemento e: sm.getElementos()){
                            if(e.getType().compareTo("FIRMA")==0){
                                UploadImageSec uis = new UploadImageSec(mContext, e.getFileName(), e.getFirma());
                                uis.execute(dummy.URL_UPLOAD_IMG_SECURITY);

                            }
                        }
                    }
                }
                reg.clearPreferences();
            }

            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

    private class UploadImageSec extends AsyncTask<String, String, String> {

        private Context mContext;
        Bitmap imagen;
        String Filename;
        ProgressDialog dialog;

        public UploadImageSec(Context mContext, String FileName, Bitmap img) {
            this.mContext = mContext;
            this.imagen = img;
            this.Filename = FileName;
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";

            DateFormat timestamp_name = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

                try {


                    Log.i("ENVIANDO", Filename);
                    HttpURLConnection conn;
                    DataOutputStream dos;
                    String lineEnd = "\r\n";
                    String twoHyphens = "--";
                    String boundary = "*****";
                    int bytesRead, bytesAvailable, bufferSize;

                    File done = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), Filename);
                    done.createNewFile();

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    imagen.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
                    FileOutputStream fos = new FileOutputStream(done);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();


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
                        conn.setRequestProperty("uploaded_file", Filename);

                        dos = new DataOutputStream(conn.getOutputStream());

                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + Filename + "\"" + lineEnd);
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

                    }


                } catch (Exception e) {
                    Log.d("TAG", "Error: " + e.getMessage());
                    response = "ERROR";
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
            ((Activity)mContext).finish();
            super.onPostExecute(s);
        }


    }

    private void saveData(){
        if(firma != null){
            reg.addValue("FIRMA", Funciones.encodeTobase64(firma));
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



}
