package rule_applicability_reg;

import analyzers.IMorphAnalyzer;

import datamodel.IDataset;
import datamodel.IWord;
import datamodel.MorphemedWord;
import factories.IMorphAnalyzerFactory;
import helpers.DatasetConverter;
import helpers.SuffixesHelper;

import java.util.*;

/**
 * My own developed approach. The main idea -
 * collecting rules of transformation from word to its lemma
 * and training binary naive bayes classifier.
 */
public class BayesRuleApplicabilityFactory implements IMorphAnalyzerFactory{

    private final Set<IWord> words;


    public BayesRuleApplicabilityFactory(IDataset dictionary){
        this.words = Objects.requireNonNull(dictionary.get(),
                "Dataset cannot be null.");

    }

    @Override
    public IMorphAnalyzer create() {

        // Collect all ending morphemes
        MorphemeExtractor me = new MorphemeExtractor(DatasetConverter.collectMorphemes(words));

        // Form the fastest structure for searching rules
        Map<String, Set<ExtendedLemmaRule>> rules = formRulesCollection();

        // Train bayes classifier for each rules
        trainRules(rules);

        return new BayesRulesApplicabilityAnalyzer(me, rules);
    }

    /**
     * Form the fastest structure for searching rules.
     * @return Collection of rules grouped by their ending
     */
    private Map<String, Set<ExtendedLemmaRule>> formRulesCollection(){

        Map<String, Set<ExtendedLemmaRule>> result = new HashMap<>();

        for (IWord word : words){

            String end = DatasetConverter.extractMorphemes(word).getEnding();

            if (!result.containsKey(end)){
                result.put(end, new HashSet<>());
            }

            int comPref = SuffixesHelper.getCommonPrefixLength(word.getWord(), word.getLemma());

            ExtendedLemmaRule elr = new ExtendedLemmaRule(
                    word.getWord().substring(comPref),
                    word.getLemma().substring(comPref),
                    word.getProperties(),
                    new BayesRegression());

            result.get(end).add(elr);

        }

        return result;
    }

    /**
     * Train bayes classifier for each rules.
     * @param rules Sets of rules grouped by their removed ending
     */
    private void trainRules(Map<String, Set<ExtendedLemmaRule>> rules){
        int a = 0;
        Random rnd = new Random();

        for (IWord word: words){

            // todo remove loader
            if (a++ % (words.size() / 100) == 0){
                System.out.println(a * 100 / words.size() + "%");
            }


            MorphemedWord mWord = DatasetConverter.extractMorphemes(word);

            Set<ExtendedLemmaRule> possibleRules = rules.get(mWord.getEnding());


            for (ExtendedLemmaRule rule: possibleRules){
                if (rule.getMorphProperties().equals(word.getProperties())){
                    rule.train(mWord, word.getProperties());
                }
                else{
                    if (possibleRules.size() < 5
                            || rnd.nextDouble() < (double)10 / possibleRules.size()) {
                        rule.train(mWord, word.getProperties());
                    }
                }
            }
        }

    }
}
