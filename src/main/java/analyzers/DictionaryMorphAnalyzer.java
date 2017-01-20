package analyzers;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import datamodel.IWord;

/**
 * Model figures out POS + properties by searching the word in the dictionary
 */
public class DictionaryMorphAnalyzer implements IMorphAnalyzer {

    private Set<IWord> dictionary;

    public DictionaryMorphAnalyzer(Set<IWord> words){

        if (words == null || words.size() == 0){
            final String EXC_MESSAGE = "Dictionary cannot be empty.";
            throw new IllegalArgumentException(EXC_MESSAGE);
        }

        dictionary = words;

    }


    public Collection<IWord> analyze(String word) {
        return dictionary
                .stream()
                .filter(iWord -> iWord.toString().equals(word.toLowerCase()))
                .collect(Collectors.toList());
    }


    public Boolean canHandle(String word) {
        return dictionary
                .stream()
                .anyMatch(iword -> iword.toString().equals(word.toLowerCase()));
    }
}
