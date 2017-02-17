package bin_class_approach;

import analyzers.IMorphAnalyzer;
import datamodel.ILemmaRule;
import datamodel.IWord;
import datamodel.MorphemedWord;
import datamodel.Word;


import java.io.Serializable;
import java.util.*;


/**
 *
 */
public class RulesApplicabilityAnalyzer implements IMorphAnalyzer,Serializable {

    private static final long serialVersionUID = -8710575846863354282L;
    private final IClassifierApplicability classifier;
    private final Set<ILemmaRule> rules;
    private final MorphemeExtractor morphemeExtractor;

    RulesApplicabilityAnalyzer(IClassifierApplicability classifier,
                               Set<ILemmaRule> rules,
                               MorphemeExtractor morphemeExtractor){
        this.classifier = Objects.requireNonNull(classifier,
                "Classifier cannot be null");
        this.rules = Objects.requireNonNull(rules,
                "List of rules cannot be null");
        this.morphemeExtractor = Objects.requireNonNull(morphemeExtractor,
                "MorphemeExtractor cannot be null");;
    }

    @Override
    public Collection<IWord> analyze(String word) {

        // Firstly, try to find some morphemes in the word...
        Collection<MorphemedWord> morphemedWords = morphemeExtractor.extract(word);

        Collection<IWord> result = new ArrayList<>();

        // Using extracted morphemes try to define Properties
        for (MorphemedWord mWord : morphemedWords) {

            // For each variant of morpheme extraction try to define properties
            for (ILemmaRule rule: rules) {
                if (rule.isApplicable(mWord) &&
                        classifier.isApplicable(mWord, rule)){
                    result.add(
                            new Word(mWord.getWord(),
                                    rule.apply(mWord).getWord(),
                                    rule.getMorphProperties())
                    );
                }
            }
        }

        return result;
    }

    @Override
    public Boolean canHandle(String word) {
        return analyze(word) != null && analyze(word).size() > 0;
    }
}
