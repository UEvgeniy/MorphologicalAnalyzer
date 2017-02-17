package bin_class_approach;

import bin_class_approach.naive_bayes.BayesClassifier;
import datamodel.ILemmaRule;
import datamodel.IWord;
import datamodel.MorphemedWord;
import helpers.DatasetConverter;

import javax.naming.NameNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Factory for BinBayesClassifier
 */
class BinBayesClassifierFactory implements IClassifierApplicabilityFactory {

    private final Set<IWord> words;

    BinBayesClassifierFactory(Set<IWord> words) {
        this.words = new HashSet<>();

        this.words.addAll(
                Objects.requireNonNull(words, "Dictionary cannot be null.")
                        .stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public IClassifierApplicability create() {

        Map<String,
                Map<ILemmaRule,
                        BayesClassifier<String, Boolean>
                        >
                > result = new HashMap<>();

        words.removeIf((a) -> a.getWord().length() < 13 );
        System.out.print(words.size());

        // Add and train TRUE features
        for (IWord word : words) {

            ILemmaRule rule = DatasetConverter.getRuleFromWord(word);
            MorphemedWord mWord = DatasetConverter.extractMorphemes(word);

            if (!result.containsKey(mWord.getEnding())) {
                result.put(mWord.getEnding(), new HashMap<>());
            }

            if (!result.get(mWord.getEnding()).containsKey(rule)) {
                result.get(mWord.getEnding()).put(rule, new BayesClassifier<>());
            }
            // todo remove
            if (rule.getMorphProperties().contains("a=sg,m,acc,inan,plen")){
                int a = 12;
            }

            result.get(mWord.getEnding()).get(rule)
                    .learn(true, NGrams.get(mWord.getRoot(), 2));
        }


        // Train FALSE features
        for (IWord word : words) {
            MorphemedWord mWord = DatasetConverter.extractMorphemes(word);
            ILemmaRule rule = DatasetConverter.getRuleFromWord(word);

            // todo remove
            if (rule.getMorphProperties().contains("a=sg,m,acc,inan,plen")){
                int a = 12;
            }

            // For each variant of ending
            String ending = mWord.getEnding();
            do {
                if (result.containsKey(ending)) {

                    Map<ILemmaRule,
                            BayesClassifier<String, Boolean>
                            > classifiers = result.get(ending);

                    classifiers.forEach((k, v) ->
                    {
                        if (rule != k) {
                            v.learn(false,
                                    NGrams.get(mWord.getRoot(), 2));
                        }
                    });

                }
                ending = ending.isEmpty() ? ending : ending.substring(1);
            }
            while (!ending.isEmpty());
        }
        return new BinBayesClassifier(result);
    }
}
