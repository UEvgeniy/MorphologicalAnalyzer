package factories;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import analyzers.MorphemeExtractor;
import datamodel.IMorpheme;
import datamodel.IWord;
import datamodel.Morpheme;

public class MorphemeExtractorTrainer {

    private Set<IWord> dictionary;

    public MorphemeExtractorTrainer(Set<IWord> words) {

        dictionary = new HashSet<>();

        dictionary.addAll(
                words
                .stream()
                .filter((word) -> word != null)
                .collect(Collectors.toSet())
        );
    }

    public MorphemeExtractor train(){

        Set<IMorpheme> morphemes = new HashSet<>();


        for (IWord word : dictionary){

            String w = word.toString(), lemma = word.getLemma();

            // the similar way of extracting morphemes as in TrivialLemmaRulePropertyPredictorTrainer

            // Morpheme here is unique substring for lemma

            // todo Warning! copy-paste
            short i = SuffixesHelper.getCommonPrefixLength(w, lemma);

            Morpheme newMorph = new Morpheme(w.substring(i, w.length()));

            if (i != w.length() && !morphemes.contains(newMorph)) {
                morphemes.add(
                        newMorph
                );
            }
        }


        return new MorphemeExtractor(morphemes);
    }

	


}
