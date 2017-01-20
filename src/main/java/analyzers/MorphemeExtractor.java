package analyzers;

import datamodel.IMorpheme;
import datamodel.Morpheme;
import datamodel.MorphemedWord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class MorphemeExtractor {

    private final List<IMorpheme> morphemes;

    public MorphemeExtractor(Collection<IMorpheme> morphemes){
        this.morphemes = new ArrayList<>();

        this.morphemes.addAll(morphemes);
    }

    public Collection<MorphemedWord> extract(String word){

        ArrayList<MorphemedWord> result = new ArrayList<>();

        for (IMorpheme m : morphemes ){
            if (word.endsWith(m.getText())){

                IMorpheme root = new Morpheme(word.substring(0, word.length() - m.getText().length()));

                List<IMorpheme> morphemes = new ArrayList<>();
                morphemes.add(root);
                morphemes.add(m);

                MorphemedWord mWord = new MorphemedWord(morphemes);

                result.add(mWord);

            }
        }

        return result;
    }

}
