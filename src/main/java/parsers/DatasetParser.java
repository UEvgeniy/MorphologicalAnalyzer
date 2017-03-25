package parsers;

import datamodel.DataSet;
import datamodel.IDataset;
import datamodel.IWord;
import helpers.FileSearcher;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;


/**
 * Common class for parsers
 */
public class DatasetParser {

    private IWordsExtractor extractor;
    private List<File> files;

    public DatasetParser(IWordsExtractor extractor, File file){
        this.extractor = Objects.requireNonNull(extractor);
        this.files = FileSearcher.getFileList(file);

    }

    public IDataset getDataset(){

        HashSet<IWord> words = new HashSet<>();

        for (File f : files) {

            words.addAll(
                    extractor.apply(f)
            );
        }

        return new DataSet(words);
    }
}
