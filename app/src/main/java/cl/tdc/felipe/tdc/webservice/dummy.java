package cl.tdc.felipe.tdc.webservice;

public class dummy {
    /* TODO URL WS */
    public static final String IP = "190.12.95.42";
    public static final String URL_TDC = "http://"+ IP +"/tdc/ServiceTDC/TDC.php";
    public static final String URL_SITES = "http://" + IP + "/tdc/Service/NeighborSite.php";
    public static final String URL_WIFI = "http://" + IP + "/tdc/Service/collection_wifi.php";
    public static final String URL_TRACKING = "http://" + IP + "/tdc/Service/position.php";
    public static final String URL_SEFI = "http://" + IP + ":8080/sefi-ws/SefiPort";
    public static final String URL_ELEMENT = "http://" + IP + "/tdc/Service/ElementDamage.php";
    public static final String URL_COMPONENT = "http://" + IP + "/tdc/Service/ComponentDamage.php";
    public static final String URL_PREVENTIVE = "http://" + IP + "/tdc/Service/form_prev.php";
    public static final String URL_TYPE = "http://" + IP + "/tdc/Service/TypeActivity.php";
    public static final String URL_FORM_CLOSE = "http://" + IP + "/tdc/Service/form_prev2.php";
    public static final String URL_FORM_PREV = "http://" + IP + "/tdc/Service/getItemsCheckList.php";
    public static final String URL_FORM_PREV_SAVE = "http://" + IP + "/tdc/Service/responseCheckList.php";
    public static final String URL_GET_ACTIVITY = "http://" + IP + "/tdc/Service/maintenancePlanningActivity.php";
    public static final String URL_MAINTENANCE_PLANNING = "http://" + IP + "/tdc/Service/maintenancePlanning.php";
    public static final String URL_PROJECT_WORK = "http://" + IP + "/tdc/ServiceWorkTracking/ProjectWork.php";
    public static final String URL_PROJECT_ACTIVITIES = "http://" + IP + "/tdc/ServiceWorkTracking/ActivitiesCheckList.php";
    public static final String URL_PROJECT_SEND = "http://" + IP + "/tdc/ServiceWorkTracking/RespActivitiesCheckList.php";
    public static final String URL_GET_DAILYACTIVITIES = "http://" + IP + "/tdc/Service/dailyActivitiesCheckList.php";
    public static final String URL_SEND_DAILYACTIVITIES = "http://" + IP + "/tdc/Service/RespDailyActivitiesCheckList.php";
    public static final String URL_MAIN_ACT_CHECKLIST = "http://" + IP + "/tdc/Service/maintenanceActivitiesCheckList.php";
    public static final String URL_SEND_MAIN_ACT_CHECKLIST = "http://" + IP + "/tdc/Service/RespMaintenanceActivitiesCheckList.php";
    public static final String URL_UPLOAD_IMG_WORKTRACKING = "http://" + IP + "/telrad/uploadWorkTracking.php";
    public static final String URL_UPLOAD_IMG_SECURITY = "http://" + IP + "/telrad/uploadTag.php";
    public static final String URL_UPLOAD_IMG_MAINTENANCE = "http://" + IP + "/telrad/uploadToServer.php";

    /** AVERIA**/
    public static final String URL_DEPTO = "http://" + IP + "/tdc/ServiceFailure/department.php";
    public static final String URL_PROV = "http://" + IP + "/tdc/ServiceFailure/province.php";
    public static final String URL_DISTRICT = "http://" + IP + "/tdc/ServiceFailure/district.php";
    public static final String URL_STATION = "http://" + IP + "/tdc/ServiceFailure/station.php";
    public static final String URL_UPLOAD_IMG = "http://" + IP + "/telrad/uploadFailure.php";
    public static final String URL_AVERIA = "http://" + IP + "/tdc/ServiceFailure/failureReport.php";

    /**RELEVAR*/
    public static final String URL_R_DEPTO = "http://" + IP + "/tdc/ServiceRelevamiento/department.php";
    public static final String URL_R_PROV = "http://" + IP + "/tdc/ServiceRelevamiento/province.php";
    public static final String URL_R_DISTRICT = "http://" + IP + "/tdc/ServiceRelevamiento/district.php";
    public static final String URL_R_STATION = "http://" + IP + "/tdc/ServiceRelevamiento/station.php";
    public static final String URL_R_CHECK = "http://" + IP + "/tdc/ServiceRelevamiento/itemsCheckRelevamiento.php";
    public static final String URL_R_R_CHECK = "http://" + IP + "/tdc/ServiceRelevamiento/itemsCheckRecomendado.php";
    public static final String URL_R_SEND_CHECK = "http://" + IP + "/tdc/ServiceRelevamiento/answerCheckRelevamiento.php";
    public static final String URL_R_SEND_CHECK_IMGS = "http://" + IP + "/telrad/uploadRel.php ";
    public static final String URL_R_R_SEND_CHECK = "http://" + IP + "/tdc/ServiceRelevamiento/itemsCheckRecomendado.php";
    public static final String URL_R_R_SEND_CHECK_IMGS = "http://" + IP + "/telrad/uploadRelXtra.php";


    /**RF **/
    public static final String URL_RF = "http://" + IP + "/tdc/ServicePreAsbuilt/nodob.php";
    public static final String URL_RF_CHECK = "http://" + IP + "/tdc/ServicePreAsbuilt/itemsCheckNodob.php";
    public static final String URL_RF_SEND = "http://" + IP + "/tdc/ServicePreAsbuilt/answerCheckNodob.php";
    public static final String URL_RF_IMG = "http://" + IP + "/telrad/uploadRf.php ";

    /**MW **/
    public static final String URL_MW = "http://" + IP + "/tdc/ServicePreAsbuilt/MW.php";
    public static final String URL_MW_CHECK = "http://" + IP + "/tdc/ServicePreAsbuilt/itemsCheckMW.php";
    public static final String URL_MW_SEND = "http://" + IP + "/tdc/ServicePreAsbuilt/answerCheckMW.php";
    public static final String URL_MW_IMG = "http://" + IP + "/telrad/uploadMw.php";


    public static final String ERROR_CONNECTION = "No se pudo conectar con el servidor, compruebe su conexión a internet y reinente";
    public static final String ERROR_PARSE = "Error al leer el XML, por favor reinente";
    public static final String ERROR_GENERAL = "Ha ocurrido un error, por favor reintente";



    public static String notifyExample = "<SOAP-ENV:Envelope SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:tns=\"urn:Configurationwsdl\">" +
            "<SOAP-ENV:Body>" +
            "<ns1:requestResponse xmlns:ns1=\"urn:Configurationwsdl\">" +
            "<Response xsi:type=\"tns:Response\">" +
            "<Code xsi:type=\"xsd:string\">10</Code>" +
            "<Description xsi:type=\"xsd:string\">Datos ingresados exitosamente</Description>" +
            "<Element xsi:type=\"xsd:string\">TRACKING</Element>" +
            "<OperationType xsi:type=\"xsd:string\">INSERT</OperationType>" +
            "</Response>" +
            "</ns1:requestResponse>" +
            "</SOAP-ENV:Body>" +
            "</SOAP-ENV:Envelope>";

    public static String checklistSaveResponse = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><SOAP-ENV:Envelope SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:tns=\"urn:Configurationwsdl\">" +
            "<SOAP-ENV:Body>" +
            "<ns1:requestResponse xmlns:ns1=\"urn:Configurationwsdl\">" +
            "<Response xsi:type=\"tns:Response\">" +
            "<Code xsi:type=\"xsd:string\">1</Code>" +
            "<Description xsi:type=\"xsd:string\">El checklist se recibio exitosamente</Description>" +
            "<MaintenanceId xsi:type=\"xsd:string\">465</MaintenanceId>" +
            "<Date xsi:type=\"xsd:string\">2015-07-08 15:07:42</Date>" +
            "</Response>" +
            "</ns1:requestResponse>" +
            "</SOAP-ENV:Body>" +
            "</SOAP-ENV:Envelope>";

    public static String getNewInformation = "<SOAP-ENV:Envelope SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:tns=\"urn:Configurationwsdl\">" +
            "<SOAP-ENV:Body>" +
            "<ns1:requestResponse xmlns:ns1=\"urn:Configurationwsdl\">" +
            "<Response xsi:type=\"tns:Response\">" +
            "<Information xsi:type=\"tns:Information\">" +
            "<Maintenance xsi:type=\"tns:Maintenance\">" +
            "<Date xsi:type=\"xsd:string\">2015-07-06 00:00:00</Date>" +
            "<Latitude xsi:type=\"xsd:string\">-7.17883</Latitude>" +
            "<Longitude xsi:type=\"xsd:string\">-76.7266</Longitude>" +
            "<Address xsi:type=\"xsd:string\">Ca. Miguel Grau 536</Address>" +
            "<Station xsi:type=\"xsd:string\">0102325_SM_Juanjui</Station>" +
            "<Status xsi:type=\"xsd:string\">ASSIGNED</Status>" +
            "<IdMaintenance xsi:type=\"xsd:string\">465</IdMaintenance>" +
            "<Type xsi:type=\"xsd:string\">Preventivo</Type>" +
            "<Systems xsi:type=\"tns:Systems\">" +
            "<System xsi:type=\"tns:System\">" +
            "<NameSystem xsi:type=\"xsd:string\">EQUIPOS DE RF, MW, BANDA ANCHA, EQUIPAMIENTO</NameSystem>" +
            "<Activities xsi:type=\"tns:Activities\">" +
            "<Activity xsi:type=\"tns:Activity\">" +
            "<NameActivity xsi:type=\"xsd:string\">Actualización de inventario general de la Estación.</NameActivity>" +
            "<IdActivity xsi:type=\"xsd:string\">24</IdActivity>" +
            "<Description xsi:nil=\"true\" xsi:type=\"xsd:string\"/>" +
            "</Activity>" +
            "<Activity xsi:type=\"tns:Activity\">" +
            "<NameActivity xsi:type=\"xsd:string\">Aumento de cables de E1s entre unidad indoor de microondas (IDU) y controlador (ISC) de la red iDEN</NameActivity>" +
            "<IdActivity xsi:type=\"xsd:string\">42</IdActivity>" +
            "<Description xsi:nil=\"true\" xsi:type=\"xsd:string\"/>" +
            "</Activity>" +
            "<Activity xsi:type=\"tns:Activity\">" +
            "<NameActivity xsi:type=\"xsd:string\">Cambio de bridas en los cables coaxiales.</NameActivity>" +
            "<IdActivity xsi:type=\"xsd:string\">22</IdActivity>" +
            "<Description xsi:nil=\"true\" xsi:type=\"xsd:string\"/>" +
            "</Activity>" +
            "</Activities>" +
            "</System>" +
            "</Systems>" +
            "</Maintenance>" +
            "</Information>" +
            "<Code xsi:type=\"xsd:string\">0</Code>" +
            "<Description xsi:type=\"xsd:string\">Datos consultados exitosamente.</Description>" +
            "<Flag xsi:type=\"xsd:string\">0</Flag>" +
            "<Element xsi:type=\"xsd:string\">PLANNING</Element>" +
            "<OperationType xsi:type=\"xsd:string\">GET</OperationType>" +
            "</Response>" +
            "</ns1:requestResponse>" +
            "</SOAP-ENV:Body>" +
            "</SOAP-ENV:Envelope>";

    public static String getInformation = "<SOAP-ENV:Envelope SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:tns=\"urn:Configurationwsdl\">" +
            "<SOAP-ENV:Body>" +
            "<ns1:requestResponse xmlns:ns1=\"urn:Configurationwsdl\">" +
            "<Response xsi:type=\"tns:Response\">" +
            "<Information xsi:type=\"tns:Information\">" +
            "<Maintenance xsi:type=\"tns:Maintenance\">" +
            "<Date xsi:type=\"xsd:string\">2015-05-27 00:00:00</Date>" +
            "<System xsi:type=\"xsd:string\">Generador de Energia1</System>" +
            "<Latitude xsi:type=\"xsd:string\">-33.3313</Latitude>" +
            "<Longitude xsi:type=\"xsd:string\">-71.001</Longitude>" +
            "<Address xsi:type=\"xsd:string\">Calle</Address>" +
            "<Station xsi:type=\"xsd:string\">station2</Station>" +
            "<Status xsi:type=\"xsd:string\">ASSIGNED</Status>" +
            "<IdMaintenance xsi:type=\"xsd:string\">74</IdMaintenance>" +
            "<Type xsi:type=\"xsd:string\">Preventivo</Type>" +
            "<Activities xsi:type=\"tns:Activities\">" +
            "<Name xsi:type=\"xsd:string\">Limpieza Generador</Name>" +
            "<Description xsi:nil=\"true\" xsi:type=\"xsd:string\"/>" +
            "<IdActivity xsi:type=\"xsd:string\">1</IdActivity>" +
            "</Activities>" +
            "<Activities xsi:type=\"tns:Activities\">" +
            "<Name xsi:type=\"xsd:string\">Cargar generador</Name>" +
            "<Description xsi:nil=\"true\" xsi:type=\"xsd:string\"/>" +
            "<IdActivity xsi:type=\"xsd:string\">2</IdActivity>" +
            "</Activities>" +
            "</Maintenance>" +
            "<Maintenance xsi:type=\"tns:Maintenance\">" +
            "<Date xsi:type=\"xsd:string\">2015-05-27 00:00:00</Date>" +
            "<System xsi:type=\"xsd:string\">Generador de Energia2</System>" +
            "<Latitude xsi:type=\"xsd:string\">-33.3213</Latitude>" +
            "<Longitude xsi:type=\"xsd:string\">-70.001</Longitude>" +
            "<Address xsi:type=\"xsd:string\">Calle</Address>" +
            "<Station xsi:type=\"xsd:string\">station1</Station>" +
            "<Status xsi:type=\"xsd:string\">TERMINATED</Status>" +
            "<IdMaintenance xsi:type=\"xsd:string\">74</IdMaintenance>" +
            "<Type xsi:type=\"xsd:string\">Preventivo</Type>" +
            "<Activities xsi:type=\"tns:Activities\">" +
            "<Name xsi:type=\"xsd:string\">Limpieza Generador</Name>" +
            "<Description xsi:nil=\"true\" xsi:type=\"xsd:string\"/>" +
            "<IdActivity xsi:type=\"xsd:string\">1</IdActivity>" +
            "</Activities>" +
            "<Activities xsi:type=\"tns:Activities\">" +
            "<Name xsi:type=\"xsd:string\">Limpieza Generador</Name>" +
            "<Description xsi:nil=\"true\" xsi:type=\"xsd:string\"/>" +
            "<IdActivity xsi:type=\"xsd:string\">2</IdActivity>" +
            "</Activities>" +
            "<Activities xsi:type=\"tns:Activities\">" +
            "<Name xsi:type=\"xsd:string\">Cargar generador</Name>" +
            "<Description xsi:nil=\"true\" xsi:type=\"xsd:string\"/>" +
            "<IdActivity xsi:type=\"xsd:string\">3</IdActivity>" +
            "</Activities>" +
            "<Activities xsi:type=\"tns:Activities\">" +
            "<Name xsi:type=\"xsd:string\">Limpieza Generador</Name>" +
            "<Description xsi:nil=\"true\" xsi:type=\"xsd:string\"/>" +
            "<IdActivity xsi:type=\"xsd:string\">4</IdActivity>" +
            "</Activities>" +
            "</Maintenance>" +
            "</Information>" +
            "<Code xsi:type=\"xsd:string\">0</Code>" +
            "<Description xsi:type=\"xsd:string\">Datos ingresados exitosamente</Description>" +
            "<Element xsi:type=\"xsd:string\">PLANNING</Element>" +
            "<OperationType xsi:type=\"xsd:string\">GET</OperationType>" +
            "</Response>" +
            "</ns1:requestResponse>" +
            "</SOAP-ENV:Body>" +
            "</SOAP-ENV:Envelope>";

    public static String updateTechnicians = "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
            "<S:Body>" +
            "<ns2:updateTechniciansResponse xmlns:ns2=\"http://bean.ws.sefi.com/\">" +
            "<return>" +
            "<returnCode>0</returnCode>" +
            "<returnDescription>OK</returnDescription>" +
            "<responses>" +
            "<entityId>Cuadrilla1</entityId>" +
            "<returnCode>0</returnCode>" +
            "<returnType>OK</returnType>" +
            "<returnDesc>actualizado correctamente. Actualizadas las [ 0] zonas. Actualizadas los [ 0] skills</returnDesc>" +
            "</responses>" +
            "</return>" +
            "</ns2:updateTechniciansResponse>" +
            "</S:Body>" +
            "</S:Envelope>";

    public static String updateActivities = "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
            "<S:Body>" +
            "<ns2:updateActivitiesResponse xmlns:ns2=\"http://bean.ws.sefi.com/\">" +
            "<return>" +
            "<returnCode>0</returnCode>" +
            "<returnDescription>OK</returnDescription>" +
            "<responses>" +
            "<entityId>ACTIVIDAD6</entityId>" +
            "<returnCode>0</returnCode>" +
            "<returnType>OK</returnType>" +
            "<returnDesc/>" +
            "</responses>" +
            "</return>" +
            "</ns2:updateActivitiesResponse>" +
            "</S:Body>" +
            "</S:Envelope>";

    public static String getItemCheckList = "<SOAP-ENV:Envelope SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:tns=\"urn:Configurationwsdl\">" +
            "<SOAP-ENV:Body>" +
            "<ns1:requestResponse xmlns:ns1=\"urn:Configurationwsdl\">" +
            "<Response xsi:type=\"tns:Response\">" +
            "<Code xsi:type=\"xsd:string\">0</Code>" +
            "<Description xsi:type=\"xsd:string\">Se ha realizado correctamente la operación</Description>" +
            "<MaintenanceId xsi:type=\"xsd:string\">1</MaintenanceId>" +
            "<Date xsi:nil=\"true\" xsi:type=\"xsd:string\">2015-03-24 01:01:36</Date>" +
            "<System>" +
            "<IdSystem>8</IdSystem>" +
            "<NameSystem>AIR CONDITIONING SYSTEM</NameSystem>" +
            "<Subsystem>" +
            "<IdSubsystem>1</IdSubsystem>" +
            "<NameSubsystem>SISTEMA DE AIRE ACONDICIONADO</NameSubsystem>" +
            "<Item>" +
            "<IdItem>1</IdItem>" +
            "<NameItem>Temperatura del Shelter</NameItem>" +
            "<Attribute>" +
            "<NameAttribute></NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>2</IdItem>" +
            "<NameItem>Amperaje de breaker</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>3</IdItem>" +
            "<NameItem>Presión: alta y baja</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>4</IdItem>" +
            "<NameItem>Aislamiento de los Motores</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>5</IdItem>" +
            "<NameItem>Tomar lectura de voltaje y corriente del compresor</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>6</IdItem>" +
            "<NameItem>Pruebas de arranque AA con termostato</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>7</IdItem>" +
            "<NameItem>Emisión de ruido de ambas unidades </NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>8</IdItem>" +
            "<NameItem>Verificación del funcionamiento del sistema y prueba con" +
            "alarmas en el secuenciador y en el arrancador</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>9</IdItem>" +
            "<NameItem>Revisión de válvulas</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>10</IdItem>" +
            "<NameItem>Limpiar los filtros de aire (cambiar si es necesario)</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>11</IdItem>" +
            "<NameItem>Limpieza del condensador y el evaporador. " +
            "Lubricación de sus partes móviles.</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>12</IdItem>" +
            "<NameItem>Reajuste de pernos, tornillos y soporte</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>13</IdItem>" +
            "<NameItem>Limpieza de gabinete y bandeja de condensador</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>14</IdItem>" +
            "<NameItem>Limpieza de difusores y rejillas</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>15</IdItem>" +
            "<NameItem>Limpieza de componentes del sistema eléctrico (contactores " +
            "y relés)</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>16</IdItem>" +
            "<NameItem>Reajuste de pernos, tornillos y soporte</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>17</IdItem>" +
            "<NameItem>Verificar o reemplazar terminales del cableado eléctrico</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>18</IdItem>" +
            "<NameItem>Limpieza de bobinas</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>19</IdItem>" +
            "<NameItem>Sustitución de los pernos y tornillos en general.</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>20</IdItem>" +
            "<NameItem>Recarga de gas refrigerante, si es necesario</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>21</IdItem>" +
            "<NameItem>Verificación de las aletas de refrigeración de los serpentines " +
            "(evaporador y condensador)</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>22</IdItem>" +
            "<NameItem>Verificar el estado de paletas de ventilación. Cambiar " +
            "si es necesario</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>23</IdItem>" +
            "<NameItem>Revisar el rodaje y/o bocinas del evaporador y condensador " +
            "del aire acondicionado</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>24</IdItem>" +
            "<NameItem>Compruebe si aire acondicionado de backup está operativo</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "</Subsystem>" +
            "<Subsystem>" +
            "<IdSubsystem>2</IdSubsystem>" +
            "<NameSubsystem>SISTEMA DE AIRE ACONDICIONADO APM 30</NameSubsystem>" +
            "<Item>" +
            "<IdItem>25</IdItem>" +
            "<NameItem>Limpieza y Lavado de Filtros de Aire de APM30</NameItem>" +
            "<Attribute>" +
            "<NameAttribute></NameAttribute>" +
            "<Values>" +
            "<TypeValue>TEXT</TypeValue>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>26</IdItem>" +
            "<NameItem>Limpieza de Extractores de Aire (Mantto de Motor y Ventilador)</NameItem>" +
            "<Attribute>" +
            "<NameAttribute></NameAttribute>" +
            "<Values>" +
            "<TypeValue>TEXT</TypeValue>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "</Subsystem>" +
            "</System>" +
            "<System>" +
            "<IdSystem>8</IdSystem>" +
            "<NameSystem>AIR CONDITIONING SYSTEM</NameSystem>" +
            "<Subsystem>" +
            "<IdSubsystem>1</IdSubsystem>" +
            "<NameSubsystem>SISTEMA DE AIRE ACONDICIONADO</NameSubsystem>" +
            "<Item>" +
            "<IdItem>1</IdItem>" +
            "<NameItem>Temperatura del Shelter</NameItem>" +
            "<Attribute>" +
            "<NameAttribute></NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>2</IdItem>" +
            "<NameItem>Amperaje de breaker</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>3</IdItem>" +
            "<NameItem>Presión: alta y baja</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>4</IdItem>" +
            "<NameItem>Aislamiento de los Motores</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>5</IdItem>" +
            "<NameItem>Tomar lectura de voltaje y corriente del compresor</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>6</IdItem>" +
            "<NameItem>Pruebas de arranque AA con termostato</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>7</IdItem>" +
            "<NameItem>Emisión de ruido de ambas unidades </NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>8</IdItem>" +
            "<NameItem>Verificación del funcionamiento del sistema y prueba con" +
            "alarmas en el secuenciador y en el arrancador</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>9</IdItem>" +
            "<NameItem>Revisión de válvulas</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>10</IdItem>" +
            "<NameItem>Limpiar los filtros de aire (cambiar si es necesario)</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>11</IdItem>" +
            "<NameItem>Limpieza del condensador y el evaporador. " +
            "Lubricación de sus partes móviles.</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>12</IdItem>" +
            "<NameItem>Reajuste de pernos, tornillos y soporte</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>13</IdItem>" +
            "<NameItem>Limpieza de gabinete y bandeja de condensador</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>14</IdItem>" +
            "<NameItem>Limpieza de difusores y rejillas</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>15</IdItem>" +
            "<NameItem>Limpieza de componentes del sistema eléctrico (contactores " +
            "y relés)</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>16</IdItem>" +
            "<NameItem>Reajuste de pernos, tornillos y soporte</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>17</IdItem>" +
            "<NameItem>Verificar o reemplazar terminales del cableado eléctrico</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>18</IdItem>" +
            "<NameItem>Limpieza de bobinas</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>19</IdItem>" +
            "<NameItem>Sustitución de los pernos y tornillos en general.</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>20</IdItem>" +
            "<NameItem>Recarga de gas refrigerante, si es necesario</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>21</IdItem>" +
            "<NameItem>Verificación de las aletas de refrigeración de los serpentines " +
            "(evaporador y condensador)</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>22</IdItem>" +
            "<NameItem>Verificar el estado de paletas de ventilación. Cambiar " +
            "si es necesario</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>23</IdItem>" +
            "<NameItem>Revisar el rodaje y/o bocinas del evaporador y condensador " +
            "del aire acondicionado</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>24</IdItem>" +
            "<NameItem>Compruebe si aire acondicionado de backup está operativo</NameItem>" +
            "<Attribute>" +
            "<NameAttribute>AA#1</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "<Attribute>" +
            "<NameAttribute>AA#2</NameAttribute>" +
            "<Values>" +
            "<TypeValue>CHECK</TypeValue>" +
            "<State>" +
            "<ValueState>OK</ValueState>" +
            "<ValueState>N OK</ValueState>" +
            "</State>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "</Subsystem>" +
            "<Subsystem>" +
            "<IdSubsystem>2</IdSubsystem>" +
            "<NameSubsystem>SISTEMA DE AIRE ACONDICIONADO APM 30</NameSubsystem>" +
            "<Item>" +
            "<IdItem>25</IdItem>" +
            "<NameItem>Limpieza y Lavado de Filtros de Aire de APM30</NameItem>" +
            "<Attribute>" +
            "<NameAttribute></NameAttribute>" +
            "<Values>" +
            "<TypeValue>TEXT</TypeValue>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "<Item>" +
            "<IdItem>26</IdItem>" +
            "<NameItem>Limpieza de Extractores de Aire (Mantto de Motor y Ventilador)</NameItem>" +
            "<Attribute>" +
            "<NameAttribute></NameAttribute>" +
            "<Values>" +
            "<TypeValue>TEXT</TypeValue>" +
            "</Values>" +
            "</Attribute>" +
            "</Item>" +
            "</Subsystem>" +
            "</System>" +
            "</Response>" +
            "</ns1:requestResponse>" +
            "</SOAP-ENV:Body>" +
            "</SOAP-ENV:Envelope>";
}
