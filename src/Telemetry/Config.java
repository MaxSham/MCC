package Telemetry;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class Config {
    public static String KNPFilePath = "Data/source/190829_v29854.KNP";
    public static String XMLFilePath = "Data/source/KNP-173.14.33.58.dat.xml";
    public static String DimFilePath = "Data/source/dimens.ion";
    public static String RezFilePath = "Data/out/";

    public static int PageSize = 40;

    public static void setConfig(String confFilePath) {
        try {
            File xmlFile = new File(confFilePath);
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = documentBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            KNPFilePath = doc.getElementsByTagName("DataFile").item(0).getTextContent();
            XMLFilePath = doc.getElementsByTagName("XMLFile").item(0).getTextContent();
            DimFilePath = doc.getElementsByTagName("DimensionsFile").item(0).getTextContent();
            RezFilePath = doc.getElementsByTagName("ResultsDir").item(0).getTextContent();
            PageSize = Integer.parseInt(doc.getElementsByTagName("PageSize").item(0).getTextContent());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
