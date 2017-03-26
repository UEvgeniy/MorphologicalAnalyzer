package aot_based;

import analyzers.IMorphAnalyzer;
import datamodel.IDataset;
import factories.IMorphAnalyzerFactory;

import java.util.Objects;

/**
 * This approach based on http://www.aot.ru/docs/sokirko/Dialog2004.htm description
 */
public class AotBasedFactory implements IMorphAnalyzerFactory{

    private final IDataset dataset;

    public AotBasedFactory(IDataset dictionary){
        this.dataset = Objects.requireNonNull(dictionary);
    }


    @Override
    public IMorphAnalyzer create() {
        return new AotBasedAnalyzer(dataset);
    }
}
