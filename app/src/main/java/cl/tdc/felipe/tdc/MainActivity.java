package cl.tdc.felipe.tdc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import cl.tdc.felipe.tdc.daemon.PositionTrackerTDC;
import cl.tdc.felipe.tdc.daemon.WifiTrackerTDC;
import cl.tdc.felipe.tdc.extras.Funciones;
import cl.tdc.felipe.tdc.objects.PreAsBuilt.Informacion;
import cl.tdc.felipe.tdc.preferences.MaintenanceReg;
import cl.tdc.felipe.tdc.preferences.PreferencesTDC;
import cl.tdc.felipe.tdc.webservice.SoapRequest;
import cl.tdc.felipe.tdc.webservice.SoapRequestCheckLists;
import cl.tdc.felipe.tdc.webservice.SoapRequestPreAsBuilt;
import cl.tdc.felipe.tdc.webservice.SoapRequestTDC;
import cl.tdc.felipe.tdc.webservice.XMLParser;
import cl.tdc.felipe.tdc.webservice.XMLParserChecklists;
import cl.tdc.felipe.tdc.webservice.XMLParserPreAsBuilt;
import cl.tdc.felipe.tdc.webservice.XMLParserTDC;
import cl.tdc.felipe.tdc.webservice.dummy;

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
        setContentView(R.layout.activity_main_horizontal_bak);
        Log.i(TAG, "MainActyvity Start");
        actividad = this;
        preferencesTDC = new PreferencesTDC(this);
        mContext = this;

        File carpetaTDC = new File(Environment.getExternalStorageDirectory() + "/TDC@");
        if(!carpetaTDC.exists())
            if(!carpetaTDC.mkdirs()){
                Log.e("TDC", "fallo en crear carpeta TDC@");
            }

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

        Actualizar a = new Actualizar();
        a.execute();
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
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage("¿Seguro que desea salir la aplicación?");
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
    public void onClick_preasbuilt(View v) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);

        b.setItems(new CharSequence[]{"RF","MW"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PreAsBuilt task = new PreAsBuilt(mContext, i);
                task.execute();

            }
        });
        b.setTitle("Seleccione una opción");
        b.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        b.show();
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

    private class PreAsBuilt extends AsyncTask<String, String, String>{
        Context context;
        ProgressDialog d;
        boolean ok = false;
        int type;
        String message;

        private PreAsBuilt(Context context, int type) {
            this.context = context;
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            d = new ProgressDialog(context);
            if(type == XMLParserPreAsBuilt.RF)
                d.setMessage("Buscando informacion sobre RF");
            if(type == XMLParserPreAsBuilt.MW)
                d.setMessage("Buscando informacion sobre MW");
            d.setCanceledOnTouchOutside(false);
            d.setCancelable(false);
            d.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                String query;
                if(type == XMLParserPreAsBuilt.RF)
                    query = SoapRequestPreAsBuilt.getNodob(IMEI);
                else
                    query = SoapRequestPreAsBuilt.getNodoMW(IMEI);

                ArrayList<String> parse = XMLParser.getReturnCode2(query);

                ok = parse.get(0).equals("0");

                if(ok)
                    return query;
                else
                    return parse.get(1);
            } catch (SAXException | ParserConfigurationException | XPathExpressionException e) {
                e.printStackTrace();
                message = dummy.ERROR_PARSE;
            } catch (IOException e) {
                e.printStackTrace();
                message =dummy.ERROR_CONNECTION;
            } catch (Exception e) {
                e.printStackTrace();
                message = dummy.ERROR_GENERAL;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if(ok){
                try {
                    Informacion info = XMLParserPreAsBuilt.getInfoPreAsBuilt(s, type);

                    Intent i = new Intent(mContext, PreAsBuiltActivity.class);
                    i.putExtra("ID", info.getId());
                    i.putExtra("TYPE", type);
                    i.putExtra("QUERY", s);
                    startActivity(i);
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                    Toast.makeText(context, dummy.ERROR_PARSE, Toast.LENGTH_LONG).show();
                } catch (SAXException e) {
                    e.printStackTrace();
                    Toast.makeText(context, dummy.ERROR_PARSE, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, dummy.ERROR_CONNECTION, Toast.LENGTH_LONG).show();
                } catch (XPathExpressionException e) {
                    e.printStackTrace();
                    Toast.makeText(context, dummy.ERROR_PARSE, Toast.LENGTH_LONG).show();
                }
            }else{

                if(s == null)
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                else {
                    try {
                        String test = new String(s.getBytes(Xml.Encoding.ISO_8859_1.name()), Xml.Encoding.UTF_8.name());
                        Log.d("PREASBUILT", test);
                        Toast.makeText(context, test, Toast.LENGTH_LONG).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(d.isShowing())d.dismiss();
        }
    }


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

            } catch (IOException e) {
                e.printStackTrace();
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
        ProgressDialog progressDialog = null;
        Context tContext;
        String ATAG = "MAINTASK";
        String mensaje;

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
            try {
                publishProgress("Verificando Jornada...");
                String query = SoapRequest.updateTechnician(IMEI);
                return XMLParser.getReturnCode(query);
            } catch (IOException e) {
                Log.e(ATAG, e.getMessage() + ":\n" + e.getCause());
                mensaje = dummy.ERROR_CONNECTION;
            } catch (SAXException | XPathExpressionException | ParserConfigurationException e) {
                e.printStackTrace();
                mensaje = dummy.ERROR_PARSE;
            } catch (Exception e) {
                e.printStackTrace();
                mensaje = dummy.ERROR_GENERAL;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            if (s == null) {
                Toast.makeText(tContext, mensaje, Toast.LENGTH_LONG).show();
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
    private class Actualizar extends AsyncTask<String, String, ArrayList<String>> {
        ProgressDialog progressDialog = null;
        String ATAG = "ACTUALIZACION";
        String mensaje;

        public Actualizar() {
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(actividad);
            progressDialog.setMessage("Buscando Actualizaciones...");
            progressDialog.show();
        }


        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            try {
                String query = SoapRequestTDC.updateApk(IMEI);
                return XMLParserTDC.getUpdateInfo(query);
            } catch (IOException e) {
                Log.e(ATAG, e.getMessage() + ":\n" + e.getCause());
                mensaje = dummy.ERROR_CONNECTION;
            } catch (SAXException | XPathExpressionException | ParserConfigurationException e) {
                e.printStackTrace();
                mensaje = dummy.ERROR_PARSE;
            } catch (Exception e) {
                e.printStackTrace();
                mensaje = dummy.ERROR_GENERAL;
            }
            return null;
        }

        @Override
        protected void onPostExecute(final ArrayList<String> s) {
            progressDialog.dismiss();

            if (s == null) {
                AlertDialog.Builder error = new AlertDialog.Builder(actividad);
                error.setMessage(mensaje);
                error.setNeutralButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                error.show();
            } else {
                String version = getResources().getString(R.string.version);
                if(s.get(0).equals("")){
                    Toast.makeText(mContext, "No se encontró actualización", Toast.LENGTH_SHORT).show();
                }else if(version.compareTo(s.get(0)) < 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(actividad);
                    builder.setMessage("Versión Instalada: "
                            + version
                            + "\nVersión a Instalar: " + s.get(0)
                            + "\nDebe actualizar la aplicación para continuar utilizándola.");
                    builder.setTitle("Hay una nueva versión de la Aplicación");
                    builder.setCancelable(false);

                    builder.setPositiveButton("Descargar e Instalar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        /*Toast.makeText(mContext, "Descargando... si claro", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/app-debug.apk")), "application/vnd.android.package-archive");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        p.finish();
                        dialog.dismiss();*/
                            Update u = new Update(mContext, s.get(1));
                            u.execute();
                        }
                    });
                    builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            actividad.finish();
                            dialog.dismiss();
                        }
                    });
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            actividad.finish();
                        }
                    });
                    builder.show();
                }
                }


            }
        }

    private class Update extends AsyncTask<String, String, File> {
        Context ctx;
        ProgressDialog d;
        String url;

        private Update(Context ctx, String URL) {
            this.ctx = ctx;
            this.url = URL;
        }

        @Override
        protected void onPreExecute() {
            d = new ProgressDialog(ctx);
            d.setCancelable(false);
            d.setCanceledOnTouchOutside(false);
            d.setMessage("Descargando nueva versión...");
            d.show();
        }

        @Override
        protected File doInBackground(String... params) {
            return Funciones.Update(url);
        }

        @Override
        protected void onPostExecute(File file) {
            if (d.isShowing()) d.dismiss();
            if (file != null) {
                //Toast.makeText(ctx, file, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
            actividad.finish();
        }
    }



}
