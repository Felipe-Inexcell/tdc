package cl.tdc.felipe.tdc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.util.ArrayList;
import java.util.List;

import cl.tdc.felipe.tdc.extras.Funciones;
import cl.tdc.felipe.tdc.objects.ControSeguridadDiario.Elemento;
import cl.tdc.felipe.tdc.objects.MaintChecklist.Modulo;
import cl.tdc.felipe.tdc.objects.MaintChecklist.SubModulo;
import cl.tdc.felipe.tdc.objects.MaintChecklist.Section;
import cl.tdc.felipe.tdc.preferences.FormCheckReg;
import cl.tdc.felipe.tdc.preferences.MaintenanceReg;
import cl.tdc.felipe.tdc.webservice.SoapRequestCheckLists;
import cl.tdc.felipe.tdc.webservice.XMLParserChecklists;

public class FormCheckMaintenance extends Activity {
    Context mContext;
    String Response;
    ArrayList<Modulo> formulario;
    ScrollView scrollViewMain;
    Bitmap firma;
    String ID;
    public static Activity actividad;
    FormCheckReg reg;

    ArrayList<View> vistas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formchecksecurity);

        reg = new FormCheckReg(this, "MAINTENANCEREG");
        mContext = this;
        actividad = this;
        scrollViewMain = (ScrollView) findViewById(R.id.cerca_content);
        Response = getIntent().getStringExtra("RESPONSE");
        ID = getIntent().getStringExtra("ID");
        ObtenerFormulario init = new ObtenerFormulario(this);
        init.execute();
    }

    public void onClick_apagar(View v) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage("¿Seguro que desea salir del CheckList de Mantención?");
        b.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveData();
                actividad.finish();
                if (AgendaActivity.actividad != null)
                    AgendaActivity.actividad.finish();
                if (MainActivity.actividad != null)
                    MainActivity.actividad.finish();
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
            b.setMessage("¿Seguro que desea salir del CheckList de Mantención?");
            b.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    saveData();
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

    public void enviar_form(View v) {
        Enviar task = new Enviar(this);
        task.execute();

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


    /*
    TODO DIBUJAR VISTA
     */
    private void dibujarVista(ArrayList<Modulo> form) {
        LinearLayout contenido = (LinearLayout) this.findViewById(R.id.contenido);

        for (Modulo modulo : form) {
            LinearLayout lModulotitle = new LinearLayout(this);
            lModulotitle.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            lModulotitle.setOrientation(LinearLayout.VERTICAL);

            LinearLayout lModulo = new LinearLayout(this);
            lModulo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            lModulo.setOrientation(LinearLayout.VERTICAL);
            lModulo.setPadding(10, 6, 10, 6);

            TextView tModulo = new TextView(this);
            tModulo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tModulo.setGravity(Gravity.CENTER_HORIZONTAL);
            tModulo.setTextAppearance(this, android.R.style.TextAppearance_Medium);
            tModulo.setText(modulo.getName());
            lModulotitle.addView(tModulo);

            for (SubModulo subModulo : modulo.getSubModulos()) {

                LinearLayout lSubModulo = new LinearLayout(this);
                lSubModulo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                lSubModulo.setOrientation(LinearLayout.VERTICAL);
                lSubModulo.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView tSubModulo = new TextView(this);
                tSubModulo.setText(subModulo.getName());
                tSubModulo.setBackgroundResource(R.drawable.titlebar);
                tSubModulo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                tSubModulo.setGravity(Gravity.CENTER_HORIZONTAL);
                tSubModulo.setTextAppearance(this, android.R.style.TextAppearance_Medium);
                tSubModulo.setTextColor(getResources().getColor(android.R.color.white));
                lSubModulo.addView(tSubModulo);

                for (Section section : subModulo.getSections()) {

                    LinearLayout lSection = new LinearLayout(this);
                    lSection.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    lSection.setOrientation(LinearLayout.VERTICAL);
                    lSection.setGravity(Gravity.CENTER_HORIZONTAL);
                    lSection.setBackgroundResource(R.drawable.fondo_general1_bottom);

                    TextView tSection = new TextView(this);
                    tSection.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    tSection.setText(section.getName());
                    tSection.setGravity(Gravity.CENTER_HORIZONTAL);
                    tSection.setBackgroundColor(Color.parseColor("#226666"));
                    tSection.setTextAppearance(this, android.R.style.TextAppearance_Small);
                    tSection.setTextColor(getResources().getColor(android.R.color.white));
                    lSection.addView(tSection);


                    for (final Elemento elemento : section.getElementos()) {

                        LinearLayout lElemento = new LinearLayout(this);
                        lElemento.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        lElemento.setOrientation(LinearLayout.VERTICAL);
                        lElemento.setGravity(Gravity.CENTER_HORIZONTAL);
                        lElemento.setPadding(24, 8, 24, 8);

                        TextView tElemento = new TextView(this);
                        tElemento.setText(elemento.getName());
                        tElemento.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        tElemento.setGravity(Gravity.CENTER_HORIZONTAL);
                        lElemento.addView(tElemento);

                        if (elemento.getType().compareTo("CHECK") == 0) {
                            LinearLayout checkboxLayout = new LinearLayout(this);
                            checkboxLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            checkboxLayout.setOrientation(LinearLayout.VERTICAL);

                            ArrayList<CheckBox> checkBoxes = new ArrayList<>();
                            int count = 0;
                            while (count < elemento.getValues().size()) {
                                LinearLayout dump = new LinearLayout(this);
                                dump.setOrientation(LinearLayout.HORIZONTAL);
                                dump.setGravity(Gravity.CENTER_HORIZONTAL);
                                dump.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                for (int p = 0; p < 3; p++) {
                                    if (count < elemento.getValues().size()) {
                                        String state = elemento.getValues().get(count);
                                        CheckBox cb = new CheckBox(this);
                                        String id = modulo.getId()+subModulo.getId()+section.getId()+elemento.getId()+elemento.getName()+elemento.getValues().get(count);
                                        cb.setId(Funciones.str2int(id));
                                        cb.setChecked(reg.getBoolean("CHECK"+cb.getId()));
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
                            lElemento.addView(checkboxLayout);

                            elemento.setCheckBoxes(checkBoxes);
                        }
                        if (elemento.getType().compareTo("TEXT") == 0) {
                            EditText campo = new EditText(this);
                            String id = modulo.getId()+subModulo.getId()+elemento.getId()+elemento.getName();
                            campo.setId(Funciones.str2int(id));
                            campo.setText(reg.getString("TEXT"+campo.getId()));
                            campo.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            campo.setBackgroundResource(R.drawable.fondo_edittext);
                            vistas.add(campo);
                            lElemento.addView(campo);
                            elemento.setEditText(campo);
                        }
                        if (elemento.getType().compareTo("FIRMA") == 0) {
                            LinearLayout firmaLayout = new LinearLayout(this);
                            firmaLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            firmaLayout.setOrientation(LinearLayout.HORIZONTAL);
                            firmaLayout.setGravity(Gravity.CENTER_HORIZONTAL);

                            Button firmar = new Button(this);
                            final Button verFirma = new Button(this);

                            firmar.setBackgroundResource(R.drawable.custom_button_rounded_green);
                            verFirma.setBackgroundResource(R.drawable.custom_button_rounded_green);

                            firmar.setText("Firmar");
                            verFirma.setText("Ver Firma");

                            verFirma.setEnabled(false);

                            firmar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    View v = LayoutInflater.from(FormCheckMaintenance.this).inflate(R.layout.view_signature, null, false);

                                    final SignaturePad pad = (SignaturePad) v.findViewById(R.id.signature_pad);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                                    builder.setView(v);
                                    builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            firma = pad.getSignatureBitmap();
                                            elemento.setFirma(firma);
                                            verFirma.setEnabled(true);
                                        }
                                    });
                                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });

                                    builder.setNeutralButton("Borrar", null);
                                    final AlertDialog dialog = builder.create();
                                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                                        @Override
                                        public void onShow(DialogInterface d) {

                                            Button b = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                                            b.setOnClickListener(new View.OnClickListener() {

                                                @Override
                                                public void onClick(View view) {
                                                    pad.clear();
                                                }
                                            });
                                        }
                                    });

                                    dialog.show();
                                }
                            });

                            verFirma.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                                    b.setNeutralButton("Cerrar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    b.setNegativeButton("Borrar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            firma = null;
                                            elemento.setFirma(firma);
                                            verFirma.setEnabled(false);
                                            Toast.makeText(mContext, " Se  ha eliminado la firma.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    View v = LayoutInflater.from(mContext).inflate(R.layout.view_signature_preview, null, false);
                                    ImageView IVpreview = (ImageView) v.findViewById(R.id.preview);
                                    IVpreview.setImageBitmap(firma);
                                    b.setView(v);
                                    b.setTitle("Vista Previa Firma");
                                    b.show();
                                }
                            });

                            String tmp = reg.getString("FIRMA");
                            if(!tmp.equals("")){
                                firma = Funciones.decodeBase64(tmp);
                                verFirma.setEnabled(true);
                            }

                            firmaLayout.addView(firmar);
                            firmaLayout.addView(verFirma);


                            lElemento.addView(firmaLayout);
                        }

                        lSection.addView(lElemento);

                    }
                    lSubModulo.addView(lSection);
                }

                lModulo.addView(lSubModulo);
            }

            contenido.addView(lModulotitle);
            contenido.addView(lModulo);
        }
    }


    /*
    TODO Obtener Formulario
     */
    private class ObtenerFormulario extends AsyncTask<String, String, ArrayList<Modulo>> {
        private final String ASYNCTAG = "OBTENERFORMULARIO";
        Context context;
        ProgressDialog dialog;

        public ObtenerFormulario(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage("Obteniendo Checklist...");
            dialog.show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<Modulo> doInBackground(String... params) {
            try {
                Log.w("FORMCHECK", Response);
                ArrayList<Modulo> m = XMLParserChecklists.getChecklistMaintenance(Response);
                return m;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(ASYNCTAG, e.getMessage() + ": " + e.getCause());
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Modulo> response) {
            if (response != null) {
                dibujarVista(response);
                formulario = response;
            } else {
                Toast.makeText(context, "Ha ocurrido un error al dibujar el checklist, por favor reintente", Toast.LENGTH_LONG).show();
                FormCheckMaintenance.this.finish();
            }
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

    /*
   TODO Enviar Formulario
    */
    private class Enviar extends AsyncTask<String, String, String> {
        private final String ASYNCTAG = "ENVIARFORMULARIO";
        Context context;
        ProgressDialog dialog;
        boolean ok = false;

        public Enviar(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage("Enviando Formulario...");
            dialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            try {
                String request = SoapRequestCheckLists.sendMainChecklist(ID, formulario);
                Log.d("ENVIANDOFORM", request);
                String[] parse = XMLParserChecklists.getResultCode(request).split(";");

                if (parse[0].compareTo("0") == 0) {
                    ok = true;
                    return parse[1];
                } else {
                    ok = false;
                    return parse[1];
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(ASYNCTAG, e.getMessage() + ": " + e.getCause());
                return e.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(mContext, response, Toast.LENGTH_LONG).show();
            if (ok) {
                MaintenanceReg pref = new MaintenanceReg(mContext);
                pref.setChecklistState(true);
                ((Activity) mContext).finish();
            }

            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

    private void saveData(){
        if(firma != null){
            reg.addValue("FIRMA", Funciones.encodeTobase64(firma));
        }
        for(View v: vistas){
            if(v instanceof CheckBox ){
                reg.addValue("CHECK" + v.getId(), ((CheckBox) v).isChecked());
            }
            if(v instanceof EditText){
                reg.addValue("TEXT" + v.getId(), ((EditText) v).getText().toString());
            }
        }
    }

}
