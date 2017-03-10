package rule_applicability_reg;

import datamodel.MorphemedWord;

public interface IClassifierGood {
	boolean predict(MorphemedWord word);
	double getProbability(MorphemedWord word);
}
