package datamodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Rule containing Pos + properties and the process of word transformation to lemma
 */

// todo now Lemma rule has trivial mechanism of transformation, may be need to be updated
public class LemmaRule implements ILemmaRule{

    private Collection<IMorpheme> removed;
    private  Collection<IMorpheme> added;

    private String properties;

    public LemmaRule(Collection<IMorpheme> removed, Collection<IMorpheme> added, String properties){
        this.removed = removed;
        this.added = added;
        this.properties = properties;
    }


    @Override
    public Boolean isApplicable(MorphemedWord word) {

        Collection<IMorpheme> morphemesInWord = word.getMorphemes();

        for (IMorpheme morpheme : removed){
            if (!morphemesInWord.contains(morpheme)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public MorphemedWord apply(MorphemedWord word) {
        //String lemma = word.getWord();
        List<IMorpheme> lemmaMorphemes = new ArrayList<>(word.getMorphemes());



        for (IMorpheme m : removed){
        //    lemma = lemma.replace(m.getText(), "");
            lemmaMorphemes.removeIf((morpheme) -> morpheme.getText().equals(m.getText()));
        }

        for (IMorpheme m : added){
        //    lemma = lemma.concat(m.getText());
            lemmaMorphemes.add(m);
        }

        return new MorphemedWord(lemmaMorphemes);
    }

    @Override
    public String getMorphProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != LemmaRule.class){
            return false;
        }

        LemmaRule o = ((LemmaRule) obj);

        return this.removed.containsAll(o.removed) && o.removed.containsAll(this.removed)
                && this.added.containsAll(o.added) && o.added.containsAll(this.added)
                && this.properties.equals(o.properties);
    }
}
