import Telemetry.App;
import Telemetry.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Set;

public class GUI extends JFrame implements ActionListener {
    Box box;
    JButton bStart;
    JButton bAll;
    JButton bClear;
    JScrollPane jScrollPane;

    App app;
    boolean isReadyForPrint = false;

    
    public GUI(App _app){
        app = _app;

        setTitle("Телеметрия КАНОПУС-В");
        setIconImage(new ImageIcon("Data/source/Kanopus-V.jpg").getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(470,730);
        setResizable(false);


        bStart = new JButton("Прочитать");
        bStart.setBounds(0,600,200,100);
        bStart.addActionListener(this);

        bAll = new JButton("+");
        bAll.setBounds(200,0,125,50);
        bAll.addActionListener(this);

        bClear = new JButton("-");
        bClear.setBounds(325,0,125,50);
        bClear.addActionListener(this);

        box = Box.createVerticalBox();

        jScrollPane = new JScrollPane(box);
        jScrollPane.setBounds(200, 50, 250, 650);
        add(jScrollPane);

        setLayout(null);
        add(bStart);
        add(bAll);
        add(bClear);
        setVisible(true);
    }
    private void setSelected(boolean state){
        int size = box.getComponentCount();
        for(int i = 0; i < size; ++i){
            JCheckBox checkBoxes = (JCheckBox) box.getComponent(i);
            checkBoxes.setSelected(state);
        }
    }

    private void printToFile(){
        String resultFilePath = Config.RezFilePath;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        resultFilePath += dtf.format(now) + ".txt";
        List<String> filter = getSelected();
        app.printToFile(filter, resultFilePath, true);
    }

    public void fillParams(List<String> params){
        for(String str : params){
            JCheckBox jCheckBox = new JCheckBox(str);
            box.add(jCheckBox);
        };
        jScrollPane.updateUI();
    }

    private List<String> getSelected(){
        List<String> result = new ArrayList<>();

        int size = box.getComponentCount();
        for(int i = 0; i < size; ++i){
            JCheckBox checkBox = (JCheckBox) box.getComponent(i);
            if(checkBox.isSelected()){
                result.add(checkBox.getText());
            }
        }
        return result;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == bAll){
            setSelected(true);
        } else if (e.getSource() == bClear) {
            setSelected(false);
        } else if (e.getSource() == bStart){
            if(isReadyForPrint){
                printToFile();
            }else {
                app.run();
                fillParams(app.getParamsInside());
                bStart.setText("Печать");
                isReadyForPrint = true;
            }

        }
    }
}
