package cl.tdc.felipe.tdc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.xml.sax.SAXException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import cl.tdc.felipe.tdc.objects.PreAsBuilt.Informacion;
import cl.tdc.felipe.tdc.webservice.XMLParserPreAsBuilt;

public class PreAsBuiltActivity extends FragmentActivity implements GoogleMap.OnMapLoadedCallback {
    public static Activity activity;
    Context mContext;
    int TYPE;
    TextView tipo, nombre, departamento, provincia, distrito, direccion, comentario;
    public static final int ZOOM_LEVEL = 12;
    LatLng punto;
    private GoogleMap mapa;
    ProgressDialog mapDialog;
    Informacion info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preasbuilt);

        mContext = this;
        activity = this;

        tipo = (TextView) findViewById(R.id.type);
        nombre = (TextView) findViewById(R.id.nameStation);
        departamento = (TextView) findViewById(R.id.depto);
        provincia = (TextView) findViewById(R.id.province);
        distrito = (TextView) findViewById(R.id.district);
        direccion = (TextView) findViewById(R.id.address);
        comentario = (TextView) findViewById(R.id.comment);
        init();
    }

    private void init() {
        Intent data = getIntent();
        TYPE = data.getIntExtra("TYPE", -1);
        if (TYPE == XMLParserPreAsBuilt.RF)
            tipo.setText("RF");
        else
            tipo.setText("MW");

        try {
            info = XMLParserPreAsBuilt.getInfoPreAsBuilt(data.getStringExtra("QUERY"), TYPE);
            nombre.setText("ESTACION: " + info.getName().replace("\n", ""));
            departamento.setText("DEPARTAMENTO: " + info.getDepartament().replace("\n", ""));
            provincia.setText("PROVINCIA: " + info.getProvince().replace("\n", ""));
            distrito.setText("DISTRITO: " + info.getDistrict().replace("\n", ""));
            direccion.setText("DIRECCIÓN: " + info.getAddress().replace("\n", ""));


            punto = new LatLng(Double.parseDouble(info.getLatitude()), Double.parseDouble(info.getLongitude()));

            if (info.getComment().length() > 0)
                comentario.setText("OBSERVACIÓN: " + info.getComment());
            else comentario.setVisibility(View.INVISIBLE);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        mapDialog = new ProgressDialog(this);
        mapDialog.setMessage("Cargando Mapa...");
        mapDialog.show();

        mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        if (mapa != null) {
            mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setZoomControlsEnabled(true);
            mapa.getUiSettings().setCompassEnabled(true);
            mapa.getUiSettings().setMyLocationButtonEnabled(true);
            mapa.setOnMapLoadedCallback(this);


        } else {
            new AlertDialog.Builder(this).setMessage("Compruebe que tiene la aplicacion de GoogleMap y PlayStore").setTitle("No se pudo cargar el mapa").setNeutralButton("Cerrar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();
                }
            }).show();
            finish();
        }

    }

    @Override
    public void onMapLoaded() {
        if (punto != null) {
            mapa.addMarker(new MarkerOptions().position(punto).snippet("AQUI"));
            mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(punto, ZOOM_LEVEL));
            //mapa.addPolyline(new PolylineOptions().add(new LatLng(mapa.getMyLocation().getLatitude(), mapa.getMyLocation().getLongitude()),punto));
        }
        mapDialog.dismiss();
    }

    public void onClick_apagar(View v) {

        if (MainActivity.actividad != null)
            MainActivity.actividad.finish();
        finish();

    }


    public void onClick_back(View v) {
        finish();
    }

    public void onClick_continuar(View v) {
        Intent i;
        if(TYPE == XMLParserPreAsBuilt.RF){
            i = new Intent(this, FormPreAsBuiltActivityRF.class);
        }else{
            i = new Intent(this, FormPreAsBuiltActivityMW.class);
        }
        i.putExtra("ID", info.getId());
        startActivity(i);
    }
}
