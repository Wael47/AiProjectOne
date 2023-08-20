package build4GramLanguageModel;

public class LanguageModel {
    private int gram ;
    private int frequency = 1;
    private float probability;

    public LanguageModel(int gram) {
        this.gram = gram;
        this.frequency = 1;
    }

    public LanguageModel(int gram, int frequency, float probability) {
        this.gram = gram;
        this.frequency = frequency;
        this.probability = probability;
    }

    public void increaseFrequency(){
        this.frequency ++;
    }

    public int getGram() {
        return gram;
    }

    public int getFrequency() {
        return frequency;
    }

    public float getProbability() {
        return probability;
    }

    public void setProbability(float probability) {
        this.probability = probability;
    }

    @Override
    public String toString() {
        return this.gram + " " + this.frequency + " " + this.probability;
    }
}
