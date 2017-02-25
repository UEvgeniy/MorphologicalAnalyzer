package factories;

import analyzers.DictionaryMorphAnalyzer;
import analyzers.IMorphAnalyzer;
import datamodel.IDataset;
import datamodel.IWord;
import helpers.FileSearcher;

import java.io.File;
import java.util.*;

/**
 * Class forms dictionary from texts, saves it and creates DictionaryMorphAnalyzer instance.
 */
public class DictionaryFactory implements IMorphAnalyzerFactory {

    private final IDataset dataset;

    public DictionaryFactory(IDataset dictionary) {
        dataset = Objects.requireNonNull(dictionary, "Data set cannot be null.");
    }

    @Override
    public IMorphAnalyzer create() {

        return new DictionaryMorphAnalyzer(dataset);
    }
}
