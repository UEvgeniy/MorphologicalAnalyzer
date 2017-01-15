package analyzers;

import datamodel.IWord;
import datamodel.Word;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Model of predicting words POS + properties
 */
public class MorphemeBasedMorphAnalyzer implements IMorphAnalyzer
{

    private MorphemeExtractor morphemeExtractor;
    private IPropertyPredictor propertyPredictor;

    public MorphemeBasedMorphAnalyzer(MorphemeExtractor me, IPropertyPredictor pp){
        morphemeExtractor = me;
        propertyPredictor = pp;
    }

    public Collection<IWord> analyze(String word) {
        // todo implement MorphemeBasedMorphAnalyzer
        List<IWord> result = new ArrayList<>();
        result.add(new Word(word, word, "PoS + some properties"));

        return result;
    }

    public Boolean canHandle(String word) {
        return true;
    }
}
