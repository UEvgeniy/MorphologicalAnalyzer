package factories;

import analyzers.IMorphAnalyzer;
import analyzers.MorphemeBasedMorphAnalyzer;
import datamodel.IWord;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Class uses machine learning for predicting POS + properties of the word
 */
public class MorphAnalyzerTrainer implements IMorphAnalyzerFactory{

    private List<File> files;

    public MorphAnalyzerTrainer(File dataset) {

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

        IPropertyPredictorFactory propertyPredictorFactory =
                new LemmaRulePropertyPredictorTrainer();

        MorphemeExtractorTrainer morphemeExtractorTrainer = new MorphemeExtractorTrainer(words);

        return new MorphemeBasedMorphAnalyzer(
                morphemeExtractorTrainer.train(),
                propertyPredictorFactory.create()
        );
    }
}
