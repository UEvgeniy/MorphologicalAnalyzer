package ru.ispras;

import analyzers.IMorphAnalyzer;
import aot_based.AotBasedFactory;
import datamodel.IDataset;
import datamodel.properties.PoS;
import factories.IMorphAnalyzerFactory;
import helpers.Datasets;
import helpers.Datasets.Filters;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.bayes.NaiveBayesClassifier;
import net.sf.javaml.core.Dataset;
import parsers.DatasetParser;
import parsers.IWordsExtractor;
import parsers.Parsers;
import rule_applicability_reg.*;

import java.io.File;
import java.util.Random;
import java.util.function.Function;

/**
 * Collection of functions
 */
class Examples {

    /**
     * Example of using Dataset, DatasetParser and Datasets.Filter
     * @param pathname one file or a directory, where parsed files located
     * @return Dataset of IWords
     */
    static IDataset extractFilterDataset(String pathname){

        // Now two parsers implemented
        IWordsExtractor ruscorpora = Parsers::RusCorpora;
        IWordsExtractor syntagrus = Parsers::SyntagRus;

        DatasetParser parser = new DatasetParser(syntagrus, new File(pathname));

        // Start the process of extracting...
        IDataset dataset = parser.getDataset();

        // It is possible to remove useless IWords from dataset using Predicate<IWord>
        dataset = dataset.filter(
                // Dataset will store only words with listed PoS'es below...
                Datasets.Filters.byPoS(PoS.NOUN, PoS.ADJ, PoS.VERB, PoS.ADV, PoS.NUM)
        );

        return dataset;
    }


    static IMorphAnalyzer generateAnalyzer(IDataset dataset){

        // 1. AOT
        IMorphAnalyzerFactory aotF = new AotBasedFactory(dataset);
        IMorphAnalyzer aot =  aotF.create();


        // 2.1 Create Classifier and IClassifierTrainer
        Classifier bayes = new NaiveBayesClassifier(false, false, true);
        IClassifierTrainer trainer = getThresholdTrainer(bayes);
        // 2.2 Factory
        IMorphAnalyzerFactory ruleAppF = new RuleApplicabilityFactory(dataset, trainer);

        // 2.2 Create rules applicability analyzer (bayes, threshold)
        IMorphAnalyzer bayes_threshold = ruleAppF.create();

        return bayes_threshold; // return aot;
    }


    static IClassifierTrainer getThresholdTrainer(Classifier classifier){

        Function<Dataset, Classifier> classifierFunction = (d) ->
        {
            classifier.buildClassifier(d);
            return classifier;
        };

        IClassifierTrainer wrapped = new JavaMlClassifierTrainer(
                new NGrams(2),
                classifierFunction
        );

        return new ThresholdClassifierTrainer(
                wrapped,
                0.8,
                new Random(0));
    }

}
