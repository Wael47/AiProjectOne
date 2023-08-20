package plagiarismDetection;

public class LanguageModel {
    private int gram;
    private int frequency = 1;
    private float probability;

    public LanguageModel(){

    }
    public LanguageModel(int gram, int frequency, float probability) {
        this.gram = gram;
        this.frequency = frequency;
        this.probability = probability;
    }

    public int getGram() {
        return gram;
    }

    public void setGram(int gram) {
        this.gram = gram;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public float getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }

    @Override
    public String toString() {
        return this.gram + "," + this.frequency + "," + this.probability;
    }
}
