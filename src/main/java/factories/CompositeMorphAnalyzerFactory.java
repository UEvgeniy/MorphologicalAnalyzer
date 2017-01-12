package factories;


import analyzers.CompositeMorphAnalyzer;
import analyzers.IMorphAnalyzer;

/**
 * Factory for creating instances of CompositeMMorphAnalyzer
 */
public class CompositeMorphAnalyzerFactory implements IMorphAnalyzerFactory {

    private String msg;

    public CompositeMorphAnalyzerFactory(String message){
        msg = message;
    }


    public IMorphAnalyzer create() {
        // todo remove
        return new CompositeMorphAnalyzer(msg);
    }
}
