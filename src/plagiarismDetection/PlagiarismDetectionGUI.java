package plagiarismDetection;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

public class PlagiarismDetectionGUI {

    private static JFrame frame;
    private HashMap<String, LanguageModel> languageModel;
    private JTextArea inputStringTextArea;
    private JPanel mainPanel;
    private JButton languageModelSelectButton;
    private JLabel languageModelFileNameLabel;
    private JButton calculatePlagiarismButton;
    private JLabel plagiarismRateLabel;

    public PlagiarismDetectionGUI() {

        inputStringTextArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        languageModelSelectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileFilter(new FileNameExtensionFilter(".csv", "csv"));
                int is = jFileChooser.showOpenDialog(frame);

                if (is == JFileChooser.APPROVE_OPTION) {
                    File file = jFileChooser.getSelectedFile();
                    String filePath = file.getPath();
                    languageModelFileNameLabel.setText(filePath);
                    languageModel = PlagiarismDetection.readLanguageModelFile(filePath);
                }
            }
        });
        calculatePlagiarismButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (languageModel != null) {
                    String inputString = inputStringTextArea.getText().trim();
                    inputString = inputString.replaceAll("[a-zA-Z1-9\\d`~@#$%^&?*()/|+=;'\",\\[\\]<>{}»“”…‘’]", "");
                    inputString = inputString.replaceAll("[a-zA-Z0-9]","");
                    inputString = inputString.replaceAll("[؟!-_؛،:.]", " ");
                    inputString = inputString.replaceAll("\\s+", " ");
                    inputString = inputString.replaceAll("\\n+"," ");
                    int plagiarismRate = PlagiarismDetection.calculatePlagiarismRate(languageModel,inputString);
                    plagiarismRateLabel.setText(""+ plagiarismRate + "%");
                }
            }
        });
    }

    public static void main(String[] args) {
        frame = new JFrame("Plagiarism Detection");
        frame.setContentPane(new PlagiarismDetectionGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
