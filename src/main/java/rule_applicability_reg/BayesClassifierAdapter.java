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
class BayesClassifierAdapter implements IClassifier, Serializable {

    private static final long serialVersionUID = 6152194846914216600L;
    private final BayesClassifier<String, Boolean> bayes;
    private double lower_bound;

    BayesClassifierAdapter(){
        bayes = new BayesClassifier<>();
        lower_bound = 0;
    }

    @Override
    public void setLowerBound(double lower_bound) {
        this.lower_bound = lower_bound;
    }



    @Override
    public boolean isApplicable(MorphemedWord word) {
        return getProbability(word) > lower_bound;
    }

    public void train(Collection<String> features, Boolean category){
        bayes.learn(category, features);
    }

    @Override
    public double getProbability(MorphemedWord word) {

        SortedSet<Classification<String, Boolean>> result =
                bayes.classifyDetailed(NGrams.get(word.getRoot(), 2));

        if (result == null)
            return 0;

        Classification<String, Boolean> positive =
                result.first().getCategory() ? result.first() : result.last();

        return positive.getProbability();
    }


}
