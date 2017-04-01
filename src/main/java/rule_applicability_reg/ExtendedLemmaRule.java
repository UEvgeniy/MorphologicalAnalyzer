package rule_applicability_reg;

import datamodel.*;
import datamodel.properties.IMorphProperties;

import java.io.Serializable;
import java.util.*;

/**
 * Extended class that use classifier classifier to define applicability
 */
class ExtendedLemmaRule implements ILemmaRule, Serializable {

    private static final long serialVersionUID = -1952315395633816558L;
    private final LemmaRule rule;
    private final IClassifier classifier;
    
    ExtendedLemmaRule(LemmaRule rule, IClassifier classifier) {
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

    String getClassifierInfo(){
        return classifier.getInfo();
    }


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ExtendedLemmaRule)) return false;
		ExtendedLemmaRule that = (ExtendedLemmaRule) o;
		return Objects.equals(rule, that.rule) &&
				Objects.equals(classifier, that.classifier);
	}

	@Override
	public int hashCode() {
		return Objects.hash(rule, classifier);
	}
}
