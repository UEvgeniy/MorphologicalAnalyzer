package baseline;

import analyzers.IMorphAnalyzer;
import analyzers.IPropertyPredictor;
import datamodel.IWord;
import datamodel.MorphemedWord;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Model of predicting words POS + properties
 */
class MorphemeBasedMorphAnalyzer implements IMorphAnalyzer, Serializable {

    private static final long serialVersionUID = 5069847036891425458L;
    private MorphemeExtractor morphemeExtractor;
    private IPropertyPredictor propertyPredictor;

    MorphemeBasedMorphAnalyzer(MorphemeExtractor me, IPropertyPredictor pp){
        String msg = " cannot be null.";
        morphemeExtractor = Objects.requireNonNull(me, "Morpheme extractor".concat(msg));
        propertyPredictor = Objects.requireNonNull(pp, "Property predictor".concat(msg));
    }

    @Override
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

    @Override
    public Boolean canHandle(String word) {
        return analyze(word).size() > 0;
    }
}
