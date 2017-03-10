package rule_applicability_reg;

import datamodel.*;
import helpers.DatasetConverter;

import java.io.Serializable;
import java.util.*;

/**
 * Extended class that use classifier classifier to define applicability
 */
class ExtendedLemmaRule implements ILemmaRule, Serializable {

    private static final long serialVersionUID = -1952315395633816558L;
    private final LemmaRule rule;
    private final IClassifierGood classifier;

    ExtendedLemmaRule(String remove, String add, IMorphProperties properties,
                      IClassifierGood classifier) {
        this.rule = new LemmaRule(Arrays.asList(new Morpheme(remove)), Arrays.asList(new Morpheme(add)), properties);
        this.classifier = Objects.requireNonNull(classifier);
    }
    
    public ExtendedLemmaRule(LemmaRule rule, IClassifierGood classifier) {
		this.rule = rule;
		this.classifier = classifier;
	}

	@Override
    public Boolean isApplicable(MorphemedWord word) {
        return rule.isApplicable(word) &&
                classifier.predict(word);
    }

    @Override
    public IWord apply(MorphemedWord word) {
       return rule.apply(word);
    }

    @Override
    public MorphemedWord formLemma(MorphemedWord word) {
        return rule.formLemma(word);
    }


    @Override
    public IMorphProperties getMorphProperties() {
        return rule.getMorphProperties();
    }


    /**
     * Training classifier with good & bad examples
     * @param mWord word with extracted morphemes
     * @param property morphological properies for the word
     */
    void train(Set<IWord> dataset){

        Map<String, Boolean> res = new HashMap<>();

        for (IWord word : dataset) {

            // If the basic applicability rule doesn't fit the word,
            //      then classifier classifier would not trained.
            if (!word.getWord().endsWith(removed)) {
                throw new IllegalArgumentException
                        ("Inappropriate word for train:" + word.getWord());
            }

            res.put(
                    DatasetConverter.extractMorphemes(word).getRoot(),
                    word.getProperties().equals(properties)
            );
        }

        classifier.train(res);
    }

    /**
     * Set lower bound of probability
     * @param bound double probability from 0 to 1
     */
    public void setLowerBound(double bound){
        // todo return
        //if (bound > 1 || bound < 0){
        //    throw new IllegalArgumentException("Probability cannot be out of [0;1]");
        //}
        classifier.setLowerBound(bound);
    }

    /**
     * Count the probability of applicability rule to the word
     * @param word word with extracted morphemes
     * @return double probability from 0 to 1
     */
    double getProbability(MorphemedWord word){
        if (!word.getEnding().equals(removed)) {
            return 0;
        }
        return classifier.getProbability(word);
    }


    /**
     * Can word be applied to currect rule
     * @param word word with extracted morphemes
     * @return True, if word and rule has similar end. If not, False
     */
    boolean canApply(MorphemedWord word){
        return word.getEnding().equals(removed);
    }

    /**
     * Can word be applied to currect rule
     * @param word word with extracted morphemes
     * @return True, if word and rule has similar end. If not, False
     */
    boolean fullyApplicable(IWord word){
        MorphemedWord mWord = DatasetConverter.extractMorphemes(word);
        return canApply(mWord) &&
                apply(mWord).equals(word);
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classifier == null) ? 0 : classifier.hashCode());
		result = prime * result + ((rule == null) ? 0 : rule.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExtendedLemmaRule other = (ExtendedLemmaRule) obj;
		if (classifier == null) {
			if (other.classifier != null)
				return false;
		} else if (!classifier.equals(other.classifier))
			return false;
		if (rule == null) {
			if (other.rule != null)
				return false;
		} else if (!rule.equals(other.rule))
			return false;
		return true;
	}

    
}
