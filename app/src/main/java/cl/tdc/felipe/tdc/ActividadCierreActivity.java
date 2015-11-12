package cl.tdc.felipe.tdc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import cl.tdc.felipe.tdc.objects.Maintenance.Agenda;
import cl.tdc.felipe.tdc.preferences.FormCierreReg;
import cl.tdc.felipe.tdc.webservice.SoapRequest;
import cl.tdc.felipe.tdc.webservice.SoapRequestTDC;
import cl.tdc.felipe.tdc.webservice.XMLParser;

public class ActividadCierreActivity extends Activity implements View.OnClickListener {
    private static String TITLE = "Cierre de Actividad";
    private static String IMEI;
    String idMain;
    TextView PAGETITLE;
    public static Activity actividad;
    Context mContext;

    FormCierreReg REG;

    Button IDEN, TRESG;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cierre_actividad);
        actividad = this;
        mContext = this;

        REG = new FormCierreReg(this, "LISTADO");

        idMain = getIntent().getStringExtra("MAINTENANCE");

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = telephonyManager.getDeviceId();

        PAGETITLE = (TextView) this.findViewById(R.id.header_actual);
        PAGETITLE.setText(TITLE);

        IDEN = (Button) this.findViewById(R.id.IDEN);
        TRESG = (Button) this.findViewById(R.id.TRESG);
        IDEN.setOnClickListener(this);
        TRESG.setOnClickListener(this);
        boolean idenState = REG.getBoolean("IDEN" + idMain);
        if (idenState)
            IDEN.setEnabled(false);


    }

    public void onClick_apagar(View v) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage("¿Seguro que desea salir de TDC?");
        b.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

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
        b.setMessage("¿Seguro que desea salir de Cierre de Actividad?");
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


    @Override
    public void onClick(View view) {
        Intent intent;
        if (view.getId() == R.id.IDEN) {
            buscar_form task = new buscar_form("IDEN");
            task.execute();
        }
        if (view.getId() == R.id.TRESG) {
            buscar_form task = new buscar_form("3G");
            task.execute();
        }
    }

    private String getAction(String type) {
        if (type.equals("IDEN")) return SoapRequestTDC.ACTION_IDEN;
        if (type.equals("3G")) return SoapRequestTDC.ACTION_3G;
        else return "";
    }


    private class buscar_form extends AsyncTask<String, String, String> {
        ProgressDialog dialog;
        String type;
        String query;
        boolean flag = false;

        private buscar_form(String type) {
            this.type = type;
            dialog = new ProgressDialog(mContext);
            dialog.setMessage("Buscando formulario " + type);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                query = SoapRequestTDC.getFormularioCierre(IMEI, idMain, getAction(type));
                ArrayList<String> returnCode = XMLParser.getReturnCode2(query);
                if (returnCode.get(0).equals("0"))
                    flag = true;

                return returnCode.get(1);
            } catch (SAXException | ParserConfigurationException | XPathExpressionException e) {
                e.printStackTrace();
                return "Error en el XML.";
            } catch (IOException e) {
                e.printStackTrace();
                return "Se agotó el tiempo de conexión. Por favor reintente.";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (dialog.isShowing()) dialog.dismiss();
            if (flag) {
                Intent intent = new Intent(actividad, ActividadCierreFormActivity.class);
                intent.putExtra("TITULO", this.type);
                intent.putExtra("ID", idMain);
                intent.putExtra("XML", query);
                int code;
                if (type.equals("IDEN")) {
                    code = 0;
                } else
                    code = -1;
                startActivityForResult(intent, code);
            } else {
                AlertDialog.Builder b = new AlertDialog.Builder(actividad);
                b.setTitle("Error");
                b.setIcon(android.R.drawable.ic_dialog_alert);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                IDEN.setEnabled(false);
                REG.addValue("IDEN" + idMain, true);
            }
        }
    }

    public void enviar(View v) {
        AlertDialog.Builder b = new AlertDialog.Builder(actividad);
        b.setMessage("¿Desea cerrar el mantenimiento?");
        b.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Cierre t = new Cierre();
                t.execute();
            }
        });
        b.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        b.setCancelable(false);
        b.show();
    }

    private class Cierre extends AsyncTask<String, String, String> {
        ProgressDialog p;
        boolean ok = false;

        private Cierre() {
            p = new ProgressDialog(mContext);
            p.setMessage("Cerrando Mantenimiento...");
            p.setCanceledOnTouchOutside(false);
            p.setCancelable(false);
        }

        @Override
        protected void onPreExecute() {
            p.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String response = SoapRequestTDC.cerrarMantenimiento(IMEI, idMain);

                ArrayList<String> parse = XMLParser.getReturnCode2(response);
                if(parse.get(0).equals("0")){
                    ok = true;
                    return parse.get(1);

                }else{
                    return "Error Code: "+parse.get(0)+"\n"+parse.get(1);
                }

            } catch (ParserConfigurationException | SAXException | XPathExpressionException e) {
                return "Problema al recibir la respuesta";
            } catch (IOException e) {
                return "Problema al recibir la respuesta";
            } catch (Exception e) {
                return "Se agotó el tiempo de conexión";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (p.isShowing()) p.dismiss();
            AlertDialog.Builder b = new AlertDialog.Builder(actividad);
            b.setMessage(s);
            if(ok){
                b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        REG.clearPreferences();
                        if(AgendaActivity.actividad != null)
                            AgendaActivity.actividad.finish();

                        actividad.finish();
                    }
                });


            }else{
                b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
            }
            b.show();
        }
    }
}
