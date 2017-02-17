package helpers;

import datamodel.*;

import java.util.*;

/**
 * Class for extracting
 */
public class DatasetConverter {

    public static Set<ILemmaRule> formRules(Collection<IWord> words){

        Set<ILemmaRule> resultRules = new HashSet<>();

        // For each IWord new Lemma Rule will be defined and added to collection
        for (IWord word : words){

            resultRules.add(getRuleFromWord(word));
        }

        return resultRules;
    }

    public static Set<IMorpheme> formMorphemes(Collection<IWord> words){

        Set<IMorpheme> resultMorphemes = new HashSet<>();

        for (IWord word : words) {

            short i = SuffixesHelper
                    .getCommonPrefixLength(word.getWord(), word.getLemma());

            resultMorphemes.add(new Morpheme(word.getWord().substring(i)));
        }
        return resultMorphemes;
    }

    public static ILemmaRule getRuleFromWord(IWord word){
        String w = word.getWord();
        String lemma = word.getLemma();

        short i = SuffixesHelper.getCommonPrefixLength(w, lemma);

        //Build LemmaRule object
        List<IMorpheme> removed = new ArrayList<>();
        removed.add(new Morpheme(w.substring(i)));

        List<IMorpheme> added = new ArrayList<>();
        added.add(new Morpheme(lemma.substring(i)));

        return new LemmaRule(removed, added, word.getProperties());
    }

    public static MorphemedWord extractMorphemes(IWord iWord){

        String word = iWord.getWord();
        String lemma = iWord.getLemma();

        short common = SuffixesHelper.getCommonPrefixLength(word, lemma);

        List<IMorpheme> morphemes = new ArrayList<>();
        morphemes.add(new Morpheme(word.substring(0, common))); // root
        morphemes.add(new Morpheme(word.substring(common)));    // ending

        return new MorphemedWord(morphemes);
    }
}
