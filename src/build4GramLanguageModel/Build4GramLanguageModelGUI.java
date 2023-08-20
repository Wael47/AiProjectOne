package build4GramLanguageModel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public class Build4GramLanguageModelGUI extends JFrame implements ActionListener {

    private HashMap<String, LanguageModel> languageModel;
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JTable languageModelTable;
    private JToolBar toolBar;

    private JMenuItem selectCorpusMenuItem;
    private JMenuItem openLanguageModelMenuItem;
    private JMenuItem saveLanguageModelMenuItem;
    public Build4GramLanguageModelGUI() {


        selectCorpusMenuItem = new JMenuItem("Select corpus");
        selectCorpusMenuItem.addActionListener(this);

        openLanguageModelMenuItem = new JMenuItem("Open Language Model");
        openLanguageModelMenuItem.addActionListener(this);

        saveLanguageModelMenuItem = new JMenuItem("Save");
        saveLanguageModelMenuItem.addActionListener(this);

        JMenu file = new JMenu("File");
        file.add(selectCorpusMenuItem);
        file.add(openLanguageModelMenuItem);
        file.add(saveLanguageModelMenuItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBounds(0,0,500,20);
        menuBar.add(file);

        toolBar.add(menuBar);


    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Build4GramLanguageModelGUI");
        frame.setContentPane(new Build4GramLanguageModelGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == selectCorpusMenuItem){
            JFileChooser jFileChooser = new JFileChooser();
            int is = jFileChooser.showOpenDialog(this);

            if (is == JFileChooser.APPROVE_OPTION) {
                File file = jFileChooser.getSelectedFile();
                String filePath = file.getPath();
                this.languageModel = Build4GramLanguageModel.buildLanguageModel(filePath);
                fillTable();
            }
        }
        if (e.getSource() == openLanguageModelMenuItem){
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileFilter(new FileNameExtensionFilter(".csv", "csv"));
            int is = jFileChooser.showOpenDialog(this);

            if (is == JFileChooser.APPROVE_OPTION) {
                File file = jFileChooser.getSelectedFile();
                String filePath = file.getPath();
                this.languageModel = Build4GramLanguageModel.readLanguageModelFile(filePath);
                fillTable();
            }
        }
        if (languageModel != null) {
            if (e.getSource() == saveLanguageModelMenuItem){
                JFileChooser jFileChooser = new JFileChooser();
                int is = jFileChooser.showSaveDialog(this);

                if (is == JFileChooser.APPROVE_OPTION) {
                    File file = jFileChooser.getSelectedFile();
                    String filePath = file.getPath();
                    //System.out.println(filePath);
                    Build4GramLanguageModel.printLanguageModelToCSVFile(languageModel,filePath);
                }
            }
        }

    }

    private void fillTable(){
        Object [] columns = {"Token","Gram","Frequency","Probability"};
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columns);
        languageModelTable.setModel(tableModel);
        this.languageModel.forEach((k,v)->{
            String token = k;
            int gram = v.getGram();
            int frequency = v.getFrequency();
            float probability = v.getProbability();

            Object[] row = {token,gram,frequency,probability};
            tableModel.addRow(row);
        });

    }
}
