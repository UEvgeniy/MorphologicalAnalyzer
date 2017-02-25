package rule_applicability_reg;

import datamodel.IMorpheme;
import datamodel.Morpheme;
import datamodel.MorphemedWord;

import java.io.Serializable;
import java.util.*;

/**
 * Class extracts morphemes from word using a collection of morphemes
 */
class MorphemeExtractor implements Serializable {


    private static final long serialVersionUID = -1139787217716018414L;
    private final Set<IMorpheme> morphemes;

    MorphemeExtractor(Set<IMorpheme> morphemes){

        this.morphemes = Objects.requireNonNull(morphemes,
                "Collection of morphemes cannot be null");
    }

    /**
     * Method finds all possible splitting words on morphemes using collection of morphemes
     * @param word The word for extracting
     * @return the collection of possible splitting
     */
    Collection<MorphemedWord> extract(String word){

        ArrayList<MorphemedWord> result = new ArrayList<>();

        for (IMorpheme m : morphemes ){
            if (word.endsWith(m.getText())){

                IMorpheme root =
                        new Morpheme(word.substring(0,
                                word.length() - m.getText().length()));

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
