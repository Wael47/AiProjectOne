package build4GramLanguageModel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Build4GramLanguageModel {

    public static final int NGRAM = 4;

    public static HashMap<String, LanguageModel> buildLanguageModel(String corpusFile) {

        StringBuilder corpus = readFile(corpusFile);


        ArrayList<String> tokensList = tokenize(corpus);

        HashMap<String, LanguageModel> languageModel = n_gram(tokensList);


        languageModel.forEach((k,v)->{
            calculateProbability(k,languageModel);
        });
        return languageModel;

/*        languageModel.forEach((k,v)->{
            System.out.println(k + "," + v.toString());
        });*/

        /*printLanguageModelToCSVFile(languageModel);

        System.out.println("Language Model is Complete");*/

    }
    private static StringBuilder readFile(String fileName){
        Path path = Paths.get(fileName);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(path)) {

            String line;
            while ((line = reader.readLine()) != null) {

                line = line.replaceAll("[a-zA-Z1-9\\d`~@#$%^&?*()/|+=;'\",\\[\\]<>{}»“”…‘’]", "");
                line = line.replaceAll("[a-zA-Z0-9]","");
                line = line.replaceAll("[؟!-_؛،:.]", " ");
                line = line.replaceAll("\\s+", " ");
                line = line.replaceAll("\\n+"," ");
                stringBuilder.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder;
    }

    public static HashMap<String, LanguageModel> readLanguageModelFile(String LanguageModelFilePath) {
        Path path = Paths.get(LanguageModelFilePath);
        HashMap<String, LanguageModel> languageModel = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] model = line.split(",");
                String token = model[0].trim();
                int gram = Integer.parseInt(model[1].trim());
                int freq = Integer.parseInt(model[2].trim());
                float prob = Float.parseFloat(model[3].trim());
                languageModel.put(token, new LanguageModel(gram, freq, prob));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return languageModel;
    }

    private static ArrayList<String> tokenize(StringBuilder corpus){

        return new ArrayList<>(Arrays.asList(corpus.toString().trim().split(" ")));
    }

    private static HashMap<String, LanguageModel> n_gram(ArrayList<String> tokensList){
        HashMap<String, LanguageModel> languageModel = new HashMap<>();

        for (int i = 0; i < tokensList.size(); i++) {
            for (int gram = 1; gram <= NGRAM; gram++) {
                StringBuilder stringBuilder = new StringBuilder();
                if (i+gram <= tokensList.size()) {
                    for (int j = 0; j < gram; j++) {
                        stringBuilder.append(" ").append(tokensList.get(i + j));
                    }
                }else {
                    break;
                }
                String token = stringBuilder.toString().trim();
                if (!languageModel.containsKey(token)) {
                    languageModel.put(token, new LanguageModel(gram));
                }else {
                    languageModel.get(token).increaseFrequency();
                }
            }
        }

        return  languageModel;
    }

    private static void calculateProbability(String token, HashMap<String, LanguageModel> model){
        if (model.get(token).getGram() == 1){
            model.get(token).setProbability(1);
        } else if (model.get(token).getGram() > 1) {
            int aB = model.get(token).getFrequency();
            String tokenB = token.substring(0,token.lastIndexOf(" "));
            int b = model.get(tokenB).getFrequency();
            model.get(token).setProbability((float) aB/b);
        }
    }

    public static void printLanguageModelToCSVFile(HashMap<String, LanguageModel> model , String filePath) {
        try {
            File file = new File(filePath+".csv");
            PrintWriter writer = new PrintWriter(file);
            model.forEach((k, v) -> writer.println(k + "," + v.getGram() + "," + v.getFrequency() + "," + v.getProbability()));
            writer.close();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

}