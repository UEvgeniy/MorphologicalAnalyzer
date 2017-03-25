package datamodel;


import datamodel.properties.IMorphProperties;
import datamodel.properties.RusCorporaProps;

import java.io.Serializable;
import java.util.Objects;

public class Word implements IWord, Serializable {

    private static final long serialVersionUID = -2599795694043548965L;
    private final String word, lemma;
    private final IMorphProperties properties;

    public Word(String word, String lemma, IMorphProperties properties){
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
    public IMorphProperties getProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word)) return false;
        Word word1 = (Word) o;
        return Objects.equals(word, word1.word) &&
                Objects.equals(lemma, word1.lemma) &&
                Objects.equals(properties, word1.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, lemma, properties);
    }

    @Override
    public String toString() {
        return "Word: " + word +
                ". Lemma: " + lemma +
                ". Propreties: " + properties;
    }
}
