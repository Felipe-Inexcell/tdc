package cl.tdc.felipe.tdc.webservice;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cl.tdc.felipe.tdc.objects.ControSeguridadDiario.Elemento;
import cl.tdc.felipe.tdc.objects.ControSeguridadDiario.Modulo;
import cl.tdc.felipe.tdc.objects.ControSeguridadDiario.SubModulo;
import cl.tdc.felipe.tdc.objects.MaintChecklist.Section;


public class SoapRequestCheckLists {


    public static String getdailyActivities(String IMEI) throws Exception {
        String response = null;
        String xml = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fecha = new Date();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(dummy.URL_GET_DAILYACTIVITIES);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.encodingStyle = SoapSerializationEnvelope.ENC;
        envelope.dotNet = false;
        envelope.implicitTypes = true;

        xml = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:Configurationwsdl\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                "<urn:request soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                "<System xsi:type=\"urn:System\">" +
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
                "<Date xsi:type=\"xsd:string\">" + formatter.format(fecha) + "</Date>" +
                "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                "</Header>" +
                "<Form_Header xsi:type=\"urn:Form_Header\">" +
                "<Imei xsi:type=\"xsd:string\">" + IMEI + "</Imei>" +
                "</Form_Header>" +
                "</Request>" +
                "</System>" +
                "</urn:request>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        StringEntity se = new StringEntity(xml, HTTP.UTF_8);
        se.setContentType("text/xml");
        httpPost.addHeader("", dummy.URL_GET_DAILYACTIVITIES);

        httpPost.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity resEntity = httpResponse.getEntity();
        response = EntityUtils.toString(resEntity);
        return response;
    }

    public static String getMainChecklist(String ID) throws Exception {
        String response = null;
        String xml = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fecha = new Date();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(dummy.URL_MAIN_ACT_CHECKLIST);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.encodingStyle = SoapSerializationEnvelope.ENC;
        envelope.dotNet = false;
        envelope.implicitTypes = true;

        xml = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:Configurationwsdl\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                "<urn:request soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                "<System xsi:type=\"urn:System\">" +
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
                "<Date xsi:type=\"xsd:string\">" + formatter.format(fecha) + "</Date>" +
                "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                "</Header>" +
                "<Form_Header xsi:type=\"urn:Form_Header\">" +
                "<MaintenanceId xsi:type=\"xsd:string\">" + ID + "</MaintenanceId>" +
                "</Form_Header>" +
                "</Request>" +
                "</System>" +
                "</urn:request>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        StringEntity se = new StringEntity(xml, HTTP.UTF_8);
        se.setContentType("text/xml");
        httpPost.addHeader("", dummy.URL_MAIN_ACT_CHECKLIST);

        httpPost.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity resEntity = httpResponse.getEntity();
        response = EntityUtils.toString(resEntity);
        return response;
    }

    public static String sendDailyActivities(String IMEI, ArrayList<Modulo> form) throws Exception {
        String response = null;
        String xml = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fecha = new Date();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(dummy.URL_SEND_DAILYACTIVITIES);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.encodingStyle = SoapSerializationEnvelope.ENC;
        envelope.dotNet = false;
        envelope.implicitTypes = true;

        xml = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:Configurationwsdl\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                "<urn:request soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                "<System xsi:type=\"urn:System\">" +
                "<Request xsi:type=\"urn:Request\">" +
                "<Form_Detail xsi:type=\"urn:Form_Detail\">" +
                "<Parameters xsi:type=\"urn:Parameters\">" +
                "<Risk xsi:type=\"urn:Risk\">" +
                "<Modules xsi:type=\"urn:Modules\">";
        for (Modulo m : form) {
            xml += "<Module xsi:type=\"urn:Module\">" +
                    "<IdModule xsi:type=\"xsd:string\">" + m.getId() + "</IdModule>" +
                    "<NameModule xsi:type=\"xsd:string\">" + m.getName() + "</NameModule>" +
                    "<SubModules xsi:type=\"urn:SubModules\">";
            for (SubModulo sm : m.getSubModulos()) {
                xml += "<SubModule xsi:type=\"urn:SubModule\">" +
                        "<IdSubModule xsi:type=\"xsd:string\">" + sm.getId() + "</IdSubModule>" +
                        "<NameSubModule xsi:type=\"xsd:string\">" + sm.getName() + "</NameSubModule>" +
                        "<Activities xsi:type=\"urn:Activities\">";

                for (Elemento e : sm.getElementos()) {
                    xml += "<Activity xsi:type=\"urn:Activity\">" +
                            "<IdActivity xsi:type=\"xsd:string\">" + e.getId() + "</IdActivity>" +
                            "<NameActivity xsi:type=\"xsd:string\">" + e.getName() + "</NameActivity>" +
                            "<ValueActivity xsi:type=\"xsd:string\">" + e.getValue() + "</ValueActivity>" +
                            "</Activity>";
                }
                xml += "</Activities>" +
                        "</SubModule>";
            }
            xml += "</SubModules>" +
                    "</Module>";
        }
        xml += "</Modules>" +
                "</Risk>" +
                "</Parameters>" +
                "</Form_Detail>" +
                "<Header xsi:type=\"urn:Header\">" +
                "<Date xsi:type=\"xsd:string\">" + formatter.format(fecha) + "</Date>" +
                "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                "</Header>" +
                "<Form_Header xsi:type=\"urn:Form_Header\">" +
                "<Imei xsi:type=\"xsd:string\">" + IMEI + "</Imei>" +
                "</Form_Header>" +
                "</Request>" +
                "</System>" +
                "</urn:request>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        Log.i("SEND DAILY", xml);
        StringEntity se = new StringEntity(xml, HTTP.UTF_8);
        se.setContentType("text/xml");
        httpPost.addHeader("", dummy.URL_SEND_DAILYACTIVITIES);

        httpPost.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity resEntity = httpResponse.getEntity();
        response = EntityUtils.toString(resEntity);
        return response;
    }

    public static String sendMainChecklist(String ID, ArrayList<cl.tdc.felipe.tdc.objects.MaintChecklist.Modulo> form) throws Exception {
        String response = null;
        String xml = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fecha = new Date();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(dummy.URL_SEND_MAIN_ACT_CHECKLIST);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.encodingStyle = SoapSerializationEnvelope.ENC;
        envelope.dotNet = false;
        envelope.implicitTypes = true;

        xml = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:Configurationwsdl\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                "<urn:request soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
                "<System xsi:type=\"urn:System\">" +
                "<Request xsi:type=\"urn:Request\">" +
                "<Form_Detail xsi:type=\"urn:Form_Detail\">" +
                "<Parameters xsi:type=\"urn:Parameters\">" +
                "<Answers xsi:type=\"urn:Answers\">";
        for(cl.tdc.felipe.tdc.objects.MaintChecklist.Modulo m: form){
            for(cl.tdc.felipe.tdc.objects.MaintChecklist.SubModulo sm: m.getSubModulos()){
                for(Section s: sm.getSections()){
                    for(Elemento e: s.getElementos()){
                        xml += "<Answer xsi:type=\"urn:Answer\">" +
                                "<IdActivity xsi:type=\"xsd:string\">"+e.getId()+"</IdActivity>" +
                                "<AnswerActivity xsi:type=\"xsd:string\">"+e.getValue()+"</AnswerActivity>" +
                                "</Answer>";
                    }
                }
            }
        }

        xml += "</Answers>" +
                "</Parameters>" +
                "</Form_Detail>" +
                "<!--Optional:-->" +
                "<Header xsi:type=\"urn:Header\">" +
                "<Date xsi:type=\"xsd:string\">" + formatter.format(fecha) + "</Date>" +
                "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                "</Header>" +
                "<!--Optional:-->" +
                "<Form_Header xsi:type=\"urn:Form_Header\">" +
                "<MaintenanceId xsi:type=\"xsd:string\">" + ID + "</MaintenanceId>" +
                "</Form_Header>" +
                "</Request>" +
                "</System>" +
                "</urn:request>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        StringEntity se = new StringEntity(xml, HTTP.UTF_8);
        se.setContentType("text/xml");
        httpPost.addHeader("", dummy.URL_SEND_MAIN_ACT_CHECKLIST);

        httpPost.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity resEntity = httpResponse.getEntity();
        response = EntityUtils.toString(resEntity);
        return response;
    }


}

