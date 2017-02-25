package baseline;

import analyzers.IMorphAnalyzer;
import datamodel.IDataset;
import datamodel.IWord;
import factories.IDatasetParser;
import factories.IMorphAnalyzerFactory;
import factories.IPropertyPredictorFactory;

import java.util.Set;

/**
 * Class uses machine learning for predicting POS + properties of the word
 */
public class MorphAnalyzerTrainer implements IMorphAnalyzerFactory {

    private final IDatasetParser parser;

    public MorphAnalyzerTrainer(IDatasetParser parser){
    	this.parser = parser;
    }

    @Override
    public IMorphAnalyzer create() {

        IDataset words = parser.getDataset();

        IPropertyPredictorFactory propertyPredictorFactory =
                new TrivialLemmaRulePropertyPredictorTrainer(words.get());

        MorphemeExtractorTrainer morphemeExtractorTrainer = new MorphemeExtractorTrainer(words.get());

        return new MorphemeBasedMorphAnalyzer(
                morphemeExtractorTrainer.train(),
                propertyPredictorFactory.create()
        );
    }
}
