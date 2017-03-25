package datamodel;

import java.util.List;

/**
 * Class of words containing morphemes
 */
public class MorphemedWord {

    private List<IMorpheme> morphemes;

    public MorphemedWord(List<IMorpheme> morphemes){
        this.morphemes = morphemes;
    }

    /**
     * @return String representation of the word
     */
    public String get(){
        String word = "";

        for (IMorpheme m : morphemes){
            word = word.concat(m.get());
        }

        return word;
    }

    /**
     * @return The last morpheme in the word
     */
    public String getEnding(){
        return morphemes.get(morphemes.size() - 1).get();
    }

    public String getRoot() {
        return morphemes.get(0).get();
    }


    /**
     * @return Collection of morphemes that make up the word
     */
    List<IMorpheme> getMorphemes(){
        return morphemes;
    }
}
