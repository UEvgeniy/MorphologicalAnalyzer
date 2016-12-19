package analyzers;


import datamodel.IWord;
import java.util.Collection;


/**
 * An interface for classes that figure out PoS and morphological properties
 */
public interface IMorphAnalyzer {
    Collection<IWord> analyze(String word);
    Boolean canHandle(String word);
}