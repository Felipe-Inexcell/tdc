package cl.tdc.felipe.tdc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import cl.tdc.felipe.tdc.daemon.MyLocationListener;
import cl.tdc.felipe.tdc.daemon.PositionTrackerTDC;
import cl.tdc.felipe.tdc.objects.Averia.Item;
import cl.tdc.felipe.tdc.preferences.PreferencesTDC;
import cl.tdc.felipe.tdc.webservice.SoapRequest;
import cl.tdc.felipe.tdc.webservice.XMLParser;
import cl.tdc.felipe.tdc.webservice.dummy;

public class AveriaActivity extends Activity {
    public static Activity actividad;
    private Context context;
    private PositionTrackerTDC trackerTDC;

    private ArrayList<Item> departamentos, provincias, distritos, estaciones;
    private String name;
    private Spinner elementos, siniestros, depto, province, district, station;
    private Bitmap b = null, bmini = null;
    private static int TAKE_PICTURE = 1;
    private static int SELECT_PICTURE = 2;
    final CharSequence[] opcionCaptura = {
            "Tomar Fotografía"
    };

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
        setContentView(R.layout.activity_averia);
        actividad = this;
        context = this;
        name = Environment.getExternalStorageDirectory() + "/TDC@/captura.jpg";
        init();
    }

    private void init() {
        elementos = (Spinner) findViewById(R.id.cb_elemento);
        elementos.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Collections.EMPTY_LIST));
        siniestros = (Spinner) findViewById(R.id.cb_siniestro);
        siniestros.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Collections.EMPTY_LIST));
        depto = (Spinner) findViewById(R.id.cb_dpto);
        province = (Spinner) findViewById(R.id.cb_prov);
        district = (Spinner) findViewById(R.id.cd_dist);
        station = (Spinner) findViewById(R.id.cb_station);

        depto.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Collections.EMPTY_LIST));
        province.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Collections.EMPTY_LIST));
        district.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Collections.EMPTY_LIST));
        station.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Collections.EMPTY_LIST));

        elementos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ObtenerSiniestros os = new ObtenerSiniestros(context);
                os.execute((String) adapterView.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ObtenerDeptos obtenerDeptos = new ObtenerDeptos(this, this);
        obtenerDeptos.execute();

        ObtenerElementos o = new ObtenerElementos(this, this);
        o.execute();

    }

    // TODO: funcion onClick del botón apagar.

    public void onClick_apagar(View v) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage("¿Seguro que desea cerrar Notificar Avería?");
        b.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (MainActivity.actividad != null) {
                    MainActivity.actividad.finish();
                }
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
        //super.onBackPressed();
        onClick_back(null);
    }

    public void onClick_back(View v) {
        InputMethodManager imm = (InputMethodManager) actividad.getSystemService(Context.INPUT_METHOD_SERVICE);
        View foco = actividad.getCurrentFocus();
        if (foco == null || !imm.hideSoftInputFromWindow(foco.getWindowToken(), 0)) {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setMessage("¿Seguro que desea cerrar Notificar Avería?");
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

    public void onClick_enviar(View v) {


        //Obtenemos Datos del formulario
        final String sin = siniestros.getSelectedItem().toString();
        if (sin == null || sin.length() == 0) {
            Toast.makeText(context, "Seleccione un Siniestro.", Toast.LENGTH_LONG).show();
            return;
        }
        final String elem = elementos.getSelectedItem().toString();
        if (elem == null || elem.length() == 0) {
            Toast.makeText(context, "Seleccione un Elemento", Toast.LENGTH_LONG).show();
            return;
        }
        final String comen = ((TextView) findViewById(R.id.et_comentario)).getText().toString();
        if (comen == null || comen.length() == 0) {
            Toast.makeText(context, "Ingrese un Comentario.", Toast.LENGTH_LONG).show();
            return;
        }

        if (b == null) {

            Toast.makeText(context, "Debe ingresar una Fotografía", Toast.LENGTH_LONG).show();
            return;
        }

        final String estat = station.getSelectedItem().toString();
        if (estat == null || estat.length() == 0) {
            Toast.makeText(context, "Seleccione una Estación.", Toast.LENGTH_LONG).show();
            return;
        }

        //Debemos Checkear que este lo "no" opcional

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_averia);
        dialog.setTitle("¿Todo Correcto?");


        // set the custom dialog components - text, image and button
        TextView elemento = (TextView) dialog.findViewById(R.id.dialogaveria_elemento);
        TextView siniestro = (TextView) dialog.findViewById(R.id.dialogaveria_siniestro);
        TextView comentario = (TextView) dialog.findViewById(R.id.dialogaveria_comentario);
        TextView estacion = (TextView) dialog.findViewById(R.id.dialogaveria_estacion);
        elemento.setText(elem);
        siniestro.setText(sin);
        comentario.setText(comen);
        estacion.setText(estat);
        ImageView image = (ImageView) dialog.findViewById(R.id.dialogaveria_captura);
        image.setImageBitmap(b);

        ImageButton dialogOk = (ImageButton) dialog.findViewById(R.id.dialogaveria_ok);
        ImageButton dialogNok = (ImageButton) dialog.findViewById(R.id.dialogaveria_nok);
        // if button is clicked, close the custom dialog
        dialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviar(estaciones.get(station.getSelectedItemPosition()).getId(), elem, sin, comen, b);
                dialog.dismiss();
            }
        });

        dialogNok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void enviar(int station, String elemento, String siniestro, String comentario, Bitmap imagen) {
        EnviarAveria a = new EnviarAveria(this, station, elemento, siniestro, comentario, imagen);
        Log.d("ENVIANDO", elemento + "-" + siniestro + "-" + station);
        a.execute();
    }

    /**
     * Botón Cámara *
     */
    public void onClick_foto(View view) {

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
        } else if (requestCode == SELECT_PICTURE) {
            Uri selectedImage = data.getData();
            InputStream is;
            try {
                is = getContentResolver().openInputStream(selectedImage);
                BufferedInputStream bis = new BufferedInputStream(is);
                b = BitmapFactory.decodeStream(bis);

            } catch (FileNotFoundException e) {
            }
        }
        try {
            //b = Bitmap.createScaledBitmap(b, 640, 480, true);
            bmini = Bitmap.createScaledBitmap(b, 64, 64, true);
        } catch (Exception ex) {
        }


    }


    private class EnviarAveria extends AsyncTask<String, String, ArrayList<String>> {
        private String elemento, siniestro, comentario;
        private Bitmap imagen;
        private Context mContext;
        private ProgressDialog progressDialog;
        private int station;
        boolean uploadOK = false;

        public EnviarAveria(Context context, int st, String e, String s, String c, Bitmap i) {
            this.station = st;
            this.elemento = e;
            this.siniestro = s;
            this.comentario = c;
            this.imagen = i;
            this.mContext = context;
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Enviando averia...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            progressDialog.setMessage(values[0]);
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            String response = "";
            DateFormat timestamp_name = new SimpleDateFormat("yyyyMMddHHmmss");
            String fileName = siniestro + "_" + station + "_" + timestamp_name.format(new Date()) + ".png";
            publishProgress("Subiendo imagen:\n" + fileName);
            try {

                Log.i("ENVIANDO", fileName);
                HttpURLConnection conn;
                DataOutputStream dos;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;

                File done = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName);
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
                    URL url = new URL(dummy.URL_UPLOAD_IMG);

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
                        uploadOK = true;
                    }
                }


            } catch (Exception e) {
                uploadOK = false;
                Log.d("TAG", "Error: " + e.getMessage());
            }

            if (uploadOK) {
                try {
                    publishProgress("Enviando Avería...");
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String query = SoapRequest.sendAveria(telephonyManager.getDeviceId(), station, elemento, siniestro, comentario, fileName, String.valueOf(trackerTDC.gps.getLatitude()), String.valueOf(trackerTDC.gps.getLongitude()));
                    ArrayList<String> parse = XMLParser.getElements(query);
                    Log.d("ENVIVANDO", parse.toString());
                    return parse;
                } catch (Exception e) {
                    Log.e("ENVIANDO ERROR", e.getMessage() + ":\n" + e.getCause());
                    return null;
                }
            } else {
                ArrayList<String> r = new ArrayList<>();
                r.add("-1");
                r.add("Ocurrio un problema al subir la imagen. Por favor reintente");
                return r;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            if (s != null) {
                if (s.get(0).compareTo("0") == 0) {
                    Toast.makeText(context, "Averia enviada con exito", Toast.LENGTH_LONG).show();
                    actividad.finish();
                } else {
                    Toast.makeText(context, s.get(1), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(context, "Error en la conexión.", Toast.LENGTH_LONG).show();
            }

            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

    private class ObtenerElementos extends AsyncTask<String, String, ArrayList<String>> {
        Activity esta;
        Context context;
        ProgressDialog progressDialog;

        public ObtenerElementos(Activity activity, Context context) {
            esta = activity;
            this.context = context;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Buscando Elementos...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String query = SoapRequest.getElements(telephonyManager.getDeviceId());
                Log.d("ELEMENTS", query);
                ArrayList<String> parse = XMLParser.getElements(query);
                Log.d("ELEMENTS", parse.toString());
                return parse;

            } catch (Exception e) {
                Log.e("ELEMENTS", e.getMessage() + ": \n" + e.getCause());
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            if (s != null) {
                List<String> e = new ArrayList<>();
                String codigo = s.get(0);
                String description = s.get(1);

                if (codigo.compareTo("0") == 0) {
                    String[] datos = s.get(2).split("&");
                    for (int i = 0; i < datos.length; i++) {
                        String[] par = datos[i].split(";");
                        e.add(par[1]);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, e);
                    elementos.setAdapter(adapter);
                } else {
                    Toast.makeText(getApplicationContext(), description, Toast.LENGTH_LONG).show();
                }
            }

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    private class ObtenerSiniestros extends AsyncTask<String, String, ArrayList<String>> {
        Context mContext;
        ProgressDialog progressDialog;

        public ObtenerSiniestros(Context context) {
            this.mContext = context;
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Buscando Componentes...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String query = SoapRequest.getComponents(telephonyManager.getDeviceId(), strings[0]);
                Log.d("COMPONENTES", strings[0]);
                Log.d("COMPONENTES", query);
                ArrayList<String> parse = XMLParser.getElements(query);
                Log.d("COMPONENTES", parse.toString());
                return parse;
            } catch (Exception e) {
                Log.e("COMPONENTES", e.getMessage() + ":\n" + e.getCause());
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            if (s != null) {
                List<String> e = new ArrayList<>();
                String codigo = s.get(0);
                String description = s.get(1);

                if (codigo.compareTo("0") == 0) {
                    String[] datos = s.get(2).split("&");
                    for (int i = 0; i < datos.length; i++) {
                        String[] par = datos[i].split(";");
                        e.add(par[1]);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, e);
                    siniestros.setAdapter(adapter);
                } else {
                    Toast.makeText(getApplicationContext(), description, Toast.LENGTH_LONG).show();
                }
            }

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
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
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String query = SoapRequest.getDepartament(telephonyManager.getDeviceId());
                ArrayList<String> parse = XMLParser.getReturnCode2(query);
                Log.d("ELEMENTS", query);
                if (parse.get(0).equals("0")) {
                    state = true;
                    return query;
                } else
                    return parse.get(1);

            } catch (IOException e) {
                Log.e("ELEMENTS", e.getMessage() + ": \n" + e.getCause());
                return dummy.ERROR_CONNECTION;
            } catch (SAXException e) {
                e.printStackTrace();
                return dummy.ERROR_PARSE;
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                return dummy.ERROR_PARSE;
            } catch (XPathExpressionException e) {
                e.printStackTrace();
                return dummy.ERROR_PARSE;
            } catch (Exception e) {
                e.printStackTrace();
                return dummy.ERROR_GENERAL;
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
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), dummy.ERROR_CONNECTION, Toast.LENGTH_LONG).show();
                    esta.finish();
                } catch (ParserConfigurationException | SAXException e) {
                    Toast.makeText(getApplicationContext(), dummy.ERROR_PARSE, Toast.LENGTH_LONG).show();
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
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Integer... strings) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String query = SoapRequest.getProvince(telephonyManager.getDeviceId(), strings[0]);
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
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Integer... strings) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String query = SoapRequest.getDistrict(telephonyManager.getDeviceId(), strings[0]);
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
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Integer... strings) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String query = SoapRequest.getStation(telephonyManager.getDeviceId(), strings[0]);
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
}
