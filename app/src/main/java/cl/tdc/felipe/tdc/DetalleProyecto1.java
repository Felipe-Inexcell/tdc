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
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import cl.tdc.felipe.tdc.extras.Funciones;
import cl.tdc.felipe.tdc.objects.Seguimiento.Actividad;
import cl.tdc.felipe.tdc.objects.Seguimiento.Dia;
import cl.tdc.felipe.tdc.objects.Seguimiento.ImagenDia;
import cl.tdc.felipe.tdc.objects.Seguimiento.Proyecto;
import cl.tdc.felipe.tdc.webservice.SoapRequestSeguimiento;
import cl.tdc.felipe.tdc.webservice.UploadImage;
import cl.tdc.felipe.tdc.webservice.XMLParserSeguimiento;
import cl.tdc.felipe.tdc.webservice.dummy;

public class DetalleProyecto1 extends Activity {
    private static final String TAG = "DETALLE";
    public static Activity actividad;
    public static Context mContext;
    public static String IMEI;
    float avance_real_total;
    List<ImagenDia> imgToSendList;
    ImagenDia imgActual;
    String FLOAT_FORMAT = "%.4f";

    TextView nombreProyecto, detalle_progreso;
    EditText dia, inicio, fin;
    ProgressBar mProgressBar;

    LinearLayout listadoDias;

    ArrayList<Dia> mDias;
    ArrayList<TextView> mAvances;
    boolean error = false;

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
        setContentView(R.layout.activity_detalle_pryecto1);

        init();

        nombreProyecto.setText(mProyecto.getNombre());
        dia.setText(String.valueOf(mProyecto.getDia()));
        inicio.setText(mProyecto.getFecha_inicio());
        fin.setText(mProyecto.getFecha_final());

        name = Environment.getExternalStorageDirectory() + "/TDC@/SeguimientoCaptura.jpg";

        mAvances = new ArrayList<>();

        imgToSendList = new ArrayList<>();
        try {
            avance_real_total = Float.parseFloat(mProyecto.getAvance_real());
            float real = Float.parseFloat(String.format(Locale.US, FLOAT_FORMAT, avance_real_total));
            float progra = Float.parseFloat(mProyecto.getAvance_programado());
            detalle_progreso.setText("Progreso: " + String.format(Locale.US, FLOAT_FORMAT, avance_real_total) + "%");
            mProgressBar.setMax((100 * 10000));
            mProgressBar.setProgress((int) (real * 10000));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void init() {
        actividad = this;
        mContext = this;

        mProyecto = new Proyecto(getIntent().getExtras().getString("PROYECTO"));
        listadoDias = (LinearLayout) findViewById(R.id.listado_dias);


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

        if(fechasOK()) {
            Enviar task = new Enviar(this);
            task.execute();
            /*if (!error) {
                task.execute();
            } else {
                Toast.makeText(mContext, "Revisar formato de fechas y reintentar.", Toast.LENGTH_LONG).show();
            }*/
        }

    }

    private boolean fechasOK() {
        for(final Dia d: mDias){
            String fecha = d.getFecha().getText().toString();
            if(fecha.length() > 0){
                if(!Funciones.validateDate(fecha)){
                    AlertDialog.Builder b1 = new AlertDialog.Builder(mContext);
                    b1.setIcon(android.R.drawable.ic_dialog_alert);
                    b1.setMessage("Formato de fecha Incorrecto.\nDebe ser AAAA-MM-DD");
                    b1.setTitle("Dia " + d.getDayNumber());
                    b1.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(d.getlContenido().getVisibility() == View.GONE){
                                d.getlContenido().setVisibility(View.VISIBLE);
                            }
                            d.getFecha().requestFocus();
                            dialogInterface.dismiss();

                        }
                    });
                    b1.show();
                    return false;
                }else {
                    try {
                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date hoy = new Date();
                        Date fecha_envia = formatter.parse(fecha);

                        Log.d("FECHAS", hoy + "  " + fecha_envia);
                        if (formatter.format(fecha_envia).compareTo(formatter.format(hoy)) < 0) {
                            AlertDialog.Builder b1 = new AlertDialog.Builder(mContext);
                            b1.setIcon(android.R.drawable.ic_dialog_alert);
                            b1.setMessage("No puede ingresar una fecha anterior al día de hoy.");
                            b1.setTitle("Dia " + d.getDayNumber());
                            b1.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(d.getlContenido().getVisibility() == View.GONE){
                                        d.getlContenido().setVisibility(View.VISIBLE);
                                    }
                                    d.getFecha().requestFocus();
                                    dialogInterface.dismiss();
                                }
                            });
                            b1.show();
                            return false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, "Ocurrió un error al comprobar la fecha, por favor reintente", Toast.LENGTH_LONG).show();
                        return false;
                    }

                }
            }
        }
        return true;
    }


    private void dibujarDias() {
        for (int i = 0; i < mDias.size(); i++) {
            final List<String> needPhotos = new ArrayList<>();

            View vDia = LayoutInflater.from(this).inflate(R.layout.proyectos_detalle, null, false);

            TextView dia_p_programado = (TextView) vDia.findViewById(R.id.programado);
            final TextView dia_p_real = (TextView) vDia.findViewById(R.id.real);
            TextView dia_n = (TextView) vDia.findViewById(R.id.dia);
            final LinearLayout dia_contenido = (LinearLayout) vDia.findViewById(R.id.content);
            final LinearLayout dia_actividades = (LinearLayout) vDia.findViewById(R.id.activityList);
            final EditText p_real = (EditText) vDia.findViewById(R.id.avance);
            final EditText p_fecha = (EditText) vDia.findViewById(R.id.fecha);
            EditText p_observacion = (EditText) vDia.findViewById(R.id.observacion);
            final ImageButton expand = (ImageButton) vDia.findViewById(R.id.expand);
            ImageButton dia_foto = (ImageButton) vDia.findViewById(R.id.foto);

            /*p_fecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        p_fecha.setText("");
                    }
                }
            });*/
            dia_foto.setVisibility(View.GONE);

            expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dia_contenido.getVisibility() == View.GONE) {
                        dia_contenido.setVisibility(View.VISIBLE);
                        expand.setImageResource(R.drawable.ic_action_close);
                    } else {
                        dia_contenido.setVisibility(View.GONE);
                        expand.setImageResource(R.drawable.ic_action_open);
                    }
                }
            });

            final Dia dia = mDias.get(i);
            final ArrayList<Actividad> actividadArrayList = dia.getActividades();
            dia.setAdvanceToday("0");


            p_fecha.setText(dia.getDate());

            /*p_fecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b) {
                        if (!Funciones.validateDate(p_fecha.getText().toString())) {
                            AlertDialog.Builder b1 = new AlertDialog.Builder(mContext);
                            b1.setIcon(android.R.drawable.ic_dialog_alert);
                            b1.setMessage("Formato de fecha Incorrecto.\nDebe ser AAAA-MM-DD");
                            b1.setTitle("Dia " + dia.getDayNumber());
                            b1.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            b1.show();
                            error = true;
                        } else {
                            error = false;
                            try {
                                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Date hoy = new Date();
                                Date fecha_envia = formatter.parse(p_fecha.getText().toString());

                                Log.d("FECHAS", hoy + "  " + fecha_envia);
                                if (formatter.format(fecha_envia).compareTo(formatter.format(hoy)) < 0) {
                                    AlertDialog.Builder b1 = new AlertDialog.Builder(mContext);
                                    b1.setIcon(android.R.drawable.ic_dialog_alert);
                                    b1.setMessage("No puede ingresar una fecha anterior al día de hoy.");
                                    b1.setTitle("Dia " + dia.getDayNumber());
                                    b1.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    b1.show();
                                    error = true;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                error = true;
                            }

                        }
                    }
                }
            });*/

            p_observacion.setText(dia.getDescriptionDay());
            dia_p_programado.setText(dia.getProgrammedAdvance());
            dia_p_real.setText(dia.getRealAdvance());
            dia_n.setText("DIA " + dia.getDayNumber());

            dia.setFecha(p_fecha);
            dia.setlContenido(dia_contenido);

            ArrayList<CheckBox> checkBoxes = new ArrayList<>();
            for (int j = 0; j < actividadArrayList.size(); j++) {
                final Actividad a = actividadArrayList.get(j);

                /** SI una actividad requiere foto se activara el boton **/
                if (a.isFoto()) dia_foto.setVisibility(View.VISIBLE);
                needPhotos.add(dia.getDayNumber() + ";" + a.getNameActivity());

                CheckBox c = new CheckBox(mContext);
                c.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                c.setText(a.getNameActivity());
                c.setPadding(0, 0, 0, 24);

                if (a.isSelected()) {
                    c.setEnabled(false);
                    c.setChecked(true);
                }

                c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        Log.i("CHECKBOX LISTENER", "****");
                        float advance;
                        if (dia.getAdvanceToday() == null) {
                            advance = 0;
                        } else
                            advance = Float.parseFloat(dia.getAdvanceToday());
                        Log.i("CHECKBOX LISTENER", "advance: " + advance);
                        try {
                            if (b) {
                                advance += a.getAdvance();
                                avance_real_total += a.getAdvance();
                                dia.setModify(true);

                                for (int k = dia.getDayNumber() - 1; k < mAvances.size(); k++) {
                                    float actual = Float.parseFloat(mAvances.get(k).getText().toString());
                                    mAvances.get(k).setText(String.format(Locale.US, FLOAT_FORMAT, (actual + a.getAdvance())));
                                }

                            } else {
                                advance -= a.getAdvance();
                                avance_real_total -= a.getAdvance();
                                if (advance < 0) advance *= -1;

                                for (int k = dia.getDayNumber() - 1; k < mAvances.size(); k++) {
                                    float actual = Float.parseFloat(mAvances.get(k).getText().toString());
                                    mAvances.get(k).setText(String.format(Locale.US, FLOAT_FORMAT, (actual - a.getAdvance())));
                                }
                            }

                            Log.i("CHECKBOX LISTENER", "to: " + advance);
                            dia.setAdvanceToday(String.format(Locale.US, FLOAT_FORMAT, advance));
                            p_real.setText(String.format(Locale.US, FLOAT_FORMAT, advance));
                            updateProgresoTotal();
                        } catch (Exception e) {
                            Toast.makeText(mContext, e.getMessage() + "\n" + e.getCause() + "\n" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                checkBoxes.add(c);
                dia_actividades.addView(c);
            }

            dia_foto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DateFormat timestamp_name = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                    imgActual = new ImagenDia(mProyecto.getId(), dia.getDayNumber(), null, new Date());
                    imgActual.setFilename(imgActual.getIdproject()+"_"+imgActual.getIdday()+"_"+timestamp_name.format(imgActual.getTimestamp())+".png");
                    tomarFotografia();
                }
            });

            mAvances.add(dia_p_real);
            dia.setFecha(p_fecha);
            dia.setObservacion(p_observacion);
            dia.setAvance(p_real);
            dia.setCheckBoxes(checkBoxes);
            listadoDias.addView(vDia);
        }
    }

    private void updateProgresoTotal() {
        detalle_progreso.setText("Progreso: " + String.format(Locale.US, FLOAT_FORMAT, avance_real_total) + "%");
        float real = Float.parseFloat(String.format(Locale.US, FLOAT_FORMAT, avance_real_total));
        mProgressBar.setProgress((int) (real * 10000));

    }

    public void tomarFotografia() {

        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    code = SELECT_PICTURE;
                }
                startActivityForResult(intent, code);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();*/

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
            b = Bitmap.createScaledBitmap(b, 640, 480, true);

            imgActual.setBitmap(b);
            imgToSendList.add(imgActual);
            imgActual = null;
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
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
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

                    dibujarDias();

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
            dialog.setCanceledOnTouchOutside(false);
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

                if (dialog.isShowing())
                    dialog.dismiss();
                UploadImage ui = new UploadImage(aContext, imgToSendList);
                ui.execute(dummy.URL_UPLOAD_IMG_WORKTRACKING);
            } else {
                Toast.makeText(aContext, s, Toast.LENGTH_LONG).show();
            }
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }


    public void onClick_apagar(View v) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage("¿Seguro que desea cerrar el Detalle del Proyecto");
        b.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (Seguimiento.actividad != null)
                    Seguimiento.actividad.finish();
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
        //super.onBackPressed();
        onClick_back(null);
    }

    public void onClick_back(View v) {
        InputMethodManager imm = (InputMethodManager) actividad.getSystemService(Context.INPUT_METHOD_SERVICE);
        View foco = actividad.getCurrentFocus();
        if (foco == null || !imm.hideSoftInputFromWindow(foco.getWindowToken(), 0)){
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setMessage("¿Seguro que desea cerrar el Detalle del Proyecto?");
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
}
