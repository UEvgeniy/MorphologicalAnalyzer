package ru.ispras;

import analyzers.IMorphAnalyzer;
import datamodel.Morpheme;
import datamodel.MorphemedWord;
import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.bayes.NaiveBayesClassifier;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;
import net.sf.javaml.distance.DistanceMeasure;
import quality_assess.*;
import datamodel.IDataset;
import rule_applicability_reg.NGrams;

import java.util.*;
import java.util.function.Function;


public class Main {

    public static void main(String[] args) {

        example();
    }

    private static void example(){
        IDataset dataset = Examples.extractFilterDataset("D://dict/syntagrus");

        Collection<Function<IDataset, IMorphAnalyzer>> converters = new ArrayList<>();
        converters.add(Analyzers.AOT());
        //converters.add(Analyzers.BayesThreshold(2, 0.8));
        converters.add(Analyzers.SVMBinClassifier(2));
        converters.add(Analyzers.BayesBinClassifier(2));


        Map<IMorphAnalyzer, QualityResult> results = Examples.assessQuality(converters, dataset);

        for (Map.Entry<IMorphAnalyzer, QualityResult> res : results.entrySet()) {
            System.out.println(res.getKey().getClass().getCanonicalName() + ":");
            System.out.println(res.getValue().getInfo(QualityResult.Info.PrecisionRecall));
        }
    }

}