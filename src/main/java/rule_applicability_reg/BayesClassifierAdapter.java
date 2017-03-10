package rule_applicability_reg;

import com.sun.org.apache.xpath.internal.operations.Bool;
import datamodel.IWord;
import datamodel.MorphemedWord;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;
import rule_applicability_reg.naive_bayes.BayesClassifier;
import rule_applicability_reg.naive_bayes.Classification;

import java.io.Serializable;
import java.util.*;

import net.sf.javaml.classification.bayes.*;
import net.sf.javaml.classification.Classifier;
/**
 * Bayes classifier
 */
class BayesClassifierAdapter implements IClassifier, Serializable {

    private static final long serialVersionUID = 6152194846914216600L;
    //private final BayesClassifier<String, Boolean> bayes;
    private final NaiveBayesClassifier newBayes;

    private double lower_bound;
    private final int ngrams;

    BayesClassifierAdapter(int NGrams){
        //bayes = new BayesClassifier<>();
        newBayes = new NaiveBayesClassifier(false, true, true);
        lower_bound = 0;
        this.ngrams = NGrams;
    }

    @Override
    public void setLowerBound(double lower_bound) {
        this.lower_bound = lower_bound;
    }



    @Override
    public boolean isApplicable(MorphemedWord word) {
        return getProbability(word) > lower_bound;
    }

    public void train(Map<String, Boolean> dataset){
        Set<Instance> instances = new HashSet<>();

        for (Map.Entry<String, Boolean> entry : dataset.entrySet()){

            Instance i = new SparseInstance();

            i.putAll(NGrams.get(entry.getKey(), ngrams));
            i.setClassValue(entry.getValue().toString());

            instances.add(i);
        }

        newBayes.buildClassifier(new DefaultDataset(instances));
        //bayes.learn(category, features);
    }

    @Override
    public double getProbability(MorphemedWord word) {


        Instance instance = new SparseInstance();
        instance.putAll(NGrams.get(word.getRoot(), ngrams));

        return newBayes.classDistribution(instance).get("true");


        /*if (result == null)
            return 0;

        Classification<String, Boolean> positive =
                result.first().getCategory() ? result.first() : result.last();

        return positive.getProbability();
        */
    }


}
