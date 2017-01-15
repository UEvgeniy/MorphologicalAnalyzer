package factories;


import analyzers.MorphemeExtractor;
import datamodel.IMorpheme;
import datamodel.IWord;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class MorphemeExtractorTrainer {

    private HashSet<IWord> dictionary;

    public MorphemeExtractorTrainer(HashSet<IWord> words) {

        dictionary = new HashSet<>();

        dictionary.addAll(
                words
                .stream()
                .filter((word) -> word != null)
                .collect(Collectors.toSet())
        );
    }

    public MorphemeExtractor train(){

        List<IMorpheme> morphemes = new ArrayList<>();

        // todo Machine Learning

        for (IWord word : dictionary){

        }


        return new MorphemeExtractor(morphemes);
    }


}
