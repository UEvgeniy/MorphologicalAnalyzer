package aot_based;

import analyzers.IMorphAnalyzer;
import datamodel.IDataset;
import datamodel.IWord;
import datamodel.Word;
import helpers.SuffixesHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * This approach based on http://www.aot.ru/docs/sokirko/Dialog2004.htm description
 */
public class AotBasedAnalyzer implements IMorphAnalyzer, Serializable {

    private static final long serialVersionUID = -1781794547268400899L;
    private final Set<IWord> dictionary;

    public AotBasedAnalyzer(IDataset dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary.get());
    }

    @Override
    public Collection<IWord> analyze(String word) {

        short longestSuffix = 0;
        short wordLemmaDif = 0;
        IWord bestCoincidence = null;

        // Find the word with the longest common suffixes

        // Second condition means that the common prefix among analyzed word and word from
        // must be longer than differing prefix among dictionary word and its lemma,
        // otherwise lemmatisation of analyzed word would try to delete non-existing letters
        //
        // The BAD example (2nd condition) is illustrated below:
        //  analyzed___word
        //        |   |   |
        //        | equal |
        //        |   |   |
        //  dictionary_word
        //      |    |    |
        //      |different|
        //      |    |    |
        //  dict_word_lemma

        for (IWord w: dictionary){

            short commonSuffix = SuffixesHelper.getCommonSuffixesLength(word, w.getWord());
            short difference = 0;
            if (commonSuffix > longestSuffix &&
                    /*Second condition*/
                    commonSuffix > (difference =
                            SuffixesHelper.getDifferingSuffixesLength(w.getWord(), w.getLemma()))) {
                bestCoincidence = w;
                longestSuffix = commonSuffix;
                wordLemmaDif = difference;
            }
        }



        Collection<IWord> result = new ArrayList<>();

        if (bestCoincidence != null){

            String del = word.substring(0, word.length() - wordLemmaDif);
            String new_lemma = del.concat(bestCoincidence.getLemma().substring(
                    SuffixesHelper.getCommonPrefixLength(bestCoincidence.getWord(), bestCoincidence.getLemma())
            ));

            result.add(new Word(word, new_lemma, bestCoincidence.getProperties()));
        }
        return result;
    }

    @Override
    public Boolean canHandle(String word) {
        return analyze(word).size() >0;
    }
}
