import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import Telemetry.*;

public class Main {
    public static void main(String[] args){
        try {
            Config.setConfig("Data/source/config.xml");
            App app = new App(Config.KNPFilePath);
            GUI gui = new GUI(app);
        } catch (Exception e) {
            System.out.println("App error:");
            e.printStackTrace();
        }
    }
}