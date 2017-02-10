package ru.ispras;

import analyzers.IMorphAnalyzer;
import baseline.MorphAnalyzerTrainer;
import bin_class_approach.RulesApplicabilityFactory;
import bin_class_approach.naive_bayes.Classification;
import bin_class_approach.naive_bayes.BayesClassifier;
import datamodel.IWord;
import datamodel.Word;
import factories.*;
import helpers.FileSearcher;
import maslov_segalovich_based.MSFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        binClassApr();
    }

    private static void binClassApr(){

        IDatasetParser parser = new RusCorporaParser(new File("D:/dict/texts"));
        IMorphAnalyzerFactory fact = new RulesApplicabilityFactory(parser);

        IMorphAnalyzer my = fact.create();

        tryAnalyze(my, words);
    }

    private static String[] words = {
            "гоношилась", "подошел", "невпечатляющих","коготочка",
            "гаманка",      // based on "наркоманка" expected "гаманок"
            "шуфлядки",     // based on "оглядки"
            "млявого",      // based on "кудрявого"
            "сабан",        // based on "кабан"
            "студака"       // based on "чудака"
    };



    private static void testSegalovich(){
        Path saveTo = new File("D://dict/aot.nlzr").toPath();

        List<File> dict = FileSearcher.getFileList(new File("D://dict"), ".xhtml");

        IDatasetParser parser = new RusCorporaParser(null);

        IMorphAnalyzerFactory segFact = new MSFactory(parser);

        try {
            IMorphAnalyzerFactory saver = new MorphAnalyzerSaver(segFact, saveTo);
            IMorphAnalyzer seg = saver.create();

            //IMorphAnalyzer aot = new MorphAnalyzerLoader(saveTo).create();

            tryAnalyze(seg, words);


        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }

    private static void testAllAnalyzers(){
        // A .xhtml file or folder containing .xhtml files (RusCorpora)
        final File dict = new File("D://dict");

        // File for saving dictionary
        final File saveTo = new File("D://analyzer.nlzr");

        // Can analyze only words with an even word length and define lemma and properties as 42
        //IMorphAnalyzerFactory fact42 = veryPlainAnalyzer();

        IDatasetParser parser = new RusCorporaParser(null);

        // Baseline implementation of Morpheme Extractor and Property Predictor
        IMorphAnalyzerFactory mbma = new MorphAnalyzerTrainer(parser);

        try {

            // Parse dictionary and save it
            IMorphAnalyzerFactory dictFactory = new DictionaryFactory(dict);
            IMorphAnalyzerFactory saver = new MorphAnalyzerSaver(dictFactory, saveTo.toPath());
            saver.create();

            // Load saved analyzer
            MorphAnalyzerLoader loader = new MorphAnalyzerLoader(saveTo.toPath());

            // Order of arguments in constructor is IMPORTANT!
            // Swap arguments and analyzer behavior may be changed.
            CompositeMorphAnalyzerFactory comp = new CompositeMorphAnalyzerFactory(loader, mbma);

            IMorphAnalyzer analyzer = comp.create();

            // Recommended to add words with odd/even length and also words from parsed dictionary
            tryAnalyze(analyzer, "и");         // dictionary analyzer
            tryAnalyze(analyzer, "колобки");   // MorphemeBasedAnalyzer
            tryAnalyze(analyzer, "лестницы");     // MorphemeBasedAnalyzer
            tryAnalyze(analyzer, "скрывающееся");// MorphemeBasedAnalyzer
            tryAnalyze(analyzer, "менять");    //
            tryAnalyze(analyzer, "стимулом");  // dictionary analyzer
            tryAnalyze(analyzer, "что");       // dictionary analyzer

        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }

    private static void tryAnalyze(IMorphAnalyzer an, String... words){

        for (String word: words) {
            if (an.canHandle(word)) {
                Collection<IWord> answers = an.analyze(word);
                System.out.println("WORD: " + word);
                for (IWord w : answers) {
                    System.out.println("\tLEMMA: " + w.getLemma() + "\tPROPS: " + w.getProperties());
                }
            } else {
                System.out.println("Word " + word + "\n\tcannot be analyzed");
            }
        }
    }

    // This analyzer has no use and was created only for demonstration of CompositeAnalyzer
    private static IMorphAnalyzerFactory veryPlainAnalyzer(){

        return () -> new IMorphAnalyzer() {
            @Override
            public Collection<IWord> analyze(String word) {
                ArrayList<IWord> res = new ArrayList<>();
                res.add(new Word(word, "42", "42"));
                return res;
            }

            @Override
            public Boolean canHandle(String word) {
                return (word.length() % 2) == 0;
            }
        };
    }

}