package cl.tdc.felipe.tdc.webservice;

import android.util.Log;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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

import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cl.tdc.felipe.tdc.extras.Constantes;
import cl.tdc.felipe.tdc.objects.FormularioCierre.AREA;
import cl.tdc.felipe.tdc.objects.FormularioCierre.ITEM;
import cl.tdc.felipe.tdc.objects.FormularioCierre.PHOTO;
import cl.tdc.felipe.tdc.objects.FormularioCierre.QUESTION;
import cl.tdc.felipe.tdc.objects.FormularioCierre.SET;
import cl.tdc.felipe.tdc.objects.FormularioCierre.SYSTEM;

public class SoapRequestTDC {

    public static final String ACTION_IDEN = "checkiDen";
    public static final String ACTION_3G = "check3G";
    public static final String ACTION_AC = "checkAC";
    public static final String ACTION_DC = "checkDC";
    public static final String ACTION_SG = "checkSystemGround";
    public static final String ACTION_AIR = "checkAir";
    public static final String ACTION_FAENA = "checkFaena";
    public static final String ACTION_SEND_FAENA = "Faena";
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

    public static String getPlanningMaintenance(String IMEI) throws Exception {
        final String SOAP_ACTION = "urn:Configurationwsdl#planningMaintenance";
        String response = null;
        String xml = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fecha = new Date();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(dummy.URL_TDC);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.encodingStyle = SoapSerializationEnvelope.ENC;
        envelope.dotNet = false;
        envelope.implicitTypes = true;

        xml =
                "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:Configurationwsdl\">" +
                        "<soapenv:Header/>" +
                        "<soapenv:Body>" +
                        "<urn:planningMaintenance soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                        "<Planning xsi:type=\"urn:Planning\">" +
                        "<RequestPlan xsi:type=\"urn:RequestPlan\">" +
                        "<!--Optional:-->" +
                        "<HeaderPlan xsi:type=\"urn:HeaderPlan\">" +
                        "<Date xsi:type=\"xsd:string\">" + formatter.format(fecha) + "</Date>" +
                        "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                        "<User xsi:type=\"xsd:string\">" + IMEI + "</User>" +
                        "</HeaderPlan>" +
                        "</RequestPlan>" +
                        "</Planning>" +
                        "</urn:planningMaintenance>" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";

        StringEntity se = new StringEntity(xml, HTTP.UTF_8);
        se.setContentType("text/xml");
        httpPost.addHeader(SOAP_ACTION, dummy.URL_TDC);

        httpPost.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity resEntity = httpResponse.getEntity();
        response = EntityUtils.toString(resEntity);
        return response;
    }

    public static String getFormularioCierre(String IMEI, String ID_MAINTENANCE, String TYPE) throws IOException {
        final String SOAP_ACTION = "urn:Configurationwsdl#" + TYPE;
        String response = null;
        String xml = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fecha = new Date();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(dummy.URL_TDC);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.encodingStyle = SoapSerializationEnvelope.ENC;
        envelope.dotNet = false;
        envelope.implicitTypes = true;

        xml =
                "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:Configurationwsdl\">" +
                        "<soapenv:Header/>" +
                        "<soapenv:Body>" +
                        "<urn:" + TYPE + " soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                        "<Checklist xsi:type=\"urn:Checklist\">" +
                        "<Request xsi:type=\"urn:Request\">" +
                        "<Header xsi:type=\"urn:Header\">" +
                        "<Date xsi:type=\"xsd:string\">" + formatter.format(fecha) + "</Date>" +
                        "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                        "<Imei xsi:type=\"xsd:string\">" + IMEI + "</Imei>" +
                        "<Maintenance xsi:type=\"xsd:string\">" + ID_MAINTENANCE + "</Maintenance>" +
                        "</Header>" +
                        "<!--Optional:-->" +
                        "<Form_Header xsi:type=\"urn:Form_Header\">" +
                        "<MaintenanceId xsi:type=\"xsd:string\">" + ID_MAINTENANCE + "</MaintenanceId>" +
                        "</Form_Header>" +
                        "</Request>" +
                        "</Checklist>" +
                        "</urn:" + TYPE + ">" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";

        StringEntity se = new StringEntity(xml, HTTP.UTF_8);
        se.setContentType("text/xml");
        httpPost.addHeader(SOAP_ACTION, dummy.URL_TDC);

        httpPost.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity resEntity = httpResponse.getEntity();
        response = EntityUtils.toString(resEntity);
        return response;
    }

    public static String sendAnswerIDEN(String IMEI, String ID_MAINTENANCE, ArrayList<SYSTEM> SYSTEMS) throws IOException {
        final String SOAP_ACTION = "urn:Configurationwsdl#answerIden";
        String response = null;
        String xml = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fecha = new Date();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(dummy.URL_TDC);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.encodingStyle = SoapSerializationEnvelope.ENC;
        envelope.dotNet = false;
        envelope.implicitTypes = true;

        xml =
                "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:Configurationwsdl\">" +
                        "<soapenv:Header/>" +
                        "<soapenv:Body>" +
                        "<urn:answerIden soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                        "<RequestAnswerIden xsi:type=\"urn:RequestAnswerIden\">" +
                        "<RequestIden xsi:type=\"urn:RequestIden\">" +
                        "<Header xsi:type=\"urn:Header\">" +
                        "<Date xsi:type=\"xsd:string\">" + formatter.format(fecha) + "</Date>" +
                        "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                        "<Imei xsi:type=\"xsd:string\">" + IMEI + "</Imei>" +
                        "<Maintenance xsi:type=\"xsd:string\">" + ID_MAINTENANCE + "</Maintenance>" +
                        "</Header>" +
                        "<!--Optional:-->";
        for (SYSTEM S : SYSTEMS) {
            xml += "<SystemsRpta xsi:type=\"urn:SystemsRpta\">" +
                    "<IdSystems xsi:type=\"xsd:string\">" + S.getIdSystem() + "</IdSystems>";
            if (S.getAreas() != null) {
                xml += "<SetRptaItem xsi:type=\"urn:SetRptaItem\">";
                for (AREA A : S.getAreas()) {

                    for (ITEM I : A.getItems()) {
                        xml += "<RptaItem xsi:type=\"urn:RptaItem\">" +
                                "<IdItem xsi:type=\"xsd:string\">" + I.getIdItem() + "</IdItem>";
                        if (I.getQuestions() != null) {
                            xml += "<SetAnswerQuestion xsi:type=\"urn:SetAnswerQuestion\">";
                            for (QUESTION Q : I.getQuestions()) {
                                int countFoto = 0;
                                String xmlphotos = "";
                                if (Q.getFoto() != null) {
                                    countFoto += 1;
                                    PHOTO photo = Q.getFoto();
                                    File file = new File(photo.getNamePhoto());
                                    if (file.exists()) {
                                        xmlphotos += "<Photo xsi:type=\"urn:Photo\">" +
                                                "<NamePhoto xsi:type=\"xsd:string\">" + file.getName() + "</NamePhoto>" +
                                                "<TitlePhoto xsi:type=\"xsd:string\">" + photo.getTitlePhoto() + "</TitlePhoto>" +
                                                "<DateTime xsi:type=\"xsd:string\">" + photo.getDateTime() + "</DateTime>" +
                                                "<CoordX xsi:type=\"xsd:string\">" + photo.getCoordX() + "</CoordX>" +
                                                "<CoordY xsi:type=\"xsd:string\">" + photo.getCoordY() + "</CoordY>" +
                                                "</Photo>";
                                    }
                                }

                                if (Q.getFotos() != null) {
                                    for (PHOTO p : Q.getFotos()) {
                                        File file = new File(p.getNamePhoto());
                                        if (file.exists()) {
                                            xmlphotos += "<Photo xsi:type=\"urn:Photo\">" +
                                                    "<NamePhoto xsi:type=\"xsd:string\">" + file.getName() + "</NamePhoto>" +
                                                    "<TitlePhoto xsi:type=\"xsd:string\">" + p.getTitlePhoto() + "</TitlePhoto>" +
                                                    "<DateTime xsi:type=\"xsd:string\">" + p.getDateTime() + "</DateTime>" +
                                                    "<CoordX xsi:type=\"xsd:string\">" + p.getCoordX() + "</CoordX>" +
                                                    "<CoordY xsi:type=\"xsd:string\">" + p.getCoordY() + "</CoordY>" +
                                                    "</Photo>";
                                            countFoto += 1;
                                        }
                                    }
                                }


                                xml += "<AnswerQuestion xsi:type=\"urn:AnswerQuestion\">" +
                                        "<IdQuestion xsi:type=\"xsd:string\">" + Q.getIdQuestion() + "</IdQuestion>" +
                                        "<IdType xsi:type=\"xsd:string\">" + Q.getIdType() + "</IdType>" +
                                        "<IdAnswer xsi:type=\"xsd:string\">" + Q.getAswerIDEN() + "</IdAnswer>" +
                                        "<CountPhoto xsi:type=\"xsd:string\">" + countFoto + "</CountPhoto>";

                                xml += "<SetPhotos xsi:type=\"urn:SetPhotos\">" +
                                        xmlphotos +
                                        "</SetPhotos>";
                                xml += "</AnswerQuestion>";
                            }
                            xml += "</SetAnswerQuestion>";
                        }

                        if (I.getSetArrayList() != null && I.getValues() != null) {
                            xml += "<SetAnswerSet xsi:type=\"urn:SetAnswerSet\">";
                            String answerXML = "";
                            int count = 0;
                            for (CheckBox checkBox : I.getCheckBoxes()) {
                                if (checkBox.isChecked()) {
                                    count += 1;
                                    int posChecked = I.getCheckBoxes().indexOf(checkBox);
                                    answerXML += "<SetAnswer xsi:type=\"urn:SetAnswer\">" +
                                            "<IdValue xsi:type=\"xsd:string\">" + I.getValues().get(posChecked).getNameValue() + "</IdValue>" +
                                            "<SetAnswerQuestion xsi:type=\"urn:SetAnswerQuestion\">";

                                    ArrayList<SET> repeat = I.getSetlistArrayList().get(posChecked);
                                    for (SET set : repeat) {
                                        //answerXML += "<SetAnswerQuestion xsi:type=\"urn:SetAnswerQuestion\">";
                                        if (set.getQuestions() != null) {
                                            for (QUESTION Q : set.getQuestions()) {
                                                int countFoto = 0;
                                                String xmlphotos = "";
                                                if (Q.getFoto() != null) {
                                                    countFoto += 1;
                                                    PHOTO photo = Q.getFoto();
                                                    File file = new File(photo.getNamePhoto());
                                                    if (file.exists()) {
                                                        xmlphotos += "<Photo xsi:type=\"urn:Photo\">" +
                                                                "<NamePhoto xsi:type=\"xsd:string\">" + file.getName() + "</NamePhoto>" +
                                                                "<TitlePhoto xsi:type=\"xsd:string\">" + photo.getTitlePhoto() + "</TitlePhoto>" +
                                                                "<DateTime xsi:type=\"xsd:string\">" + photo.getDateTime() + "</DateTime>" +
                                                                "<CoordX xsi:type=\"xsd:string\">" + photo.getCoordX() + "</CoordX>" +
                                                                "<CoordY xsi:type=\"xsd:string\">" + photo.getCoordY() + "</CoordY>" +
                                                                "</Photo>";
                                                    }
                                                }
                                                if (Q.getFotos() != null) {
                                                    for (PHOTO p : Q.getFotos()) {
                                                        File file = new File(p.getNamePhoto());
                                                        if (file.exists()) {
                                                            xmlphotos += "<Photo xsi:type=\"urn:Photo\">" +
                                                                    "<NamePhoto xsi:type=\"xsd:string\">" + file.getName() + "</NamePhoto>" +
                                                                    "<TitlePhoto xsi:type=\"xsd:string\">" + p.getTitlePhoto() + "</TitlePhoto>" +
                                                                    "<DateTime xsi:type=\"xsd:string\">" + p.getDateTime() + "</DateTime>" +
                                                                    "<CoordX xsi:type=\"xsd:string\">" + p.getCoordX() + "</CoordX>" +
                                                                    "<CoordY xsi:type=\"xsd:string\">" + p.getCoordY() + "</CoordY>" +
                                                                    "</Photo>";
                                                            countFoto += 1;
                                                        }
                                                    }
                                                }
                                                answerXML += "<AnswerQuestion xsi:type=\"urn:AnswerQuestion\">" +
                                                        "<IdSet xsi:type=\"xsd:string\">" + set.getIdSet() + "</IdSet>" +
                                                        "<IdQuestion xsi:type=\"xsd:string\">" + Q.getIdQuestion() + "</IdQuestion>" +
                                                        "<IdAnswer xsi:type=\"xsd:string\">" + Q.getAswerIDEN() + "</IdAnswer>" +
                                                        "<IdType xsi:type=\"xsd:string\">" + Q.getIdType() + "</IdType>" +
                                                        "<CountPhoto xsi:type=\"xsd:string\">" + countFoto + "</CountPhoto>";


                                                answerXML += "<SetPhotos xsi:type=\"urn:SetPhotos\">" +
                                                        xmlphotos +
                                                        "</SetPhotos>";
                                                answerXML += "</AnswerQuestion>";
                                            }

                                        }

                                    }
                                    answerXML += "</SetAnswerQuestion>";
                                    answerXML += "</SetAnswer>";
                                }
                            }
                            xml += "<CountAnswerSet xsi:type=\"xsd:string\">" + count + "</CountAnswerSet>";
                            xml += answerXML;
                            xml += "</SetAnswerSet>";
                        }
                        xml += "</RptaItem>";
                    }


                }
                xml += "</SetRptaItem>";
            }
            xml += "</SystemsRpta>";
        }

        xml += "</RequestIden>" +
                "</RequestAnswerIden>" +
                "</urn:answerIden>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        StringEntity se = new StringEntity(xml, HTTP.UTF_8);
        se.setContentType("text/xml");
        httpPost.addHeader(SOAP_ACTION, dummy.URL_TDC);


        httpPost.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity resEntity = httpResponse.getEntity();
        response = EntityUtils.toString(resEntity);
        return response;
    }

    public static String sendAnswer3G(String IMEI, String ID_MAINTENANCE, ArrayList<SYSTEM> SYSTEMS) throws IOException {
        final String SOAP_ACTION = "urn:Configurationwsdl#answer3G";
        String response = null;
        String xml = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fecha = new Date();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(dummy.URL_TDC);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.encodingStyle = SoapSerializationEnvelope.ENC;
        envelope.dotNet = false;
        envelope.implicitTypes = true;

        xml =
                "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:Configurationwsdl\">" +
                        "<soapenv:Header/>" +
                        "<soapenv:Body>" +
                        "<urn:answer3G soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                        "<RequestAnswer3G xsi:type=\"urn:RequestAnswerIden\">" +
                        "<Request3G xsi:type=\"urn:Request3G\">" +
                        "<Header xsi:type=\"urn:Header\">" +
                        "<Date xsi:type=\"xsd:string\">" + formatter.format(fecha) + "</Date>" +
                        "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                        "<Imei xsi:type=\"xsd:string\">" + IMEI + "</Imei>" +
                        "<Maintenance xsi:type=\"xsd:string\">" + ID_MAINTENANCE + "</Maintenance>" +
                        "</Header>" +
                        "<!--Optional:-->";
        for (SYSTEM S : SYSTEMS) {
            xml += "<SystemsRpta xsi:type=\"urn:SystemsRpta\">" +
                    "<IdSystems xsi:type=\"xsd:string\">" + S.getIdSystem() + "</IdSystems>";
            if (S.getAreas() != null) {
                xml += "<SetRptaItem xsi:type=\"urn:SetRptaItem\">";
                for (AREA A : S.getAreas()) {

                    for (ITEM I : A.getItems()) {
                        xml += "<RptaItem xsi:type=\"urn:RptaItem\">" +
                                "<IdItem xsi:type=\"xsd:string\">" + I.getIdItem() + "</IdItem>";

                        String itemResponse = "";
                        String questionResponse = "";

                        if (I.getSetArrayList() == null && I.getValues() != null) {
                            itemResponse += "<AnswerQuestion xsi:type=\"urn:AnswerQuestion\">" +
                                    "<!--Optional:-->" +
                                    "<IdSet xsi:type=\"xsd:string\"></IdSet>" +
                                    "<IdQuestion xsi:type=\"xsd:string\"></IdQuestion>" +
                                    "<IdAnswer xsi:type=\"xsd:string\">" + I.getAnswer3G() + "</IdAnswer>" +
                                    "<CountPhoto xsi:type=\"xsd:string\">0</CountPhoto>" +
                                    "</AnswerQuestion>";
                        }

                        if (I.getQuestions() != null) {
                            //xml += "<SetAnswerQuestion xsi:type=\"urn:SetAnswerQuestion\">";
                            for (QUESTION Q : I.getQuestions()) {
                                int countFoto = 0;
                                String xmlphotos = "";
                                if (Q.getFoto() != null) {
                                    countFoto += 1;
                                    PHOTO photo = Q.getFoto();
                                    File file = new File(photo.getNamePhoto());
                                    if (file.exists()) {
                                        xmlphotos += "<Photo xsi:type=\"urn:Photo\">" +
                                                "<NamePhoto xsi:type=\"xsd:string\">" + file.getName() + "</NamePhoto>" +
                                                "<TitlePhoto xsi:type=\"xsd:string\">" + photo.getTitlePhoto() + "</TitlePhoto>" +
                                                "<DateTime xsi:type=\"xsd:string\">" + photo.getDateTime() + "</DateTime>" +
                                                "<CoordX xsi:type=\"xsd:string\">" + photo.getCoordX() + "</CoordX>" +
                                                "<CoordY xsi:type=\"xsd:string\">" + photo.getCoordY() + "</CoordY>" +
                                                "</Photo>";
                                    }
                                }

                                if (Q.getFotos() != null) {
                                    for (PHOTO p : Q.getFotos()) {
                                        File file = new File(p.getNamePhoto());
                                        if (file.exists()) {
                                            xmlphotos += "<Photo xsi:type=\"urn:Photo\">" +
                                                    "<NamePhoto xsi:type=\"xsd:string\">" + file.getName() + "</NamePhoto>" +
                                                    "<TitlePhoto xsi:type=\"xsd:string\">" + p.getTitlePhoto() + "</TitlePhoto>" +
                                                    "<DateTime xsi:type=\"xsd:string\">" + p.getDateTime() + "</DateTime>" +
                                                    "<CoordX xsi:type=\"xsd:string\">" + p.getCoordX() + "</CoordX>" +
                                                    "<CoordY xsi:type=\"xsd:string\">" + p.getCoordY() + "</CoordY>" +
                                                    "</Photo>";
                                            countFoto += 1;
                                        }
                                    }
                                }


                                questionResponse += "<AnswerQuestion xsi:type=\"urn:AnswerQuestion\">" +
                                        "<IdQuestion xsi:type=\"xsd:string\">" + Q.getIdQuestion() + "</IdQuestion>" +
                                        "<IdType xsi:type=\"xsd:string\">" + Q.getIdType() + "</IdType>" +
                                        "<IdAnswer xsi:type=\"xsd:string\">" + Q.getAswer3G() + "</IdAnswer>" +
                                        "<CountPhoto xsi:type=\"xsd:string\">" + countFoto + "</CountPhoto>" +
                                        "<SetPhotos xsi:type=\"urn:SetPhotos\">" +
                                        xmlphotos +
                                        "</SetPhotos>" +
                                        "</AnswerQuestion>";
                            }
                        }

                        if (!questionResponse.equals("") || !itemResponse.equals("")) {
                            xml += "<SetAnswerQuestion xsi:type=\"urn:SetAnswerQuestion\">" +
                                    itemResponse +
                                    questionResponse +
                                    "</SetAnswerQuestion>";
                        }


                        if (I.getSetArrayList() != null && I.getValues() != null) {

                            if (I.getIdType().equals(Constantes.RADIO)) {
                                xml += "<SetAnswerSet xsi:type=\"urn:SetAnswerSet\">";
                                String answerXML = "";

                                RadioGroup rg = (RadioGroup) I.getView();
                                int selected = rg.getCheckedRadioButtonId();

                                if (selected != -1) {
                                    RadioButton btn = (RadioButton) rg.findViewById(rg.getCheckedRadioButtonId());
                                    int position = rg.indexOfChild(btn) + 1;

                                    xml += "<CountAnswerSet xsi:type=\"xsd:string\">" + position + "</CountAnswerSet>";
                                    for (int i = 0; i < position; i++) {
                                        answerXML += "<SetAnswer xsi:type=\"urn:SetAnswer\">";
                                        answerXML += "<IdValue xsi:type=\"xsd:string\">" + I.getValues().get(i).getNameValue() + "</IdValue>";
                                        if (I.getSetArrayList() != null) {
                                            answerXML += "<SetAnswerQuestion xsi:type=\"urn:SetAnswerQuestion\">";

                                            ArrayList<SET> repeat = I.getSetlistArrayList().get(i);
                                            for (SET set : repeat) {
                                                if (set.getQuestions() != null) {
                                                    for (QUESTION Q : set.getQuestions()) {
                                                        int countFoto = 0;
                                                        String xmlphotos = "";
                                                        if (Q.getFoto() != null) {
                                                            countFoto += 1;
                                                            PHOTO photo = Q.getFoto();
                                                            File file = new File(photo.getNamePhoto());
                                                            if (file.exists()) {
                                                                xmlphotos += "<Photo xsi:type=\"urn:Photo\">" +
                                                                        "<NamePhoto xsi:type=\"xsd:string\">" + file.getName() + "</NamePhoto>" +
                                                                        "<TitlePhoto xsi:type=\"xsd:string\">" + photo.getTitlePhoto() + "</TitlePhoto>" +
                                                                        "<DateTime xsi:type=\"xsd:string\">" + photo.getDateTime() + "</DateTime>" +
                                                                        "<CoordX xsi:type=\"xsd:string\">" + photo.getCoordX() + "</CoordX>" +
                                                                        "<CoordY xsi:type=\"xsd:string\">" + photo.getCoordY() + "</CoordY>" +
                                                                        "</Photo>";
                                                            }
                                                        }
                                                        if (Q.getFotos() != null) {
                                                            for (PHOTO p : Q.getFotos()) {
                                                                File file = new File(p.getNamePhoto());
                                                                if (file.exists()) {
                                                                    xmlphotos += "<Photo xsi:type=\"urn:Photo\">" +
                                                                            "<NamePhoto xsi:type=\"xsd:string\">" + file.getName() + "</NamePhoto>" +
                                                                            "<TitlePhoto xsi:type=\"xsd:string\">" + p.getTitlePhoto() + "</TitlePhoto>" +
                                                                            "<DateTime xsi:type=\"xsd:string\">" + p.getDateTime() + "</DateTime>" +
                                                                            "<CoordX xsi:type=\"xsd:string\">" + p.getCoordX() + "</CoordX>" +
                                                                            "<CoordY xsi:type=\"xsd:string\">" + p.getCoordY() + "</CoordY>" +
                                                                            "</Photo>";
                                                                    countFoto += 1;
                                                                }
                                                            }
                                                        }
                                                        answerXML += "<AnswerQuestion xsi:type=\"urn:AnswerQuestion\">" +
                                                                "<IdSet xsi:type=\"xsd:string\">" + set.getIdSet() + "</IdSet>" +
                                                                "<IdQuestion xsi:type=\"xsd:string\">" + Q.getIdQuestion() + "</IdQuestion>" +
                                                                "<IdAnswer xsi:type=\"xsd:string\">" + Q.getAswer3G() + "</IdAnswer>" +
                                                                "<IdType xsi:type=\"xsd:string\">" + Q.getIdType() + "</IdType>" +
                                                                "<CountPhoto xsi:type=\"xsd:string\">" + countFoto + "</CountPhoto>";


                                                        answerXML += "<SetPhotos xsi:type=\"urn:SetPhotos\">" +
                                                                xmlphotos +
                                                                "</SetPhotos>";
                                                        answerXML += "</AnswerQuestion>";
                                                    }

                                                }

                                            }
                                        }
                                        answerXML += "</SetAnswerQuestion>";
                                        answerXML += "</SetAnswer>";

                                    }

                                }
                                xml += answerXML;
                                xml += "</SetAnswerSet>";
                            }

                        }
                        xml += "</RptaItem>";
                    }


                }
                xml += "</SetRptaItem>";
            }
            xml += "</SystemsRpta>";
        }

        xml += "</Request3G>" +
                "</RequestAnswer3G>" +
                "</urn:answer3G>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        StringEntity se = new StringEntity(xml, HTTP.UTF_8);
        se.setContentType("text/xml");
        httpPost.addHeader(SOAP_ACTION, dummy.URL_TDC);


        httpPost.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity resEntity = httpResponse.getEntity();
        response = EntityUtils.toString(resEntity);
        Log.d("RESPONSE", response);
        return response;
    }

    public static String sendAnswer(String IMEI, String ID_MAINTENANCE, ArrayList<SYSTEM> SYSTEMS, String ACTION) throws IOException {
        final String SOAP_ACTION = "urn:Configurationwsdl#answer" + ACTION;
        String response = null;
        String xml = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fecha = new Date();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(dummy.URL_TDC);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.encodingStyle = SoapSerializationEnvelope.ENC;
        envelope.dotNet = false;
        envelope.implicitTypes = true;

        xml = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:Configurationwsdl\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                "<urn:answer" + ACTION + " soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                "<RequestAnswer" + ACTION + " xsi:type=\"urn:RequestAnswer" + ACTION + "\">" +
                "<Request" + ACTION + " xsi:type=\"urn:Request" + ACTION + "\">" +
                "<Header xsi:type=\"urn:Header\">" +
                "<Date xsi:type=\"xsd:string\">" + formatter.format(fecha) + "</Date>" +
                "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                "<Imei xsi:type=\"xsd:string\">" + IMEI + "</Imei>" +
                "<Maintenance xsi:type=\"xsd:string\">" + ID_MAINTENANCE + "</Maintenance>" +
                "</Header>";

        for (SYSTEM S : SYSTEMS) {
            xml += "<SystemsRptaUni xsi:type=\"urn:SystemsRptaUni\">";
            for (AREA A : S.getAreas()) {
                String idArea = A.getIdArea();
                for (ITEM I : A.getItems()) {
                    String idItem = I.getIdItem();

                    String lat = "", lon = "", title = "";
                    if (I.getPhoto() != null) {
                        PHOTO f = I.getPhoto();
                        lat = f.getCoordX();
                        lon = f.getCoordY();
                        title = f.getTitlePhoto();
                    }
                    String aAux = "";
                    if(I.getSetlistArrayList() != null){
                        aAux = I.getAnswerFaena();
                    }

                    xml += "<SetRptaItemUni xsi:type=\"urn:SetRptaItemUni\">" +
                            "<IdArea xsi:type=\"xsd:string\">" + idArea + "</IdArea>" +
                            "<IdItem xsi:type=\"xsd:string\">" + idItem + "</IdItem>" +
                            "<IdSet xsi:type=\"xsd:string\"></IdSet>" +
                            "<IdQuestion xsi:type=\"xsd:string\"></IdQuestion>" +
                            "<Answer xsi:type=\"xsd:string\">" + I.getAnswerFaena() + "</Answer>" +
                            "<AnswerAux xsi:type=\"xsd:string\">"+aAux+"</AnswerAux>" +
                            "<Lat xsi:type=\"xsd:string\">" + lat + "</Lat>" +
                            "<Long xsi:type=\"xsd:string\">" + lon + "</Long>" +
                            "<Title xsi:type=\"xsd:string\">" + title + "</Title>" +
                            "</SetRptaItemUni>";

                    if (I.getQuestions() != null) {
                        for (int i = 0; i < I.getQuestions().size(); i++) {
                            QUESTION Q = I.getQuestions().get(i);

                            xml += "<SetRptaItemUni xsi:type=\"urn:SetRptaItemUni\">" +
                                    "<IdArea xsi:type=\"xsd:string\">" + idArea + "</IdArea>" +
                                    "<IdItem xsi:type=\"xsd:string\">" + idItem + "</IdItem>" +
                                    "<IdSet xsi:type=\"xsd:string\"></IdSet>" +
                                    "<IdQuestion xsi:type=\"xsd:string\">" + Q.getIdQuestion() + "</IdQuestion>" +
                                    "<Answer xsi:type=\"xsd:string\">" + Q.getAswer3G() + "</Answer>" +
                                    "<AnswerAux xsi:type=\"xsd:string\"></AnswerAux>" +
                                    "<Lat xsi:type=\"xsd:string\"></Lat>" +
                                    "<Long xsi:type=\"xsd:string\"></Long>" +
                                    "<Title xsi:type=\"xsd:string\"></Title>" +
                                    "</SetRptaItemUni>";
                        }
                    }
                    if (I.getSetlistArrayList() != null) {
                        String ans = I.getAnswerFaena();
                        if(!ans.equals("")) {
                            int n = Integer.parseInt(ans);
                            for (int i = 0; i < n; i++) {
                                ArrayList<SET> setList = I.getSetlistArrayList().get(i);
                                for (SET set : setList) {
                                    if (set.getQuestions() != null) {
                                        for (QUESTION Q : set.getQuestions()) {
                                            xml += "<SetRptaItemUni xsi:type=\"urn:SetRptaItemUni\">" +
                                                    "<IdArea xsi:type=\"xsd:string\">" + idArea + "</IdArea>" +
                                                    "<IdItem xsi:type=\"xsd:string\">" + idItem + "</IdItem>" +
                                                    "<IdSet xsi:type=\"xsd:string\">" + set.getIdSet() + "</IdSet>" +
                                                    "<IdQuestion xsi:type=\"xsd:string\">" + Q.getIdQuestion() + "</IdQuestion>" +
                                                    "<Answer xsi:type=\"xsd:string\">" + Q.getAswer3G() + "</Answer>" +
                                                    "<AnswerAux xsi:type=\"xsd:string\">" + (i + 1) + "</AnswerAux>" +
                                                    "<Lat xsi:type=\"xsd:string\"></Lat>" +
                                                    "<Long xsi:type=\"xsd:string\"></Long>" +
                                                    "<Title xsi:type=\"xsd:string\"></Title>" +
                                                    "</SetRptaItemUni>";
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            xml += "</SystemsRptaUni>";
        }
        xml += "</Request" + ACTION + ">" +
                "</RequestAnswer" + ACTION + ">" +
                "</urn:answer" + ACTION + ">" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        StringEntity se = new StringEntity(xml, HTTP.UTF_8);
        se.setContentType("text/xml");
        httpPost.addHeader(SOAP_ACTION, dummy.URL_TDC);


        httpPost.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity resEntity = httpResponse.getEntity();
        response = EntityUtils.toString(resEntity);
        Log.d("RESPONSE", response);
        return response;
    }


    public static String cerrarMantenimiento(String IMEI, String ID) throws Exception {
        final String SOAP_ACTION = "urn:Configurationwsdl#closeTicket";
        String response = null;
        String xml = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fecha = new Date();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(dummy.URL_TDC);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.encodingStyle = SoapSerializationEnvelope.ENC;
        envelope.dotNet = false;
        envelope.implicitTypes = true;

        xml =
                "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:Configurationwsdl\">" +
                        "<soapenv:Header/>" +
                        "<soapenv:Body>" +
                        "<urn:closeTicket soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                        "<Close xsi:type=\"urn:Close\">" +
                        "<Request xsi:type=\"urn:Request\">" +
                        "<Header xsi:type=\"urn:Header\">" +
                        "<Date xsi:type=\"xsd:string\">" + formatter.format(fecha) + "</Date>" +
                        "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                        "<Imei xsi:type=\"xsd:string\">" + IMEI + "</Imei>" +
                        "<Maintenance xsi:type=\"xsd:string\">" + ID + "</Maintenance>" +
                        "</Header>" +
                        "</Request>" +
                        "</Close>" +
                        "</urn:closeTicket>" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";

        StringEntity se = new StringEntity(xml, HTTP.UTF_8);
        se.setContentType("text/xml");
        httpPost.addHeader(SOAP_ACTION, dummy.URL_TDC);

        httpPost.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity resEntity = httpResponse.getEntity();
        response = EntityUtils.toString(resEntity);
        Log.d("RESPONSE", response);
        return response;
    }

    public static String updateApk(String IMEI) throws Exception {
        final String SOAP_ACTION = "urn:Configurationwsdl#updateApk";
        String response = null;
        String xml = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fecha = new Date();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(dummy.URL_TDC);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.encodingStyle = SoapSerializationEnvelope.ENC;
        envelope.dotNet = false;
        envelope.implicitTypes = true;

        xml = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:Configurationwsdl\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                "<urn:updateApk soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                "<Apk xsi:type=\"urn:Apk\">" +
                "<RequestApk xsi:type=\"urn:Request\">" +
                "<Header xsi:type=\"urn:Header\">" +
                "<Date xsi:type=\"xsd:string\">"+formatter.format(fecha)+"</Date>" +
                "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                "<Imei xsi:type=\"xsd:string\">"+IMEI+"</Imei>" +
                "<Maintenance xsi:type=\"xsd:string\">?</Maintenance>" +
                "</Header>" +
                "</RequestApk>" +
                "</Apk>" +
                "</urn:updateApk>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        StringEntity se = new StringEntity(xml, HTTP.UTF_8);
        se.setContentType("text/xml");
        httpPost.addHeader(SOAP_ACTION, dummy.URL_TDC);

        httpPost.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity resEntity = httpResponse.getEntity();
        response = EntityUtils.toString(resEntity);
        Log.d("RESPONSE", response);
        return response;
    }


}

