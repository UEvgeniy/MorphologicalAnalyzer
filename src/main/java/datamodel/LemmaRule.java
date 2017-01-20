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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((added == null) ? 0 : added.hashCode());
		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
		result = prime * result + ((removed == null) ? 0 : removed.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		LemmaRule other = (LemmaRule) obj;
		if (added == null) {
			if (other.added != null)
				return false;
		} else if (!added.equals(other.added))
			return false;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		if (removed == null) {
			if (other.removed != null)
				return false;
		} else if (!removed.equals(other.removed))
			return false;
		return true;
	}
}
