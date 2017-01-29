package factories;


import analyzers.MorphemeExtractor;
import datamodel.IMorpheme;
import datamodel.IWord;
import datamodel.Morpheme;
import helpers.SuffixesHelper;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MorphemeExtractorTrainer {

    private Set<IWord> dictionary;

    public MorphemeExtractorTrainer(Set<IWord> words) {

        dictionary = new HashSet<>();

        dictionary.addAll(
                Objects.requireNonNull(words, "Dictionary cannot be null.")
                .stream()
                .filter((word) -> word != null)
                .collect(Collectors.toSet())
        );
    }

    /**
     * Method forms collection of morphemes based on difference between word and its lemma
     * @return Morpheme extractor with collection of morphemes
     */
    public MorphemeExtractor train(){

        Set<IMorpheme> morphemes = new HashSet<>();

        for (IWord word : dictionary){

            String w = word.getWord();
            String lemma = word.getLemma();

            short i = SuffixesHelper.getCommonPrefixLength(w, lemma);

            Morpheme newMorph = new Morpheme(w.substring(i, w.length()));

            morphemes.add(newMorph);

        }

        return new MorphemeExtractor(morphemes);
    }

	


}
