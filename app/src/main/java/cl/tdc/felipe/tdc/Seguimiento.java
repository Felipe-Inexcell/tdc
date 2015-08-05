package cl.tdc.felipe.tdc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import cl.tdc.felipe.tdc.objects.Seguimiento.Proyecto;
import cl.tdc.felipe.tdc.webservice.SoapRequestSeguimiento;
import cl.tdc.felipe.tdc.webservice.XMLParserSeguimiento;

public class Seguimiento extends Activity {
    private static final String TAG = "SEGUIMIENTO";

    public static Activity actividad;
    public static String IMEI;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Proyecto> proyectos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguimiento);
        actividad = this;

        TelephonyManager fono = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = fono.getDeviceId();

        mRecyclerView = (RecyclerView)this.findViewById(R.id.proyectos);

        /*getProjects task = new getProjects(this);
        task.execute();*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        getProjects task = new getProjects(this);
        task.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void onClick_apagar(View v) {
        MainActivity.actividad.finish();
        finish();

    } public void onClick_back(View v) {
        finish();
    }


    public class ProyectosAdapter extends RecyclerView.Adapter<ProyectosAdapter.ViewHolder> {
        private ArrayList<Proyecto> mDataset;
        private final AdapterView.OnItemClickListener itemClickListener;
        public ProyectosAdapter(ArrayList<Proyecto> myDataset, AdapterView.OnItemClickListener click) {
            mDataset = myDataset;
            itemClickListener = click;
        }

        @Override
        public ProyectosAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                       final int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.proyectos_recyclerview, parent, false);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(null, view, viewType, 0);
                }
            });
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Proyecto p = mDataset.get(position);
            holder.mTitulo.setText(p.getNombre());
            holder.mDetalle.setText(p.getAvance_real()+"%/"+p.getAvance_programado()+"%");
        }


        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTitulo, mDetalle;
            public ViewHolder(View v) {
                super(v);
                mTitulo = (TextView) v.findViewById(R.id.titulo);
                mDetalle = (TextView) v.findViewById(R.id.progreso);
            }
        }
    }


    private class getProjects extends AsyncTask<String, String, String>{
        Context aContext;
        ProgressDialog dialog;
        boolean resultOk = false;

        private getProjects(Context aContext) {
            this.aContext = aContext;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(aContext);
            dialog.setMessage("Buscando Proyectos...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                String query = SoapRequestSeguimiento.getProyects(IMEI);

                String[] result = XMLParserSeguimiento.getResultCode(query).split(";");

                if(result[0].compareTo("0")==0) {
                    resultOk = true;
                    return query;
                }else if(result[0].compareTo("1")==0){
                    resultOk = false;
                    return "No hay proyectos asignados";
                }else{
                    resultOk = false;
                    return result[1];
                }

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(resultOk){
                try{
                    proyectos = XMLParserSeguimiento.getProjects(s);

                    mRecyclerView.setHasFixedSize(true);

                    mLayoutManager = new LinearLayoutManager(aContext);
                    mRecyclerView.setLayoutManager(mLayoutManager);

                    mAdapter = new ProyectosAdapter(proyectos, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            int itemPosition = mRecyclerView.getChildPosition(view);
                            Proyecto p = proyectos.get(itemPosition);

                            Intent intent = new Intent(actividad.getApplication(), DetalleProyecto.class);
                            intent.putExtra("PROYECTO", p.toString());
                            startActivity(intent);
                        }
                    });
                    mRecyclerView.setAdapter(mAdapter);

                }catch (ParserConfigurationException e){
                    Toast.makeText(aContext, "ParseConfigurationException", Toast.LENGTH_LONG).show();
                    actividad.finish();
                }catch (SAXException e){
                    Toast.makeText(aContext, "SAXException", Toast.LENGTH_LONG).show();
                    actividad.finish();
                }catch (IOException e){
                    Toast.makeText(aContext, "IOException", Toast.LENGTH_LONG).show();
                    actividad.finish();
                }catch (XPathExpressionException e){
                    Toast.makeText(aContext, "XPathExpressionException", Toast.LENGTH_LONG).show();
                    actividad.finish();
                }
            }else{
                Toast.makeText(aContext, s, Toast.LENGTH_LONG).show();
                actividad.finish();
            }
            if(dialog.isShowing())
                dialog.dismiss();
        }

    }


}