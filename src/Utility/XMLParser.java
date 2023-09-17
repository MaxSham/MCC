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
    Map<Short, String> map;
    Map<Short, String> mapDiscription;

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
        mapDiscription = new HashMap<Short, String>();
        map.put((short)-1, "-Tech-");

        NodeList nodeList = doc.getElementsByTagName("Param");
        for(int i = 0; i < nodeList.getLength(); i++){
            NamedNodeMap nnm = nodeList.item(i).getAttributes();
            String paramNameDisc = getParamString(nnm, 0) + "@"+ getDiscription(nodeList.item(i).getChildNodes());
            map.put(Short.valueOf(getParamString(nnm, 1)), paramNameDisc);
        }

    }
    private String getDiscription(NodeList nodeList){
        int size = nodeList.getLength();
        for(int i = 0; i < size; ++i){
            if (nodeList.item(i).getNodeName() == "Description") {
                System.out.println(nodeList.item(i).getTextContent());
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
}
