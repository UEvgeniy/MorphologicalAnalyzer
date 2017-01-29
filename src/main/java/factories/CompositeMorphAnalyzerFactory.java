package factories;


import analyzers.CompositeMorphAnalyzer;
import analyzers.IMorphAnalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Factory for creating instances of CompositeMMorphAnalyzer
 */
public class CompositeMorphAnalyzerFactory implements IMorphAnalyzerFactory {

    private List<IMorphAnalyzerFactory> factories;

    public CompositeMorphAnalyzerFactory(IMorphAnalyzerFactory... factories) {

        this.factories = new ArrayList<>();

        for (IMorphAnalyzerFactory f : Objects.requireNonNull(factories, "Collection cannot be null")){
            this.factories.add(Objects.requireNonNull(f, "Analyzer cannot be null"));
        }
    }

    @Override
    public IMorphAnalyzer create() {
        List<IMorphAnalyzer> analyzers = new ArrayList<>();

        for (IMorphAnalyzerFactory f : factories){
            analyzers.add(f.create());
        }

        return new CompositeMorphAnalyzer(analyzers);
    }
}
