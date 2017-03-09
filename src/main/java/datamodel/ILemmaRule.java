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
     * Method applies the rule and form word's lemma.
     * @param word  with extracted morphemes.
     * @return Word's lemma.
     */
    MorphemedWord formLemma(MorphemedWord word);

    /**
     * Method trasforms word using current lemma rule
     * @return IWord with word's form, its lemma and morph properties
     */
    IWord apply(MorphemedWord word);

    /**
     * @return Morphological properties of this lemma rule.
     */
    IMorphProperties getMorphProperties();
}
