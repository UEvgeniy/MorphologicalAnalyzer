package ru.ispras;

import analyzers.IMorphAnalyzer;
import datamodel.*;
import quality_assess.*;

import java.util.*;
import java.util.function.Function;


public class Main {

    private static final String path = "D://dict/syntagrus";

    private static final String[] analyzed =
            "Глокая куздра штеко будланула бокра и курдячит бокрёнка".split(" ");

    public static void main(String[] args) {

        assessment();
        //analyzeConcreteWords();
    }

    private static void assessment(){

        IDataset dataset = Examples.extractFilterDataset(path);

        Collection<Function<IDataset, IMorphAnalyzer>> converters = new ArrayList<>();
        converters.add(Analyzers.AOT());
        //converters.add(Analyzers.BayesThreshold(2, 0.8));
        converters.add(Analyzers.SVMBinClassifier(2));
        //converters.add(Analyzers.BayesBinClassifier(2));
        //converters.add(Analyzers.RandomForestBinClassifier(2, 2));


        Map<IMorphAnalyzer, QualityResult> results = Examples.assessQuality(converters, dataset);

        for (Map.Entry<IMorphAnalyzer, QualityResult> res : results.entrySet()) {
            System.out.println(res.getKey().toString());
            System.out.println(res.getValue().getInfo(QualityResult.Info.PrecisionRecall));
        }
    }

    private static void analyzeConcreteWords(){

        IDataset dataset = Examples.extractFilterDataset(path);

        IMorphAnalyzer aot = Analyzers.AOT().apply(dataset);
        IMorphAnalyzer bayes = Analyzers.BayesThreshold(2, 0.8).apply(dataset);

        String[] words = {"домой", "радости"};

        //Examples.tryAnalyze(aot, words);
        Examples.tryAnalyze(bayes, words);

    }

}