package datamodel;

/**
 * Interface for rules of converting word to their initial forms (lemmas)
 */
public interface ILemmaRule {

    /**
     * Method checks whether word it is possible to apply the rule for the word.
     * @param word Word with extracted morphemes.
     * @return True, if rule is applicable for the word. False, if not.
     */
    Boolean isApplicable(MorphemedWord word);

    /**
     * Method applies the rule for the word.
     * @param word Word with extracted morphemes.
     * @return Word's lemma.
     */
    MorphemedWord apply(MorphemedWord word);

    /**
     * @return Morphological properties of this lemma rule.
     */
    String getMorphProperties();
}
