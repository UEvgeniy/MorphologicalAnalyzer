package analyzers;

import datamodel.IDataset;
import datamodel.IWord;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Model figures out POS + properties by searching the word in the dictionary
 */
public class DictionaryMorphAnalyzer implements IMorphAnalyzer, Serializable {

    private static final long serialVersionUID = -760462216430499658L;
    private Set<IWord> dictionary;

    public DictionaryMorphAnalyzer(IDataset dictionary){

        this.dictionary = Objects.requireNonNull(dictionary.get(),
                "Dictionary cannot be empty.");
    }

    @Override
    public Collection<IWord> analyze(String word) {
        return dictionary
                .stream()
                .filter(iWord -> iWord.getWord().equals(word.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean canHandle(String word) {
        return dictionary
                .stream()
                .anyMatch(iWord -> iWord.getWord().equals(word.toLowerCase()));
    }
}
