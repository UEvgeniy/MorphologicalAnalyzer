package rule_applicability_reg;

import java.util.Map;
import java.util.function.Function;

import datamodel.MorphemedWord;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.core.Instance;

public class JavaMLClassifierAdapter implements IClassifierGood{

	private final Classifier classifier;
	private final Function<MorphemedWord, Instance> instanceFactory;

	public JavaMLClassifierAdapter(Classifier classifier, Function<MorphemedWord, Instance> instanceFactory) {
		this.classifier = classifier;
		this.instanceFactory = instanceFactory;
	}

	@Override
	public boolean predict(MorphemedWord word) {
		Instance instance = instanceFactory.apply(word);
		Map<Object, Double> classDistribution= classifier.classDistribution(instance);
		return getMostProbableClass(classDistribution);
	}
	
	private boolean getMostProbableClass(Map<Object, Double> classDistribution) {
		//get key with max value
		return false;
	}

	@Override
	public double getProbability(MorphemedWord word) {
		Instance instance = instanceFactory.apply(word);
		Map<Object, Double> classDistribution= classifier.classDistribution(instance);
		return classDistribution.getOrDefault("true", 0.0);
	}
}
