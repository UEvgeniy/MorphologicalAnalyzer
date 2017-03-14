package rule_applicability_reg;

import analyzers.IMorphAnalyzer;

import datamodel.*;
import factories.IMorphAnalyzerFactory;
import helpers.DatasetConverter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * My own developed approach. The main idea -
 * collecting rules of transformation from word to its lemma
 * and training binary naive bayes classifier.
 */
public class RuleApplicabilityFactory implements IMorphAnalyzerFactory {

    // Data set of words for training analyzer
    private final IDataset words;
    private final IClassifierTrainer classifierTrainer;

    // How many times can a 'good' and 'bad' dataset differ in training binary classifier
    private static final double difference = 2;
    // The minimum number of words, for which LemmaRule must be fully applied
    private static final int minWordForRule = 5;

    /**
     * Constructor
     *
     * @param dictionary Data set of words for training analyzer
     */
    public RuleApplicabilityFactory(IDataset dictionary, IClassifierTrainer classifierTrainer) {
        this.words = Objects.requireNonNull(dictionary,
                "Data set cannot be null.");
        this.classifierTrainer = classifierTrainer;
    }

    @Override
    public IMorphAnalyzer create() {

        // Collect all ending morphemes
        MorphemeExtractor me = new MorphemeExtractor(
                DatasetConverter.collectMorphemes(words.get()));

        // Generate rules and filter unpopular rules(is determined by minWordForRule)
        Set<LemmaRule> rules = generateRule(words.get(), minWordForRule);

        // Extract MorphemedWord for each IWord
        Map<IWord, MorphemedWord> morphemed = extractMorphemes(words.get());

        // Mapping from ending to its possibly applicable rules
        Map<String, Set<ExtendedLemmaRule>> extLemmaRules =
                groupTrainRules(rules, morphemed);

        return new RulesApplicabilityAnalyzer(me, extLemmaRules);
    }

    /**
     * @param rules     Set of rules for which classifiers will be trained
     * @param morphemed Map from IWord to its morphemes
     * @return Map from removing part of word to its
     */
    private Map<String, Set<ExtendedLemmaRule>> groupTrainRules(
            Set<LemmaRule> rules, Map<IWord, MorphemedWord> morphemed) {

        Map<String, Set<ExtendedLemmaRule>> extLemmaRules = new HashMap<>();


        int progress = 0;
        for (LemmaRule rule : rules) {

            showProgress(progress++, rules.size());

            // Collection of Morhemed word which will be used in training binary classifier
            Map<MorphemedWord, Boolean> trainingDataset = new HashMap<>();

            for (Map.Entry<IWord, MorphemedWord> entry : morphemed.entrySet()) {
                // If word cannot be possibly applied, skip it
                if (!rule.isApplicable(entry.getValue())) {
                    continue;
                }

                trainingDataset.put(entry.getValue(),
                        fullyApplicable(rule, entry.getKey(), entry.getValue()));
            }
            trainingDataset = normalize(trainingDataset);

            if (trainingDataset.isEmpty()){
                continue;
            }

            ExtendedLemmaRule extLemmaRule =
                    new ExtendedLemmaRule(rule, classifierTrainer.apply(trainingDataset));

            extLemmaRules.computeIfAbsent(
                    rule.getRemoved(),
                    key -> new HashSet<>()).add(extLemmaRule);
        }

        return extLemmaRules;
    }

    private void showProgress(int current, int full) {
        if ((current * 100 / full) % 2 == 0)
            System.out.println((double) (current) / full * 100);
    }

    /**
     * Method form rules from each IWord from set and get its Set
     *
     * @param words Set of IWords
     * @return Set of filtered rules
     */
    private Set<LemmaRule> generateRule(Set<IWord> words, int min) {
        Map<LemmaRule, Integer> countRules = new HashMap<>();

        for (IWord word : words) {
            // Form lemma rule for word
            LemmaRule rule = DatasetConverter.getRuleFromWord(word);
            // Count rules
            countRules.merge(rule, 1, (a, b) -> ++a);
        }

        // Remove all rules for which number of words less than min and return set
        return countRules.entrySet()
                .stream()
                .filter(map -> map.getValue() >= min)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                .keySet();
    }

    /**
     * Extract morpheme from each IWord
     *
     * @param words Set of IWords
     * @return Mapping from word to its morphemed word
     */
    private Map<IWord, MorphemedWord> extractMorphemes(Set<IWord> words) {
        return words.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        DatasetConverter::extractMorphemes));
    }

    /**
     * Decrease a difference between positive and negative datasets
     *
     * @param dataset Non-normilized dataset
     * @return Dataset with reduced difference between positive and negative datasets
     */
    private Map<MorphemedWord, Boolean> normalize(Map<MorphemedWord, Boolean> dataset) {

        // Form lists of positive and negative dataset
        List<MorphemedWord> good = dataset.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<MorphemedWord> bad = dataset.entrySet().stream()
                .filter(e -> !e.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Normalize the difference between positive and negative datasets
        if (good.size() > bad.size()) {
            good = getSubList(good, (int) (bad.size() * difference));
        } else {
            bad = getSubList(bad, (int) (good.size() * difference));
        }

        // Add positive dataset
        Map<MorphemedWord, Boolean> normalized = good.stream()
                .collect(Collectors.toMap(Function.identity(), a -> true));

        // Add negative dataset
        normalized.putAll(
                bad.stream()
                        .collect(Collectors.toMap(Function.identity(), a -> false))
        );

        return normalized;
    }

    private static List<MorphemedWord> getSubList(List<MorphemedWord> list, int size) {
        return list.subList(0, list.size() < size ? list.size() : size);
    }

    private boolean fullyApplicable(LemmaRule rule, IWord word, MorphemedWord morphemed) {
        return rule.isApplicable(morphemed) &&
                rule.apply(morphemed).equals(word);
    }
}