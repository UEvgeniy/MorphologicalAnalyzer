package ru.ispras;

import analyzers.IMorphAnalyzer;
import datamodel.IWord;
import datamodel.Word;
import factories.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class Main {

    public static void main(String[] args) {

        // A .xhtml file or folder containing .xhtml files (RusCorpora)
        final File dict = new File("D://dict");

        // File for saving dictionary
        final File saveTo = new File("D://analyzer.nlzr");

        // Can analyze only words with an even word length and define lemma and properties as 42
        IMorphAnalyzerFactory fact42 = veryPlainAnalyzer();


        try {

            // Parse dictionary and save it
            IMorphAnalyzerFactory dictionary = new DictionaryFactory(dict);
            IMorphAnalyzerFactory saver = new MorphAnalyzerSaver(dictionary, saveTo.toPath());
            saver.create();

            // Load saved analyzer
            MorphAnalyzerLoader loader = new MorphAnalyzerLoader(saveTo.toPath());

            // Order of arguments in constructor is IMPORTANT!
            // Swap arguments and analyzer behavior may be changed.
            CompositeMorphAnalyzerFactory comp = new CompositeMorphAnalyzerFactory(fact42, loader);

            IMorphAnalyzer analyzer = comp.create();

            // Recommended to add words with odd/even length and also words from parsed dictionary
            tryAnalyze(analyzer, "и");
            tryAnalyze(analyzer, "делал");
            tryAnalyze(analyzer, "менять");
            tryAnalyze(analyzer, "стимулом");
            tryAnalyze(analyzer, "что");

        } catch (IOException e) {
            System.out.print(e.getMessage());
        }


    }

    private static void tryAnalyze(IMorphAnalyzer an, String word){
        if (an.canHandle(word)) {
            IWord w = an.analyze(word).iterator().next();
            System.out.println("WORD: " + w.toString() + " LEMMA: " + w.getLemma() + " PROPS: " + w.getProperties() );
        }
        else{
            System.out.println("Word " + word + " cannot be analyzed");
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

