package cl.tdc.felipe.tdc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import cl.tdc.felipe.tdc.objects.Relevar.Modulo;
import cl.tdc.felipe.tdc.webservice.SoapRequestPreAsBuilt;
import cl.tdc.felipe.tdc.webservice.XMLParser;
import cl.tdc.felipe.tdc.webservice.XMLParserPreAsBuilt;


public class FormPreAsBuiltActivityMW extends Activity {
    Context mContext;
    int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_mw);

        mContext = this;
        ID = getIntent().getIntExtra("ID", -1);

        ObtenerCheck task = new ObtenerCheck();
        task.execute();
    }



    public void onClick_apagar(View v) {

        if (PreAsBuiltActivity.activity != null)
            PreAsBuiltActivity.activity.finish();
        if (MainActivity.actividad != null)
            MainActivity.actividad.finish();
        finish();

    }


    public void onClick_back(View v) {
        finish();
    }

    public void onClick_enviar(View v) {

    }

    private class ObtenerCheck extends AsyncTask<String, String, String>{
        ProgressDialog d;
        boolean ok = false;

        private ObtenerCheck() {
        }

        @Override
        protected void onPreExecute() {
            d = new ProgressDialog(mContext);
            d.setMessage("Cargando CheckList...");
            d.setCancelable(false);
            d.setCanceledOnTouchOutside(false);
            d.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {


                String query = SoapRequestPreAsBuilt.getCheckMW(ID);

                ArrayList<String>parse = XMLParser.getReturnCode2(query);

                if(ok = parse.get(0).equals("0")){
                    return query;
                }else
                    return parse.get(1);


            }catch (Exception e){
                e.printStackTrace();
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(ok){

            }else{
                Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
                ((Activity)mContext).finish();
            }
            if(d.isShowing())d.dismiss();
        }
    }

}
