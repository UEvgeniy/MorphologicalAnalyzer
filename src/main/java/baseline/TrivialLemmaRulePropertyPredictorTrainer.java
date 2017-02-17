package baseline;

import analyzers.IPropertyPredictor;
import datamodel.*;
import factories.IPropertyPredictorFactory;
import helpers.DatasetConverter;
import helpers.SuffixesHelper;

import java.util.*;

/**
 * Factory for classes which predict PoS + properties for morphemed word
 */
public class TrivialLemmaRulePropertyPredictorTrainer implements IPropertyPredictorFactory {

    private Collection<IWord> words;

    TrivialLemmaRulePropertyPredictorTrainer(Collection<IWord> words){
        this.words = words;
    }


    @Override
    public IPropertyPredictor create() {

        Set<ILemmaRule> resultRules = DatasetConverter.formRules(words);

        return new TrivialRulePropertyPredictor(resultRules);

    }
}
