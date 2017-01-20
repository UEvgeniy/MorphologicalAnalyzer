package analyzers;


import java.util.Collection;

import datamodel.IWord;


/**
 * An interface for classes that figure out PoS and morphological properties
 */
public interface IMorphAnalyzer {
    Collection<IWord> analyze(String word);
    Boolean canHandle(String word);
}