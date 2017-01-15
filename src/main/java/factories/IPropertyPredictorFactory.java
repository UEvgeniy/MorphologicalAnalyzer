package factories;

import analyzers.IPropertyPredictor;

/**
 * An interface for classes that construct instances of classes implementing IPropertyPredictor
 */
public interface IPropertyPredictorFactory {
    IPropertyPredictor create();
}
