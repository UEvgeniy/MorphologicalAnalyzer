package analyzers;


import datamodel.IWord;

import java.util.Collection;


/**
 * An interface for classes that compute PoS and morphological properties
 */
public interface IMorphAnalyzer {
    /**
     * Method executes the analysis of the input word
     * @param word Analyzed word
     * @return Collection of possible properties of the word
     */
    Collection<IWord> analyze(String word);

    /**
     * Method checks the possibility of analyzing the word
     * @param word Analyzed word
     * @return True, if analyzer is able to analyze the word. False, if not.
     */
    Boolean canHandle(String word);
}