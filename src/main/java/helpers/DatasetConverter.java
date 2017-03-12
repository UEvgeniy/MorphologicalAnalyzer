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

    public static Set<IMorpheme> collectMorphemes(Collection<IWord> words){

        Set<IMorpheme> resultMorphemes = new HashSet<>();

        for (IWord word : words) {

            short i = SuffixesHelper
                    .getCommonPrefixLength(word.getWord(), word.getLemma());

            resultMorphemes.add(new Morpheme(word.getWord().substring(i)));
        }
        return resultMorphemes;
    }

    public static LemmaRule getRuleFromWord(IWord iWord){

        Objects.requireNonNull(iWord);

        String word = iWord.getWord();
        String lemma = iWord.getLemma();

        short commonPrefixLength = SuffixesHelper.getCommonPrefixLength(word, lemma);

        //Build LemmaRule object
        List<IMorpheme> removed = new ArrayList<>();
        removed.add(new Morpheme(word.substring(commonPrefixLength)));

        List<IMorpheme> added = new ArrayList<>();
        added.add(new Morpheme(lemma.substring(commonPrefixLength)));

        return new LemmaRule(removed, added, iWord.getProperties());
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
