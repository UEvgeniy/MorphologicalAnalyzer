package rule_applicability_reg;

import datamodel.MorphemedWord;

import java.util.Collection;

/**
 * An interface for making decision about the applicability of the rule for the word
 */
interface IRegressionApplicability {

    /**
     * For word with extracted morphemes define the applicability of the rule.
     * @param word word with extracted morphemes.
     * @return True, if it is possible to apply rule to word. False, if not.
     */
    double isApplicable(MorphemedWord word);

    void train(Collection<String> features, Boolean category);
}
