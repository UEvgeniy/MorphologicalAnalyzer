package analyzers;


import datamodel.IWord;
import java.util.Collection;

/**
 * Morphological analyzer which uses other analyzers todo ?
 */
public class CompositeMorphAnalyzer implements IMorphAnalyzer {

    String msg;
    public CompositeMorphAnalyzer(String message){
        msg = message;
    }

    public Collection<IWord> analyze(String word) {
        System.out.println(msg);
        return null;
    }

    public Boolean canHandle(String word) {
        return null;
    }
}
