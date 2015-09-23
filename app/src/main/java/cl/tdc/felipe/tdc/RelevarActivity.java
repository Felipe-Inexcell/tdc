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
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import cl.tdc.felipe.tdc.extras.Funciones;
import cl.tdc.felipe.tdc.objects.Averia.Item;
import cl.tdc.felipe.tdc.objects.FormImage;
import cl.tdc.felipe.tdc.objects.Relevar.Modulo;
import cl.tdc.felipe.tdc.preferences.FormCheckReg;
import cl.tdc.felipe.tdc.webservice.SoapRequest1;
import cl.tdc.felipe.tdc.webservice.XMLParser;
import cl.tdc.felipe.tdc.webservice.dummy;

public class RelevarActivity extends Activity {
    public static Activity actividad;
    private Context context;
    ArrayList<Modulo> modulos;
    ArrayList<FormImage> imagenes = new ArrayList<>();
    FormImage imgTmp;
    LinearLayout contenido;
    int idRelevo = -100;
    ImageButton enviar;

    FormCheckReg reg;

    private ArrayList<Item> departamentos, provincias, distritos, estaciones;

    private String name;
    private Spinner depto, province, district, station;
    private static int TAKE_PICTURE = 1;

    ArrayList<View> vistas = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relevo);
        actividad = this;
        context = this;
        enviar = (ImageButton) findViewById(R.id.imageButton3);
        reg = new FormCheckReg(this, "RELEVAREG");


        if (reg.getBoolean("SEND")) {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setCancelable(false);
            b.setMessage("Desea llenar el Checklist recomendado pendiente?");
            b.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent in = new Intent(context, RelevarRecomendadoActivity.class);
                    in.putExtra("ID", reg.getInt("IDRELEVO"));
                    startActivityForResult(in, idRelevo);
                }
            });
            b.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    reg.clearPreferences();
                    init();
                    dialogInterface.dismiss();
                }
            });
            b.show();
        } else {
            name = Environment.getExternalStorageDirectory() + "/TDC@/captura.jpg";
            init();


        }
    }

    private void init() {

        contenido = (LinearLayout) findViewById(R.id.contenidoCheck);
        depto = (Spinner) findViewById(R.id.cb_dpto);
        province = (Spinner) findViewById(R.id.cb_prov);
        district = (Spinner) findViewById(R.id.cd_dist);
        station = (Spinner) findViewById(R.id.cb_station);

        depto.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Collections.EMPTY_LIST));
        province.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Collections.EMPTY_LIST));
        district.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Collections.EMPTY_LIST));
        station.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Collections.EMPTY_LIST));


        depto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int id = departamentos.get(i).getId();
                ObtenerProvince p = new ObtenerProvince((Activity) context, context);
                p.execute(id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int id = provincias.get(i).getId();
                ObtenerDistrict p = new ObtenerDistrict((Activity) context, context);
                p.execute(id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int id = distritos.get(i).getId();
                ObtenerStation p = new ObtenerStation((Activity) context, context);
                p.execute(id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        station.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int id = estaciones.get(i).getId();
                ObtenerChecklist p = new ObtenerChecklist((Activity) context, context);
                p.execute(id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ObtenerDeptos obtenerDeptos = new ObtenerDeptos(this, this);
        obtenerDeptos.execute();
    }

    // TODO: funcion onClick del botón apagar.

    public void onClick_apagar(View v) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage("¿Seguro que desea salir del CheckList?");
        b.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveData();
                actividad.finish();
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

    public void onClick_enviar(View v) {
        for (Modulo m : modulos) {
            for (cl.tdc.felipe.tdc.objects.Relevar.Item item : m.getItems()) {
                if (item.getValor().equals("NO RESPONDE") || item.getValor().length() == 0) {
                    Toast.makeText(this, "Debe responder el CheckList completo.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

        UploadImage task = new UploadImage(this, v);
        task.execute();

    }


    /**
     * Botón Cámara *
     */
    public void onClick_recomendado(View view) {
        if (idRelevo == -100) {
            Toast.makeText(this, "Debe enviar el CheckList actual para obtener acceso al recomendado", Toast.LENGTH_LONG).show();
        } else {
            Intent i = new Intent(this, RelevarRecomendadoActivity.class);
            i.putExtra("ID", idRelevo);
            startActivityForResult(i, idRelevo);
        }
    }


    public void tomarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        int code = TAKE_PICTURE;
        Uri output = Uri.fromFile(new File(name));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        startActivityForResult(intent, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == -1) {
                if (data != null) {
                    if (data.hasExtra("data")) {
                        imgTmp.setImage(Bitmap.createScaledBitmap((Bitmap) data.getParcelableExtra("data"), 640, 480, true));
                    }
                } else {
                    imgTmp.setImage(Bitmap.createScaledBitmap(BitmapFactory.decodeFile(name), 640, 480, true));

                }

                final EditText desc = new EditText(this);
                desc.setLines(1);
                desc.setMaxLines(1);

                desc.setBackgroundResource(R.drawable.fondo_edittext);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(desc);

                builder.setIcon(R.drawable.ic_camera);
                builder.setTitle("¿Qué fotografió?");
                builder.setPositiveButton("Guardar", null);
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        imgTmp = null;
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
                        if (desc.getText().toString().length() == 0) {
                            Toast.makeText(getApplicationContext(), "Ingrese una descripción de lo fotografiado", Toast.LENGTH_LONG).show();
                            return;
                        }
                        imgTmp.setComment(desc.getText().toString());
                        imgTmp.newNameRelevo(imgTmp.getIdSystem(), imgTmp.getComment());
                        imagenes.add(imgTmp);
                        imgTmp = null;
                        alert.dismiss();
                    }
                });
            }
        }

        if(requestCode == idRelevo){
            if(resultCode == Activity.RESULT_OK){
                reg.clearPreferences();
                ((Activity)context).finish();
            }
        }

    }

    private class ObtenerDeptos extends AsyncTask<String, String, String> {
        Activity esta;
        Context context;
        ProgressDialog progressDialog;
        boolean state = false;

        public ObtenerDeptos(Activity activity, Context context) {
            esta = activity;
            this.context = context;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Buscando Departamentos...");
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String query = SoapRequest1.getDepartament(telephonyManager.getDeviceId());
                ArrayList<String> parse = XMLParser.getReturnCode2(query);
                Log.d("ELEMENTS", query);
                if (parse.get(0).equals("0")) {
                    state = true;
                    return query;
                } else
                    return parse.get(1);

            } catch (IOException e) {
                e.printStackTrace();
                return "No se pudo conectar con el servidor, compruebe se conexión a internet y reintente";
            } catch (SAXException e) {
                e.printStackTrace();
                return "Error al leer el XML, por favor reintente";
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                return "Error al leer el XML, por favor reintente";
            } catch (XPathExpressionException e) {
                e.printStackTrace();
                return "Error al leer el XML, por favor reintente";
            } catch (Exception e) {
                e.printStackTrace();
                return "Ha ocurrido un error, por favor reintente";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (state) {
                try {
                    departamentos = XMLParser.getItem(s, "Department");

                    List<String> e = new ArrayList<>();
                    for (Item item : departamentos) {
                        e.add(item.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, e);
                    depto.setAdapter(adapter);

                    depto.setSelection(adapter.getPosition(reg.getString("SELECT" + depto.getId())));
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "No se pudo conectar con el servidor, compruebe se conexión a internet y reintente", Toast.LENGTH_LONG).show();
                    esta.finish();
                }catch (ParserConfigurationException e) {
                    Toast.makeText(getApplicationContext(), "Error al leer el XML, por favor reintente", Toast.LENGTH_LONG).show();
                    esta.finish();
                }catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Ha ocurrido un error, por favor reintente", Toast.LENGTH_LONG).show();
                    esta.finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                esta.finish();
            }


            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    private class ObtenerProvince extends AsyncTask<Integer, String, String> {
        Activity esta;
        Context context;
        ProgressDialog progressDialog;
        boolean state = false;

        public ObtenerProvince(Activity activity, Context context) {
            esta = activity;
            this.context = context;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Buscando Provincias...");
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Integer... strings) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String query = SoapRequest1.getProvince(telephonyManager.getDeviceId(), strings[0]);
                ArrayList<String> parse = XMLParser.getReturnCode2(query);
                if (parse.get(0).equals("0")) {
                    state = true;
                    return query;
                } else
                    return parse.get(1);

            } catch (Exception e) {
                Log.e("ELEMENTS", e.getMessage() + ": \n" + e.getCause());
                state = false;
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (state) {
                try {
                    provincias = XMLParser.getItem(s, "Province");

                    List<String> e = new ArrayList<>();
                    for (Item item : provincias) {
                        e.add(item.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, e);
                    province.setAdapter(adapter);

                    province.setSelection(adapter.getPosition(reg.getString("SELECT" + province.getId())));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    esta.finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                esta.finish();
            }


            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    private class ObtenerDistrict extends AsyncTask<Integer, String, String> {
        Activity esta;
        Context context;
        ProgressDialog progressDialog;
        boolean state = false;

        public ObtenerDistrict(Activity activity, Context context) {
            esta = activity;
            this.context = context;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Buscando Distritos...");
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Integer... strings) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String query = SoapRequest1.getDistrict(telephonyManager.getDeviceId(), strings[0]);
                ArrayList<String> parse = XMLParser.getReturnCode2(query);
                if (parse.get(0).equals("0")) {
                    state = true;
                    return query;
                } else
                    return parse.get(1);

            } catch (Exception e) {
                Log.e("ELEMENTS", e.getMessage() + ": \n" + e.getCause());
                state = false;
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (state) {
                try {
                    distritos = XMLParser.getItem(s, "District");

                    List<String> e = new ArrayList<>();
                    for (Item item : distritos) {
                        e.add(item.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, e);
                    district.setAdapter(adapter);
                    district.setSelection(adapter.getPosition(reg.getString("SELECT" + district.getId())));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    esta.finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                esta.finish();
            }


            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    private class ObtenerStation extends AsyncTask<Integer, String, String> {
        Activity esta;
        Context context;
        ProgressDialog progressDialog;
        boolean state = false;

        public ObtenerStation(Activity activity, Context context) {
            esta = activity;
            this.context = context;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Buscando Estaciones...");
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Integer... strings) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String query = SoapRequest1.getStation(telephonyManager.getDeviceId(), strings[0]);
                ArrayList<String> parse = XMLParser.getReturnCode2(query);
                if (parse.get(0).equals("0")) {
                    state = true;
                    return query;
                } else
                    return parse.get(1);

            } catch (Exception e) {
                Log.e("ELEMENTS", e.getMessage() + ": \n" + e.getCause());
                state = false;
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (state) {
                try {
                    estaciones = XMLParser.getItem(s, "Station");

                    List<String> e = new ArrayList<>();
                    for (Item item : estaciones) {
                        e.add(item.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, e);
                    station.setAdapter(adapter);
                    station.setSelection(adapter.getPosition(reg.getString("SELECT" + station.getId())));

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }


            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    private class ObtenerChecklist extends AsyncTask<Integer, String, String> {
        Activity esta;
        Context context;
        ProgressDialog progressDialog;
        boolean state = false;

        public ObtenerChecklist(Activity activity, Context context) {
            esta = activity;
            this.context = context;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Cargando Checklist...");
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Integer... strings) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String query = SoapRequest1.getCheckList(telephonyManager.getDeviceId(), strings[0]);
                ArrayList<String> parse = XMLParser.getReturnCode2(query);
                if (parse.get(0).equals("0")) {
                    state = true;
                    return query;
                } else
                    return parse.get(1);

            } catch (Exception e) {
                Log.e("CHECKLIST", e.getMessage() + ": \n" + e.getCause());
                state = false;
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (state) {
                try {
                    modulos = XMLParser.getRelevoCheck(s);
                    dibujarCheck();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }


            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    private void dibujarCheck() {

        contenido.removeAllViews();

        for (Modulo m : modulos) {
            TextView mTitulo = new TextView(this);
            mTitulo.setText(m.getName());
            mTitulo.setBackgroundColor(Color.parseColor("#226666"));
            mTitulo.setTextColor(Color.WHITE);
            mTitulo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mTitulo.setPadding(0, 6, 0, 6);
            mTitulo.setGravity(Gravity.CENTER_HORIZONTAL);
            contenido.addView(mTitulo);

            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            itemLayout.setBackgroundResource(R.drawable.fondo_1);
            itemLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            itemLayout.setOrientation(LinearLayout.VERTICAL);
            itemLayout.setPadding(16, 5, 16, 5);

            for (cl.tdc.felipe.tdc.objects.Relevar.Item item : m.getItems()) {
                TextView iTitulo = new TextView(this);
                iTitulo.setText(item.getName());
                iTitulo.setBackgroundColor(Color.GREEN);
                iTitulo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                iTitulo.setPadding(0, 8, 0, 4);
                iTitulo.setGravity(Gravity.CENTER_HORIZONTAL);

                View vista = getView(m.getId(), item);
                if (vista == null)
                    continue;

                itemLayout.addView(iTitulo);
                itemLayout.addView(vista);
                vistas.add(vista);
            }

            contenido.addView(itemLayout);

        }

        Button agregarFoto = new Button(this);
        agregarFoto.setText("Agregar Foto");
        agregarFoto.setBackgroundResource(R.drawable.custom_button_rounded_green);
        agregarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgTmp = new FormImage();
                imgTmp.setIdSystem(estaciones.get(station.getSelectedItemPosition()).getId());
                tomarFoto();
            }
        });
        contenido.addView(agregarFoto);


    }

    private View getView(int mId, cl.tdc.felipe.tdc.objects.Relevar.Item item) {
        String type = item.getType();
        List<String> values = item.getValues();
        LinearLayout contenido = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contenido.setLayoutParams(params);
        params.setMargins(0, 6, 0, 0);
        contenido.setOrientation(LinearLayout.VERTICAL);
        contenido.setGravity(Gravity.CENTER_HORIZONTAL);
        String comment = "";

        if (type.equals("SELECT")) {
            Spinner s = new Spinner(this);
            s.setBackgroundResource(R.drawable.spinner_bg);
            s.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, values);
            s.setAdapter(adapter);
            String id = mId + item.getId() + item.getName() + values.toString();
            s.setId(Funciones.str2int(id));
            String Selected = reg.getString("SELECT" + s.getId());
            s.setSelection(adapter.getPosition(Selected));
            contenido.addView(s);
            item.setVista(s);

            comment = reg.getString("COMMENTSELECT" + s.getId());

        } else if (type.equals("CHECK")) {
            LinearLayout checkboxLayout = new LinearLayout(this);
            checkboxLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            checkboxLayout.setOrientation(LinearLayout.VERTICAL);
            ArrayList<CheckBox> checkBoxes = new ArrayList<>();
            int count = 0;
            while (count < values.size()) {
                LinearLayout dump = new LinearLayout(this);
                dump.setOrientation(LinearLayout.HORIZONTAL);
                dump.setGravity(Gravity.CENTER_HORIZONTAL);
                dump.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                for (int p = 0; p < 3; p++) {
                    if (count < values.size()) {
                        String state = values.get(count);
                        CheckBox cb = new CheckBox(this);
                        String id = mId + item.getId() + item.getName() + values.get(count);
                        cb.setId(Funciones.str2int(id));
                        cb.setChecked(reg.getBoolean("CHECK" + cb.getId()));
                        cb.setText(state);

                        checkBoxes.add(cb);
                        dump.addView(cb);
                        if(count == 0)
                            comment = reg.getString("COMMENTCHECK"+cb.getId());
                        count++;
                        vistas.add(cb);
                    }
                }
                checkboxLayout.addView(dump);
            }
            makeOnlyOneCheckable(checkBoxes);
            item.setCheckBoxes(checkBoxes);
            contenido.addView(checkboxLayout);
        } else if (type.equals("NUM")) {
            EditText e = new EditText(this);
            e.setBackgroundResource(R.drawable.fondo_edittext);
            e.setInputType(InputType.TYPE_CLASS_NUMBER);

            String id = mId + item.getId() + item.getName();
            e.setId(Funciones.str2int(id));
            e.setText(reg.getString("TEXT" + e.getId()));
            e.setLayoutParams(new LinearLayout.LayoutParams(100, ViewGroup.LayoutParams.WRAP_CONTENT));
            e.setGravity(Gravity.CENTER_HORIZONTAL);
            vistas.add(e);
            item.setVista(e);
            contenido.addView(e);

            comment = reg.getString("COMMENTTEXT" + e.getId());
        } else if (type.equals("VARCHAR")) {
            EditText e = new EditText(this);
            e.setBackgroundResource(R.drawable.fondo_edittext);
            e.setInputType(InputType.TYPE_CLASS_TEXT);
            e.setLines(2);
            e.setGravity(Gravity.LEFT | Gravity.TOP);

            String id = mId + item.getId() + item.getName();
            e.setId(Funciones.str2int(id));
            e.setText(reg.getString("TEXT" + e.getId()));
            vistas.add(e);
            item.setVista(e);
            contenido.addView(e);
            comment = reg.getString("COMMENTTEXT" + e.getId());
        }

        if(!item.getName().equals("COMENTARIOS")) {
            EditText comentario = new EditText(this);
            comentario.setLayoutParams(params);
            comentario.setBackgroundResource(R.drawable.fondo_edittext);
            comentario.setLines(3);
            comentario.setText(comment);
            comentario.setHint("Observaciones");
            item.setDescription(comentario);
            contenido.addView(comentario);
        }
        return contenido;

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

    private void saveData() {

        reg.addValue("SELECT" + depto.getId(), depto.getSelectedItem().toString());
        reg.addValue("SELECT" + province.getId(), province.getSelectedItem().toString());
        reg.addValue("SELECT" + district.getId(), district.getSelectedItem().toString());
        reg.addValue("SELECT" + station.getId(), station.getSelectedItem().toString());


        for (Modulo m : modulos) {
            for (cl.tdc.felipe.tdc.objects.Relevar.Item i : m.getItems()) {
                View v = i.getVista();
                View c = i.getDescription();
                if (i.getCheckBoxes() != null) {
                    for (int x = 0; x < i.getCheckBoxes().size(); x++) {
                        CheckBox check = i.getCheckBoxes().get(x);
                        reg.addValue("CHECK" + check.getId(), check.isChecked());
                        if (x == 0)
                            reg.addValue("COMMENTCHECK" + check.getId(), ((EditText) c).getText().toString());
                    }
                }
                if (v instanceof Spinner) {
                    reg.addValue("SELECT" + v.getId(), ((Spinner) v).getSelectedItem().toString());
                    reg.addValue("COMMENTSELECT" + v.getId(), ((EditText) c).getText().toString());
                }
                if (v instanceof EditText) {
                    reg.addValue("TEXT" + v.getId(), ((EditText) v).getText().toString());
                    if(c != null)
                        reg.addValue("COMMENTTEXT" + v.getId(), ((EditText) c).getText().toString());
                }

            }
        }

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

    }

    private class Enviar extends AsyncTask<String, String, String> {
        Context context;
        ProgressDialog d;
        boolean ok = false;
        View boton;

        private Enviar(Context context, View v) {
            this.context = context;
            this.boton = v;
        }

        @Override
        protected void onPreExecute() {
            d = new ProgressDialog(context);
            d.setCancelable(false);
            d.setCanceledOnTouchOutside(false);
            d.setMessage("Enviando CheckList...");
            d.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            try {
                String query = SoapRequest1.sendCheckList(telephonyManager.getDeviceId(), estaciones.get(station.getSelectedItemPosition()).getId(), modulos, imagenes);

                ArrayList<String> response = XMLParser.getReturnCode2(query);

                ok = response.get(0).equals("0");
                if (ok) return query;
                else return response.get(1);

            } catch (SAXException e) {
                e.printStackTrace();
                return "Error al leer el XML";
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                return "Error al leer el XML";
            } catch (XPathExpressionException e) {
                e.printStackTrace();
                return "Error al leer el XML";
            } catch (IOException e) {
                e.printStackTrace();
                return "Error al leer el XML";
            } catch (Exception e) {
                e.printStackTrace();
                return "Ha ocurrido un error";
            }
        }

        @Override
        protected void onPostExecute(String s) {

            if (ok) {
                boton.setEnabled(false);
                reg.addValue("SEND", true);
                try {
                    idRelevo = XMLParser.getIdRelevo(s);
                    reg.addValue("IDRELEVO", idRelevo);
                    Toast.makeText(context, "Se envió correctamente", Toast.LENGTH_LONG).show();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
            }
            if (d.isShowing()) d.dismiss();
        }
    }

    private class UploadImage extends AsyncTask<String, String, String> {

        private Context mContext;
        ProgressDialog dialog;
        View view;

        public UploadImage(Context mContext, View v) {
            this.view = v;
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
                        URL url = new URL(dummy.URL_R_SEND_CHECK_IMGS);

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
            Enviar task = new Enviar(mContext, view);
            task.execute();
            super.onPostExecute(s);
        }


    }

}
