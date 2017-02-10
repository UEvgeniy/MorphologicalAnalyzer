package factories;

import analyzers.DictionaryMorphAnalyzer;
import analyzers.IMorphAnalyzer;
import datamodel.IWord;
import helpers.FileSearcher;

import java.io.File;
import java.util.*;

/**
 * Class forms dictionary from texts, saves it and creates DictionaryMorphAnalyzer instance.
 */
public class DictionaryFactory implements IMorphAnalyzerFactory {

    private File file;

    public DictionaryFactory(File dataset) {
        file = Objects.requireNonNull(dataset, "Data set cannot be null.");
    }

    @Override
    public IMorphAnalyzer create() {

        RusCorporaParser parser = new RusCorporaParser(file);
        HashSet<IWord> words = parser.getDictionary();

        return new DictionaryMorphAnalyzer(words);
    }
}
