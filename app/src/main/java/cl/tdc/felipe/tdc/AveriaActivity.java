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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cl.tdc.felipe.tdc.daemon.MyLocationListener;
import cl.tdc.felipe.tdc.daemon.PositionTrackerTDC;
import cl.tdc.felipe.tdc.preferences.PreferencesTDC;
import cl.tdc.felipe.tdc.webservice.SoapRequest;
import cl.tdc.felipe.tdc.webservice.XMLParser;

/**
 * Created by Felipe on 13/02/2015.
 */
public class AveriaActivity extends Activity{
    public static Activity actividad;
    private Context context;
    private PositionTrackerTDC trackerTDC;

    private String name;
    private Spinner elementos, siniestros;
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

    private void init(){
        elementos = (Spinner)findViewById(R.id.cb_elemento);
        elementos.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Collections.EMPTY_LIST));
        siniestros = (Spinner)findViewById(R.id.cb_siniestro);
        siniestros.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Collections.EMPTY_LIST));

        elementos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ObtenerSiniestros os = new ObtenerSiniestros(context);
                os.execute((String)adapterView.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ObtenerElementos o = new ObtenerElementos(this, this);
        o.execute();

    }

    // TODO: funcion onClick del botón apagar.

    public void onClick_apagar(View v){
        MainActivity.actividad.finish();
        finish();
    }

    public void onClick_back(View v){
        finish();
    }

    public void onClick_enviar(View v){


        //Obtenemos Datos del formulario
        final String sin = siniestros.getSelectedItem().toString();
        if(sin == null || sin.length() == 0){
            return;
        }
        final String elem = elementos.getSelectedItem().toString();
        if(elem == null || elem.length() == 0){
            return;
        }
        final String comen = ((TextView)findViewById(R.id.et_comentario)).getText().toString();
        if(comen == null || comen.length() == 0){
            return;
        }

        if(b == null) {
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
        elemento.setText(elem);
        siniestro.setText(sin);
        comentario.setText(comen);
        ImageView image = (ImageView) dialog.findViewById(R.id.dialogaveria_captura);
        image.setImageBitmap(b);

        ImageButton dialogOk = (ImageButton) dialog.findViewById(R.id.dialogaveria_ok);
        ImageButton dialogNok = (ImageButton) dialog.findViewById(R.id.dialogaveria_nok);
        // if button is clicked, close the custom dialog
        dialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviar(elem, sin, comen, b);
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

    public void enviar(String elemento, String siniestro, String comentario, Bitmap imagen){
        EnviarAveria a= new EnviarAveria(this, elemento, siniestro, comentario, imagen);
        a.execute();
    }

    /** Botón Cámara **/
    public void onClick_foto(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escoja una Opcion:");
        builder.setIcon(R.drawable.ic_camera);
        builder.setItems(opcionCaptura, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                int code = TAKE_PICTURE;
                if (item==TAKE_PICTURE) {
                    Uri output = Uri.fromFile(new File(name));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
                } else if (item==SELECT_PICTURE){
                    intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    code = SELECT_PICTURE;
                }
                startActivityForResult(intent, code);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
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
        } else if (requestCode == SELECT_PICTURE){
            Uri selectedImage = data.getData();
            InputStream is;
            try {
                is = getContentResolver().openInputStream(selectedImage);
                BufferedInputStream bis = new BufferedInputStream(is);
                b = BitmapFactory.decodeStream(bis);

            } catch (FileNotFoundException e) {}
        }
        try{
            //b = Bitmap.createScaledBitmap(b, 640, 480, true);
            bmini = Bitmap.createScaledBitmap(b, 64, 64, true);
        }catch(Exception ex){}


    }


    private class EnviarAveria extends AsyncTask<String,String, ArrayList<String>>{
        private String elemento, siniestro, comentario;
        private Bitmap imagen;
        private Context mContext;
        private ProgressDialog progressDialog;

        public EnviarAveria(Context context, String e, String s, String c, Bitmap i) {
            this.elemento = e;
            this.siniestro = s;
            this.comentario = c;
            this.imagen = i;
            this.mContext = context;
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Enviando averia...");
        }

        @Override
        protected void onPreExecute() {
           progressDialog.show();
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {

            try{
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                imagen.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();
                String image = Base64.encodeToString(bytes, Base64.DEFAULT);
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String query = SoapRequest.sendAveria(telephonyManager.getDeviceId(), elemento, siniestro, comentario, image, String.valueOf(trackerTDC.gps.getLatitude()), String.valueOf(trackerTDC.gps.getLongitude()));
                ArrayList<String> parse = XMLParser.getElements(query);
                Log.d("ENVIVANDO", parse.toString());
                return parse;
            }catch(Exception e){
                Log.e("ENVIANDO ERROR", e.getMessage()+":\n"+e.getCause());
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            if(s!= null){
                if(s.get(0).compareTo("0")==0){
                    Toast.makeText(context, "Averia enviada con exito", Toast.LENGTH_LONG).show();
                    actividad.finish();
                }
                else{
                    Toast.makeText(context, s.get(1), Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(context,"Error en la conexión.", Toast.LENGTH_LONG).show();
            }

            if(progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

    private class ObtenerElementos extends AsyncTask<String, String,ArrayList<String>>{
        Activity esta;
        Context context;
        ProgressDialog progressDialog;

        public ObtenerElementos(Activity activity, Context context){
            esta = activity;
            this.context = context;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Buscando Elementos...");
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            try{
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String query = SoapRequest.getElements(telephonyManager.getDeviceId());
                Log.d("ELEMENTS", query);
                ArrayList<String> parse = XMLParser.getElements(query);
                Log.d("ELEMENTS", parse.toString());
                return parse;

            }catch (Exception e){
                Log.e("ELEMENTS", e.getMessage()+": \n"+e.getCause());
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            if(s != null)
            {
                List<String> e = new ArrayList<>();
                String codigo = s.get(0);
                String description = s.get(1);

                if(codigo.compareTo("0")==0) {
                    String[] datos = s.get(2).split("&");
                    for (int i = 0; i < datos.length; i++) {
                        String[] par = datos[i].split(";");
                        e.add(par[1]);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, e);
                    elementos.setAdapter(adapter);
                }  else {
                    Toast.makeText(getApplicationContext(), description, Toast.LENGTH_LONG).show();
                }
            }

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }

    private class ObtenerSiniestros extends AsyncTask<String, String, ArrayList<String>>{
        Context mContext;
        ProgressDialog progressDialog;
        public ObtenerSiniestros(Context context){
            this.mContext = context;
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Buscando Componentes...");
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            try{
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String query = SoapRequest.getComponents(telephonyManager.getDeviceId(), strings[0]);
                Log.d("COMPONENTES", strings[0]);
                Log.d("COMPONENTES", query);
                ArrayList<String> parse = XMLParser.getElements(query);
                Log.d("COMPONENTES", parse.toString());
                return parse;
            }catch (Exception e){
                Log.e("COMPONENTES", e.getMessage()+":\n"+e.getCause());
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            if(s != null)
            {
                List<String> e = new ArrayList<>();
                String codigo = s.get(0);
                String description = s.get(1);

                if(codigo.compareTo("0")==0) {
                    String[] datos = s.get(2).split("&");
                    for (int i = 0; i < datos.length; i++) {
                        String[] par = datos[i].split(";");
                        e.add(par[1]);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, e);
                    siniestros.setAdapter(adapter);
                }  else {
                    Toast.makeText(getApplicationContext(), description, Toast.LENGTH_LONG).show();
                }
            }

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }
}
