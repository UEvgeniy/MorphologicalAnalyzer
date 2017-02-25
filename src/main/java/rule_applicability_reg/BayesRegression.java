package rule_applicability_reg;

import datamodel.MorphemedWord;
import rule_applicability_reg.naive_bayes.BayesClassifier;
import rule_applicability_reg.naive_bayes.Classification;

import java.io.Serializable;
import java.util.Collection;
import java.util.SortedSet;

/**
 * Bayes classifier
 */
class BayesRegression implements IRegressionApplicability, Serializable {

    private static final long serialVersionUID = 6152194846914216600L;
    private final BayesClassifier<String, Boolean> bayes;

    BayesRegression(){
        bayes = new BayesClassifier<>();
    }

    public void train(Collection<String> features, Boolean category){
        bayes.learn(category, features);
    }

    @Override
    public double isApplicable(MorphemedWord word) {
        SortedSet<Classification<String, Boolean>> result =
                bayes.classifyDetailed(NGrams.get(word.getRoot(), 2));

        if (result == null)
            return 0;

        Classification<String, Boolean> positive =
                result.first().getCategory() ? result.first() : result.last();

        return positive.getProbability();
    }
}
