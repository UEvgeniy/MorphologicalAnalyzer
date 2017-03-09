package rule_applicability_reg;

import datamodel.*;
import helpers.DatasetConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Extended class that use classifier classifier to define applicability
 */
class ExtendedLemmaRule implements ILemmaRule, Serializable {

    private static final long serialVersionUID = -1952315395633816558L;
    private final String removed;
    private final String added;
    private final IMorphProperties properties;
    private final IClassifier classifier;

    ExtendedLemmaRule(String remove, String add, IMorphProperties properties,
                      IClassifier classifier) {
        this.removed = Objects.requireNonNull(remove);
        this.added = Objects.requireNonNull(add);
        this.properties = Objects.requireNonNull(properties);
        this.classifier = Objects.requireNonNull(classifier);
    }

    @Override
    public Boolean isApplicable(MorphemedWord word) {
        return canApply(word) &&
                classifier.isApplicable(word);
    }

    @Override
    public IWord apply(MorphemedWord word) {
        return new Word(
                word.getWord(),
                formLemma(word).getWord(),
                getMorphProperties());
    }

    @Override
    public MorphemedWord formLemma(MorphemedWord word) {
        if (word.getMorphemes().size() != 2){
            throw new IllegalArgumentException("Incorrect number of morphemes");
        }
        List<IMorpheme> morphemes = new ArrayList<>();

        morphemes.add(new Morpheme(word.getRoot()));
        morphemes.add(new Morpheme(added));

        return new MorphemedWord(morphemes);
    }


    @Override
    public IMorphProperties getMorphProperties() {
        return properties;
    }


    /**
     * Training classifier with good & bad examples
     * @param mWord word with extracted morphemes
     * @param property morphological properies for the word
     */
    void train(MorphemedWord mWord, IMorphProperties property){

        // If the basic applicability rule doesn't fit the word,
        //      then classifier classifier would not trained.
        if (!mWord.getEnding().equals(removed)) {
            throw new IllegalArgumentException
                    ("Inappropriate word for train:" + mWord.getWord());
        }

        classifier.train(NGrams.get(mWord.getRoot(), 2),
                property.equals(this.properties));
    }

    /**
     * Set lower bound of probability
     * @param bound double probability from 0 to 1
     */
    public void setLowerBound(double bound){
        if (bound > 1 || bound < 0){
            throw new IllegalArgumentException("Probability cannot be out of [0;1]");
        }
        classifier.setLowerBound(bound);
    }

    /**
     * Count the probability of applicability rule to the word
     * @param word word with extracted morphemes
     * @return double probability from 0 to 1
     */
    double getProbability(MorphemedWord word){
        if (!word.getEnding().equals(removed)) {
            return 0;
        }
        return classifier.getProbability(word);
    }


    /**
     * Can word be applied to currect rule
     * @param word word with extracted morphemes
     * @return True, if word and rule has similar end. If not, False
     */
    boolean canApply(MorphemedWord word){
        return word.getEnding().equals(removed);
    }

    /**
     * Can word be applied to currect rule
     * @param word word with extracted morphemes
     * @return True, if word and rule has similar end. If not, False
     */
    boolean fullyApplicable(IWord word){
        MorphemedWord mWord = DatasetConverter.extractMorphemes(word);
        return canApply(mWord) &&
                apply(mWord).equals(word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(removed, added, properties);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExtendedLemmaRule)) return false;
        ExtendedLemmaRule that = (ExtendedLemmaRule) o;
        return Objects.equals(removed, that.removed) &&
                Objects.equals(added, that.added) &&
                Objects.equals(properties, that.properties);
    }
}
