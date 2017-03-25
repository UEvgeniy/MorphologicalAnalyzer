package ru.ispras;

import analyzers.IMorphAnalyzer;
import comparator.EvaluationCriteria;
import comparator.IEvaluationCriteria;
import comparator.QualityAssessment;
import comparator.QualityResult;
import datamodel.IDataset;
import datamodel.IWord;
import factories.*;
import libsvm.LibSVM;
import parsers.DatasetParser;
import parsers.Parsers;
import rule_applicability_reg.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;


public class Main {

    public static void main(String[] args) {

        IDataset dataset = Examples.extractFilterDataset("D://dict/syntagrus");




    }

    private static void compareClassifiers(){

        Collection<Function<IDataset, IMorphAnalyzer>> fromDataSetToAnalyzers =
                new ArrayList<>();

        //IClassifierTrainer bayes = getThresholdTrainer(
        //        new NaiveBayesClassifier(false, false, true));

        IClassifierTrainer svm  = Examples.getThresholdTrainer(new LibSVM());

        fromDataSetToAnalyzers.add(

                (d) -> {
                    //IMorphAnalyzerFactory ruleFact = new RuleApplicabilityFactory(d, svm);
                    try {
                        return new MorphAnalyzerLoader(new File("D://dict/svm")).create();
                        //return new MorphAnalyzerSaver(ruleFact, new File("D:/dict/svm")).create();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                });

        comparatorExample(fromDataSetToAnalyzers);
    }



    private static void comparatorExample(
            Collection<Function<IDataset, IMorphAnalyzer>> fromDataSetToAnalyzers) {


        IEvaluationCriteria criteria = new EvaluationCriteria();

        IDataset dataset = new DatasetParser(
                Parsers::RusCorpora,
                new File("D://dict")).getDataset();

        List<IDataset> splitted = dataset.split(99, new Random(0));

        // Create and launch
        QualityAssessment assessment = new QualityAssessment(
                fromDataSetToAnalyzers,
                splitted.get(0),
                splitted.get(1),
                criteria);


        Map<IMorphAnalyzer, QualityResult> res = assessment.start();

        for (Map.Entry<IMorphAnalyzer, QualityResult> entry : res.entrySet()) {
            System.out.println(entry.getKey().getClass().getName() + ":");
            System.out.println(entry.getValue().getInfo());
        }

    }


    private static String[] words = {
            "гоношилась", "подошёл", "невпечатляющих", "коготочка",
            "гаманка",      // based on "наркоманка" expected "гаманок"
            "шуфлядки",     // based on "оглядки"
            "млявого",      // based on "кудрявого"
            "сабан",        // based on "кабан"
            "студака"       // based on "чудака"
    };

    /*
    private static void testSegalovich() {
        File saveTo = new File("D://dict/aot.nlzr");

        List<File> dict = FileSearcher.getFileList(new File("D://dict"));



        IMorphAnalyzerFactory segFact = new MSFactory(parser.getDataset());

        try {
            IMorphAnalyzerFactory saver = new MorphAnalyzerSaver(segFact, saveTo);
            IMorphAnalyzer seg = saver.create();

            //IMorphAnalyzer aot = new MorphAnalyzerLoader(saveTo).create();

            tryAnalyze(seg, words);


        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }
    */

    private static void tryAnalyze(IMorphAnalyzer an, String... words) {

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
}