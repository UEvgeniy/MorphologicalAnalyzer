package ru.ispras;

import analyzers.IMorphAnalyzer;
import datamodel.Dataset;
import datamodel.IDataset;
import datamodel.IWord;
import datamodel.properties.PoS;
import helpers.Datasets;
import quality_assess.IEvaluationCriteria;
import quality_assess.QualityAssessment;

import java.util.*;
import java.util.function.Function;

/**
 * Experiment
 */
class Experiment {

    static void crossValidation
            (int partNum, Collection<Function<IDataset,
                    IMorphAnalyzer>> funcs, IDataset d, IEvaluationCriteria criteria, PoS[] pos){

        if (partNum <= 0)
            throw new IllegalArgumentException("partNum must be > 0");
        Objects.requireNonNull(funcs);
        Objects.requireNonNull(d);

        // first, divide dataset to equals in number subsets
        ArrayList<Set<IWord>> subsets = new ArrayList<>(partNum);
        for (int i = 0; i < partNum; i++){
            subsets.add(new HashSet<>());
        }

        // Fill subsets
        int index = 0;
        for (IWord w: d){
            subsets.get(index++ % partNum).add(w);
        }

        // Now let's have partNum experiments
        for (int i = 0; i < partNum; i++){
            System.out.println("Experiment #" + (i + 1));
            IDataset train = getTrain(subsets, i);
            IDataset test = getTest(subsets, i);

            QualityAssessment QA = new QualityAssessment(funcs, train);

            // For each part of speech have an experiment
            for (PoS p : pos) {
                System.out.println(p);
                Examples.showAssessRes(
                        QA.start(
                                test.filter(Datasets.Filters.byPoS(p)),
                                criteria));
            }
        }
    }

    private static IDataset getTrain(ArrayList<Set<IWord>> subsets, int testPart){
        Set<IWord> set = new HashSet<>();
        for (int i = 0; i < subsets.size(); i++){
            if (i != testPart){
                set.addAll(subsets.get(i));
            }
        }

        return new Dataset(set);
    }

    private static IDataset getTest(ArrayList<Set<IWord>> subsets, int testPart){
        return new Dataset(subsets.get(testPart));
    }
}
