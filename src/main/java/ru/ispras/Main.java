package ru.ispras;

import analyzers.IMorphAnalyzer;
import aot_based.AotBasedAnalyzer;
import aot_based.AotBasedFactory;
import comparator.QualityAssessment;
import comparator.EvaluationCriteria;
import comparator.IEvaluationCriteria;
import comparator.QualityResult;
import datamodel.IDataset;
import datamodel.IWord;
import factories.*;
import helpers.FileSearcher;
import maslov_segalovich_based.MSFactory;
import rule_applicability_reg.BayesRuleApplicabilityFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

public class Main {

    public static void main(String[] args) {

        /*IDataset dataset = new RusCorporaParser(new File("D:/dict")).getDataset();

        AotBasedFactory fact = new AotBasedFactory(dataset);

        IMorphAnalyzer analyzer = fact.create();

        tryAnalyze(analyzer, words);*/

        comparatorExample();


        //comparatorExample();
    }

    private static void comparatorExample() {
        IEvaluationCriteria criteria = new EvaluationCriteria();

        IDataset dataset = new RusCorporaParser(new File("D:/dict")).getDataset();

        List<IDataset> splitted = dataset.split(99, new Random(0));

        Collection<Function<IDataset, IMorphAnalyzer>> funcs =
                new ArrayList<>();

        funcs.add(iDataset -> new AotBasedFactory(iDataset).create());
        //funcs.add(iDataset -> new BayesRuleApplicabilityFactory(iDataset).create());

        QualityAssessment assessment = new QualityAssessment(
                funcs,
                splitted.get(0),
                splitted.get(1),
                criteria);


        Map<IMorphAnalyzer, QualityResult> res = assessment.start();

        for (Map.Entry<IMorphAnalyzer, QualityResult> entry : res.entrySet()) {
            System.out.println(entry.getKey().getClass().getName() + ":");
            System.out.println(entry.getValue().getInfo());
        }

    }

    private static void binClassApr() {

        IDatasetParser parser = new RusCorporaParser(new File("D:/dict/texts"));

        IMorphAnalyzerFactory fact = new BayesRuleApplicabilityFactory(parser.getDataset());

        File analyzer = new File("D:/dict/rules.nlzr");

        IMorphAnalyzer my = fact.create();
        /*try {
            //my = new MorphAnalyzerSaver(fact, analyzer.toPath()).create();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
*/
        tryAnalyze(my, words);
    }

    private static String[] words = {
            "гоношилась", "подошел", "невпечатляющих", "коготочка",
            "гаманка",      // based on "наркоманка" expected "гаманок"
            "шуфлядки",     // based on "оглядки"
            "млявого",      // based on "кудрявого"
            "сабан",        // based on "кабан"
            "студака"       // based on "чудака"
    };


    private static void testSegalovich() {
        Path saveTo = new File("D://dict/aot.nlzr").toPath();

        List<File> dict = FileSearcher.getFileList(new File("D://dict"), ".xhtml");

        IDatasetParser parser = new RusCorporaParser(null);

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

    private static void testAllAnalyzers() {
        // A .xhtml file or folder containing .xhtml files (RusCorpora)
        final File dict = new File("D://dict");

        // File for saving dictionary
        final File saveTo = new File("D://analyzer.nlzr");

        // Can analyze only words with an even word length and define lemma and properties as 42
        //IMorphAnalyzerFactory fact42 = veryPlainAnalyzer();

        IDatasetParser parser = new RusCorporaParser(null);


        try {

            // Parse dictionary and save it
            IDataset dataset = new RusCorporaParser(dict).getDataset();
            IMorphAnalyzerFactory dictFactory = new DictionaryFactory(dataset);
            IMorphAnalyzerFactory saver = new MorphAnalyzerSaver(dictFactory, saveTo.toPath());
            saver.create();

            // Load saved analyzer
            MorphAnalyzerLoader loader = new MorphAnalyzerLoader(saveTo.toPath());

            // Order of arguments in constructor is IMPORTANT!
            // Swap arguments and analyzer behavior may be changed.
            CompositeMorphAnalyzerFactory comp = new CompositeMorphAnalyzerFactory(loader);

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

    private static void tryAnalyze(IMorphAnalyzer an, String... words) {

        for (String word : words) {
            if (an.canHandle(word)) {
                Collection<IWord> answers = an.analyze(word);
                System.out.println("WORD: " + word);
                for (IWord w : answers) {
                    System.out.println("\tLEMMA: " + w.getLemma() + "\tPROPS: " + w.getProperties().get());
                }
            } else {
                System.out.println("Word " + word + "\n\tcannot be analyzed");
            }
        }
    }
}