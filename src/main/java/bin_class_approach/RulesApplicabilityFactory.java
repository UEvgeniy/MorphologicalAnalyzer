package bin_class_approach;

import analyzers.IMorphAnalyzer;
import datamodel.IDataset;
import datamodel.IWord;
import factories.IDatasetParser;
import factories.IMorphAnalyzerFactory;
import helpers.DatasetConverter;
import helpers.SuffixesHelper;
import java.util.Objects;
import java.util.Set;

/**
 * My own developed approach. The main idea -
 * collecting rules of transformation from word to its lemma
 * and training binary naive bayes classifier.
 */
public class RulesApplicabilityFactory implements IMorphAnalyzerFactory{

    private final IDataset dataset;

    public RulesApplicabilityFactory(IDataset dictionary){
        this.dataset = Objects.requireNonNull(dictionary, "Dataset cannot be null.");

    }

    @Override
    public IMorphAnalyzer create() {

        Set<IWord> words = dataset.get();

        IClassifierApplicabilityFactory factory =
                new BinBayesClassifierFactory(words);

        return new RulesApplicabilityAnalyzer(
                factory.create(),
                DatasetConverter.formRules(words),
                new MorphemeExtractor(DatasetConverter.formMorphemes(words))
        );
    }
}
