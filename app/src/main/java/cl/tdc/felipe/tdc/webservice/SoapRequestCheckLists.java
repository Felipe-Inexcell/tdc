package cl.tdc.felipe.tdc.webservice;

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
import java.util.Date;


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
                "<Date xsi:type=\"xsd:string\">"+formatter.format(fecha)+"</Date>" +
                "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                "</Header>" +
                "<Form_Header xsi:type=\"urn:Form_Header\">" +
                "<Imei xsi:type=\"xsd:string\">"+IMEI+"</Imei>" +
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
    
    public static String getMainChecklist(int ID) throws Exception {
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
                "<Date xsi:type=\"xsd:string\">"+formatter.format(fecha)+"</Date>" +
                "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                "</Header>" +
                "<Form_Header xsi:type=\"urn:Form_Header\">" +
                "<MaintenanceId xsi:type=\"xsd:string\">"+ID+"</MaintenanceId>" +
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

    public static String sendDailyActivities(String IMEI) throws Exception {
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
                "<Modules xsi:type=\"urn:Modules\">" +
                "<Module xsi:type=\"urn:Module\">" +
                "<IdModule xsi:type=\"xsd:string\">?</IdModule>" +
                "<NameModule xsi:type=\"xsd:string\">?</NameModule>" +
                "<SubModules xsi:type=\"urn:SubModules\">" +
                "<SubModule xsi:type=\"urn:SubModule\">" +
                "<IdSubModule xsi:type=\"xsd:string\">?</IdSubModule>" +
                "<NameSubModule xsi:type=\"xsd:string\">?</NameSubModule>" +
                "<Activities xsi:type=\"urn:Activities\">" +
                "<Activity xsi:type=\"urn:Activity\">" +
                "<IdActivity xsi:type=\"xsd:string\">?</IdActivity>" +
                "<NameActivity xsi:type=\"xsd:string\">?</NameActivity>" +
                "<ValueActivity xsi:type=\"xsd:string\">?</ValueActivity>" +
                "</Activity>" +
                "</Activities>" +
                "</SubModule>" +
                "</SubModules>" +
                "</Module>" +
                "</Modules>" +
                "</Risk>" +
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
                "</System>" +
                "</urn:request>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";

        StringEntity se = new StringEntity(xml, HTTP.UTF_8);
        se.setContentType("text/xml");
        httpPost.addHeader("", dummy.URL_SEND_DAILYACTIVITIES);

        httpPost.setEntity(se);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity resEntity = httpResponse.getEntity();
        response = EntityUtils.toString(resEntity);
        return response;
    }

    public static String sendMainChecklist(int ID) throws Exception {
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
                "<Answers xsi:type=\"urn:Answers\">" +
                "<Answer xsi:type=\"urn:Answer\">" +
                "<IdActivity xsi:type=\"xsd:string\">?</IdActivity>" +
                "<AnswerActivity xsi:type=\"xsd:string\">?</AnswerActivity>" +
                "</Answer>" +
                "</Answers>" +
                "</Parameters>" +
                "</Form_Detail>" +
                "<!--Optional:-->" +
                "<Header xsi:type=\"urn:Header\">" +
                "<Date xsi:type=\"xsd:string\">"+formatter.format(fecha)+"</Date>" +
                "<Platafform xsi:type=\"xsd:string\">MOBILE</Platafform>" +
                "</Header>" +
                "<!--Optional:-->" +
                "<Form_Header xsi:type=\"urn:Form_Header\">" +
                "<MaintenanceId xsi:type=\"xsd:string\">"+ID+"</MaintenanceId>" +
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

