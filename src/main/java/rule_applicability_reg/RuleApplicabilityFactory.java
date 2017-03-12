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

    // ===================== END OF CLASS ===================== //
}
    
    
    /*
     * Form the fastest structure for searching rules and words.
     */
    /*
    private void collectRulesAndGroupWords() {

        rules = new HashMap<>();
        wordSets = new HashMap<>();

        for (IWord word : words.get()) {

            // Get the removing end from IWord
            String end = DatasetConverter.extractMorphemes(word).getEnding();

            // Add key to maps, if it's new key
            if (!wordSets.containsKey(end)) {
                wordSets.put(end, new HashSet<>());
            }
            //wordSets.computeIfAbsent(end, x -> new HashSet<>()).add(word);
            if (!rules.containsKey(end)) {
                rules.put(end, new HashSet<>());
            }

            // Add values to maps
            wordSets.get(end).add(word);

            int commonPrefixLen = SuffixesHelper.
                    getCommonPrefixLength(word.getWord(), word.getLemma());


            //if (end.isEmpty() && commonPrefixLen < word.getLemma().length()){
            //    continue;
            //}
            if (end.isEmpty())
                continue;

            ExtendedLemmaRule elr = new ExtendedLemmaRule(
                    end,
                    word.getLemma().substring(commonPrefixLen),
                    word.getProperties(),
                    new BayesClassifierAdapter(ngramSize));

            rules.get(end).add(elr);

        }
    }

    /**
     * Train bayes classifier for each rules.
     *
     * @param rules Sets of rules grouped by their removed ending
     */

    /*
    private void trainRules() {
        int a = 0;
        if (a++ % (rules.size() / 100) == 0)
            System.out.println(a * 100 / rules.size() + "%");


        for (Map.Entry<String, Set<ExtendedLemmaRule>> entry : rules.entrySet()) {

            if (a++ % (rules.size() / 100) == 0)
                System.out.println(a * 100 / rules.size() + "%");

            // Get the set of words for which rule can be applied

            Set<ExtendedLemmaRule> rules = entry.getValue();
            Set<IWord> possibleWords = wordSets.get(entry.getKey());

            for (ExtendedLemmaRule rule : rules) {



                Set<IWord> good = new HashSet<>();
                Set<IWord> bad = new HashSet<>();

                // Form "good" and "bad" sets of words
                for (IWord word : possibleWords) {

                    if (rule.fullyApplicable(word)) {
                        good.add(word);
                    } else {
                        bad.add(word);
                    }

                }

                List<IDataset> goodDatasets = new DataSet(good)
                        .split(80, random);
                List<IDataset> badDatasets = new DataSet(bad)
                        .split(80, random);

                trainRule(rule, goodDatasets.get(0), badDatasets.get(0), difference);

                IDataset goodDev = goodDatasets.get(1);
                IDataset badDev = badDatasets.get(1);

                double bound = findBound(rule, goodDev, badDev);
                rule.setLowerBound(bound);
            }
        }
    }


    private void trainRule(
            ExtendedLemmaRule rule,
            IDataset good,
            IDataset bad,
            int difference) {

        // Normalize sets size
        int minSize = good.size() < bad.size() ? good.size() : bad.size();
        Set<IWord> biggerSet = good.size() == minSize ? bad.get() : good.get();

        List<IWord> biggerList = new ArrayList<>(biggerSet);
        Collections.shuffle(biggerList, random);

        // If set of bad words empty then train classifier only with good words
        if (minSize == 0) {
            if (good.get().isEmpty()) {
                throw new RuntimeException("Set of correct words cannot be empty.");
            }

            rule.train(good.get());

            /*for (int i = 0; i < good.size(); i++) {
                rule.train(
                        DatasetConverter.extractMorphemes(biggerList.get(i)),
                        biggerList.get(i).getProperties());

            }
            return;
        }
/*
        Set<IWord> forTrainSet = good.size() == minSize ? good.get() : bad.get();

        // Train bigger set with less than difference * minimumSetSize elements
/*        for (int i = 0; i < minSize * difference && i < biggerList.size(); i++) {
            forTrainSet.add(biggerList.get(i));
        }

        rule.train(forTrainSet);

    }
*/
    /*
    private double findBound(ExtendedLemmaRule rule, IDataset good, IDataset bad) {

        List<Double> goodProbs = new ArrayList<>();
        List<Double> badProbs = new ArrayList<>();

        for (IWord word : good.get()) {
            goodProbs.add(rule.getProbability(DatasetConverter.extractMorphemes(word)));
        }

        for (IWord word : bad.get()) {
            badProbs.add(rule.getProbability(DatasetConverter.extractMorphemes(word)));
        }

        double good_mean = 0;
        double bad_mean = 0;
        for (double val : goodProbs) {
            good_mean += val;
        }
        for (double val : badProbs) {
            bad_mean += val;
        }

        bad_mean = bad_mean > 0 ? bad_mean / bad.size() : 0;
        good_mean = good_mean > 0 ? good_mean / good.size() : 0;

        return (bad_mean + good_mean) / 2;
    }

}
*/
