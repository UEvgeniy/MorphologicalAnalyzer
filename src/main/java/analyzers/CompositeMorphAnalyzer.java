package analyzers;


import datamodel.IWord;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Morphological analyzer which uses other analyzers
 */
public class CompositeMorphAnalyzer implements IMorphAnalyzer, Serializable {

    private static final long serialVersionUID = -707741344160142770L;
	private List<IMorphAnalyzer> analyzers;

    public CompositeMorphAnalyzer(List<IMorphAnalyzer> analyzers){
        this.analyzers = Objects.requireNonNull(analyzers, "List of analyzers cannot be null");
    }


    @Override
    public Collection<IWord> analyze (String word) {

        // Use the first analyzer which is able to analyze the word
        for (IMorphAnalyzer an : analyzers) {
            if (an.canHandle(word)){
                return an.analyze(word);
            }
        }

        // In order to avoid this exception user should check the possibility of analyzer
        // by invoking canHandle() method
        throw new RuntimeException("No analyzer can analyze word \'" + word + "\'");
    }


    @Override
    public Boolean canHandle(String word) {

        for (IMorphAnalyzer an : analyzers) {
            if (an.canHandle(word)){
                return true;
            }
        }
        return false;
    }
}
