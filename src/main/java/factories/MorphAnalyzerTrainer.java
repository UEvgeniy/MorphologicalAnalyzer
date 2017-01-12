package factories;

import analyzers.IMorphAnalyzer;

/**
 * Class uses machine learning for predicting POS + properties of the word
 */
public class MorphAnalyzerTrainer implements IMorphAnalyzerFactory{

    public IMorphAnalyzer create() {
        // todo use MorphemeExtractorTrainer and IPropertyPredictorTrainer
        return null;
    }
}
