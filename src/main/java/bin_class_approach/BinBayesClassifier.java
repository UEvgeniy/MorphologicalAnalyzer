package bin_class_approach;

import bin_class_approach.naive_bayes.BayesClassifier;
import bin_class_approach.naive_bayes.Classification;
import datamodel.ILemmaRule;
import datamodel.LemmaRule;
import datamodel.MorphemedWord;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * Applicability of rules is based on naive bayes classification
 */
class BinBayesClassifier implements IClassifierApplicability, Serializable {

    private static final long serialVersionUID = -6460556060858602712L;
    private final
    Map<String,
            Map<ILemmaRule,
                    BayesClassifier<String, Boolean>
                    >
            >
            classifiers;


    BinBayesClassifier(Map<String,
            Map<ILemmaRule,
                    BayesClassifier<String, Boolean>
                    >
            > classifiers) {

        this.classifiers = Objects.requireNonNull(classifiers);
    }



    @Override
    public boolean isApplicable(MorphemedWord word, ILemmaRule rule) {
        if (word.getMorphemes().size() == 1){
            throw new IllegalArgumentException
                    ("Word must have at least two morphemes.");
        }

        if (!rule.isApplicable(word)){
            return false;
        }

        if (!classifiers.containsKey(word.getEnding())){
            return false;
        }

        if (!classifiers.get(word.getEnding()).containsKey(rule)){
            return false;
        }

        Classification<String, Boolean> result = classifiers
                .get(word.getEnding())
                .get(rule).classify(NGrams
                        .get(word.getRoot(), 2));

        return result.getCategory();
    }
}
