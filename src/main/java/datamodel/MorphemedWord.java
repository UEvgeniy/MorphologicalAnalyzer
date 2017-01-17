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


    // todo replace this to toString() method (like IWord realization)
    public String getWord(){
        String word = "";

        for (IMorpheme m : morphemes){
            word = word.concat(m.getText());
        }

        return word;
    }

    public List<IMorpheme> getMorphemes(){
        return morphemes;
    }
}
