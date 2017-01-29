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

    private List<File> files;

    public DictionaryFactory(File dataset) {

        files = FileSearcher.getFileList(
                Objects.requireNonNull(dataset, "Data set cannot be null."),
                ".xhtml"
        );
    }

    @Override
    public IMorphAnalyzer create() {

        // todo replace constructor argument File -> Parser
        RusCorporaParser parser = new RusCorporaParser(files);
        HashSet<IWord> words = parser.getDictionary();

        return new DictionaryMorphAnalyzer(words);
    }
}
