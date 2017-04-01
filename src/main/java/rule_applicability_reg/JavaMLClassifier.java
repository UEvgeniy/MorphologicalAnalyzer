package rule_applicability_reg;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Function;

import datamodel.MorphemedWord;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.core.Instance;

public class JavaMLClassifier implements IClassifier, Serializable {

	private static final long serialVersionUID = -4849568672079240168L;
	private final Classifier classifier;
	private final Function<MorphemedWord, Instance> instanceFactory;

	JavaMLClassifier(Classifier classifier, Function<MorphemedWord, Instance> instanceFactory) {
		this.classifier = classifier;
		this.instanceFactory = instanceFactory;
	}

	@Override
	public boolean predict(MorphemedWord word) {
		Instance instance = instanceFactory.apply(word);
		return classifier.classify(instance).equals("true");
	}

	@Override
	public double getProbability(MorphemedWord word) {
		Instance instance = instanceFactory.apply(word);
		Map<Object, Double> classDistribution= classifier.classDistribution(instance);
		return classDistribution.getOrDefault("true", 0.0);
	}

	@Override
	public String getInfo() {
		return this.getClass().getSimpleName() +
				" [Classifier: " + classifier.getClass().getSimpleName() + "]";
	}
}
