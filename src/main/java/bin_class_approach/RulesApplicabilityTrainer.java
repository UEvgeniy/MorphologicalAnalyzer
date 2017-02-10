package bin_class_approach;

import analyzers.IMorphAnalyzer;
import bin_class_approach.naive_bayes.BayesClassifier;
import bin_class_approach.naive_bayes.Classification;
import datamodel.IWord;


import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


/**
 *
 */
public class RulesApplicabilityTrainer implements IMorphAnalyzer,Serializable {

    private static final long serialVersionUID = 7507623538075147573L;
    private final BayesClassifier<String, IApplyRule> classifier;

    RulesApplicabilityTrainer(BayesClassifier<String, IApplyRule> classifier){
        this.classifier = Objects.requireNonNull(classifier,
                "Classifier cannot be null");
    }

    @Override
    public Collection<IWord> analyze(String word) {
        Set<Classification<String,IApplyRule>> prediction =
                classifier.classifyDetailed(Bigrams.get(word));

        Collection<IWord> res = new ArrayList<>();

        List<Classification<String,IApplyRule>> sorted_list = prediction
                .stream()
                .filter((a) -> a.getCategory().isApplicable(word))
                .collect(Collectors.toList());

        // todo show not 3 most likely rules, look at probability
        for (int i = 0; i < 3; i++){
            res.add(
                    sorted_list
                            .remove(sorted_list.size() - 1)
                            .getCategory()
                            .apply(word)
            );
        }

        return res;
    }

    @Override
    public Boolean canHandle(String word) {
        return classifier != null && classifier.getCategories().size() > 0;
    }
}
