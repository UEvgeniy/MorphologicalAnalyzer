package factories;


import analyzers.CompositeMorphAnalyzer;
import analyzers.IMorphAnalyzer;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating instances of CompositeMMorphAnalyzer
 */
public class CompositeMorphAnalyzerFactory implements IMorphAnalyzerFactory {

    private List<IMorphAnalyzerFactory> factories;

    public CompositeMorphAnalyzerFactory(IMorphAnalyzerFactory... factories) {

        // If composite factory includes no factories than throw exception
        if (factories == null || factories.length == 0){
            final String EXC_MESSAGE = "Constructor must contain at least one not null Analyzer Factory";
            throw new IllegalArgumentException(EXC_MESSAGE);
        }

        // Add not null factories to ArrayList
        this.factories = new ArrayList<>();

        for (IMorphAnalyzerFactory f : factories){
            if (f != null){
                this.factories.add(f);
            }
        }


    }


    public IMorphAnalyzer create() {
        List<IMorphAnalyzer> analyzers = new ArrayList<>();

        for (IMorphAnalyzerFactory f : factories){
            analyzers.add(f.create());
        }

        return new CompositeMorphAnalyzer(analyzers);
    }
}
