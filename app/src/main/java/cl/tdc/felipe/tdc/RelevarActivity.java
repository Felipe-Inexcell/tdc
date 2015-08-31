package cl.tdc.felipe.tdc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cl.tdc.felipe.tdc.daemon.PositionTrackerTDC;
import cl.tdc.felipe.tdc.extras.Funciones;
import cl.tdc.felipe.tdc.objects.Averia.Item;
import cl.tdc.felipe.tdc.objects.Relevar.Modulo;
import cl.tdc.felipe.tdc.webservice.SoapRequest1;
import cl.tdc.felipe.tdc.webservice.XMLParser;

/**
 * Created by Felipe on 13/02/2015.
 */
public class RelevarActivity extends Activity {
    public static Activity actividad;
    private Context context;
    private PositionTrackerTDC trackerTDC;
    ArrayList<Modulo> modulos;
    LinearLayout contenido;

    private ArrayList<Item> departamentos, provincias, distritos, estaciones;

    private String name;
    private Spinner depto, province, district, station;
    private Bitmap b = null, bmini = null;
    private static int TAKE_PICTURE = 1;

    ArrayList<View> vistas = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("AVERIA", "onResume");
        Intent intent = new Intent(this, PositionTrackerTDC.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("AVERIA", "onPause");
        unbindService(mConnection);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            PositionTrackerTDC.MyBinder b = (PositionTrackerTDC.MyBinder) iBinder;
            trackerTDC = b.getService();
            Log.d("AVERIA", "SERVICE CONNECTED");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            trackerTDC = null;
            Log.d("AVERIA", "SERVICE DISCONNECTED");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relevo);
        actividad = this;
        context = this;
        name = Environment.getExternalStorageDirectory() + "/TDC@/captura.jpg";
        init();
    }

    private void init() {

        contenido = (LinearLayout) findViewById(R.id.relevo_content);
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
        MainActivity.actividad.finish();
        finish();
    }

    public void onClick_back(View v) {
        finish();
    }

    public void onClick_enviar(View v) {


    }


    /**
     * Botón Cámara *
     */
    public void onClick_recomendado(View view) {
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
        if (requestCode == TAKE_PICTURE) {
            if (data != null) {
                if (data.hasExtra("data")) {
                    b = (Bitmap) data.getParcelableExtra("data");
                }
            } else {
                b = BitmapFactory.decodeFile(name);

            }
        }
        try {
            //b = Bitmap.createScaledBitmap(b, 640, 480, true);
            bmini = Bitmap.createScaledBitmap(b, 64, 64, true);
        } catch (Exception ex) {
            ex.printStackTrace();
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

            } catch (Exception e) {
                Log.e("ELEMENTS", e.getMessage() + ": \n" + e.getCause());
                return e.getMessage();
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
                    Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();

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

        for (Modulo m : modulos) {
            TextView mTitulo = new TextView(this);
            mTitulo.setText(m.getName());
            mTitulo.setBackgroundColor(Color.parseColor("#226666"));
            mTitulo.setTextColor(Color.WHITE);
            mTitulo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mTitulo.setPadding(0,6,0,6);
            mTitulo.setGravity(Gravity.CENTER_HORIZONTAL);
            contenido.addView(mTitulo);

            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            itemLayout.setBackgroundResource(R.drawable.fondo_1);
            itemLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            itemLayout.setOrientation(LinearLayout.VERTICAL);
            itemLayout.setPadding(16,5,16,5);

            for (cl.tdc.felipe.tdc.objects.Relevar.Item item : m.getItems()) {
                TextView iTitulo = new TextView(this);
                iTitulo.setText(item.getName());
                iTitulo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                iTitulo.setPadding(0,8,0,4);
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


    }

    private View getView(int mId, cl.tdc.felipe.tdc.objects.Relevar.Item item) {
        String type = item.getType();
        List<String> values = item.getValues();
        if (type.equals("SELECT")) {
            Spinner s = new Spinner(this);
            s.setBackgroundResource(R.drawable.spinner_bg);
            s.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, values);
            s.setAdapter(adapter);

            return s;
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
                        String id = mId + item.getName() + values.get(count);
                        cb.setId(Funciones.str2int(id));
                        //cb.setChecked(reg.getBoolean("CHECK"+cb.getId()));
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
            return checkboxLayout;
        } else if (type.equals("NUM")) {
            EditText e = new EditText(this);
            e.setBackgroundResource(R.drawable.fondo_edittext);
            e.setInputType(InputType.TYPE_CLASS_NUMBER);
            vistas.add(e);
            return e;
        }else if (type.equals("VARCHAR")) {
            EditText e = new EditText(this);
            e.setBackgroundResource(R.drawable.fondo_edittext);
            e.setInputType(InputType.TYPE_CLASS_TEXT);
            e.setLines(2);
            e.setGravity(Gravity.LEFT | Gravity.TOP);
            vistas.add(e);
            return e;
        } else {
            return null;
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
}
