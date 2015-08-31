package cl.tdc.felipe.tdc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cl.tdc.felipe.tdc.daemon.PositionTrackerTDC;
import cl.tdc.felipe.tdc.daemon.WifiTrackerTDC;
import cl.tdc.felipe.tdc.objects.Seguimiento.ImagenDia;
import cl.tdc.felipe.tdc.preferences.MaintenanceReg;
import cl.tdc.felipe.tdc.preferences.PreferencesTDC;
import cl.tdc.felipe.tdc.webservice.SoapRequest;
import cl.tdc.felipe.tdc.webservice.SoapRequestCheckLists;
import cl.tdc.felipe.tdc.webservice.UploadImage;
import cl.tdc.felipe.tdc.webservice.XMLParser;
import cl.tdc.felipe.tdc.webservice.XMLParserChecklists;

public class MainActivity extends ActionBarActivity {
    Context mContext;
    private static final String TAG = "MAINACTIVITY";
    public static Activity actividad;
    public static Intent service_wifi, service_pos;
    public static String IMEI;
    public static PreferencesTDC preferencesTDC;
    private static int REQUEST_SETTINGS_ACTION = 0;
    LocationManager locationManager;
    public ImageButton agendabtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_horizontal);
        Log.i(TAG, "MainActyvity Start");
        actividad = this;
        preferencesTDC = new PreferencesTDC(this);
        mContext = this;

        agendabtn = (ImageButton) findViewById(R.id.btn_agenda);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder b = new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .setMessage("Active GPS antes de iniciar la aplicación")
                    .setNeutralButton("IR A CONFIGURACION", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_SETTINGS_ACTION);
                        }
                    })
                    .setNegativeButton("SALIR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.finish();
                        }
                    });
            AlertDialog dialog = b.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }


        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = telephonyManager.getDeviceId();

        service_wifi = new Intent(this, WifiTrackerTDC.class);
        //startService(service_wifi);
        service_pos = new Intent(this, PositionTrackerTDC.class);
        startService(service_pos);
        settings();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SETTINGS_ACTION) {
            Log.d("SETTINGS", "CODE: " + resultCode);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(this, "Sigue desactivado el GPS.", Toast.LENGTH_LONG).show();
                MainActivity.this.finish();
            }
        }
    }

    public void onClick_apagar(View v) {
        finish();
    }


    public void onClick_Signature(View v) {
        MaintenanceReg pref = new MaintenanceReg(this);
        pref.getMaintenance();
    }

    public void onClick_QR(View v) {
        /*IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();*/
        MaintenanceReg pref = new MaintenanceReg(this);
        pref.newMaintenance("465", "0");
    }


    // TODO: AGENDA.
    public void onClick_btn2(View v) {
        //startActivity(new Intent(this,AgendaActivity.class));
        AgendaTask agendaTask = new AgendaTask(this);
        agendaTask.execute();
    }

    //TODO: NOTIFICAR AVERIA
    public void onClick_btn3(View v) {
        startActivity(new Intent(this, AveriaActivity.class));
    }

    //TODO:  SITIOS CERCANOS
    public void onClick_btn4(View v) {
        try {
            startActivity(new Intent(this, CercanosActivity.class));
        } catch (Exception e) {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setMessage(e.getMessage() + ":\n" + e.getCause());
            b.setTitle("Error al cargar Sitios Cercanos");
            b.setNeutralButton("Cerrar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog dialog = b.create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }
    }

  //TODO: SEGUIMIENTO DE OBRAS
    public void onClick_btn5(View v) {
        startActivity(new Intent(this, Seguimiento.class));
    }

    //TODO CHECKLIST SEGURIDAD DIARIO
    public void onClick_btn6(View v) {
        ChecklistTask c = new ChecklistTask(this);
        c.execute();
    }

    public void onClick_relevo(View v) {
        startActivity(new Intent(this, RelevarActivity.class));
    }






    void settings() {
        TelephonyManager fono = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        PreferencesTDC preferencesTDC = new PreferencesTDC(this);
        if (!preferencesTDC.sharedPreferences.contains(PreferencesTDC.SETTING_IMEI))
            preferencesTDC.setIMEI(fono.getDeviceId());
        if (!preferencesTDC.sharedPreferences.contains(PreferencesTDC.SETTING_IMSI))
            preferencesTDC.setIMSI(fono.getSimSerialNumber());
    }


//-----------------TASK ASINCRONICO------------------------------------


    private class ChecklistTask extends AsyncTask<String, String, String> {
        Context tContext;
        ProgressDialog dialog;
        boolean ok = false;

        private ChecklistTask(Context tContext) {
            this.tContext = tContext;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(tContext);
            dialog.setMessage("Solicitando Checklist...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String query = SoapRequestCheckLists.getdailyActivities(IMEI);

                String[] code = XMLParserChecklists.getResultCode(query).split(";");
                if (code[0].compareTo("0") == 0) {
                    ok = true;
                    return query;
                } else {
                    ok = false;
                    return code[1];
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Ha ocurrido un error (" + e.getMessage() + "). Por favor reintente.";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (ok) {

                Intent check = new Intent(actividad, FormCheckSecurity.class);
                check.putExtra("RESPONSE", s);
                startActivity(check);

            } else {
                Toast.makeText(tContext, s, Toast.LENGTH_LONG).show();
            }
            if (dialog.isShowing()) dialog.dismiss();
        }
    }

    private class AgendaTask extends AsyncTask<String, String, ArrayList<String>> {
        ProgressDialog progressDialog;
        Context tContext;
        String ATAG = "MAINTASK";

        public AgendaTask(Context context) {
            this.tContext = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(tContext);
            progressDialog.setTitle("Espere por favor...");
            progressDialog.show();
        }


        @Override
        protected void onProgressUpdate(String... values) {
            progressDialog.setMessage(values[0]);
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            String result;
            try {
                publishProgress("Verificando Jornada...");
                String query = SoapRequest.updateTechnician(IMEI);
                ArrayList<String> response = XMLParser.getReturnCode(query);
                return response;
            } catch (Exception e) {
                Log.e(ATAG, e.getMessage() + ":\n" + e.getCause());
                return null; //Error
            }

        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            if (s == null) {
                Toast.makeText(tContext, "Error en la conexion. Intente nuevamente.", Toast.LENGTH_LONG).show();
            } else {
                if (s.get(0).compareTo("0") == 0) {
                    Intent i = new Intent(tContext, AgendaActivity.class);
                    i.putExtra("RESPONSE", s);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), s.get(1), Toast.LENGTH_LONG).show();
                }

            }
        }
    }


}
