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

import cl.tdc.felipe.tdc.objects.FormularioCierre.AREA;
import cl.tdc.felipe.tdc.objects.FormularioCierre.ITEM;
import cl.tdc.felipe.tdc.objects.FormularioCierre.PHOTO;
import cl.tdc.felipe.tdc.objects.FormularioCierre.QUESTION;
import cl.tdc.felipe.tdc.objects.FormularioCierre.SET;
import cl.tdc.felipe.tdc.objects.FormularioCierre.SYSTEM;

public class SoapRequestTDC {

    public static final String ACTION_IDEN = "checkiDen";
    public static final String ACTION_3G = "check3G";
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
                                            "<IdValue xsi:type=\"xsd:string\">" + I.getValues().get(posChecked).getNameValue() + "</IdValue>"+
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
                            RadioGroup rg = (RadioGroup) I.getView();
                            RadioButton btn = (RadioButton) rg.findViewById(rg.getCheckedRadioButtonId());
                            int position = rg.indexOfChild(btn) + 1;

                            xml += "<CountAnswerSet xsi:type=\"xsd:string\">" + position + "</CountAnswerSet>";
                            for (int i = 0;i<position;i++) {

                                    answerXML += "<SetAnswer xsi:type=\"urn:SetAnswer\">" +
                                            "<IdValue xsi:type=\"xsd:string\">" + I.getValues().get(i).getNameValue() + "</IdValue>"+
                                            "<SetAnswerQuestion xsi:type=\"urn:SetAnswerQuestion\">";

                                    ArrayList<SET> repeat = I.getSetlistArrayList().get(i);
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

        xml += "</Request3G>" +
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
                                          "<Date xsi:type=\"xsd:string\">"+formatter.format(fecha)+"</Date>" +
                                          "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                                          "<Imei xsi:type=\"xsd:string\">"+IMEI+"</Imei>" +
                                          "<Maintenance xsi:type=\"xsd:string\">"+ID+"</Maintenance>" +
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


}

