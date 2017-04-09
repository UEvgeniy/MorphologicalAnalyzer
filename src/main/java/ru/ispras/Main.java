package ru.ispras;

import analyzers.IMorphAnalyzer;
import datamodel.*;
import datamodel.properties.PoS;
import quality_assess.*;

import java.util.*;
import java.util.function.Function;


public class Main {

    private static final String path = "D://dict/syntagrus";

    private static final String[] analyzed =
            "Глокая куздра штеко будланула бокра и курдячит бокрёнка".split(" ");

    public static void main(String[] args) {
        experiment();
    }

    private static void assessment(){

        IDataset dataset = Examples.extractFilterDataset(path);



        Collection<Function<IDataset, IMorphAnalyzer>> converters = new ArrayList<>();
        //converters.add(Analyzers.AOT());
        //converters.add(Analyzers.BayesThreshold(2, 0.8));
        converters.add(Analyzers.SVMBinClassifier(3));
        //converters.add(Analyzers.BayesBinClassifier(2));
        converters.add(Analyzers.RandomForestBinClassifier(2, 50));


        Examples.assessQualityForeachPOS(converters, dataset,
                PoS.ADV, PoS.NUM, PoS.NOUN, PoS.VERB, PoS.ADJ);
        //Examples.showAssessRes(Examples.assessQuality(converters, dataset));

        //Map<IMorphAnalyzer, QualityResult> results = Examples.assessQuality(converters, dataset);

        //Examples.showAssessRes(results);
    }

    private static void analyzeConcreteWords(){

        IDataset dataset = Examples.extractFilterDataset(path);

        //IMorphAnalyzer aot = Analyzers.AOT().apply(dataset);
        IMorphAnalyzer bayes = Analyzers.BayesThreshold(2, 0.8).apply(dataset);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Введите слово для анализа (пустая строка -  выход):");
            String[] word = scanner.nextLine().split(" ");
            if (word.length == 1 && word[0].isEmpty()){
                break;
            }
            Examples.tryAnalyze(bayes, word);
            //Examples.tryAnalyze(bayes, word);

        }

    }

    private static void experiment(){

        IDataset dataset = Examples.extractFilterDataset(path);

        Collection<Function<IDataset, IMorphAnalyzer>> analyzers = new ArrayList<>();
        analyzers.add(Analyzers.AOT());

        Experiment.crossValidation(
                10,
                analyzers,
                dataset,
                Criterias::fullCoincidence,
                new PoS[]{PoS.ADV, PoS.ADJ, PoS.NOUN, PoS.VERB}
                );
    }
}