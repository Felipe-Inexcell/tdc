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
import cl.tdc.felipe.tdc.objects.FormImage;
import cl.tdc.felipe.tdc.objects.FormSubSystem;
import cl.tdc.felipe.tdc.objects.FormSubSystemItem;
import cl.tdc.felipe.tdc.objects.FormSubSystemItemAttribute;
import cl.tdc.felipe.tdc.objects.FormSubSystemItemAttributeValues;
import cl.tdc.felipe.tdc.objects.FormSystem;
import cl.tdc.felipe.tdc.objects.FormularioCheck;
import cl.tdc.felipe.tdc.objects.Relevar.Item;
import cl.tdc.felipe.tdc.objects.Relevar.Modulo;

public class SoapRequest1 {
    /*
     * Clase Principal de Conexion SSL a WDSL
	 */

    private static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }


    public static String getDepartament(String IMEI) throws Exception {
        final String SOAP_ACTION = "urn:Configurationwsdl#request";
        String URL = dummy.URL_R_DEPTO;
        String response = null;
        String xml = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fecha = new Date();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.encodingStyle = SoapSerializationEnvelope.ENC;
        envelope.dotNet = false;
        envelope.implicitTypes = true;

        String bodyOut =
                "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:Configurationwsdl\">" +
                        "<soapenv:Header/>" +
                        "<soapenv:Body>" +
                        "<urn:request soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                        "<Service xsi:type=\"urn:Service\">" +
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
                        "<Imei xsi:type=\"xsd:string\">" + IMEI + "</Imei>" +
                        "</Form_Header>" +
                        "</Request>" +
                        "</Service>" +
                        "</urn:request>" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";
        xml = bodyOut;
        StringEntity se = new StringEntity(xml, HTTP.UTF_8);
        se.setContentType("text/xml");
        httpPost.addHeader(SOAP_ACTION, URL);

        httpPost.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity resEntity = httpResponse.getEntity();
        response = EntityUtils.toString(resEntity);
        return response;
    }

    public static String getProvince(String IMEI, int ID) throws Exception {
        final String SOAP_ACTION = "urn:Configurationwsdl#request";
        String URL = dummy.URL_R_PROV;
        String response = null;
        String xml = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fecha = new Date();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.encodingStyle = SoapSerializationEnvelope.ENC;
        envelope.dotNet = false;
        envelope.implicitTypes = true;

        String bodyOut =
                "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:Configurationwsdl\">" +
                        "<soapenv:Header/>" +
                        "<soapenv:Body>" +
                        "<urn:request soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                        "<Service xsi:type=\"urn:Service\">" +
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
                        "<Imei xsi:type=\"xsd:string\">" + IMEI + "</Imei>" +
                        "<IdDepartment xsi:type=\"xsd:string\">" + ID + "</IdDepartment>" +
                        "</Form_Header>" +
                        "</Request>" +
                        "</Service>" +
                        "</urn:request>" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";
        xml = bodyOut;
        StringEntity se = new StringEntity(xml, HTTP.UTF_8);
        se.setContentType("text/xml");
        httpPost.addHeader(SOAP_ACTION, URL);

        httpPost.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity resEntity = httpResponse.getEntity();
        response = EntityUtils.toString(resEntity);
        return response;
    }

    public static String getDistrict(String IMEI, int ID) throws Exception {
        final String SOAP_ACTION = "urn:Configurationwsdl#request";
        String URL = dummy.URL_R_DISTRICT;
        String response = null;
        String xml = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fecha = new Date();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.encodingStyle = SoapSerializationEnvelope.ENC;
        envelope.dotNet = false;
        envelope.implicitTypes = true;

        String bodyOut =
                "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:Configurationwsdl\">" +
                        "<soapenv:Header/>" +
                        "<soapenv:Body>" +
                        "<urn:request soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                        "<Service xsi:type=\"urn:Service\">" +
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
                        "<Imei xsi:type=\"xsd:string\">" + IMEI + "</Imei>" +
                        "<IdProvince xsi:type=\"xsd:string\">" + ID + "</IdProvince>" +
                        "</Form_Header>" +
                        "</Request>" +
                        "</Service>" +
                        "</urn:request>" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";
        xml = bodyOut;
        StringEntity se = new StringEntity(xml, HTTP.UTF_8);
        se.setContentType("text/xml");
        httpPost.addHeader(SOAP_ACTION, URL);

        httpPost.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity resEntity = httpResponse.getEntity();
        response = EntityUtils.toString(resEntity);
        return response;
    }

    public static String getStation(String IMEI, int ID) throws Exception {
        final String SOAP_ACTION = "urn:Configurationwsdl#request";
        String URL = dummy.URL_R_STATION;
        String response = null;
        String xml = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fecha = new Date();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.encodingStyle = SoapSerializationEnvelope.ENC;
        envelope.dotNet = false;
        envelope.implicitTypes = true;

        String bodyOut =
                "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:Configurationwsdl\">" +
                        "<soapenv:Header/>" +
                        "<soapenv:Body>" +
                        "<urn:request soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                        "<Service xsi:type=\"urn:Service\">" +
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
                        "<Imei xsi:type=\"xsd:string\">" + IMEI + "</Imei>" +
                        "<IdDistrict xsi:type=\"xsd:string\">" + ID + "</IdDistrict>" +
                        "</Form_Header>" +
                        "</Request>" +
                        "</Service>" +
                        "</urn:request>" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";
        xml = bodyOut;
        StringEntity se = new StringEntity(xml, HTTP.UTF_8);
        se.setContentType("text/xml");
        httpPost.addHeader(SOAP_ACTION, URL);

        httpPost.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity resEntity = httpResponse.getEntity();
        response = EntityUtils.toString(resEntity);
        return response;
    }

    public static String getCheckList(String IMEI, int ID) throws Exception {
        final String SOAP_ACTION = "urn:Configurationwsdl#request";
        String URL = dummy.URL_R_CHECK;
        String response = null;
        String xml = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fecha = new Date();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.encodingStyle = SoapSerializationEnvelope.ENC;
        envelope.dotNet = false;
        envelope.implicitTypes = true;

        String bodyOut =
                "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:Configurationwsdl\">" +
                        "<soapenv:Header/>" +
                        "<soapenv:Body>" +
                        "<urn:request soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                        "<Service xsi:type=\"urn:Service\">" +
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
                        "<IdStation xsi:type=\"xsd:string\">" + ID + "</IdStation>" +
                        "<Imei xsi:type=\"xsd:string\">" + IMEI + "</Imei>" +
                        "</Form_Header>" +
                        "</Request>" +
                        "</Service>" +
                        "</urn:request>" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";
        xml = bodyOut;
        StringEntity se = new StringEntity(xml, HTTP.UTF_8);
        se.setContentType("text/xml");
        httpPost.addHeader(SOAP_ACTION, URL);

        httpPost.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity resEntity = httpResponse.getEntity();
        response = EntityUtils.toString(resEntity);
        return response;
    }

    public static String sendCheckList(String IMEI, int ID, ArrayList<Modulo> modulos, ArrayList<FormImage> images) throws Exception {
        final String SOAP_ACTION = "urn:Configurationwsdl#request";
        String URL = dummy.URL_R_SEND_CHECK;
        String response = null;
        String xml = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fecha = new Date();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.encodingStyle = SoapSerializationEnvelope.ENC;
        envelope.dotNet = false;
        envelope.implicitTypes = true;

        String bodyOut =
                "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:Configurationwsdl\">" +
                        "<soapenv:Header/>" +
                        "<soapenv:Body>" +
                        "<urn:request soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                        "<Service xsi:type=\"urn:Service\">" +
                        "<Request xsi:type=\"urn:Request\">" +
                        "<!--Optional:-->" +
                        "<Form_Detail xsi:type=\"urn:Form_Detail\">" +
                        "<!--Zero or more repetitions:-->" +
                        "<Checklist xsi:type=\"urn:Checklist\">" +
                        "<!--Zero or more repetitions:-->" +
                        "<Items xsi:type=\"urn:Items\">";
        for (Modulo m : modulos) {
            for (Item i : m.getItems()) {
                String com = "";
                if(i.getDescription() != null)
                    com = i.getDescription().getText().toString();
                bodyOut += "<Item xsi:type=\"urn:Item\">" +
                        "<NameItem xsi:type=\"xsd:string\">" + i.getName() + "</NameItem>" +
                        "<IdItem xsi:type=\"xsd:string\">" + i.getId() + "</IdItem>" +
                        "<Answer xsi:type=\"xsd:string\">" + i.getValor() + "</Answer>" +
                        "<CommentItem xsi:type=\"xsd:string\">" + com + "</CommentItem>" +
                        "</Item>";
            }
        }
        bodyOut += "</Items>" +
                "<Photos xsi:type=\"urn:Photos\">";
        for (FormImage img : images) {
            if(img.isSend()) {
                bodyOut += "<Photo xsi:type=\"urn:Photo\">" +
                        "<NamePhoto xsi:type=\"xsd:string\">" + img.getName() + "</NamePhoto>" +
                        "<DescriptionPhoto xsi:type=\"xsd:string\">" + img.getComment() + "</DescriptionPhoto>" +
                        "</Photo>";
            }
        }
        bodyOut += "</Photos>" +
                "</Checklist>" +
                "</Form_Detail>" +
                "<!--Optional:-->" +
                "<Header xsi:type=\"urn:Header\">" +
                "<Date xsi:type=\"xsd:string\">" + formatter.format(fecha) + "</Date>" +
                "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                "</Header>" +
                "<!--Optional:-->" +
                "<Form_Header xsi:type=\"urn:Form_Header\">" +
                "<IdStation xsi:type=\"xsd:string\">" + ID + "</IdStation>" +
                "<Imei xsi:type=\"xsd:string\">" + IMEI + "</Imei>" +
                "</Form_Header>" +
                "</Request>" +
                "</Service>" +
                "</urn:request>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";
        xml = bodyOut;
        StringEntity se = new StringEntity(xml, HTTP.UTF_8);
        se.setContentType("text/xml");
        httpPost.addHeader(SOAP_ACTION, URL);

        httpPost.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity resEntity = httpResponse.getEntity();
        response = EntityUtils.toString(resEntity);
        return response;
    }

    public static String getCheckListRecomendado(int ID) throws Exception {
        final String SOAP_ACTION = "urn:Configurationwsdl#request";
        String URL = dummy.URL_R_R_CHECK;
        String response;
        String xml ;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fecha = new Date();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.encodingStyle = SoapSerializationEnvelope.ENC;
        envelope.dotNet = false;
        envelope.implicitTypes = true;

        String bodyOut =
                "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:Configurationwsdl\">" +
                           "<soapenv:Header/>" +
                           "<soapenv:Body>" +
                              "<urn:request soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                                 "<Service xsi:type=\"urn:Service\">" +
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
                                          "<Date xsi:type=\"xsd:string\">"+formatter.format(fecha)+"</Date>" +
                                          "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                                       "</Header>" +
                                       "<!--Optional:-->" +
                                       "<Form_Header xsi:type=\"urn:Form_Header\">" +
                                          "<IdRelevamiento xsi:type=\"xsd:string\">"+ID+"</IdRelevamiento>" +
                                       "</Form_Header>" +
                                    "</Request>" +
                                 "</Service>" +
                              "</urn:request>" +
                           "</soapenv:Body>" +
                        "</soapenv:Envelope>";
        xml = bodyOut;
        StringEntity se = new StringEntity(xml, HTTP.UTF_8);
        se.setContentType("text/xml");
        httpPost.addHeader(SOAP_ACTION, URL);

        httpPost.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity resEntity = httpResponse.getEntity();
        response = EntityUtils.toString(resEntity);
        return response;
    }

    public static String sendCheckListRecomendado(int ID, ArrayList<Modulo> modulos, ArrayList<FormImage> images) throws Exception {
        final String SOAP_ACTION = "urn:Configurationwsdl#request";
        String URL = dummy.URL_R_R_SEND_CHECK;
        String response;
        String xml ;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fecha = new Date();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URL);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.encodingStyle = SoapSerializationEnvelope.ENC;
        envelope.dotNet = false;
        envelope.implicitTypes = true;

        String bodyOut =
                "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:Configurationwsdl\">" +
                           "<soapenv:Header/>" +
                           "<soapenv:Body>" +
                              "<urn:request soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                                 "<Service xsi:type=\"urn:Service\">" +
                                    "<Request xsi:type=\"urn:Request\">" +
                                       "<!--Optional:-->" +
                                       "<Form_Detail xsi:type=\"urn:Form_Detail\">" +
                                          "<!--Zero or more repetitions:-->" +
                                          "<Checklist xsi:type=\"urn:Checklist\">" +
                                             "<!--Zero or more repetitions:-->" +
                                             "<Items xsi:type=\"urn:Items\">";
                                                for(Modulo m: modulos){
                                                    for(Modulo s: m.getSubModulo()){
                                                        for(Item i: s.getItems()){
                                                            bodyOut +="<Item xsi:type=\"urn:Item\">" +
                                                                    "<NameItem xsi:type=\"xsd:string\">"+i.getName()+"</NameItem>" +
                                                                    "<IdItem xsi:type=\"xsd:string\">"+i.getId()+"</IdItem>" +
                                                                    "<Answer xsi:type=\"xsd:string\">"+i.getValor()+"</Answer>" +
                                                                    "<CommentItem xsi:type=\"xsd:string\">"+i.getDescription().getText().toString()+"</CommentItem>" +
                                                                    "</Item>";
                                                        }
                                                    }
                                                }

        bodyOut +="</Items>" +
                                             "<!--Zero or more repetitions:-->" +
                                             "<Photos xsi:type=\"urn:Photos\">";
                                                for(FormImage i: images) {
                                                    if(i.isSend()) {
                                                        bodyOut += "<Photo xsi:type=\"urn:Photo\">" +
                                                                "<NamePhoto xsi:type=\"xsd:string\">" + i.getName() + "</NamePhoto>" +
                                                                "<IdItem xsi:type=\"xsd:string\">" + i.getIdSystem() + "</IdItem>" +
                                                                "<DescriptionPhoto xsi:type=\"xsd:string\">" + i.getComment() + "</DescriptionPhoto>" +
                                                                "</Photo>";
                                                    }
                                                }
        bodyOut +="</Photos>" +
                                          "</Checklist>" +
                                       "</Form_Detail>" +
                                       "<!--Optional:-->" +
                                       "<Header xsi:type=\"urn:Header\">" +
                                          "<Date xsi:type=\"xsd:string\">"+formatter.format(fecha)+"</Date>" +
                                          "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                                       "</Header>" +
                                       "<!--Optional:-->" +
                                       "<Form_Header xsi:type=\"urn:Form_Header\">" +
                                          "<IdRelevamiento xsi:type=\"xsd:string\">"+ID+"</IdRelevamiento>" +
                                       "</Form_Header>" +
                                    "</Request>" +
                                 "</Service>" +
                              "</urn:request>" +
                           "</soapenv:Body>" +
                        "</soapenv:Envelope>";
        xml = bodyOut;
        StringEntity se = new StringEntity(xml, HTTP.UTF_8);
        se.setContentType("text/xml");
        httpPost.addHeader(SOAP_ACTION, URL);

        httpPost.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity resEntity = httpResponse.getEntity();
        response = EntityUtils.toString(resEntity);
        return response;
    }

}

