package rule_applicability_reg;

import datamodel.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Extended class that use classifier classifier to define applicability
 */
class ExtendedLemmaRule implements Serializable {

    private static final long serialVersionUID = -1952315395633816558L;
    private final String removed;
    private final String added;
    private final String properties;
    private final IRegressionApplicability classifier;



    ExtendedLemmaRule(String remove, String add, String properties,
                             IRegressionApplicability classifier) {
        this.removed = Objects.requireNonNull(remove);
        this.added = Objects.requireNonNull(add);
        this.properties = Objects.requireNonNull(properties);
        this.classifier = Objects.requireNonNull(classifier);

    }


    void train(MorphemedWord mWord, String property){


        // If the basic applicability rule doesn't fit the word,
        //      then classifier classifier would not trained.
        if (!mWord.getEnding().equals(removed))
            return;

        classifier.train(NGrams.get(mWord.getRoot(), 2),
                property.equals(this.properties));
    }


    double getProbability(MorphemedWord word){
        if (!word.getEnding().equals(removed)) {
            return 0;
        }

        return classifier.isApplicable(word);

    }


    IWord apply(String word) {

        // Transform word to its lemma
        String lemma = word.substring(0, word.length() - removed.length()).concat(added);

        return new Word(word, lemma, properties);
    }


    String getMorphProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExtendedLemmaRule)) return false;

        ExtendedLemmaRule lemmaRule = (ExtendedLemmaRule) o;

        if (removed != null ? !removed.equals(lemmaRule.removed) : lemmaRule.removed != null) return false;
        if (added != null ? !added.equals(lemmaRule.added) : lemmaRule.added != null) return false;
        return properties != null ? properties.equals(lemmaRule.properties) : lemmaRule.properties == null;

    }

    @Override
    public int hashCode() {
        return Objects.hash(removed, added, properties);
    }
}
