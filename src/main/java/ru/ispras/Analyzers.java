package ru.ispras;

import analyzers.IMorphAnalyzer;
import aot_based.AotBasedFactory;
import datamodel.IDataset;
import factories.IMorphAnalyzerFactory;
import factories.MorphAnalyzerLoader;
import factories.MorphAnalyzerSaver;
import libsvm.LibSVM;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.bayes.NaiveBayesClassifier;
import net.sf.javaml.classification.tree.RandomForest;
import net.sf.javaml.core.Dataset;
import rule_applicability_reg.*;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Examples of functions that accepts one IDataset and produces IMorphAnalyzer.
 */
class Analyzers {

    private Analyzers(){}

    static Function<IDataset, IMorphAnalyzer> save(IMorphAnalyzerFactory fact, File file){
        return d -> {
            try {
                return new MorphAnalyzerSaver(fact, file).create();
            } catch (IOException e) {
                return null;
            }
        };
    }

    static Function<IDataset, IMorphAnalyzer> load(File file){
        return d -> {
            try {
                return new MorphAnalyzerLoader(file).create();
            } catch (IOException e) {
                return null;
            }
        };
    }

    static Function<IDataset, IMorphAnalyzer> AOT(){
        return (d) -> new AotBasedFactory(d).create();
    }

    static Function<IDataset, IMorphAnalyzer> BayesThreshold(int NGrams, double proportion){
        return (d) -> {
            IClassifierTrainer trainer =
                    getThresholdTrainer(
                            () -> new NaiveBayesClassifier(false, false, true),
                            NGrams,
                            proportion);

            RuleApplicabilityFactory fact = new RuleApplicabilityFactory(d, trainer);

            return fact.create();
        };
    }

    static Function<IDataset, IMorphAnalyzer> BayesBinClassifier(int NGrams){

        return (d) -> {
            IClassifierTrainer trainer = getBinClassifier(
                    () -> new NaiveBayesClassifier(false, false, true),
                    NGrams
            );

            RuleApplicabilityFactory fact = new RuleApplicabilityFactory(d, trainer);

            return fact.create();
        };
    }

    static Function<IDataset, IMorphAnalyzer> SVMThreshold(int NGrams, double proportion){
        return (d) -> {
            IClassifierTrainer trainer =
                    getThresholdTrainer(
                            LibSVM::new,
                            NGrams,
                            proportion);

            RuleApplicabilityFactory fact = new RuleApplicabilityFactory(d, trainer);

            return fact.create();
        };
    }

    static Function<IDataset, IMorphAnalyzer> SVMBinClassifier(int NGrams){

        return (d) -> {
            IClassifierTrainer trainer = getBinClassifier(
                    LibSVM::new,
                    NGrams
            );

            RuleApplicabilityFactory fact = new RuleApplicabilityFactory(d, trainer);

            return fact.create();
        };
    }

    static Function<IDataset, IMorphAnalyzer> RandomForestBinClassifier(int NGrams, int treecount){
        return d -> {
            IClassifierTrainer trainer = getBinClassifier(
                    () -> new RandomForest(treecount),
                    NGrams
            );

            RuleApplicabilityFactory fact = new RuleApplicabilityFactory(d, trainer);

            return fact.create();
        } ;
    }


    /**
     * Produces Binary Classifier for RuleApplicabilityAnalyzer
     * @param classifier Used classifier
     * @param NGrams length of substrings
     * @return Trainer
     */
    private static IClassifierTrainer getBinClassifier(Supplier<Classifier> constructor, int NGrams){

        Function<Dataset, Classifier> classifierFunction = (d) ->
        {
            Classifier classifier = constructor.get();
            classifier.buildClassifier(d);
            return classifier;
        };

        return new JavaMlClassifierTrainer(new NGrams(NGrams), classifierFunction);
    }


    /**
     * Produces Regression (with threshold) Trainer for RuleApplicabilityAnalyzer
     * @param classifier Used classifier
     * @param NGrams length of substrings
     * @param proportion proportion of splitting dataset to train and test
     * @return Trainer
     */
    private static IClassifierTrainer getThresholdTrainer(
            Supplier<Classifier> classifier, int NGrams, double proportion){

        IClassifierTrainer wrapped = getBinClassifier(classifier, NGrams);

        return new ThresholdClassifierTrainer(
                wrapped,
                proportion,
                new Random(0));
    }
}
