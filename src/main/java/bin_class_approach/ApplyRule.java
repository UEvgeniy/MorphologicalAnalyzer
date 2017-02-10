package bin_class_approach;

import datamodel.IWord;
import datamodel.Word;

import java.util.Objects;

/**
 * Applicability of rules is based on naive bayes classification
 */
public class ApplyRule implements IApplyRule {

    private final String remove;
    private final String add;
    private final String properties;

    ApplyRule(String remove, String add, String properties){
        this.remove = Objects.requireNonNull(remove);
        this.add = Objects.requireNonNull(add);
        this.properties = Objects.requireNonNull(properties);
    }

    @Override
    public boolean isApplicable(String word) {
        return word.endsWith(remove);
    }

    @Override
    public IWord apply(String word) {
        String lemma = word.substring(0, word.length() - remove.length()).concat(add);
        return new Word(word, lemma, properties);
    }

    @Override
    public String getMorphProperties() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplyRule that = (ApplyRule) o;
        return Objects.equals(remove, that.remove) &&
                Objects.equals(add, that.add) &&
                Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(remove, add, properties);
    }

    @Override
    public String toString() {
        return "[-" + remove + ", +" + add + ", props: " + properties + "]";
    }
}
