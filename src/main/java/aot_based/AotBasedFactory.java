package aot_based;

import analyzers.IMorphAnalyzer;
import factories.IDatasetParser;
import factories.IMorphAnalyzerFactory;

import java.util.Objects;

/**
 * This approach based on http://www.aot.ru/docs/sokirko/Dialog2004.htm description
 */
public class AotBasedFactory implements IMorphAnalyzerFactory{

    private final IDatasetParser parser;

    public AotBasedFactory(IDatasetParser parser){
        this.parser = Objects.requireNonNull(parser);
    }


    @Override
    public IMorphAnalyzer create() {
        return new AotBasedAnalyzer(parser.getDictionary());
    }
}
