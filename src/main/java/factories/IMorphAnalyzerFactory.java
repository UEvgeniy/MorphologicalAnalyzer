package factories;


import analyzers.IMorphAnalyzer;

/**
 * An interface for classes that construct instances of classes implementing IMorphAnalyzer
 */
public interface IMorphAnalyzerFactory {
    IMorphAnalyzer create();
}