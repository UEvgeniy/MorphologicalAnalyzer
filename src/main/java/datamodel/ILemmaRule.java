package datamodel;

import java.util.Collection;

/**
 * Interface for rules of converting word
 */
public interface ILemmaRule {
    Boolean isApplicable(MorphemedWord word);
    MorphemedWord apply(MorphemedWord word);
    Collection<IMorphProperties> getMorphProperties();
}
