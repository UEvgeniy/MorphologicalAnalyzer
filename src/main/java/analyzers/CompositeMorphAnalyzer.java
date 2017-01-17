package analyzers;


import datamodel.IWord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Morphological analyzer which uses other analyzers
 */
public class CompositeMorphAnalyzer implements IMorphAnalyzer {

    private List<IMorphAnalyzer> analyzers;

    public CompositeMorphAnalyzer(List<IMorphAnalyzer> analyzers){

        // If composite analyzer does not include not null analyzer than throw exception
        if (analyzers == null || analyzers.size() == 0){
            final String EXC_MESSAGE = "Constructor must contain at least one Morphological Analyzer";
            throw new IllegalArgumentException(EXC_MESSAGE);
        }

        // Add not null factories to ArrayList
        this.analyzers = new ArrayList<>();

        for (IMorphAnalyzer an : analyzers){
            if (an != null){
                this.analyzers.add(an);
            }
        }
    }

    public Collection<IWord> analyze (String word) {

        if (word == null){
            return null;
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

        // todo add - symbol to RegExp
        if (word == null || !word.matches("^[а-я|А-Я]+$")){
            return false;
        }

        for (IMorphAnalyzer an : analyzers) {
            if (an.canHandle(word)){
                return true;
            }
        }
        return false;
    }
}
