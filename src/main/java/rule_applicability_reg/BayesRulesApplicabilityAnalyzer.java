package rule_applicability_reg;

import analyzers.IMorphAnalyzer;
import datamodel.*;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


/**
 *
 */
public class BayesRulesApplicabilityAnalyzer implements IMorphAnalyzer,Serializable {


    private static final long serialVersionUID = 3946708460094173024L;
    private final MorphemeExtractor morphemeExtractor;
    private Map<String, Set<ExtendedLemmaRule>> rules;


    BayesRulesApplicabilityAnalyzer(MorphemeExtractor morphemeExtractor,
                                    Map<String, Set<ExtendedLemmaRule>> rules){

        this.morphemeExtractor = Objects.requireNonNull(morphemeExtractor,
                "MorphemeExtractor cannot be null");

        this.rules = Objects.requireNonNull(rules);

    }

    @Override
    public Set<IWord> analyze(String word) {

        // Firstly, try to find some morphemes in the word...
        Collection<MorphemedWord> morphemedWords = morphemeExtractor.extract(word);

        Set<IWord> result = new HashSet<>();
        Map<ExtendedLemmaRule, Double> probabilities = new LinkedHashMap<>();

        // Using extracted morphemes try to define Properties
        for (MorphemedWord mWord : morphemedWords) {

            Set<ExtendedLemmaRule> possibleRules = rules.get(mWord.getEnding());

            // For each variant of morpheme extraction try to define properties
            for (ExtendedLemmaRule rule: possibleRules) {
                int confidence = mWord.getEnding().length();
                probabilities.put(rule, rule.getProbability(mWord) * confidence);
            }
        }

        Map<ExtendedLemmaRule, Double> sorted = sortByValue(probabilities);

        double topProb = 0;
        for (Map.Entry<ExtendedLemmaRule, Double> entry : sorted.entrySet()){
            if (topProb == 0) {
                topProb = entry.getValue();
            }
            if (entry.getValue() * 2 > topProb){
                result.add(entry.getKey().apply(word));
            }
            else{
                break;
            }
        }

        return result;
    }

    /**
     * Sorts map by values
     * @param map Map which is necessary to be sorted
     * @param <K> Keys
     * @param <V> Values
     * @return Sorted map by values (descending)
     */
    private static Map<ExtendedLemmaRule, Double> sortByValue(Map<ExtendedLemmaRule, Double> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    @Override
    public Boolean canHandle(String word) {
        return analyze(word) != null && analyze(word).size() > 0;
    }
}
