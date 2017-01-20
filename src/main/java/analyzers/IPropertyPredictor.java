package analyzers;

import java.util.Collection;

import datamodel.IWord;
import datamodel.MorphemedWord;

/**
 * Class is able to define PoS + properties for morphemed words
 */
public interface IPropertyPredictor {
    Collection<IWord> predict(MorphemedWord word);
}
