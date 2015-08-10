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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.util.ArrayList;
import java.util.List;

import cl.tdc.felipe.tdc.objects.ControSeguridadDiario.Elemento;
import cl.tdc.felipe.tdc.objects.ControSeguridadDiario.Modulo;
import cl.tdc.felipe.tdc.objects.ControSeguridadDiario.SubModulo;
import cl.tdc.felipe.tdc.objects.FormSubSystem;
import cl.tdc.felipe.tdc.objects.FormSubSystemItem;
import cl.tdc.felipe.tdc.objects.FormSubSystemItemAttribute;
import cl.tdc.felipe.tdc.objects.FormSubSystemItemAttributeValues;
import cl.tdc.felipe.tdc.objects.FormSystem;
import cl.tdc.felipe.tdc.objects.FormularioCheck;
import cl.tdc.felipe.tdc.preferences.MaintenanceReg;
import cl.tdc.felipe.tdc.webservice.SoapRequest;
import cl.tdc.felipe.tdc.webservice.XMLParser;
import cl.tdc.felipe.tdc.webservice.XMLParserChecklists;

public class FormCheckSecurity extends Activity {
    Context mContext;
    String Response;
    FormularioCheck formulario;
    ArrayList<Modulo> modulos;
    ScrollView scrollViewMain;
    Bitmap firma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formchecksecurity);

        mContext = this;
        scrollViewMain = (ScrollView)findViewById(R.id.cerca_content);
        Response = getIntent().getStringExtra("RESPONSE");
        ObtenerFormulario init = new ObtenerFormulario(this);
        init.execute();
    }

    public void onClick_apagar(View v) {
        finish();
        if(AgendaActivity.actividad!=null)
            AgendaActivity.actividad.finish();
        if(MainActivity.actividad!= null)
            MainActivity.actividad.finish();
    }


    public void onClick_back(View v) {
        finish();
    }

    public void enviar_form(View v){
        String TAG = "ENVIARFORM";

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
    private void dibujarVista(ArrayList<Modulo> formulario) {
        LinearLayout contenido = (LinearLayout) this.findViewById(R.id.contenido);
        final List<LinearLayout> subcontenidos = new ArrayList<>();

        for(Modulo modulo : formulario){
            LinearLayout lModulotitle = new LinearLayout(this);
            lModulotitle.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            lModulotitle.setOrientation(LinearLayout.VERTICAL);

            LinearLayout lModulo = new LinearLayout(this);
            lModulo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            lModulo.setOrientation(LinearLayout.VERTICAL);
            lModulo.setBackgroundResource(R.drawable.fondo_general_bottom);
            lModulo.setPadding(10,6,10,6);

            TextView tModulo = new TextView(this);
            tModulo.setBackgroundResource(R.drawable.titlebar);
            tModulo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tModulo.setGravity(Gravity.CENTER_HORIZONTAL);
            tModulo.setTextAppearance(this, android.R.style.TextAppearance_Medium);
            tModulo.setTextColor(getResources().getColor(android.R.color.white));
            tModulo.setText(modulo.getName());
            lModulotitle.addView(tModulo);

            for(SubModulo subModulo: modulo.getSubModulos()){

                LinearLayout lSubModulo = new LinearLayout(this);
                lSubModulo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                lSubModulo.setOrientation(LinearLayout.VERTICAL);
                lSubModulo.setGravity(Gravity.CENTER_HORIZONTAL);
                lSubModulo.setBackgroundResource(R.drawable.fondo_1);

                TextView tSubModulo = new TextView(this);
                tSubModulo.setText(subModulo.getName());
                tSubModulo.setBackgroundColor(Color.parseColor("#226666"));
                tSubModulo.setTextAppearance(this, android.R.style.TextAppearance_Small);
                tSubModulo.setTextColor(getResources().getColor(android.R.color.white));
                tSubModulo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                tSubModulo.setGravity(Gravity.CENTER_HORIZONTAL);
                lSubModulo.addView(tSubModulo);



                for(Elemento elemento : subModulo.getElementos()){
                    List<CheckBox> checkBoxes = new ArrayList<>();

                    LinearLayout lElemento = new LinearLayout(this);
                    lElemento.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    lElemento.setOrientation(LinearLayout.VERTICAL);
                    lElemento.setGravity(Gravity.CENTER_HORIZONTAL);
                    lElemento.setPadding(24,8,24,8);

                    TextView tElemento = new TextView(this);
                    tElemento.setText(elemento.getName());
                    tElemento.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    tElemento.setGravity(Gravity.CENTER_HORIZONTAL);
                    lElemento.addView(tElemento);

                    if(elemento.getType().compareTo("CHECK") == 0){
                        LinearLayout checkboxLayout = new LinearLayout(this);
                        checkboxLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        checkboxLayout.setOrientation(LinearLayout.VERTICAL);

                        int count = 0;
                        while(count < elemento.getValues().size()){
                            LinearLayout dump = new LinearLayout(this);
                            dump.setOrientation(LinearLayout.HORIZONTAL);
                            dump.setGravity(Gravity.CENTER_HORIZONTAL);
                            dump.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            for(int p = 0; p < 3; p++){
                                if(count < elemento.getValues().size()) {
                                    String state = elemento.getValues().get(count);
                                    CheckBox cb = new CheckBox(this);
                                    cb.setText(state);
                                    checkBoxes.add(cb);
                                    dump.addView(cb);
                                    count++;
                                }
                            }
                            checkboxLayout.addView(dump);

                        }
                        makeOnlyOneCheckable(checkBoxes);
                        lElemento.addView(checkboxLayout);
                    }
                    if(elemento.getType().compareTo("TEXT") == 0){
                        EditText campo = new EditText(this);
                        //campo.setText("test");
                        campo.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        campo.setBackgroundResource(R.drawable.fondo_edittext);
                        lElemento.addView(campo);
                    }
                    if(elemento.getType().compareTo("FIRMA")== 0){
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
                                View v = LayoutInflater.from(FormCheckSecurity.this).inflate(R.layout.view_signature, null, false);

                                final SignaturePad pad = (SignaturePad) v.findViewById(R.id.signature_pad);

                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                                builder.setView(v);
                                builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        firma = pad.getSignatureBitmap();
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

                        firmaLayout.addView(firmar);
                        firmaLayout.addView(verFirma);



                        lElemento.addView(firmaLayout);
                    }

                    lSubModulo.addView(lElemento);

                }

                lModulo.addView(lSubModulo);
            }

            contenido.addView(lModulotitle);
            contenido.addView(lModulo);
        }
        /*for (FormSystem System : formulario.getSystem()) {

            Button buttonSystem = new Button(this);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            //buttonSystem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            buttonSystem.setText(System.getNameSystem());

            if(formulario.getSystem().indexOf(System) == 0) {
                buttonSystem.setBackgroundResource(R.drawable.accordion_top);
            }
            else{
                buttonSystem.setBackgroundResource(R.drawable.accordion_topc);
                param.setMargins(0, 10, 0, 0);
            }
            buttonSystem.setLayoutParams(param);
            buttonSystem.setClickable(true);
            contenido.addView(buttonSystem);


            final LinearLayout bottom = new LinearLayout(this);
            bottom.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            bottom.setOrientation(LinearLayout.VERTICAL);
            bottom.setBackgroundResource(R.drawable.accordion_bot);
            bottom.setVisibility(View.GONE);

            for (FormSubSystem subSystem : System.getSubSystemList()) {
                TextView nameSubSystem = new TextView(this);
                nameSubSystem.setText(subSystem.getNameSubSystem());
                nameSubSystem.setTextSize(16);
                nameSubSystem.setGravity(Gravity.CENTER_HORIZONTAL);
                nameSubSystem.setPadding(0, 15, 0, 10);

                //contenido.addView(nameSubSystem);
                bottom.addView(nameSubSystem);

                for (int i = 0; i < subSystem.getItemList().size(); i++) {
                    FormSubSystemItem item = subSystem.getItemList().get(i);
                    LinearLayout itemLayout = new LinearLayout(this);

                    /*if (i == 0)
                        itemLayout.setBackgroundResource(R.drawable.fondo_general1_top);
                    else if (i == (subSystem.getItemList().size() - 1))
                        itemLayout.setBackgroundResource(R.drawable.fondo_general1_bottom);
                    else
                        itemLayout.setBackgroundResource(R.drawable.fondo_general1_center);
                        *

                    itemLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                    itemLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    itemLayout.setPadding(16, 5, 16, 5);
                    itemLayout.setOrientation(LinearLayout.VERTICAL);

                    TextView titulo = new TextView(this);
                    titulo.setText(item.getNameItem());
                    titulo.setTextSize(14);
                    titulo.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    titulo.setGravity(Gravity.CENTER_HORIZONTAL);
                    titulo.setBackgroundResource(R.drawable.fondo_general_top);
                    titulo.setTextColor(Color.WHITE);

                    itemLayout.addView(titulo);

                    LinearLayout atributosLayout = new LinearLayout(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    atributosLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                    atributosLayout.setOrientation(LinearLayout.VERTICAL);
                    atributosLayout.setPadding(10, 10, 10, 10);
                    atributosLayout.setBackgroundResource(R.drawable.fondo_general_bottom);

                    for (FormSubSystemItemAttribute attribute : item.getAttributeList()) {
                        LinearLayout valuesLayout = new LinearLayout(this);
                        valuesLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        valuesLayout.setOrientation(LinearLayout.VERTICAL);

                        TextView atributo = new TextView(this);
                        atributo.setText(attribute.getNameAttribute());
                        atributo.setTextSize(14);
                        atributo.setTextColor(Color.BLUE);
                        atributo.setPadding(0, 8, 0, 0);
                        atributo.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        atributo.setGravity(Gravity.CENTER_HORIZONTAL);
                        if (attribute.getNameAttribute().compareTo("") == 0)
                            atributo.setVisibility(View.GONE);


                        for (FormSubSystemItemAttributeValues values : attribute.getValuesList()) {
                            if (values.getValueState() != null && values.getTypeValue().compareTo("CHECK") == 0) {
                                List<CheckBox> checkBoxes = new ArrayList<>();
                                /*LinearLayout checkboxLayout = new LinearLayout(this);
                                checkboxLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                checkboxLayout.setOrientation(LinearLayout.HORIZONTAL);
                                for (int st = 0; st < values.getValueState().size(); st++) {
                                    String state = values.getValueState().get(st);
                                    CheckBox cb = new CheckBox(this);
                                    cb.setText(state);
                                    checkBoxes.add(cb);
                                    checkboxLayout.addView(cb);

                                }*
                                LinearLayout checkboxLayout = new LinearLayout(this);
                                checkboxLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                checkboxLayout.setOrientation(LinearLayout.VERTICAL);

                                /*
                                Este WHILE lo que hace es dibujar a los mas 2 checkbox por linea, para evitar que queden
                                elementos fuera de la vista.


                                int count = 0;
                                while(count < values.getValueState().size()){
                                    LinearLayout dump = new LinearLayout(this);
                                    dump.setOrientation(LinearLayout.HORIZONTAL);
                                    dump.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    for(int p = 0; p < 2; p++){
                                        String state = values.getValueState().get(count);
                                        CheckBox cb = new CheckBox(this);
                                        cb.setText(state);
                                        checkBoxes.add(cb);
                                        dump.addView(cb);
                                        count++;
                                    }
                                    checkboxLayout.addView(dump);

                                }
                                makeOnlyOneCheckable(checkBoxes);
                                values.setCheckBoxes(checkBoxes);
                                valuesLayout.addView(atributo);
                                valuesLayout.addView(checkboxLayout);

                            }
                            if (values.getTypeValue().compareTo("TEXT") == 0) {
                                EditText campo = new EditText(this);
                                //campo.setText("test");
                                campo.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                campo.setBackgroundResource(R.drawable.fondo_edittext);
                                valuesLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                valuesLayout.addView(atributo);
                                valuesLayout.addView(campo);
                                values.setEditText(campo);
                            }

                        }
                        atributosLayout.addView(valuesLayout);
                    }
                    atributosLayout.setLayoutParams(layoutParams);
                    itemLayout.addView(atributosLayout);

                    //contenido.addView(itemLayout);
                    bottom.addView(itemLayout);
                }
            }
            subcontenidos.add(bottom);
            contenido.addView(bottom);
            System.setContenido(bottom);

            buttonSystem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(bottom.getVisibility() == View.GONE){
                        bottom.setVisibility(View.VISIBLE);
                        for(LinearLayout l: subcontenidos){
                            if(l != bottom)
                                l.setVisibility(View.GONE);
                        }
                        scrollViewMain.smoothScrollTo(0, bottom.getTop());
                    }else{
                        bottom.setVisibility(View.GONE);
                    }
                }
            });


        }*/
    }

    private View addItem(String type) {
        LinearLayout l = new LinearLayout(this);
        switch (type){
            case "CHECK":
                break;
            case "TEXT":
                break;
            case "FIRMA":
                break;
        }

        return l;

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
                ArrayList<Modulo> m = XMLParserChecklists.getChecklistDaily(Response);
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
                    modulos = response;
            } else {
                Toast.makeText(context, "Ha ocurrido un error al dibujar el checklist, por favor reintente", Toast.LENGTH_LONG).show();
                FormCheckSecurity.this.finish();
            }
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

    /*
   TODO Enviar Formulario
    */
    private class Enviar extends AsyncTask<String, String, ArrayList<String>> {
        private final String ASYNCTAG = "OBTENERFORMULARIO";
        Context context;
        ProgressDialog dialog;

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
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            try {
                String request = SoapRequest.FormSave("", formulario);
                Log.d("ENVIANDOFORM", request);
                ArrayList<String> parse = XMLParser.getReturnCode1(request);
                return parse;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(ASYNCTAG, e.getMessage() + ": " + e.getCause());
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<String> response) {
            if (response != null) {
                if(response.get(0).compareTo("0")==0){
                    Toast.makeText(context, response.get(1), Toast.LENGTH_LONG).show();

                    MaintenanceReg m = new MaintenanceReg(getApplicationContext());
                    m.clearPreferences();

                    if(AgendaActivity.actividad != null){
                        AgendaActivity.actividad.finish();
                    }
                    FormCheckSecurity.this.finish();

                }else{
                    Toast.makeText(context, response.get(1), Toast.LENGTH_LONG).show();
                    FormCheckSecurity.this.finish();
                }
            } else {
                Toast.makeText(context, "Ha ocurrido un error, por favor reintente", Toast.LENGTH_LONG).show();

            }
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

}
