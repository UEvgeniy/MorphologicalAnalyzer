package analyzers;

import datamodel.IMorpheme;
import datamodel.MorphemedWord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class MorphemeExtractor {

    List<IMorpheme> morphemes;

    public MorphemeExtractor(Collection<IMorpheme> morphemes){
        morphemes = new ArrayList<>();

        this.morphemes.addAll(morphemes);
    }

    public Collection<MorphemedWord> extract(String word){
        // todo implement words morpheme extraction
        return new ArrayList<>();
    }

}
