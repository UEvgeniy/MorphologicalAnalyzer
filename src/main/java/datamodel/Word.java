package datamodel;


import java.io.Serializable;
import java.util.Objects;

public class Word implements IWord, Serializable {

    private static final long serialVersionUID = -2599795694043548965L;
    private final String word, lemma, properties;

    public Word(String word, String lemma, String properties){
        this.word = word;
        this.lemma = lemma;
        this.properties = properties;
    }

    @Override
    public String getWord() {
        return word;
    }

    @Override
    public String getLemma() {
        return lemma;
    }

    @Override
    public String getProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word)) return false;

        Word word1 = (Word) o;

        if (word != null ? !word.equals(word1.word) : word1.word != null) return false;
        if (lemma != null ? !lemma.equals(word1.lemma) : word1.lemma != null) return false;
        return properties != null ? properties.equals(word1.properties) : word1.properties == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, lemma, properties);
    }
}
