package cl.tdc.felipe.tdc.webservice;

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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cl.tdc.felipe.tdc.objects.FormImage;
import cl.tdc.felipe.tdc.objects.Relevar.Item;
import cl.tdc.felipe.tdc.objects.Relevar.Modulo;

public class SoapRequestPreAsBuilt {
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


    public static String getNodob(String IMEI) throws IOException {
        final String SOAP_ACTION = "urn:Configurationwsdl#request";
        String URL = dummy.URL_RF;
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
                                       "<Form_Detail xsi:type=\"urn:Form_Detail\">" +
                                          "<Parameters xsi:type=\"urn:Parameters\">" +
                                             "<Parameter xsi:type=\"urn:Parameter\">" +
                                                "<Name xsi:type=\"xsd:string\">EQUIPOID</Name>" +
                                                "<Value xsi:type=\"xsd:string\">1</Value>" +
                                             "</Parameter>" +
                                          "</Parameters>" +
                                       "</Form_Detail>" +
                                       "<Header xsi:type=\"urn:Header\">" +
                                          "<Date xsi:type=\"xsd:string\">"+formatter.format(fecha)+"</Date>" +
                                          "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                                       "</Header>" +
                                       "<Form_Header xsi:type=\"urn:Form_Header\">" +
                                          "<Imei xsi:type=\"xsd:string\">"+IMEI+"</Imei>" +
                                       "</Form_Header>" +
                                    "</Request>" +
                                 "</Service>" +
                              "</urn:request>" +
                           "</soapenv:Body>"+
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

    public static String getNodoMW(String IMEI)  throws IOException {
        final String SOAP_ACTION = "urn:Configurationwsdl#request";
        String URL = dummy.URL_MW;
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
                                          "<Date xsi:type=\"xsd:string\">"+formatter.format(fecha)+"</Date>" +
                                          "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                                       "</Header>" +
                                       "<!--Optional:-->" +
                                       "<Form_Header xsi:type=\"urn:Form_Header\">" +
                                          "<Imei xsi:type=\"xsd:string\">"+IMEI+"</Imei>" +
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

    public static String getCheckRF(int ID) throws Exception {
        final String SOAP_ACTION = "urn:Configurationwsdl#request";
        String URL = dummy.URL_RF_CHECK;
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
                                                "<Name xsi:type=\"xsd:string\">?</Name>" +
                                                "<Value xsi:type=\"xsd:string\">?</Value>" +
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
                                          "<Id_NodoB xsi:type=\"xsd:string\">"+ID+"</Id_NodoB>" +
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

    public static String getCheckMW(int ID) throws Exception {
        final String SOAP_ACTION = "urn:Configurationwsdl#request";
        String URL = dummy.URL_MW_CHECK;
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
                                          "<Date xsi:type=\"xsd:string\">"+formatter.format(fecha)+"</Date>" +
                                          "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                                       "</Header>" +
                                       "<!--Optional:-->" +
                                       "<Form_Header xsi:type=\"urn:Form_Header\">" +
                                          "<IdMW xsi:type=\"xsd:string\">"+ID+"</IdMW>" +
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

    public static String sendCheckRF(int ID, String itemsXML, String aerialsXML) throws Exception {
        final String SOAP_ACTION = "urn:Configurationwsdl#request";
        String URL = dummy.URL_RF_SEND;
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
                                             "<CommentChecklist xsi:type=\"xsd:string\">?</CommentChecklist>" +
                                             "<!--Zero or more repetitions:-->" +
                                             "<Items xsi:type=\"urn:Items\">"+
                                            itemsXML+
                                            "<Aerial xsi:type=\"urn:Aerial\">" +
                                                   aerialsXML+
                                                "</Aerial>"+

                                            "</Items>" +
                                          "</Checklist>" +
                                       "</Form_Detail>" +
                                       "<!--Optional:-->" +
                                       "<Header xsi:type=\"urn:Header\">" +
                                          "<Date xsi:type=\"xsd:string\">"+formatter.format(fecha)+"</Date>" +
                                          "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                                       "</Header>" +
                                       "<!--Optional:-->" +
                                       "<Form_Header xsi:type=\"urn:Form_Header\">" +
                                          "<Id_NodoB xsi:type=\"xsd:string\">"+ID+"</Id_NodoB>" +
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

    public static String sendCheckMW(int ID, String items, String aerials, String photos) throws Exception {
        final String SOAP_ACTION = "urn:Configurationwsdl#request";
        String URL = dummy.URL_MW_SEND;
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
                                       "<Form_Detail xsi:type=\"urn:Form_Detail\">" +
                                          "<Checklist xsi:type=\"urn:Checklist\">" +
                                             "<Items xsi:type=\"urn:Items\">" +
                                               items+
                                                "<!--Zero or more repetitions:-->" +
                                                "<Aerial xsi:type=\"urn:Aerial\">" +
                                                   "<!--Zero or more repetitions:-->" +
                                                   aerials+
                                                "</Aerial>" +
                                             "</Items>" +
                                             "<Photos xsi:type=\"urn:Photos\">" +
                                                photos+
                                             " </Photos>"+
                                          "</Checklist>" +
                                       "</Form_Detail>" +
                                       "<!--Optional:-->" +
                                       "<Header xsi:type=\"urn:Header\">" +
                                          "<Date xsi:type=\"xsd:string\">"+formatter.format(fecha)+"</Date>" +
                                          "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                                       "</Header>" +
                                       "<!--Optional:-->" +
                                       "<Form_Header xsi:type=\"urn:Form_Header\">" +
                                          "<IdMW xsi:type=\"xsd:string\">"+ID+"</IdMW>" +
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

    public static String generateItemsXML(ArrayList<Modulo> modulos, ArrayList<FormImage> imagenes) {
        String bodyOut = "";
        for(Modulo m: modulos){
            for(Modulo sm: m.getSubModulo()){
                for(Item i: sm.getItems()){
                    bodyOut+="<Item xsi:type=\"urn:Item\">" +
                            "<NameItem xsi:type=\"xsd:string\">"+i.getName()+"</NameItem>" +
                            "<IdItem xsi:type=\"xsd:string\">"+i.getId()+"</IdItem>" +
                            "<Answer xsi:type=\"xsd:string\">"+i.getValor()+"</Answer>" +
                            "<CommentItem xsi:type=\"xsd:string\">"+i.getDescription().getText().toString()+"</CommentItem>";

                    String imgs = "<Photo xsi:type=\"xsd:string\">?</Photo>";
                    for(FormImage img: imagenes){
                        if(img.getIdSystem() == i.getId()){
                            imgs="<Photo xsi:type=\"xsd:string\">"+img.getName()+"</Photo>";
                            break;
                        }
                    }
                    bodyOut+=imgs+"</Item>";


                }
            }
        }
        return  bodyOut;
    }

    public static String AddItemToXML(Item i, ArrayList<FormImage> imagenes) {
        String bodyOut="<Item xsi:type=\"urn:Item\">" +
                "<NameItem xsi:type=\"xsd:string\">"+i.getName()+"</NameItem>" +
                "<IdItem xsi:type=\"xsd:string\">"+i.getId()+"</IdItem>" +
                "<Answer xsi:type=\"xsd:string\">"+i.getValor()+"</Answer>" +
                "<CommentItem xsi:type=\"xsd:string\"></CommentItem>";

        String imgs = "<Photo xsi:type=\"xsd:string\"></Photo>";
        for(FormImage img: imagenes){
            if(img.getIdSystem() == i.getId() && img.isSend()){
                imgs="<Photo xsi:type=\"xsd:string\">"+img.getName()+"</Photo>";
                break;
            }
        }
        bodyOut+=imgs+"</Item>";
        return bodyOut;
    }

    public static String AddItemToXML(Item i) {
        String bodyOut="<Item xsi:type=\"urn:Item\">" +
                "<NameItem xsi:type=\"xsd:string\">"+i.getName()+"</NameItem>" +
                "<IdItem xsi:type=\"xsd:string\">"+i.getId()+"</IdItem>" +
                "<Answer xsi:type=\"xsd:string\">"+i.getValor()+"</Answer>" +
                "<CommentItem xsi:type=\"xsd:string\"></CommentItem>";
        bodyOut+="</Item>";
        return bodyOut;
    }

    public static String AddAerialToXML(Item i, ArrayList<FormImage> imagenes) {
        String xml = "<ItemAerial xsi:type=\"urn:ItemAerial\">" +
                "<NameItemAerial xsi:type=\"xsd:string\">"+i.getName()+"</NameItemAerial>" +
                "<IdItemAerial xsi:type=\"xsd:string\">"+i.getId()+"</IdItemAerial>" +
                "<NumAerial xsi:type=\"xsd:string\">"+i.getnAerial()+"</NumAerial>" +
                "<AnswerAerial xsi:type=\"xsd:string\">"+i.getValor()+"</AnswerAerial>" +
                "<CommentItemAerial xsi:type=\"xsd:string\">"+i.getDescription().getText().toString()+"</CommentItemAerial>";


        String imgs = "<PhotoAerial xsi:type=\"xsd:string\"></PhotoAerial>";
        for(FormImage img: imagenes){
            if(img.getIdSystem() == i.getId() && img.getIdSubSystem() == i.getnAerial() && img.isSend()){
                imgs="<PhotoAerial xsi:type=\"xsd:string\">"+img.getName()+"</PhotoAerial>";
                break;
            }
        }
        xml+=imgs+"</ItemAerial>";
        return xml;
    }

    public static String AddAerialToXML(Item i) {
        String xml = "<ItemAerial xsi:type=\"urn:ItemAerial\">" +
                "<NameItemAerial xsi:type=\"xsd:string\">"+i.getName()+"</NameItemAerial>" +
                "<IdItemAerial xsi:type=\"xsd:string\">"+i.getId()+"</IdItemAerial>" +
                "<NumAerial xsi:type=\"xsd:string\">"+i.getnAerial()+"</NumAerial>" +
                "<AnswerAerial xsi:type=\"xsd:string\">"+i.getValor()+"</AnswerAerial>" +
                "<CommentItemAerial xsi:type=\"xsd:string\">"+i.getDescription().getText().toString()+"</CommentItemAerial>";

        xml+="</ItemAerial>";
        return xml;
    }

    public static String AddPhotosToXML(ArrayList<FormImage> imagenes){
        String xml = "";
        for(FormImage img: imagenes){
            if(img.isSend()) {
                xml += "<Photo xsi:type=\"urn:Photo\">" +
                        "<NamePhoto xsi:type=\"xsd:string\">" + img.getName() + "</NamePhoto>" +
                        "<TypePhoto xsi:type=\"xsd:string\">" + img.getType() + "</TypePhoto>" +
                        "<DescriptionPhoto xsi:type=\"xsd:string\">" + img.getDescription() + "</DescriptionPhoto>" +
                        "<CommentPhoto xsi:type=\"xsd:string\">" + img.getComment() + "</CommentPhoto>" +
                        "</Photo>";
            }
        }
        return xml;
    }
}

