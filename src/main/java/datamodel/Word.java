package datamodel;


public class Word implements IWord {

    private String word, lemma, propeties;

    public Word(String word, String lemma, String properties){
        this.word = word;
        this.lemma = lemma;
        this.propeties = properties;
    }


    @Override
    public String getLemma() {
        return lemma;
    }

    @Override
    public String getProperties() {
        return propeties;
    }

    @Override
    public String toString() {
        return word;
    }
}
