package cl.tdc.felipe.tdc.webservice;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import java.security.KeyStore;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cl.tdc.felipe.tdc.adapters.ComponenteCantidad;
import cl.tdc.felipe.tdc.extras.Funciones;
import cl.tdc.felipe.tdc.objects.FormSubSystem;
import cl.tdc.felipe.tdc.objects.FormSubSystemItem;
import cl.tdc.felipe.tdc.objects.FormSubSystemItemAttribute;
import cl.tdc.felipe.tdc.objects.FormSubSystemItemAttributeValues;
import cl.tdc.felipe.tdc.objects.FormSystem;
import cl.tdc.felipe.tdc.objects.FormularioCheck;
import cl.tdc.felipe.tdc.objects.Seguimiento.Actividad;
import cl.tdc.felipe.tdc.objects.Seguimiento.Dia;

public class SoapRequestSeguimiento {


    public static String getProyects(String IMEI) throws Exception {
        String response = null;
        String xml = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fecha = new Date();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(dummy.URL_PROJECT_WORK);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.encodingStyle = SoapSerializationEnvelope.ENC;
        envelope.dotNet = false;
        envelope.implicitTypes = true;

        xml = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:Configurationwsdl\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                "<urn:request soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                "<Project xsi:type=\"urn:Project\">" +
                "<Request xsi:type=\"urn:Request\">" +
                "<!--Optional:-->" +
                "<Header xsi:type=\"urn:Header\">" +
                "<Date xsi:type=\"xsd:string\">" + formatter.format(fecha) + "</Date>" +
                "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                "<User xsi:type=\"xsd:string\">" + IMEI + "</User>" +
                "</Header>" +
                "<OperationType xsi:type=\"xsd:string\">GET</OperationType>" +
                "<Element xsi:type=\"xsd:string\">PROJECT</Element>" +
                "</Request>" +
                "</Project>" +
                "</urn:request>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        StringEntity se = new StringEntity(xml, HTTP.UTF_8);
        se.setContentType("text/xml");
        httpPost.addHeader("", dummy.URL_PROJECT_WORK);

        httpPost.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity resEntity = httpResponse.getEntity();
        response = EntityUtils.toString(resEntity);
        return response;
    }

    public static String getActividades(int ID) throws Exception {
        String response = null;
        String xml = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fecha = new Date();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(dummy.URL_PROJECT_ACTIVITIES);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.encodingStyle = SoapSerializationEnvelope.ENC;
        envelope.dotNet = false;
        envelope.implicitTypes = true;

        xml = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:Configurationwsdl\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                "<urn:request soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                "<FormPrev xsi:type=\"urn:FormPrev\">" +
                "<Request xsi:type=\"urn:Request\">" +
                "<!--Optional:-->" +
                "<Form_Detail xsi:type=\"urn:Form_Detail\">" +
                "<!--Zero or more repetitions:-->" +
                "<Parameters xsi:type=\"urn:Parameters\">" +
                "<!--Zero or more repetitions:-->" +
                "<Parameter xsi:type=\"urn:Parameter\">" +
                "<Name xsi:type=\"xsd:string\">EQUIPOID</Name>" +
                "<Value xsi:type=\"xsd:string\">1</Value>" +
                "</Parameter>" +
                "</Parameters>" +
                "</Form_Detail>" +
                "<!--Optional:-->" +
                "<Header xsi:type=\"urn:Header\">" +
                "<Date xsi:type=\"xsd:string\">" + formatter.format(fecha) + "</Date>" +
                "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                "</Header>" +
                "<!--Optional:-->" +
                "<Form_Header xsi:type=\"urn:Form_Header\">" +
                "<BuildingId xsi:type=\"xsd:string\">" + ID + "</BuildingId>" +
                "</Form_Header>" +
                "</Request>" +
                "</FormPrev>" +
                "</urn:request>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        StringEntity se = new StringEntity(xml, HTTP.UTF_8);
        se.setContentType("text/xml");
        httpPost.addHeader("", dummy.URL_PROJECT_ACTIVITIES);

        httpPost.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity resEntity = httpResponse.getEntity();
        response = EntityUtils.toString(resEntity);
        Log.d("GET ACTIVIDADES", response);
        return response;
    }

    public static String sendResponse(int ID, ArrayList<Dia> dias) throws Exception {
        String response = null;
        String xml = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fecha = new Date();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(dummy.URL_PROJECT_SEND);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.encodingStyle = SoapSerializationEnvelope.ENC;
        envelope.dotNet = false;
        envelope.implicitTypes = true;

        xml = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:Configurationwsdl\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                "<urn:request soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                "<FormPrev xsi:type=\"urn:FormPrev\">" +
                "<Request xsi:type=\"urn:Request\">" +
                "<Form_Detail xsi:type=\"urn:Form_Detail\">" +
                "<Parameters xsi:type=\"urn:Parameters\">" +
                "<Building xsi:type=\"urn:Building\">" +
                "<Days xsi:type=\"urn:Days\">";

        float tadvance = 0;
        for (int j = 0; j < dias.size(); j++) {
            Dia d = dias.get(j);
            float enviar = 0;
            float real = Float.parseFloat(d.getRealAdvance());
            float today = Float.parseFloat(d.getAdvanceToday());
            tadvance+= today;

            enviar = real + tadvance;
            String date = "-1";
            if(d.getFecha().getText().length() > 0)
                date = d.getFecha().getText().toString();

            xml += "<Day xsi:type=\"urn:Day\">" +
                    "<DayNumber xsi:type=\"xsd:string\">" + d.getDayNumber() + "</DayNumber>" +
                    "<ProgrammedAdvance xsi:type=\"xsd:string\">" + d.getProgrammedAdvance() + "</ProgrammedAdvance>" +
                    "<RealAdvance xsi:type=\"xsd:string\">" + enviar + "</RealAdvance>" +
                    "<Date xsi:type=\"xsd:string\">" + date + "</Date>" +
                    "<DescriptionDay xsi:type=\"xsd:string\">" + d.getObservacion().getText() + "</DescriptionDay>" +
                    "<Activities xsi:type=\"urn:Activities\">";

            for (int i = 0; i < d.getActividades().size(); i++) {
                Actividad a = d.getActividades().get(i);

                xml += "<Activity xsi:type=\"urn:Activity\">" +
                        "<IdActivity xsi:type=\"xsd:string\">" + a.getIdActivity() + "</IdActivity>" +
                        "<NameActivity xsi:type=\"xsd:string\">" + a.getNameActivity() + "</NameActivity>";
                if (d.getCheckBoxes().get(i).isChecked()) {
                    xml += "<ActivitySelected xsi:type=\"xsd:string\">1</ActivitySelected>";
                } else {
                    xml += "<ActivitySelected xsi:type=\"xsd:string\">0</ActivitySelected>";
                }
                xml += "</Activity>";
            }
            xml += "</Activities>" +
                    "</Day>";
        }
        xml += "</Days>" +
                "</Building>" +
                "</Parameters>" +
                "</Form_Detail>" +
                "<!--Optional:-->" +
                "<Header xsi:type=\"urn:Header\">" +
                "<Date xsi:type=\"xsd:string\">" + formatter.format(fecha) + "</Date>" +
                "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                "</Header>" +
                "<!--Optional:-->" +
                "<Form_Header xsi:type=\"urn:Form_Header\">" +
                "<BuildingId xsi:type=\"xsd:string\">" + ID + "</BuildingId>" +
                "</Form_Header>" +
                "</Request>" +
                "</FormPrev>" +
                "</urn:request>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        StringEntity se = new StringEntity(xml, HTTP.UTF_8);
        se.setContentType("text/xml");
        httpPost.addHeader("", dummy.URL_PROJECT_SEND);
        Log.w("ENVIANDO", xml);
        httpPost.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity resEntity = httpResponse.getEntity();
        response = EntityUtils.toString(resEntity);
        return response;
    }


}

