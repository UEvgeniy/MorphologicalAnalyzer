package ru.ispras;

public class Main {

    public static void main(String[] args) {




        // 2a. Find lemma + properties of the word from dictionary


        // 2b. Predict lemma + properties from new word

    }


    /*private static void factoriesUseExamples() {

        String word = "Something";

        // 1a. Factory for loading dictionary of words from file and training model
        IMorphAnalyzerFactory factory = new CompositeMorphAnalyzerFactory();
        IMorphAnalyzer analyzer = factory.create();
        analyzer.analyze(word); // for testing only, for now we don't need returning Collection


        String path = "D://analyzer.out";

        // 1b. Factory for saving analyzer...
        factory = new CompositeMorphAnalyzerFactory();
        try {
            MorphAnalyzerSaver saver = new MorphAnalyzerSaver(factory, Paths.get(path));
            analyzer = saver.create();
            saver.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        analyzer.analyze(word);

        // 1c. Factory for loading the model that was trained before

        try {
            MorphAnalyzerLoader loader = new MorphAnalyzerLoader(Paths.get(path));
            analyzer = loader.create();
            analyzer.analyze(word);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    */
}

