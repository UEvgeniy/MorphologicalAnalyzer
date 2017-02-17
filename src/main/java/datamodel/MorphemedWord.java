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
    public String getWord(){
        String word = "";

        for (IMorpheme m : morphemes){
            word = word.concat(m.getText());
        }

        return word;
    }

    /**
     * @return The last morpheme in the word
     */
    public String getEnding(){
        return morphemes.get(morphemes.size() - 1).getText();
    }

    public String getRoot() {
        return morphemes.get(0).getText();
    }
    /**
     * @return Collection of morphemes that make up the word
     */
    public List<IMorpheme> getMorphemes(){
        return morphemes;
    }
}
