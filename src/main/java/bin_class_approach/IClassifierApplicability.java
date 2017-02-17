package bin_class_approach;

import datamodel.ILemmaRule;
import datamodel.LemmaRule;
import datamodel.MorphemedWord;

/**
 * An interface for making decision about the applicability of the rule for the word
 */
interface IClassifierApplicability {

    /**
     * For word with extracted morphemes define the applicability of the rule.
     * @param word word with extracted morphemes.
     * @param rule rule that may be applied to the word.
     * @return True, if it is possible to apply rule to word. False, if not.
     */
    boolean isApplicable(MorphemedWord word, ILemmaRule rule);
}
