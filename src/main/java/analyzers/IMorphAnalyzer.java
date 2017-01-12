package analyzers;


import datamodel.IWord;

import java.io.Serializable;
import java.util.Collection;


/**
 * An interface for classes that figure out PoS and morphological properties
 */
public interface IMorphAnalyzer extends Serializable {
    Collection<IWord> analyze(String word);
    Boolean canHandle(String word);
}