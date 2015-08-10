package cl.tdc.felipe.tdc.webservice;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import cl.tdc.felipe.tdc.adapters.Actividades;
import cl.tdc.felipe.tdc.adapters.Maintenance;
import cl.tdc.felipe.tdc.extras.Funciones;
import cl.tdc.felipe.tdc.objects.FormSubSystem;
import cl.tdc.felipe.tdc.objects.FormSubSystemItem;
import cl.tdc.felipe.tdc.objects.FormSubSystemItemAttribute;
import cl.tdc.felipe.tdc.objects.FormSubSystemItemAttributeValues;
import cl.tdc.felipe.tdc.objects.FormSystem;
import cl.tdc.felipe.tdc.objects.FormularioCheck;
import cl.tdc.felipe.tdc.objects.Maintenance.Activity;
import cl.tdc.felipe.tdc.objects.Maintenance.Agenda;
import cl.tdc.felipe.tdc.objects.Maintenance.MainSystem;
import cl.tdc.felipe.tdc.objects.Seguimiento.Actividad;
import cl.tdc.felipe.tdc.objects.Seguimiento.Dia;
import cl.tdc.felipe.tdc.objects.Seguimiento.Proyecto;

public class XMLParserSeguimiento {

    public static String getResultCode(String XML) throws ParserConfigurationException,
            SAXException, IOException, XPathExpressionException {

        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(XML));

        Document doc = db.parse(is);

        String code = Funciones.getCharacterDataFromElement((Element) doc.getElementsByTagName("Code").item(0));
        String description = Funciones.getCharacterDataFromElement((Element) doc.getElementsByTagName("Description").item(0));

        return code + ";" + description;
    }

    public static ArrayList<Proyecto> getProjects(String xmlRecords) throws ParserConfigurationException,
            SAXException, IOException, XPathExpressionException {
        ArrayList<Proyecto> mProjects = new ArrayList<>();


        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xmlRecords));
        Document doc = db.parse(is);

        NodeList nodes = doc.getElementsByTagName("Node");

        for (int i = 0; i < nodes.getLength(); i++) {
            Proyecto proyecto = new Proyecto();
            NodeList parameters = nodes.item(i).getChildNodes().item(0).getChildNodes();
            for(int j = 0; j < parameters.getLength(); j++){

                String name = Funciones.getCharacterDataFromElement((Element)parameters.item(j).getChildNodes().item(0));
                String value = Funciones.getCharacterDataFromElement((Element)parameters.item(j).getChildNodes().item(1));

                switch (name){
                    case "IDPROJECT":
                        proyecto.setId(value);
                        break;
                    case "DNPROJECT":
                        proyecto.setNombre(value);
                        break;
                    case "DATEINITIAL":
                        proyecto.setFecha_inicio(value);
                        break;
                    case "DATEFINAL":
                        proyecto.setFecha_final(value);
                        break;
                    case "DAY":
                        proyecto.setDia(value);
                        break;
                    case "LATE":
                        proyecto.setAtrasado(value);
                        break;
                    case "ADVANCE":
                        proyecto.setAvance_programado(value);
                        break;
                    case "ADVANCEREAL":
                        proyecto.setAvance_real(value);
                        break;
                    default:
                        break;
                }
            }
            mProjects.add(proyecto);
        }

        return mProjects;
    }

    public static ArrayList<Dia> getActivities(String xmlRecords) throws ParserConfigurationException,
            SAXException, IOException, XPathExpressionException {
        ArrayList<Dia> mDias = new ArrayList<>();


        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xmlRecords));
        Document doc = db.parse(is);

        NodeList days = doc.getElementsByTagName("Days").item(0).getChildNodes();

        for(int i = 0; i < days.getLength(); i++){
            Dia dia = new Dia();
            Element e = (Element) days.item(i);
            dia.setDayNumber(Funciones.getCharacterDataFromElement((Element)e.getElementsByTagName("DayNumber").item(0)));
            dia.setProgrammedAdvance(Funciones.getCharacterDataFromElement((Element) e.getElementsByTagName("ProgrammedAdvance").item(0)));
            dia.setRealAdvance(Funciones.getCharacterDataFromElement((Element) e.getElementsByTagName("RealAdvance").item(0)));
            dia.setDate(Funciones.getCharacterDataFromElement((Element) e.getElementsByTagName("Date").item(0)));
            dia.setDescriptionDay(Funciones.getCharacterDataFromElement((Element) e.getElementsByTagName("DescriptionDay").item(0)));

            NodeList activities = e.getElementsByTagName("Activity");
            ArrayList<Actividad> acts = new ArrayList<>();
            for(int j = 0; j < activities.getLength(); j++){
                Actividad actividad = new Actividad();
                Element element = (Element) activities.item(j);

                actividad.setIdActivity(Funciones.getCharacterDataFromElement((Element)element.getElementsByTagName("IdActivity").item(0)));
                actividad.setNameActivity(Funciones.getCharacterDataFromElement((Element) element.getElementsByTagName("NameActivity").item(0)));
                actividad.setFoto(Funciones.getCharacterDataFromElement((Element) element.getElementsByTagName("Photo").item(0)));
                actividad.setAdvance(Funciones.getCharacterDataFromElement((Element)element.getElementsByTagName("AdvanceActivity").item(0)));
                actividad.setSelected(Funciones.getCharacterDataFromElement((Element)element.getElementsByTagName("ActivitySelected").item(0)));

                acts.add(actividad);
            }
            dia.setActividades(acts);
            mDias.add(dia);
        }

        return mDias;
    }

}
