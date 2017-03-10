package rule_applicability_reg;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import datamodel.MorphemedWord;

public class ThresholdClassifierTrainer implements IClassifierTrainer{

	private static class SplittedDataset {
		private final Map<MorphemedWord, Boolean> train;
		private final Map<MorphemedWord, Boolean> development;
		
		public SplittedDataset(Map<MorphemedWord, Boolean> train, Map<MorphemedWord, Boolean> development) {
			this.train = train;
			this.development = development;
		}
	}
	
	private final IClassifierTrainer wrapped;
	private final double proportion;
	private final Random random;
	
	public ThresholdClassifierTrainer(IClassifierTrainer wrapped, double proportion, Random random) {
		this.wrapped = wrapped;
		this.proportion = proportion;
		this.random = random;
	}

	@Override
	public IClassifierGood apply(Map<MorphemedWord, Boolean> dataset) {
		SplittedDataset splitted = split(dataset);
		IClassifierGood classifier = wrapped.apply(splitted.train);
		Map<Boolean, List<Double>> distribution = 
				splitted.development.entrySet().stream()
					.map(entry -> new AbstractMap.SimpleEntry<>(entry.getValue(), classifier.getProbability(entry.getKey())))
					.collect(
							Collectors.groupingBy(
									Map.Entry::getKey, 
									Collectors.mapping(
											Map.Entry::getValue, 
											Collectors.toList())));
		return new ThresholdClassifier(classifier, computeThreshold(distribution));
	}
	
	private double computeThreshold(Map<Boolean, List<Double>> distribution){
		return distribution.values().stream()
				.mapToDouble(list -> list.stream()
						.mapToDouble(Double::doubleValue)
						.average()
						.orElse(1.0))
				.average()
				.orElse(1.0);
				
	}
	
	private SplittedDataset split(Map<MorphemedWord, Boolean> dataset){
		List<MorphemedWord> positive = getShuffled(dataset, true);
		List<MorphemedWord> negative = getShuffled(dataset, true);
		
		int bound = (int) (positive.size() * proportion);
		List<MorphemedWord> positiveTrain = new ArrayList<>(positive.subList(0, bound));
		List<MorphemedWord> positiveTest = new ArrayList<>(positive.subList(bound, positive.size()));
		
		bound = (int) (negative.size() * proportion);
		List<MorphemedWord> negativeTrain = new ArrayList<>(negative.subList(0, bound));
		List<MorphemedWord> negativeTest = new ArrayList<>(negative.subList(bound, negative.size()));
		
		return new SplittedDataset(getSubDataset(dataset, merge(positiveTrain, negativeTrain)), getSubDataset(dataset, merge(positiveTest, negativeTest)));
	}

	private Set<MorphemedWord> merge(List<MorphemedWord> positiveTrain, List<MorphemedWord> negativeTrain) {
		return Stream.of(positiveTrain, negativeTrain).flatMap(List::stream).collect(Collectors.toSet());
	}

	private Map<MorphemedWord, Boolean> getSubDataset(Map<MorphemedWord, Boolean> dataset, Set<MorphemedWord> words){
		return dataset.entrySet().stream().filter(entry -> words.contains(entry.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
	
	private List<MorphemedWord> getShuffled(Map<MorphemedWord, Boolean> dataset, Boolean classLabel) {
		List<MorphemedWord> words = dataset.entrySet().stream().filter(entry -> entry.getValue().equals(classLabel)).map(Map.Entry::getKey).collect(Collectors.toList());
		Collections.shuffle(words, random);
		return words;
	}

	
}
