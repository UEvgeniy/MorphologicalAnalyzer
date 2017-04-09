package ru.ispras;

import analyzers.IMorphAnalyzer;

import datamodel.IDataset;
import datamodel.IWord;
import datamodel.properties.PoS;

import helpers.Datasets;
import helpers.Datasets.Filters;

import parsers.DatasetParser;
import parsers.IWordsExtractor;
import parsers.Parsers;

import quality_assess.Criterias;
import quality_assess.QualityAssessment;
import quality_assess.QualityResult;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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

    static void showAssessRes(Map<IMorphAnalyzer, QualityResult> results){
        for (Map.Entry<IMorphAnalyzer, QualityResult> res : results.entrySet()) {
            System.out.println(res.getKey().toString());
            System.out.println(res.getValue().getInfo(QualityResult.Info.PrecisionRecall));
        }
    }


    static Map<IMorphAnalyzer, QualityResult> assessQuality
            (Collection<Function<IDataset, IMorphAnalyzer>> funcs, IDataset dataset){

        List<IDataset> splitted = dataset.split(90, new Random());

        QualityAssessment assessment = new QualityAssessment(funcs, splitted.get(0));

        return assessment.start(splitted.get(1), Criterias::byLemma);
    }

    static void assessQualityForeachPOS
            (Collection<Function<IDataset, IMorphAnalyzer>> funcs, IDataset dataset, PoS... p){

        List<IDataset> splitted = dataset.split(90, new Random());

        QualityAssessment assessment = new QualityAssessment(funcs, splitted.get(0));

        for (PoS pos: p) {
            System.out.println(pos);
            showAssessRes(assessment.start(
                    splitted.get(1).filter(Filters.byPoS(pos)),
                    Criterias::byLemma));
        }
    }


    /**
     * Represents
     * @param an analyzer
     * @param words List of analyzed words
     */
    static void tryAnalyze(IMorphAnalyzer an, String... words) {

        for (String word : words) {
            if (an.canHandle(word)) {
                Collection<IWord> answers = an.analyze(word);
                System.out.println("WORD: " + word);
                for (IWord w : answers) {
                    System.out.println("\tLEMMA: " + w.getLemma() + "\tPROPS: " + w.getProperties().toString());
                }
            } else {
                System.out.println("Word " + word + "\n\tcannot be analyzed");
            }
        }
    }

    static void printNumberPOSInfo(IDataset dataset, PoS... pos){
        for (PoS p: pos){
            System.out.println(p + ": " + dataset.filter(Filters.byPoS(p)).size());
        }
        System.out.println("-----------");
        System.out.print("Total: " + dataset.filter(Filters.byPoS(pos)).size());
    }



}
