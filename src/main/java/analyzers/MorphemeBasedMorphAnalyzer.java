package analyzers;

import java.util.ArrayList;
import java.util.Collection;

import datamodel.IWord;
import datamodel.MorphemedWord;

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

        // Firstly, try to find some morphemes in the word...
        Collection<MorphemedWord> morphemedWords = morphemeExtractor.extract(word);

        Collection<IWord> result = new ArrayList<>();

        // Using extracted morphemes try to define Properties
        for (MorphemedWord mWord : morphemedWords) {

            // For each variant of morpheme extraction try to define properties
           result.addAll(propertyPredictor.predict(mWord));

        }

        return result;
    }

    public Boolean canHandle(String word) {
        return analyze(word).size() > 0;
    }
}
