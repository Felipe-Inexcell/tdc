package cl.tdc.felipe.tdc.webservice;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

import cl.tdc.felipe.tdc.extras.Funciones;
import cl.tdc.felipe.tdc.objects.ControSeguridadDiario.Elemento;
import cl.tdc.felipe.tdc.objects.ControSeguridadDiario.Modulo;
import cl.tdc.felipe.tdc.objects.ControSeguridadDiario.SubModulo;
import cl.tdc.felipe.tdc.objects.MaintChecklist.Section;
import cl.tdc.felipe.tdc.objects.Seguimiento.Actividad;
import cl.tdc.felipe.tdc.objects.Seguimiento.Dia;
import cl.tdc.felipe.tdc.objects.Seguimiento.Proyecto;

public class XMLParserChecklists {

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

    public static ArrayList<Modulo> getChecklistDaily(String xmlRecords) throws ParserConfigurationException,
            SAXException, IOException, XPathExpressionException {
        ArrayList<Modulo> mModulos = new ArrayList<>();


        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xmlRecords));
        Document doc = db.parse(is);

        NodeList modules = doc.getElementsByTagName("Module");


        for(int i = 0; i< modules.getLength(); i++){
            Modulo m = new Modulo();
            Element module = (Element) modules.item(i);

            m.setId(Funciones.getCharacterDataFromElement((Element)module.getElementsByTagName("IdModule").item(0)));
            m.setName(Funciones.getCharacterDataFromElement((Element) module.getElementsByTagName("NameModule").item(0)));

            NodeList submodules = module.getElementsByTagName("SubModules").item(0).getChildNodes();

            ArrayList<SubModulo> subModulos = new ArrayList<>();
            for(int j = 0; j<submodules.getLength(); j++){
                SubModulo sm = new SubModulo();
                Element subModule = (Element) submodules.item(j);

                sm.setId(Funciones.getCharacterDataFromElement((Element)subModule.getElementsByTagName("IdSubModule").item(0)));
                sm.setName(Funciones.getCharacterDataFromElement((Element) subModule.getElementsByTagName("NameSubModule").item(0)));

                NodeList activities = subModule.getElementsByTagName("Activities").item(0).getChildNodes();
                ArrayList<Elemento> elementos = new ArrayList<>();
                for(int k = 0; k<activities.getLength();k++){
                    Elemento e = new Elemento();
                    Element actividad = (Element) activities.item(k);

                    e.setId(Funciones.getCharacterDataFromElement((Element)actividad.getElementsByTagName("IdActivity").item(0)));
                    e.setName(Funciones.getCharacterDataFromElement((Element) actividad.getElementsByTagName("NameActivity").item(0)));
                    e.setType(Funciones.getCharacterDataFromElement((Element) actividad.getElementsByTagName("TypeActivity").item(0)));

                    if(e.getType().compareTo("CHECK")==0) {
                        NodeList values = actividad.getElementsByTagName("Values").item(0).getChildNodes();
                        ArrayList<String> valores = new ArrayList<>();

                        for(int x = 0; x<values.getLength(); x++){
                            Element v = (Element) values.item(x);
                            valores.add(Funciones.getCharacterDataFromElement((Element)v.getElementsByTagName("NameValue").item(0)));

                        }
                        e.setValues(valores);
                    }
                    elementos.add(e);

                }
                sm.setElementos(elementos);
                subModulos.add(sm);
            }

            m.setSubModulos(subModulos);
            mModulos.add(m);
        }



        return mModulos;
    }

    public static ArrayList<cl.tdc.felipe.tdc.objects.MaintChecklist.Modulo> getChecklistMaintenance(String xmlRecords) throws ParserConfigurationException,
            SAXException, IOException, XPathExpressionException {
        ArrayList<cl.tdc.felipe.tdc.objects.MaintChecklist.Modulo> mModulos = new ArrayList<>();


        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xmlRecords));
        Document doc = db.parse(is);

        NodeList modules = doc.getElementsByTagName("Module");

        for(int i = 0; i< modules.getLength(); i++){
            cl.tdc.felipe.tdc.objects.MaintChecklist.Modulo m = new cl.tdc.felipe.tdc.objects.MaintChecklist.Modulo();
            Element module = (Element) modules.item(i);

            m.setId(Funciones.getCharacterDataFromElement((Element)module.getElementsByTagName("IdModule").item(0)));
            m.setName(Funciones.getCharacterDataFromElement((Element) module.getElementsByTagName("NameModule").item(0)));

            NodeList submodules = module.getElementsByTagName("SubModules").item(0).getChildNodes();

            ArrayList<cl.tdc.felipe.tdc.objects.MaintChecklist.SubModulo> subModulos = new ArrayList<>();
            for(int j = 0; j<submodules.getLength(); j++){
                cl.tdc.felipe.tdc.objects.MaintChecklist.SubModulo sm = new cl.tdc.felipe.tdc.objects.MaintChecklist.SubModulo();
                Element subModule = (Element) submodules.item(j);

                sm.setId(Funciones.getCharacterDataFromElement((Element)subModule.getElementsByTagName("IdSubModule").item(0)));
                sm.setName(Funciones.getCharacterDataFromElement((Element) subModule.getElementsByTagName("NameSubModule").item(0)));

                NodeList sections = subModule.getElementsByTagName("Sections").item(0).getChildNodes();
                ArrayList<Section> sectionArrayList = new ArrayList<>();
                for(int y = 0; y<sections.getLength(); y++) {
                    Section s = new Section();
                    Element Sections = (Element)sections.item(y);

                    s.setId(Funciones.getCharacterDataFromElement((Element)Sections.getElementsByTagName("IdSection").item(0)));
                    s.setName(Funciones.getCharacterDataFromElement((Element) Sections.getElementsByTagName("NameSection").item(0)));

                    NodeList activities = Sections.getElementsByTagName("Activities").item(0).getChildNodes();
                    ArrayList<Elemento> elementos = new ArrayList<>();
                    for (int k = 0; k < activities.getLength(); k++) {
                        Elemento e = new Elemento();
                        Element actividad = (Element) activities.item(k);

                        e.setId(Funciones.getCharacterDataFromElement((Element) actividad.getElementsByTagName("IdActivity").item(0)));
                        e.setName(Funciones.getCharacterDataFromElement((Element) actividad.getElementsByTagName("NameActivity").item(0)));
                        e.setType(Funciones.getCharacterDataFromElement((Element) actividad.getElementsByTagName("TypeActivity").item(0)));

                        if (e.getType().compareTo("CHECK") == 0) {
                            NodeList values = actividad.getElementsByTagName("Values").item(0).getChildNodes();
                            ArrayList<String> valores = new ArrayList<>();

                            for (int x = 0; x < values.getLength(); x++) {
                                Element v = (Element) values.item(x);
                                valores.add(Funciones.getCharacterDataFromElement((Element) v.getElementsByTagName("NameValue").item(0)));

                            }
                            e.setValues(valores);
                        }
                        elementos.add(e);

                    }
                    s.setElementos(elementos);
                    sectionArrayList.add(s);
                }
                sm.setSections(sectionArrayList);
                subModulos.add(sm);
            }

            m.setSubModulos(subModulos);
            mModulos.add(m);
        }


        return mModulos;
    }

}
