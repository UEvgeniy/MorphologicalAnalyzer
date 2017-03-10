package rule_applicability_reg;

import java.util.Map;

import datamodel.MorphemedWord;

/**
 * An interface for making decision about the applicability of the rule for the word
 */
interface IClassifier {

    /**
     * For word with extracted morphemes define the applicability of the rule.
     * @param word word with extracted morphemes.
     * @return True, if it is possible to apply rule to word. False, if not.
     */
    boolean isApplicable(MorphemedWord word);

    /**
     * Train the classifier
     * @param features Features of the object
     * @param category Class of the object
     */
    void train(Map<String,Boolean> dataset);

    /**
     * Set bound between good and bad probability
     * @param lowerBound probability
     */
    void setLowerBound(double lowerBound);

    /**
     * Count the probability of applicability rule to the word
     * @param word word with extracted morphemes
     * @return double probability from 0 to 1
     */
    double getProbability(MorphemedWord word);
}
