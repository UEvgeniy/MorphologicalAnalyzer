package factories;


import analyzers.IMorphAnalyzer;


public interface IMorphAnalyzerFactory {

    /**
     * Method constructs analyzer
     * @return Word's morphological analyzer
     */
    IMorphAnalyzer create();
}