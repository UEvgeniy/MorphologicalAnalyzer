package analyzers;

import datamodel.IWord;
import datamodel.MorphemedWord;

import java.util.Collection;

/**
 * Class is able to define PoS + properties for morphemed words
 */
public interface IPropertyPredictor {
    /**
     * Method analyzes the word with extracted morphemes
     * @param word Word with extracted morphemes
     * @return Collection of possible properties of the word
     */
    Collection<IWord> predict(MorphemedWord word);
}
