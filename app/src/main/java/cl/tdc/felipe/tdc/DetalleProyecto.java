package cl.tdc.felipe.tdc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import cl.tdc.felipe.tdc.objects.Seguimiento.Actividad;
import cl.tdc.felipe.tdc.objects.Seguimiento.Dia;
import cl.tdc.felipe.tdc.objects.Seguimiento.Proyecto;
import cl.tdc.felipe.tdc.webservice.SoapRequestSeguimiento;
import cl.tdc.felipe.tdc.webservice.XMLParserSeguimiento;

public class DetalleProyecto extends Activity {
    private static final String TAG = "DETALLE";
    public static Activity actividad;
    public static Context mContext;
    public static String IMEI;

    TextView nombreProyecto, detalle_progreso;
    EditText dia, inicio, fin;
    ProgressBar mProgressBar;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<Dia> mDias;

    Proyecto mProyecto;

    private String name;
    private Bitmap b = null, bmini = null;
    private static int TAKE_PICTURE = 1;
    private static int SELECT_PICTURE = 2;
    final CharSequence[] opcionCaptura = {
            "Tomar Fotografía"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pryecto);

        init();

        nombreProyecto.setText(mProyecto.getNombre());
        dia.setText(String.valueOf(mProyecto.getDia()));
        inicio.setText(mProyecto.getFecha_inicio());
        fin.setText(mProyecto.getFecha_final());
        detalle_progreso.setText("Progreso: " + mProyecto.getAvance_real() + "%");

        try {
            mProgressBar.setMax(Integer.parseInt(mProyecto.getAvance_programado()));
            mProgressBar.setProgress(Integer.parseInt(mProyecto.getAvance_real()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void init() {
        actividad = this;
        mContext = this;

        mProyecto = new Proyecto(getIntent().getExtras().getString("PROYECTO"));
        mRecyclerView = (RecyclerView) this.findViewById(R.id.recyclerView);


        getActivities task = new getActivities(this);
        task.execute();

        detalle_progreso = (TextView) this.findViewById(R.id.detalle_progress);
        nombreProyecto = (TextView) this.findViewById(R.id.nombre_proyecto);
        dia = (EditText) this.findViewById(R.id.dia);
        inicio = (EditText) this.findViewById(R.id.inicio);
        fin = (EditText) this.findViewById(R.id.fin);
        mProgressBar = (ProgressBar) this.findViewById(R.id.progressBar);
    }

    public void onClick_enviar(View view) {
        Enviar task = new Enviar(this);
        task.execute();
    }

    public class ActividadesAdapter extends RecyclerView.Adapter<ActividadesAdapter.ViewHolder> {
        public ArrayList<Dia> mDiasResp;

        public ActividadesAdapter() {
        }


        @Override
        public ActividadesAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                                final int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.proyectos_detalle, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            final Date fecha = new Date();

            final Dia d = mDias.get(position);

            holder.pProgramado.setText(d.getProgrammedAdvance() + "%");
            holder.pReal.setText(d.getRealAdvance() + "%");
            holder.avance.setText(d.getRealAdvance());

            boolean foto = false;
            float avancetotal= 0;
            final ArrayList<Actividad> actividadArrayList = d.getActividades();

            for (int i = 0; i< actividadArrayList.size(); i++) {
                final Actividad a = actividadArrayList.get(i);
                if (a.isFoto()) foto = true;

                CheckBox c = new CheckBox(mContext);
                c.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                c.setText(a.getNameActivity());
                c.setPadding(0,0,0,24);


                if(a.isSelected()){
                    c.setEnabled(false);
                    c.setChecked(a.isSelected());
                    avancetotal+= a.getAdvance();
                }

                final int index = i;
                c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        a.setSelected(b);
                        actividadArrayList.set(index,a);
                        d.setActividades(actividadArrayList);
                        float f;
                        if(b){
                            f = Float.parseFloat(d.getRealAdvance()) + a.getAdvance();
                        }else{
                            f = Float.parseFloat(d.getRealAdvance()) - a.getAdvance();
                        }

                        d.setRealAdvance(String.valueOf(f));
                        mDias.set(position, d);
                        holder.avance.setText(String.valueOf(f));

                    }
                });

                ((LinearLayout) holder.listadoActividades).addView(c);

            }

            holder.dia.setText("DÍA " + String.valueOf(d.getDayNumber()));
            if (foto) {
                holder.camara.setVisibility(View.VISIBLE);
                holder.camara.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tomarFotografia();
                    }
                });
            } else
                holder.camara.setVisibility(View.GONE);

            holder.fecha.setText(formatter.format(fecha));


            holder.observacion.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    d.setDescriptionDay(editable.toString());
                    d.setDate(formatter.format(fecha));
                    mDias.set(position, d);
                }
            });

        }


        @Override
        public int getItemCount() {
            return mDias.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView dia, pProgramado, pReal, nombre;

            private ImageButton expandir;
            public ImageButton camara;
            private View contenido;

            public View listadoActividades;

            public EditText avance, fecha, observacion;

            public ViewHolder(View v) {
                super(v);
                contenido = v.findViewById(R.id.content);
                expandir = (ImageButton) v.findViewById(R.id.expand);
                camara = (ImageButton) v.findViewById(R.id.foto);

                listadoActividades = v.findViewById(R.id.activityList);

                avance = (EditText) v.findViewById(R.id.avance);

                fecha = (EditText) v.findViewById(R.id.fecha);
                observacion = (EditText) v.findViewById(R.id.observacion);

                expandir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (contenido.getVisibility() == View.GONE) {
                            contenido.setVisibility(View.VISIBLE);
                            ((ImageButton) view).setImageResource(R.drawable.ic_action_close);
                        } else {
                            contenido.setVisibility(View.GONE);
                            ((ImageButton) view).setImageResource(R.drawable.ic_action_open);
                        }
                    }
                });

                dia = (TextView) v.findViewById(R.id.dia);
                pProgramado = (TextView) v.findViewById(R.id.programado);
                pReal = (TextView) v.findViewById(R.id.real);

                nombre = (TextView) v.findViewById(R.id.nombre_actividad);
            }
        }
    }

    public void tomarFotografia() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escoja una Opcion:");
        builder.setIcon(R.drawable.ic_camera);
        builder.setItems(opcionCaptura, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                int code = TAKE_PICTURE;
                if (item == TAKE_PICTURE) {
                    Uri output = Uri.fromFile(new File(name));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
                } else if (item == SELECT_PICTURE) {
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


    private class getActivities extends AsyncTask<String, String, String> {
        Context aContext;
        ProgressDialog dialog;
        boolean resultOk = false;

        private getActivities(Context aContext) {
            this.aContext = aContext;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(aContext);
            dialog.setMessage("Buscando Actividades...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String query = SoapRequestSeguimiento.getActividades(mProyecto.getId());

                String[] result = XMLParserSeguimiento.getResultCode(query).split(";");

                if (result[0].compareTo("0") == 0) {
                    resultOk = true;
                    return query;
                } else {
                    resultOk = false;
                    return result[1];
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (resultOk) {
                try {
                    mDias = XMLParserSeguimiento.getActivities(s);

                    mLayoutManager = new LinearLayoutManager(aContext);
                    mRecyclerView.setLayoutManager(mLayoutManager);

                    mAdapter = new ActividadesAdapter();
                    mRecyclerView.setAdapter(mAdapter);

                } catch (ParserConfigurationException e) {
                    Toast.makeText(aContext, "ParseConfigurationException", Toast.LENGTH_LONG).show();
                    actividad.finish();
                } catch (SAXException e) {
                    Toast.makeText(aContext, "SAXException", Toast.LENGTH_LONG).show();
                    actividad.finish();
                } catch (IOException e) {
                    Toast.makeText(aContext, "IOException", Toast.LENGTH_LONG).show();
                    actividad.finish();
                } catch (XPathExpressionException e) {
                    Toast.makeText(aContext, "XPathExpressionException", Toast.LENGTH_LONG).show();
                    actividad.finish();
                }
            } else {
                Toast.makeText(aContext, s, Toast.LENGTH_LONG).show();
                actividad.finish();
            }
            if (dialog.isShowing())
                dialog.dismiss();
        }

    }

    private class Enviar extends AsyncTask<String, String, String> {
        Context aContext;
        ProgressDialog dialog;
        boolean resultOk = false;

        private Enviar(Context aContext) {
            this.aContext = aContext;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(aContext);
            dialog.setMessage("Enviando Checklist");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String query = SoapRequestSeguimiento.sendResponse(mProyecto.getId(), mDias);

                String[] result = XMLParserSeguimiento.getResultCode(query).split(";");

                if (result[0].compareTo("0") == 0) {
                    resultOk = true;
                    return query;
                } else {
                    resultOk = false;
                    return result[1];
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Ah ocurrido un error, vuelva a intentarlo";
            }
        }

        @Override
        protected void onPostExecute(String s) {

            if (resultOk) {
                Toast.makeText(aContext, "Enviado Correctamente.", Toast.LENGTH_LONG).show();
                actividad.finish();
            } else {
                Toast.makeText(aContext, s, Toast.LENGTH_LONG).show();
            }
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }


    public void onClick_apagar(View v) {
        if (Seguimiento.actividad != null)
            Seguimiento.actividad.finish();
        if (MainActivity.actividad != null)
            MainActivity.actividad.finish();
        finish();
    }

    public void onClick_back(View v) {
        finish();
    }
}
