package datamodel;

/**
 * Interface for rules of converting word
 */
public interface ILemmaRule {
    Boolean isApplicable(MorphemedWord word);
    MorphemedWord apply(MorphemedWord word);
    String getMorphProperties();
}
