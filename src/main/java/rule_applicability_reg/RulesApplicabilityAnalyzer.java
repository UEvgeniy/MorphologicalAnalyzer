package rule_applicability_reg;

import analyzers.IMorphAnalyzer;
import datamodel.*;

import java.io.Serializable;
import java.util.*;


/**
 *
 */
public class RulesApplicabilityAnalyzer implements IMorphAnalyzer,Serializable {

    private static final long serialVersionUID = 3946708460094173024L;
    private final MorphemeExtractor morphemeExtractor;
    private Map<String, Set<ExtendedLemmaRule>> rules;


    /**
     * Constructor
     * @param morphemeExtractor a tool for extracting possible morphemes from String
     * @param rules Map of rules grouped by their removing end from word
     */
    RulesApplicabilityAnalyzer(MorphemeExtractor morphemeExtractor,
                               Map<String, Set<ExtendedLemmaRule>> rules){

        this.morphemeExtractor = Objects.requireNonNull(morphemeExtractor,
                "MorphemeExtractor cannot be null");

        this.rules = Objects.requireNonNull(rules,
                "Set of rule cannot be null");

    }

    @Override
    public Set<IWord> analyze(String word) {

        // Firstly, try to find all possible morphemes in the word...
        Collection<MorphemedWord> morphemedWords = morphemeExtractor.extract(word);

        Set<IWord> result = new HashSet<>();

        // Using extracted morphemes try to define Properties
        for (MorphemedWord mWord : morphemedWords) {

            Set<ExtendedLemmaRule> possibleRules =
                    rules.getOrDefault(mWord.getEnding(), new HashSet<>());

            // For each variant of morpheme extraction try to define properties
            for (ExtendedLemmaRule rule : possibleRules) {
                if (rule.isApplicable(mWord)) {
                    result.add(rule.apply(mWord));
                }
            }
        }

        return result;
    }

    @Override
    public Boolean canHandle(String word) {
        return true; /*analyze(word) != null && analyze(word).size() > 0;*/
    }

    @Override
    public String toString() {
        String info;
        try {
            info = rules.values().iterator().next().iterator().next().getClassifierInfo();
        }
        catch (NullPointerException | NoSuchElementException e){
            info = "";
        }
        return this.getClass().getSimpleName() + ": " + info;
    }
}
