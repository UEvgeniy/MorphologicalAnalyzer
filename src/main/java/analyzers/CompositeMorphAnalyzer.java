package analyzers;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import datamodel.IWord;

/**
 * Morphological analyzer which uses other analyzers
 */
public class CompositeMorphAnalyzer implements IMorphAnalyzer, Serializable {
	private static final long serialVersionUID = -5372967921686921026L;
	
	private List<IMorphAnalyzer> analyzers;

    public CompositeMorphAnalyzer(List<IMorphAnalyzer> analyzers){

        // Add not null factories to ArrayList
        this.analyzers = Objects.requireNonNull(analyzers);

        for (IMorphAnalyzer an : analyzers){
            if (an != null){
                this.analyzers.add(an);
            }
        }
    }

    public Collection<IWord> analyze (String word) {

        if (word == null){
            return new ArrayList<>();
        }

        // Use the first analyzer which is able to analyze the word
        for (IMorphAnalyzer an : analyzers) {
            if (an.canHandle(word)){
                return an.analyze(word);
            }
        }

        // In order to avoid this exception user should check the possibility of analyzer
        // by invoking canHandle() method
        final String EXC_MESSAGE = "No analyzer can analyze word \'" + word + "\'";
        throw new UnsupportedOperationException(EXC_MESSAGE);
    }


    public Boolean canHandle(String word) {

        for (IMorphAnalyzer an : analyzers) {
            if (an.canHandle(word)){
                return true;
            }
        }
        return false;
    }
}
