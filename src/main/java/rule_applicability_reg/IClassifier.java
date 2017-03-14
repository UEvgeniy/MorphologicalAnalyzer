package rule_applicability_reg;

import datamodel.MorphemedWord;

public interface IClassifier {

	boolean predict(MorphemedWord word);
	
	double getProbability(MorphemedWord word);
}
