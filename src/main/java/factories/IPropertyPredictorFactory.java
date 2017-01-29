package factories;

import analyzers.IPropertyPredictor;

/**
 * An interface for classes that construct instances of classes implementing IPropertyPredictor
 */
public interface IPropertyPredictorFactory {

    /**
     * Method constructs property predictor
     * @return Predictor of properties for words with extracted morphemes
     */
    IPropertyPredictor create();
}
