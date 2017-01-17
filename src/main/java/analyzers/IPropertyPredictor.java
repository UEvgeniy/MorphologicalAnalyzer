package analyzers;

import datamodel.ILemmaRule;
import datamodel.IWord;
import datamodel.MorphemedWord;

import java.util.Collection;

/**
 * Class is able to define PoS + properties for morphemed words
 */
public interface IPropertyPredictor {
    Collection<ILemmaRule> predict(MorphemedWord word);
    IWord apply(MorphemedWord mWord, ILemmaRule rule);
}
