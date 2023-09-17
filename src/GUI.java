import Telemetry.App;
import Telemetry.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class GUI extends JFrame implements ActionListener, ItemListener {
    Box box;
    JButton bStart;
    JButton bAll;
    JButton bClear;
    JToggleButton tbSortingTiming;
    JToggleButton tbSortingAlphabet;
    JScrollPane jScrollPane;
    JLabel labelStat;
    JLabel labelFileName;
    boolean isSortingAlphabet = true;

    App app;
    boolean isReadyForPrint = false;

    
    public GUI(App _app){
        app = _app;

        setTitle("Телеметрия КАНОПУС-В");
        setIconImage(new ImageIcon("Data/source/Kanopus-V.jpg").getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(463,735);
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

        tbSortingTiming = new JToggleButton("Время");
        tbSortingTiming.setBounds(0,550,100,50);
        tbSortingTiming.setSelected(!isSortingAlphabet);
        tbSortingTiming.addItemListener(this);

        tbSortingAlphabet = new JToggleButton("Алфавит");
        tbSortingAlphabet.setBounds(100,550,100,50);
        tbSortingAlphabet.setSelected(isSortingAlphabet);
        tbSortingAlphabet.addItemListener(this);

        labelStat = new JLabel();
        labelStat.setBounds(0,0,200, 80);

        String[] buff = Config.KNPFilePath.split("/");
        String fileName = buff[buff.length-1];
        labelFileName = new JLabel(fileName);
        labelFileName.setBounds(0, 500, 200, 50);

        box = Box.createVerticalBox();

        jScrollPane = new JScrollPane(box);
        jScrollPane.setBounds(200, 50, 250, 650);
        add(jScrollPane);

        setLayout(null);
        add(tbSortingTiming);
        add(tbSortingAlphabet);
        add(bStart);
        add(bAll);
        add(bClear);
        add(labelStat);
        add(labelFileName);
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
        app.printToFile(filter, resultFilePath, isSortingAlphabet);
    }

    public void fillParams(List<String> params){
        for(String str : params){
            String[] strNameDisc = str.split("@");
            JCheckBox jCheckBox = new JCheckBox(strNameDisc[0]);
            if(strNameDisc.length > 1) {
                System.out.println(strNameDisc[0]);
                jCheckBox.setToolTipText(strNameDisc[1]);
            }
            box.add(jCheckBox);
        };
        jScrollPane.updateUI();
        setStat(app.getStatTech(), app.getStatData(), params.size() - 1);
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
    public void setStat(int tech, int data, int params){
        labelStat.setText(conToMultiline(String.format("СТАТИСТИКА:\n" +
                        "Параметров: %d\n" +
                        "Всего записей: %d\n" +
                        "Полезных: %d\n" +
                        "Служебных: %d",
                        params, tech+data, data, tech)));
        labelStat.setVisible(true);
    }
    private String conToMultiline(String orig){
        return "<html>" + orig.replaceAll("\n", "<br>");
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
                try {
                    Desktop.getDesktop().open(new File(Config.RezFilePath));
                }catch (Exception exception){ exception.printStackTrace();}
            }else {
                app.run();
                fillParams(app.getParamsInside());
                bStart.setText("Печать");
                isReadyForPrint = true;
            }

        }
    }
    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getSource() == tbSortingTiming){
            isSortingAlphabet = !tbSortingTiming.isSelected();
            tbSortingAlphabet.setSelected(!tbSortingTiming.isSelected());
        } else if (e.getSource() == tbSortingAlphabet) {
            isSortingAlphabet = tbSortingAlphabet.isSelected();
            tbSortingTiming.setSelected(!tbSortingAlphabet.isSelected());
        }
    }
}
