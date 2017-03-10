package rule_applicability_reg;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import datamodel.MorphemedWord;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;

public class JavaMlClassifierTrainer implements IClassifierTrainer{

	private final Function<MorphemedWord, Instance> instanceFactory;
	private final Function<Dataset, Classifier> javaMLClassifierTrainer;

	public JavaMlClassifierTrainer(Function<MorphemedWord, Instance> instanceFactory,
			Function<Dataset, Classifier> javaMLClassifierTrainer) {
		this.instanceFactory = instanceFactory;
		this.javaMLClassifierTrainer = javaMLClassifierTrainer;
	}
	
	@Override
	public IClassifierGood apply(Map<MorphemedWord, Boolean> dataset) {
		Dataset convertedDataset = dataset.entrySet().stream()
		.map(entry -> buildLabeledInstance(entry.getKey(), entry.getValue()))
		.collect(Collectors.collectingAndThen(Collectors.toList(), DefaultDataset::new));
		return new JavaMLClassifierAdapter(javaMLClassifierTrainer.apply(convertedDataset), instanceFactory);
	}

	private Instance buildLabeledInstance(MorphemedWord word, Boolean classLabel){
		Instance instance = instanceFactory.apply(word);
		instance.setClassValue(classLabel.toString());
		return instance;
	}
}
