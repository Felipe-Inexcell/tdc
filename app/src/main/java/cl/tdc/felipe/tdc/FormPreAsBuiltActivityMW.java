package cl.tdc.felipe.tdc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import cl.tdc.felipe.tdc.extras.Funciones;
import cl.tdc.felipe.tdc.objects.FormImage;
import cl.tdc.felipe.tdc.objects.Relevar.Item;
import cl.tdc.felipe.tdc.objects.Relevar.Modulo;
import cl.tdc.felipe.tdc.preferences.FormCheckReg;
import cl.tdc.felipe.tdc.webservice.SoapRequest;
import cl.tdc.felipe.tdc.webservice.SoapRequestPreAsBuilt;
import cl.tdc.felipe.tdc.webservice.XMLParser;
import cl.tdc.felipe.tdc.webservice.XMLParserPreAsBuilt;
import cl.tdc.felipe.tdc.webservice.dummy;


public class FormPreAsBuiltActivityMW extends Activity {
    Context mContext;
    int ID;
    ArrayList<Modulo> modulos;
    ArrayList<FormImage> imagenes = new ArrayList<>();
    FormImage imgTmp;
    Button continuar;
    String queryResp;
    String itemsXML;
    String photosXML = "";


    public static Activity activity;

    FormCheckReg reg;
    String name;
    private static int TAKE_PICTURE = 0;

    ArrayList<Item> aerial;
    ArrayList<ArrayList<Item>> aerialList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_mw);

        mContext = this;
        activity = this;
        ID = getIntent().getIntExtra("ID", -1);
        name = Environment.getExternalStorageDirectory() + "/TDC@/MWCaptura.jpg";
        reg = new FormCheckReg(this, "CHECKMW");
        ObtenerCheck task = new ObtenerCheck();
        task.execute();

        continuar = (Button) findViewById(R.id.imageButton3);
        continuar.setText("CONTINUAR");

        aerial = new ArrayList<>();
    }


    public void onClick_apagar(View v) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage("¿Seguro que desea salir del CheckList de MW?");
        b.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveData();
                if (PreAsBuiltActivity.activity != null)
                    PreAsBuiltActivity.activity.finish();
                if (MainActivity.actividad != null)
                    MainActivity.actividad.finish();
                ((Activity) mContext).finish();
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
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View foco = activity.getCurrentFocus();
        if (foco == null || !imm.hideSoftInputFromWindow(foco.getWindowToken(), 0)){
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setMessage("¿Seguro que desea salir del CheckList de MW?");
            b.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    saveData();
                    activity.finish();
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

    public void onClick_enviar(View v) {
        for (Modulo m : modulos) {
            for (Modulo sm : m.getSubModulo()) {
                for (Item item : sm.getItems()) {
                    if (item.getValor().equals("NO RESPONDE") || item.getValor().length() == 0) {
                        Toast.makeText(this, "Debe responder el CheckList completo.", Toast.LENGTH_LONG).show();
                        if(item.getVista() != null)item.getVista().requestFocus();
                        return;
                    }
                }
            }
        }

        UploadImage task = new UploadImage(mContext);
        task.execute();
    }

    private class ObtenerCheck extends AsyncTask<String, String, String> {
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
                queryResp = query;
                ArrayList<String> parse = XMLParser.getReturnCode2(query);

                if (ok = parse.get(0).equals("0")) {
                    return query;
                } else
                    return parse.get(1);


            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (ok) {
                try {
                    modulos = XMLParserPreAsBuilt.getCheckMW(s);
                    dibujar();

                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XPathExpressionException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
                ((Activity) mContext).finish();
            }
            if (d.isShowing()) d.dismiss();
        }
    }

    private void dibujar() {
        LinearLayout contenido = (LinearLayout) findViewById(R.id.content);
        LinearLayout.LayoutParams scontentParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams tituloParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams photoParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        tituloParams.weight = 4;
        photoParams.weight = 1;

        scontentParam.setMargins(0,0,0,15);

        for (Modulo m : modulos) {
            if(m.getName().equals("EQUIPAMIENTO") && m.getItems().size() <= 1 ){
                continue;
            }
            Button mTitulo = new Button(this);
            mTitulo.setText(m.getName());
            mTitulo.setGravity(Gravity.CENTER);
            mTitulo.setTextColor(Color.WHITE);
            mTitulo.setBackgroundResource(R.drawable.module_bg);

            final LinearLayout mContent = new LinearLayout(this);
            mContent.setOrientation(LinearLayout.VERTICAL);
            mContent.setGravity(Gravity.CENTER_HORIZONTAL);

            for (final Item item : m.getItems()) {
                LinearLayout titleItem = new LinearLayout(this);
                titleItem.setBackgroundColor(Color.parseColor("#004D40"));
                titleItem.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                titleItem.setOrientation(LinearLayout.HORIZONTAL);
                titleItem.setGravity(Gravity.CENTER_HORIZONTAL);

                ImageButton photo = new ImageButton(this);
                photo.setImageResource(R.drawable.ic_camerawhite);
                photo.setBackgroundResource(R.drawable.button_dark_green);
                photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = buscarFoto(item.getId());
                        if (position == -1) {
                            imgTmp = new FormImage();
                            imgTmp.setId(ID);
                            imgTmp.setIdSystem(item.getId());
                            imgTmp.setIdSubSystem(item.getnAerial());
                            tomarFoto();
                        } else {
                            mostrarImagen(position);
                        }
                    }
                });

                TextView iTitulo = new TextView(this);
                iTitulo.setBackgroundColor(Color.parseColor("#004D40"));
                iTitulo.setTextColor(Color.WHITE);
                iTitulo.setText(item.getName());
                iTitulo.setGravity(Gravity.CENTER);
                View v = getView(m.getId(), item, -1);
                if (v == null) continue;


                titleItem.addView(iTitulo, tituloParams);
                titleItem.addView(photo, photoParams);


                mContent.addView(titleItem, itemParam);
                mContent.addView(v, scontentParam);
            }

            mTitulo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mContent.getVisibility() == View.GONE) mContent.setVisibility(View.VISIBLE);
                    else mContent.setVisibility(View.GONE);
                }
            });
            mContent.setVisibility(View.GONE);
            contenido.addView(mTitulo);
            contenido.addView(mContent);

            continuar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (Modulo m : modulos) {
                        for (Item i : m.getItems()) {
                            if (i.getType().compareTo("COMPLEX") != 0) {
                                if (i.getValor().equals("NO RESPONDE") || i.getValor().length() == 0) {
                                    if(i.getVista() != null && i.getVista() instanceof EditText){
                                        i.getVista().requestFocus();
                                    }
                                    Toast.makeText(mContext, "Debe responder el CheckList completo.", Toast.LENGTH_LONG).show();
                                    return;
                                }

                            }
                        }
                    }

                    UploadImage task = new UploadImage(mContext);
                    task.execute();


                }
            });


        }


    }


    private View getView(final int mId, final Item item, int flag) {
        loadImg(item.getId());

        String type = item.getType();
        List<String> values = item.getValues();
        LinearLayout contenido = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        contenido.setLayoutParams(params);
        params.setMargins(0, 6, 0, 0);
        contenido.setOrientation(LinearLayout.VERTICAL);
        contenido.setGravity(Gravity.CENTER_HORIZONTAL);

        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);


        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.weight = 1;
        params2.weight = 2;
        params3.weight = 4;

        String comment = "";

        String id = mId + item.getId() + item.getName() + flag;
        if (type.equals("SELECT")) {
            Spinner s = new Spinner(this);
            s.setBackgroundResource(R.drawable.spinner_bg);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, values);
            s.setAdapter(adapter);
            s.setId(Funciones.str2int(id));
            String Selected = reg.getString("SELECT" + s.getId());
            s.setSelection(adapter.getPosition(Selected));
            itemLayout.addView(s, params3);
            item.setVista(s);
            comment = reg.getString("COMMENTSELECT" + s.getId());
        } else if (type.equals("CHECK")) {
            LinearLayout checkboxLayout = new LinearLayout(this);
            checkboxLayout.setOrientation(LinearLayout.VERTICAL);
            ArrayList<CheckBox> checkBoxes = new ArrayList<>();
            int count = 0;
            while (count < values.size()) {
                LinearLayout dump = new LinearLayout(this);
                dump.setOrientation(LinearLayout.HORIZONTAL);
                dump.setGravity(Gravity.CENTER_HORIZONTAL);
                dump.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                for (int p = 0; p < 3; p++) {
                    if (count < values.size()) {
                        String state = values.get(count);
                        CheckBox cb = new CheckBox(this);
                        id += values.get(count);
                        cb.setId(Funciones.str2int(id));
                        cb.setChecked(reg.getBoolean("CHECK" + cb.getId()));
                        cb.setText(state);
                        checkBoxes.add(cb);
                        dump.addView(cb);

                        if (count == 0)
                            comment = reg.getString("COMMENTCHECK" + cb.getId());
                        count++;
                    }
                }
                checkboxLayout.addView(dump);
            }
            makeOnlyOneCheckable(checkBoxes);
            item.setCheckBoxes(checkBoxes);
            itemLayout.addView(checkboxLayout, params3);
        } else if (type.equals("NUM")) {
            EditText e = new EditText(this);
            e.setBackgroundResource(R.drawable.fondo_edittext);
            e.setInputType(InputType.TYPE_CLASS_NUMBER);

            e.setId(Funciones.str2int(id));
            e.setText(reg.getString("TEXT" + e.getId()));
            e.setGravity(Gravity.CENTER_HORIZONTAL);
            item.setVista(e);
            itemLayout.addView(e, params3);
            comment = reg.getString("COMMENTTEXT" + e.getId());
        } else if (type.equals("VARCHAR")) {
            EditText e = new EditText(this);
            e.setBackgroundResource(R.drawable.fondo_edittext);
            e.setInputType(InputType.TYPE_CLASS_TEXT);
            e.setLines(2);
            e.setGravity(Gravity.LEFT | Gravity.TOP);

            e.setId(Funciones.str2int(id));
            e.setText(reg.getString("TEXT" + e.getId()));

            item.setVista(e);
            itemLayout.addView(e, params3);
            comment = reg.getString("COMMENTTEXT" + e.getId());
        } else if (type.equals("COMPLEX")) {
            aerial = item.getSubItems();
            return null;
            /*final EditText e = new EditText(this);
            e.setBackgroundResource(R.drawable.fondo_edittext);
            e.setInputType(InputType.TYPE_CLASS_NUMBER);
            e.setEnabled(false);
            e.setText("0");
            e.setId(Funciones.str2int(id));
            //e.setText(reg.getString("TEXT" + e.getId()));
            e.setGravity(Gravity.CENTER_HORIZONTAL);
            item.setVista(e);
            itemLayout.addView(e, params3);

            ImageButton add = new ImageButton(mContext);
            add.setImageResource(R.drawable.ic_add1white);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addAerial(mId, sId,e);
                }
            });
            itemLayout.addView(add, params1);
            comment = reg.getString("COMMENTTEXT" + e.getId());
*/
        } else return null;



        //itemLayout.addView(photo, params1);
        contenido.addView(itemLayout);

        EditText comentario = new EditText(this);
        comentario.setLayoutParams(params);
        comentario.setBackgroundResource(R.drawable.fondo_edittext);
        comentario.setLines(3);
        comentario.setText(comment);
        comentario.setHint("Observaciones");

        item.setDescription(comentario);
        contenido.addView(comentario);


        return contenido;

    }



    private void mostrarImagen(final int position) {
        FormImage photo = imagenes.get(position);

        ImageView view = new ImageView(this);
        view.setImageBitmap(photo.getImage());

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setView(view);
        b.setMessage("Tipo: "+photo.getType()+"\nDescripción: "+photo.getDescription()+"\nComentario: "+photo.getComment());
        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        b.setNeutralButton("Borrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                imagenes.remove(position);
                Toast.makeText(mContext, "Imagen eliminada del registro.", Toast.LENGTH_LONG).show();
                dialogInterface.dismiss();
            }
        });
        b.show();
    }

    private int buscarFoto(int id) {
        for (int i = 0; i < imagenes.size(); i++) {
            if (imagenes.get(i).getIdSystem() == id) {
                return i;
            }
        }
        return -1;
    }

    public void tomarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        int code = TAKE_PICTURE;
        Uri output = Uri.fromFile(new File(name));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        startActivityForResult(intent, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == -1) {
                if (data != null) {
                    if (data.hasExtra("data")) {
                        imgTmp.setImage(Bitmap.createScaledBitmap((Bitmap) data.getParcelableExtra("data"), 640, 480, true));
                    }
                } else {
                    imgTmp.setImage(Bitmap.createScaledBitmap(BitmapFactory.decodeFile(name), 640, 480, true));

                }

                LinearLayout vista = new LinearLayout(this);
                vista.setOrientation(LinearLayout.VERTICAL);

                final EditText type = new EditText(this);
                type.setHint("Qué fotografió?");
                final EditText description = new EditText(this);
                description.setHint("Descripción");
                final EditText comment = new EditText(this);
                comment.setHint("Comentarios");

                vista.addView(type);
                vista.addView(description);
                vista.addView(comment);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Detalles Fotografía");
                builder.setView(vista);
                builder.setPositiveButton("Guardar", null);
                builder.setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        imgTmp = null;
                    }
                });

                final AlertDialog dialog = builder.create();
                dialog.show();

                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(type.getText().length() > 0 && description.getText().length()>0 && comment.getText().length() > 0 ){
                            imgTmp.setType(type.getText().toString());
                            imgTmp.setDescription(description.getText().toString());
                            imgTmp.setComment(comment.getText().toString());
                            String nombre = getNameItem(imgTmp.getIdSystem());
                            if (nombre != null) {
                                imgTmp.newNameRF(imgTmp.getIdSystem(), nombre);
                                imagenes.add(imgTmp);
                            }
                            imgTmp = null;

                            dialog.dismiss();
                        }else{
                            Toast.makeText(mContext, "Por favor ingrese toda la información para continuar", Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        }
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                reg.clearPreferences();
                if (PreAsBuiltActivity.activity != null) ;
                PreAsBuiltActivity.activity.finish();
                finish();
                Log.d("RESULT", "AERIAL " + resultCode);
            }
        }
    }


    private String getNameItem(int id) {
        for (Modulo m : modulos) {
                for (Item i : m.getItems()) {
                    if (id == i.getId()) {
                        return i.getName();
                    }
                }

        }
        return null;
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

    private void loadImg(int idItem) {
        if (!reg.getString("NAMEIMG" + idItem).equals("")) {
            imgTmp = new FormImage();
            imgTmp.setIdSystem(idItem);
            imgTmp.setName(reg.getString("NAMEIMG" + idItem));
            imgTmp.setImage(Funciones.decodeBase64(reg.getString("BITMAPIMG" + idItem)));
            imgTmp.setComment(reg.getString("COMMENTIMG" + idItem));
            imgTmp.setDescription(reg.getString("DESCIMG" + idItem));
            imgTmp.setType(reg.getString("TYPEIMG" + idItem));
            imagenes.add(imgTmp);
        }
    }

    private void saveData() {

        for (FormImage img : imagenes) {
            reg.addValue("BITMAPIMG" + img.getIdSystem(), Funciones.encodeTobase64(img.getImage()));
            reg.addValue("NAMEIMG" + img.getIdSystem(), img.getName());
            reg.addValue("COMMENTIMG" + img.getIdSystem(), img.getComment());
            reg.addValue("DESCIMG" + img.getIdSystem(), img.getDescription());
            reg.addValue("TYPEIMG" + img.getIdSystem(), img.getType());
        }
        for (Modulo m : modulos) {
                for (Item i : m.getItems()) {
                    View v = i.getVista();
                    View c = i.getDescription();
                    if (i.getCheckBoxes() != null) {
                        for (int x = 0; x < i.getCheckBoxes().size(); x++) {
                            CheckBox check = i.getCheckBoxes().get(x);
                            reg.addValue("CHECK" + check.getId(), check.isChecked());
                            if (x == 0)
                                reg.addValue("COMMENTCHECK" + check.getId(), ((EditText) c).getText().toString());
                        }
                    }
                    if (v instanceof Spinner) {
                        reg.addValue("SELECT" + v.getId(), ((Spinner) v).getSelectedItem().toString());
                        reg.addValue("COMMENTSELECT" + v.getId(), ((EditText) c).getText().toString());
                    }
                    if (v instanceof EditText) {
                        reg.addValue("TEXT" + v.getId(), ((EditText) v).getText().toString());
                        reg.addValue("COMMENTTEXT" + v.getId(), ((EditText) c).getText().toString());
                    }

                }

        }

    }

   /* private class Enviar extends AsyncTask<String, String, String> {
        ProgressDialog d;
        boolean ok = false;
        Context context;

        private Enviar(Context c) {
            this.context = c;
        }

        @Override
        protected void onPreExecute() {
            d = new ProgressDialog(mContext);
            d.setMessage("Enviando Checklist...");
            d.setCancelable(false);
            d.setCanceledOnTouchOutside(false);
            d.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String query = SoapRequestPreAsBuilt.sendCheckRF(getIntent().getIntExtra("ID", -100), "");

                ArrayList<String> reponse = XMLParser.getReturnCode2(query);


                ok = reponse.get(0).equals("0");

                return reponse.get(1);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                return e.getMessage();
            } catch (SAXException e) {
                e.printStackTrace();
                return e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            } catch (XPathExpressionException e) {
                e.printStackTrace();
                return e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String s) {

            Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();
            if (ok) {
                reg.clearPreferences();
                ((Activity) mContext).finish();
            }

            if (d.isShowing()) d.dismiss();
        }
    }*/


    class UploadImage extends AsyncTask<String, String, String> {

        private Context mContext;
        ProgressDialog dialog;
        private Item complex;

        public UploadImage(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            dialog.setMessage(values[0]);
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";

            for (FormImage img : imagenes) {
                if (!img.isSend()) {
                    try {
                        String fileName = img.getName();

                        publishProgress(fileName);
                        Log.i("ENVIANDO", fileName);
                        Log.i("ENVIANDO", "comentario: " + img.getComment());
                        Log.i("ENVIANDO", "system: " + img.getIdSystem());
                        Log.i("ENVIANDO", "subsystem: " + img.getIdSubSystem());
                        HttpURLConnection conn;
                        DataOutputStream dos;
                        String lineEnd = "\r\n";
                        String twoHyphens = "--";
                        String boundary = "*****";
                        int bytesRead, bytesAvailable, bufferSize;

                        File done = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName);
                        done.createNewFile();

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        img.getImage().compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                        byte[] bitmapdata = bos.toByteArray();

                        FileOutputStream fos = new FileOutputStream(done);
                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();

                        if (!done.isFile())
                            Log.e("DownloadManager", "no existe");
                        else {
                            FileInputStream fileInputStream = new FileInputStream(done);
                            URL url = new URL(dummy.URL_MW_IMG);

                            conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true);
                            conn.setDoOutput(true);
                            conn.setUseCaches(false);
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Connection", "Keep-Alive");
                            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                            conn.setRequestProperty("uploaded_file", fileName);

                            dos = new DataOutputStream(conn.getOutputStream());

                            dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);
                            dos.writeBytes(lineEnd);

                            bytesAvailable = fileInputStream.available();

                            bufferSize = Math.min(bytesAvailable, 1 * 1024 * 1024);
                            byte[] buf = new byte[bufferSize];

                            bytesRead = fileInputStream.read(buf, 0, bufferSize);

                            while (bytesRead > 0) {

                                dos.write(buf, 0, bufferSize);
                                bytesAvailable = fileInputStream.available();
                                bufferSize = Math.min(bytesAvailable, 1 * 1024 * 1024);
                                bytesRead = fileInputStream.read(buf, 0, bufferSize);

                            }

                            dos.writeBytes(lineEnd);
                            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                            int serverResponseCode = conn.getResponseCode();
                            String serverResponseMessage = conn.getResponseMessage();


                            Log.i("UploadManager", "HTTP response is: " + serverResponseMessage + ": " + serverResponseCode);

                            fileInputStream.close();
                            dos.flush();
                            dos.close();

                            InputStream responseStream = new BufferedInputStream(conn.getInputStream());

                            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
                            String line = "";
                            StringBuilder stringBuilder = new StringBuilder();
                            while ((line = responseStreamReader.readLine()) != null) {
                                stringBuilder.append(line).append("\n");
                            }
                            responseStreamReader.close();

                            response = stringBuilder.toString();

                            if (response.contains("<MESSAGE>OK</MESSAGE>")) {
                                Log.d("ENVIANDO", "OK");
                                img.setSend(true);
                            } else {
                                Log.d("ENVIANDO", "NOK");
                            }
                        }


                    } catch (Exception e) {
                        Log.d("TAG", "Error: " + e.getMessage());
                        response = "ERROR";
                    }
                }
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(mContext);
            dialog.setTitle("Subiendo imagenes");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String ss) {
            if (dialog.isShowing())
                dialog.dismiss();
            itemsXML = "";
            complex = null;
            for (Modulo m : modulos) {
                    for (Item i : m.getItems()) {
                        if (i.getType().compareTo("COMPLEX") != 0) {
                            itemsXML += SoapRequestPreAsBuilt.AddItemToXML(i);
                            photosXML = SoapRequestPreAsBuilt.AddPhotosToXML(imagenes);
                        } else {
                            complex = i;
                        }
                    }

            }

            if (complex != null) {
                AlertDialog.Builder b = new AlertDialog.Builder(mContext);
                b.setTitle(complex.getName());
                final EditText cantidad = new EditText(mContext);
                cantidad.setBackgroundResource(R.drawable.fondo_edittext);
                cantidad.setInputType(InputType.TYPE_CLASS_NUMBER);
                b.setView(cantidad);
                b.setPositiveButton("Continuar", null);

                b.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                final AlertDialog d = b.create();
                d.show();
                d.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cantidad.getText().length() > 0) {
                            complex.setnAerial(Integer.parseInt(cantidad.getText().toString()));
                            itemsXML += SoapRequestPreAsBuilt.AddItemToXML(complex, imagenes);
                            Intent n = new Intent(mContext, MWAerials.class);
                            n.putExtra("ID", ID);
                            n.putExtra("QUERY", queryResp);
                            n.putExtra("ITEMS", itemsXML);
                            n.putExtra("PHOTOS", photosXML);
                            n.putExtra("CANTIDAD", Integer.parseInt(cantidad.getText().toString()));
                            saveData();
                            d.dismiss();
                            startActivityForResult(n, 100);
                        } else {
                            Toast.makeText(mContext, "Ingrese la cantidad de antenas.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
            super.onPostExecute(ss);
        }


    }

}
