package factories;

import java.util.Set;

import analyzers.IMorphAnalyzer;
import analyzers.MorphemeBasedMorphAnalyzer;
import datamodel.IWord;

/**
 * Class uses machine learning for predicting POS + properties of the word
 */
public class MorphAnalyzerTrainer implements IMorphAnalyzerFactory{

    private final IDatasetParser parser;

    public MorphAnalyzerTrainer(IDatasetParser parser){
    	this.parser = parser;
    }
    
    public IMorphAnalyzer create() {

        Set<IWord> words = parser.getDictionary();

        IPropertyPredictorFactory propertyPredictorFactory =
                new TrivialLemmaRulePropertyPredictorTrainer(words);

        MorphemeExtractorTrainer morphemeExtractorTrainer = new MorphemeExtractorTrainer(words);

        return new MorphemeBasedMorphAnalyzer(
                morphemeExtractorTrainer.train(),
                propertyPredictorFactory.create()
        );
    }
}
