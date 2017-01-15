package factories;

import analyzers.IPropertyPredictor;
import datamodel.ILemmaRule;
import datamodel.MorphemedWord;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Factory for classes which predict PoS + properties for morphemed word
 */
public class LemmaRulePropertyPredictorTrainer implements IPropertyPredictorFactory {

    @Override
    public IPropertyPredictor create() {
        // todo implement LemmaRulePropertyPredictorTrainer
        return new IPropertyPredictor() {
            @Override
            public Collection<ILemmaRule> predict(MorphemedWord word) {
                return new ArrayList<>();
            }
        };
    }
}
