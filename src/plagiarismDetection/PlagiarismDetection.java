package plagiarismDetection;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;


public class PlagiarismDetection {

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

    public static int calculatePlagiarismRate(HashMap<String,LanguageModel> languageModel, String inputString){
        String[] inputStringWords = inputString.split(" ");

        boolean[] wordFound = new boolean[inputStringWords.length];

        for (int gram = 4; gram > 1; gram--) {
            for (int i = 0; i < inputStringWords.length - (gram - 1); i++) {

                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < gram; j++) {
                    stringBuilder.append(inputStringWords[i + j]).append(" ");
                }
                String word = stringBuilder.toString().trim();

                String[] inputWords = word.split(" ");
                if (languageModel.get(word) != null) {
                    for (int j = 0; j < inputWords.length; j++) {
                        wordFound[i + j] = true;
                    }
                    continue;
                }

                int finalI = i;
                int finalGram = gram;
                languageModel.forEach((k, v) -> {
                    if (v.getGram() == 4 && finalGram == 4) {
                        String []tempString = k.split(" ");
                        String word1 = tempString[0];
                        String word2 = tempString[1];
                        String word3 = tempString[2];
                        String word4 = tempString[3];
                        ///word1 word2 X word4
                        if (word1.equals(inputWords[0]) && word2.equals(inputWords[1]) && word4.equals(inputWords[3])) {
                            wordFound[finalI] = true;
                            wordFound[finalI + 1] = true;
                            wordFound[finalI + 3] = true;
                        }
                        ///word1 X word3 word4
                        else if (word1.equals(inputWords[0]) && word3.equals(inputWords[2]) && word4.equals(inputWords[3])) {
                            wordFound[finalI] = true;
                            wordFound[finalI + 2] = true;
                            wordFound[finalI + 3] = true;
                        }
                    }
                    if (v.getGram() == 3 && finalGram == 3) {
                        ///word1 X word3
                        String []tempString = k.split(" ");
                        String word1 = tempString[0];
                        String word3 = tempString[2];
                        if (word1.equals(inputWords[0]) && word3.equals(inputWords[2])) {
                            wordFound[finalI] = true;
                            wordFound[finalI + 2] = true;
                        }
                    }
                });
            }
        }

        int count = 0;
        for (boolean num : wordFound) {
            if (num) count++;
        }
        return Math.round(((float) count / wordFound.length) * 100);
    }
}
