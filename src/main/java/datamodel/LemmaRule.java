package datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Rule containing Pos + properties and the process of word transformation to lemma
 */

public class LemmaRule implements ILemmaRule, Serializable{


    private static final long serialVersionUID = -6753719757708841662L;
    private Collection<IMorpheme> removed;
    private  Collection<IMorpheme> added;

    private IMorphProperties properties;

    public LemmaRule(Collection<IMorpheme> removed,
                     Collection<IMorpheme> added,
                     IMorphProperties properties){
        this.removed = removed;
        this.added = added;
        this.properties = properties;
    }
    
    public LemmaRule(String removed,
            String added,
            IMorphProperties properties){
    	this(Arrays.asList(new Morpheme(removed)), Arrays.asList(new Morpheme(added)), properties);
    }

    public String getRemoved() {
		return removed.stream().map(IMorpheme::getText).collect(Collectors.joining());
	}

	public void setRemoved(Collection<IMorpheme> removed) {
		this.removed = removed;
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
    public MorphemedWord formLemma(MorphemedWord word) {

        List<IMorpheme> lemmaMorphemes = new ArrayList<>(word.getMorphemes());

        for (IMorpheme m : removed){
            lemmaMorphemes.removeIf((morpheme) -> morpheme.getText().equals(m.getText()));
        }

        for (IMorpheme m : added){
            lemmaMorphemes.add(m);
        }

        return new MorphemedWord(lemmaMorphemes);
    }

    @Override
    public IWord apply(MorphemedWord word) {
        return new Word(
                word.getWord(),
                formLemma(word).getWord(),
                properties);
    }

    @Override
    public IMorphProperties getMorphProperties() {
        return properties;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LemmaRule)) return false;

        LemmaRule lemmaRule = (LemmaRule) o;

        if (removed != null ? !removed.equals(lemmaRule.removed) : lemmaRule.removed != null) return false;
        if (added != null ? !added.equals(lemmaRule.added) : lemmaRule.added != null) return false;
        return properties != null ? properties.equals(lemmaRule.properties) : lemmaRule.properties == null;

    }

    @Override
    public int hashCode() {
        return Objects.hash(removed, added, properties);
    }
}
