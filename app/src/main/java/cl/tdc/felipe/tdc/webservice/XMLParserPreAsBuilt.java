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

import cl.tdc.felipe.tdc.objects.PreAsBuilt.Informacion;
import cl.tdc.felipe.tdc.objects.Relevar.Item;
import cl.tdc.felipe.tdc.objects.Relevar.Modulo;

public class XMLParserPreAsBuilt {
    public static int RF = 0;
    public static int MW = 1;

    private static String NAMESTATION = "NameStation";
    private static String IDSTATION = "IdStation";
    private static String LONGITUDE = "Longitude";
    private static String LATITUDE = "Latitude";
    private static String DEPARTAMENT = "Department";
    private static String PROVINCE = "Province";
    private static String DISTRICT = "District";
    private static String ADDRESS = "Address";
    private static String COMMENT = "Comment";


    public static Informacion getInfoPreAsBuilt(String XML, int type) throws ParserConfigurationException,
            SAXException, IOException, XPathExpressionException {

        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(XML));

        Document doc = db.parse(is);
        String id;
        Element nodo;

        if (type == RF) {
            id = getCharacterDataFromElement((Element) doc.getElementsByTagName("Id_NodoB").item(0));
            nodo = (Element) doc.getElementsByTagName("NodoB").item(0);
        } else {
            id = getCharacterDataFromElement((Element) doc.getElementsByTagName("IdMW").item(0));
            nodo = (Element) doc.getElementsByTagName("SiteA").item(0);
        }

        Informacion info = new Informacion();

        info.setId(id);
        info.setName(getCharacterDataFromElement((Element) nodo.getElementsByTagName(NAMESTATION).item(0)));
        info.setIdStation((getCharacterDataFromElement((Element) nodo.getElementsByTagName(IDSTATION).item(0))));
        info.setLongitude((getCharacterDataFromElement((Element) nodo.getElementsByTagName(LONGITUDE).item(0))));
        info.setLatitude((getCharacterDataFromElement((Element) nodo.getElementsByTagName(LATITUDE).item(0))));
        info.setDepartament((getCharacterDataFromElement((Element) nodo.getElementsByTagName(DEPARTAMENT).item(0))));
        info.setProvince((getCharacterDataFromElement((Element) nodo.getElementsByTagName(PROVINCE).item(0))));
        info.setDistrict((getCharacterDataFromElement((Element) nodo.getElementsByTagName(DISTRICT).item(0))));
        info.setAddress((getCharacterDataFromElement((Element) nodo.getElementsByTagName(ADDRESS).item(0))));
        info.setComment((getCharacterDataFromElement((Element) nodo.getElementsByTagName(COMMENT).item(0))));

        return info;
    }

    public static ArrayList<Modulo> getCheckRF(String XML) throws ParserConfigurationException,
            SAXException, IOException, XPathExpressionException {

        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(XML));

        Document doc = db.parse(is);

        NodeList modulos = doc.getElementsByTagName("System");
        ArrayList<Modulo> moduloArrayList = new ArrayList<>();

        for (int i = 0; i < modulos.getLength(); i++) {
            Element eModulo = (Element) modulos.item(i);
            Modulo modulo = new Modulo();

            modulo.setId(getCharacterDataFromElement((Element) eModulo.getElementsByTagName("Id_System").item(0)));
            modulo.setName(getCharacterDataFromElement((Element) eModulo.getElementsByTagName("Name_System").item(0)));

            NodeList submodulos = eModulo.getElementsByTagName("Subsystem");
            ArrayList<Modulo> submoduloArrayList = new ArrayList<>();

            for (int j = 0; j < submodulos.getLength(); j++) {
                Element eSub = (Element) submodulos.item(j);
                Modulo subModulo = new Modulo();

                subModulo.setId(getCharacterDataFromElement((Element) eSub.getElementsByTagName("Id_Subsystem").item(0)));
                subModulo.setName(getCharacterDataFromElement((Element) eSub.getElementsByTagName("Name_Subsystem").item(0)));

                NodeList items = eSub.getChildNodes().item(2).getChildNodes();
                ArrayList<Item> itemArrayList = new ArrayList<>();

                for (int k = 0; k < items.getLength(); k++) {
                    Element eItem = (Element) items.item(k);
                    Item item = new Item();

                    item.setId(getCharacterDataFromElement((Element) eItem.getElementsByTagName("Id_Item").item(0)));
                    item.setName(getCharacterDataFromElement((Element) eItem.getElementsByTagName("Name_Item").item(0)));
                    item.setType(getCharacterDataFromElement((Element) eItem.getElementsByTagName("Type_Item").item(0)));

                    if (item.getType().equals("COMPLEX")) {
                        NodeList subitems = eItem.getElementsByTagName("SubItem").item(0).getChildNodes();
                        ArrayList<Item> subItemsArrayList = new ArrayList<>();

                        for (int x = 0; x < subitems.getLength(); x++) {
                            Element eSubItem = (Element) subitems.item(x);
                            Item subitem = new Item();

                            subitem.setId(getCharacterDataFromElement((Element) eSubItem.getElementsByTagName("Id_Item").item(0)));
                            subitem.setName(getCharacterDataFromElement((Element) eSubItem.getElementsByTagName("Name_Item").item(0)));
                            subitem.setType(getCharacterDataFromElement((Element) eSubItem.getElementsByTagName("Type_Item").item(0)));

                            NodeList values = eItem.getElementsByTagName("Value");
                            ArrayList<String> valueArrayList = new ArrayList<>();

                            for (int y = 0; y < values.getLength(); y++) {
                                Element eVal = (Element) values.item(y);
                                valueArrayList.add(getCharacterDataFromElement((Element) eVal.getElementsByTagName("Name_Value").item(0)));
                            }
                            subitem.setValues(valueArrayList);
                            subItemsArrayList.add(subitem);
                        }
                        item.setSubItems(subItemsArrayList);
                    } else {


                        NodeList values = eItem.getElementsByTagName("Value");
                        ArrayList<String> valueArrayList = new ArrayList<>();

                        for (int x = 0; x < values.getLength(); x++) {
                            Element eVal = (Element) values.item(x);
                            valueArrayList.add(getCharacterDataFromElement((Element) eVal.getElementsByTagName("Name_Value").item(0)));
                        }


                        item.setValues(valueArrayList);
                    }
                    itemArrayList.add(item);
                }
                subModulo.setItems(itemArrayList);
                submoduloArrayList.add(subModulo);

            }

            modulo.setSubModulo(submoduloArrayList);
            moduloArrayList.add(modulo);

        }

        return moduloArrayList;
    }

    public static ArrayList<Modulo> getCheckMW(String XML) throws ParserConfigurationException,
            SAXException, IOException, XPathExpressionException {

        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(XML));

        Document doc = db.parse(is);

        NodeList modulos = doc.getElementsByTagName("Module");
        ArrayList<Modulo> moduloArrayList = new ArrayList<>();

        for (int i = 0; i < modulos.getLength(); i++) {
            Element eModulo = (Element) modulos.item(i);
            Modulo modulo = new Modulo();

            modulo.setId(getCharacterDataFromElement((Element) eModulo.getElementsByTagName("IdModule").item(0)));
            modulo.setName(getCharacterDataFromElement((Element) eModulo.getElementsByTagName("NameModule").item(0)));


            NodeList items = eModulo.getChildNodes().item(2).getChildNodes();
            if(items == null){
                continue;
            }
            ArrayList<Item> itemArrayList = new ArrayList<>();

            for (int k = 0; k < items.getLength(); k++) {
                Element eItem = (Element) items.item(k);
                Item item = new Item();

                item.setId(getCharacterDataFromElement((Element) eItem.getElementsByTagName("Id_Item").item(0)));
                item.setName(getCharacterDataFromElement((Element) eItem.getElementsByTagName("Name_Item").item(0)));
                item.setType(getCharacterDataFromElement((Element) eItem.getElementsByTagName("Type_Item").item(0)));

                if (item.getType().equals("COMPLEX")) {
                    NodeList subitems = eItem.getElementsByTagName("SubItem").item(0).getChildNodes();
                    ArrayList<Item> subItemsArrayList = new ArrayList<>();

                    for (int x = 0; x < subitems.getLength(); x++) {
                        Element eSubItem = (Element) subitems.item(x);
                        Item subitem = new Item();

                        subitem.setId(getCharacterDataFromElement((Element) eSubItem.getElementsByTagName("Id_Item").item(0)));
                        subitem.setName(getCharacterDataFromElement((Element) eSubItem.getElementsByTagName("Name_Item").item(0)));
                        subitem.setType(getCharacterDataFromElement((Element) eSubItem.getElementsByTagName("Type_Item").item(0)));

                        if (subitem.getType().equals("SELECT") || subitem.getType().equals("CHECK")) {
                            Element tmp = (Element) eSubItem.getChildNodes().item(3);
                            if(tmp == null)continue;
                            NodeList values = eSubItem.getChildNodes().item(3).getChildNodes();
                            ArrayList<String> valueArrayList = new ArrayList<>();

                            for (int y = 0; y < values.getLength(); y++) {
                                Element eVal = (Element) values.item(y);
                                valueArrayList.add(getCharacterDataFromElement((Element) eVal.getElementsByTagName("Name_Value").item(0)));
                            }
                            if(valueArrayList.size()>0) {
                                subitem.setValues(valueArrayList);
                                subItemsArrayList.add(subitem);
                            }
                        } else if(subitem.getType().equals("VARCHAR") || subitem.getType().equals("NUM")) {
                            subItemsArrayList.add(subitem);
                        }

                    }
                    item.setSubItems(subItemsArrayList);
                } else if(item.getType().equals("SELECT") || item.getType().equals("CHECK")){
                    Element tmp = (Element)eItem.getChildNodes().item(3);
                    if (tmp == null)continue;
                    NodeList values = eItem.getChildNodes().item(3).getChildNodes();
                    ArrayList<String> valueArrayList = new ArrayList<>();

                    for (int x = 0; x < values.getLength(); x++) {
                        Element eVal = (Element) values.item(x);
                        valueArrayList.add(getCharacterDataFromElement((Element) eVal.getElementsByTagName("Name_Value").item(0)));
                    }


                    item.setValues(valueArrayList);
                }


                if (item.getType().equals("SELECT") || item.getType().equals("CHECK")) {
                    if (item.getValues().size() > 0)
                        itemArrayList.add(item);
                } else if(item.getType().equals("VARCHAR") || item.getType().equals("NUM") || item.getType().equals("COMPLEX")) {
                    itemArrayList.add(item);
                }

            }
            if(itemArrayList.size() > 0) {
                modulo.setItems(itemArrayList);
                moduloArrayList.add(modulo);
            }
        }

        return moduloArrayList;
    }


    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }

}
