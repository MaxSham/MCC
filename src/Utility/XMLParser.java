package Utility;

import Telemetry.Config;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder    ;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class XMLParser {
    Document doc;
    static Map<Short, String> map;
    static Map<String, String> mapDiscription;
    static Map<String, Map<Integer , String>> mapTextes;

    public XMLParser() {
        try{
            File xmlFile = new File(Config.XMLFilePath);
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = documentBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            fillMap();
        } catch (Exception e) {
            System.out.println("XML ERROR: " + e.toString());
        }
    }
    private void fillMap(){
        map = new HashMap<Short, String>();
        mapDiscription = new HashMap<String, String>();
        mapTextes = new HashMap<>();
        map.put((short)-1, "-Tech-");
        mapDiscription.put("-Tech-", "Служебная запись");

        NodeList nodeList = doc.getElementsByTagName("Param");
        for(int i = 0; i < nodeList.getLength(); i++){
            NamedNodeMap nnm = nodeList.item(i).getAttributes();
            mapDiscription.put(getParamString(nnm, 0), getDiscription(nodeList.item(i).getChildNodes()));
            mapTextes.put(getParamString(nnm,0), getTextes(nodeList.item(i).getChildNodes()));
            map.put(Short.valueOf(getParamString(nnm, 1)), getParamString(nnm, 0));
        }

    }
    private Map<Integer, String> getTextes(NodeList nodeList){
        int size = nodeList.getLength();
        Map<Integer, String> result = null;
        for(int i = 0; i<size; ++i){
            if (nodeList.item(i).getNodeName() == "Textes") {
                result = new HashMap<>();
                NodeList textes = nodeList.item(i).getChildNodes();
                int s = textes.getLength();
                for(int j = 0; j < s; ++j){
                    result.put(j, textes.item(j).getTextContent());
                }
            }
        }
        return result;
    }
    private String getDiscription(NodeList nodeList){
        int size = nodeList.getLength();
        for(int i = 0; i < size; ++i){
            if (nodeList.item(i).getNodeName() == "Description") {
                return nodeList.item(i).getTextContent();
            }
        }
        return null;
    }

    private String getParamString(NamedNodeMap nnm, int num){
        String result = nnm.item(num).toString().split("=")[1];
        return result.substring(1, result.length() - 1);
    }
    public String getParamName(short number){
        return map.get(number);
    }
    public static String getDiscription(String name){
        return mapDiscription.get(name);
    }
    public static String getText(String paramName, int num){
        Map<Integer, String> result = mapTextes.get(paramName);
        if(result == null){
            return null;
        }
        return result.get(num);
    }
}
