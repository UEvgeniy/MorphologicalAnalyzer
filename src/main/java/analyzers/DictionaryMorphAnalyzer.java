package analyzers;

import datamodel.IWord;

import java.util.Collection;

/**
 * Model figures out POS + properties by searching the word in the dictionary
 */
public class DictionaryMorphAnalyzer implements IMorphAnalyzer {

    // todo add dataset ?

    public Collection<IWord> analyze(String word) {
        return null;
    }

    public Boolean canHandle(String word) {
        // return dictionary.contains(word);
        return null;
    }
}
