package ru.ispras;

import analyzers.IMorphAnalyzer;
import quality_assess.*;
import datamodel.IDataset;

import java.util.*;
import java.util.function.Function;


public class Main {

    public static void main(String[] args) {

        IDataset dataset = Examples.extractFilterDataset("D://dict/syntagrus");

        Collection<Function<IDataset, IMorphAnalyzer>> converters = new ArrayList<>();
        converters.add(Analyzers.AOT());
        //converters.add(Analyzers.BayesThreshold(2, 0.8));


        Map<IMorphAnalyzer, QualityResult> results = Examples.assessQuality(converters, dataset);

        for (Map.Entry<IMorphAnalyzer, QualityResult> res : results.entrySet()) {
            System.out.println(res.getKey().getClass().getCanonicalName() + ":");
            System.out.println(res.getValue().getInfo(QualityResult.Info.PrecisionRecall) + "\n");
        }
    }

}