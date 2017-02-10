package bin_class_approach;

import analyzers.IMorphAnalyzer;
import bin_class_approach.naive_bayes.BayesClassifier;
import datamodel.IWord;
import factories.IDatasetParser;
import factories.IMorphAnalyzerFactory;
import helpers.SuffixesHelper;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * My own developed approach. The main idea -
 * collecting rules of transformation from word to its lemma
 * and training naive bayes classifier.
 */
public class RulesApplicabilityFactory implements IMorphAnalyzerFactory{

    private final IDatasetParser parser;

    public RulesApplicabilityFactory(IDatasetParser parser){
        this.parser = Objects.requireNonNull(parser, "Parser cannot be null.");
    }

    @Override
    public IMorphAnalyzer create() {

        Set<IWord> words = parser.getDictionary();

        BayesClassifier<String, IApplyRule> bayes = new BayesClassifier<>();

        for (IWord word : words){

            String w = word.getWord();

            // Form the rule of transformation word to it lemma...
            short commonLen =
                    SuffixesHelper.getCommonPrefixLength(
                            word.getLemma(),
                            w);

            String add = word.getLemma().substring(commonLen);
            String remove = w.substring(commonLen);
            String props = word.getProperties();


            if (!add.isEmpty() || !remove.isEmpty()) {
                IApplyRule rule = new ApplyRule(remove, add, props);

                // Learn the bayes classifier
                bayes.learn(rule, Bigrams.get(w));
            }
        }

        return new RulesApplicabilityTrainer(bayes);
    }
}
