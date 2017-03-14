package rule_applicability_reg;

import datamodel.MorphemedWord;

public class ThresholdClassifier implements IClassifier {

	private final IClassifier classifier;
	private final double threshold;
	
	ThresholdClassifier(IClassifier classifier, double threshold) {
		this.classifier = classifier;
		this.threshold = threshold;
	}

	@Override
	public boolean predict(MorphemedWord word) {
		return getProbability(word) > threshold;
	}

	@Override
	public double getProbability(MorphemedWord word) {
		return classifier.getProbability(word);
	}

}
