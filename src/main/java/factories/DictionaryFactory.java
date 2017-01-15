package factories;

import analyzers.DictionaryMorphAnalyzer;
import analyzers.IMorphAnalyzer;
import datamodel.IWord;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Class forms dictionary from texts, saves it and creates DictionaryMorphAnalyzer instance.
 */
public class DictionaryFactory implements IMorphAnalyzerFactory {

    private List<File> files;

    /**
     * @param dataset Dataset is a text file or a folder containing text files.
     */
    public DictionaryFactory(File dataset) {

        if (dataset == null) {
            final String EXC_MESSAGE = "Data set in Dictionary factory cannot be null.";
            throw new IllegalArgumentException(EXC_MESSAGE);
        }

        files = new ArrayList<>();

        // If dataset is directory, than read all files from this dir
        if (dataset.isDirectory()) {
            File[] filesInDir = dataset.listFiles();

            if (filesInDir == null || filesInDir.length == 0) {
                final String EXC_MESSAGE = "Dataset directory must contain at least one file.";
                throw new IllegalArgumentException(EXC_MESSAGE);
            }

            files = Arrays.asList(filesInDir);
        }
        // else dataset is considered as file
        else {
            files.add(dataset);
        }
    }

    public IMorphAnalyzer create() {

        RusCorporaParser parser = new RusCorporaParser(files);
        HashSet<IWord> words = parser.getDictionary();

        return new DictionaryMorphAnalyzer(words);
    }



}
