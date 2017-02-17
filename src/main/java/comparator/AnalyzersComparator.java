package comparator;

import analyzers.IMorphAnalyzer;
import datamodel.IWord;
import factories.IDatasetParser;



import java.io.PrintStream;
import java.util.*;


/**
 * A tool for comparing analyzers
 */
public class AnalyzersComparator {

    private final List<IMorphAnalyzer> analyzers;
    private final IDatasetParser parser;
    private final IEvaluationCriteria criteria;

    public AnalyzersComparator(List<IMorphAnalyzer> analyzers,
                               IDatasetParser parser, IEvaluationCriteria criteria){
        this.analyzers = Objects.requireNonNull(analyzers);
        this.parser = Objects.requireNonNull(parser);
        this.criteria = Objects.requireNonNull(criteria);
    }


    public void start(PrintStream os){

        Set<IWord> words = parser.getDictionary();


        Set<IWord> train =  new HashSet<>();
        Set<IWord> test = new HashSet<>();

        random_separation(words, train, test, 90);

        // todo universal solution for initializing different classes

        for (IWord word: words){
            String str = word.getWord();

            for (IMorphAnalyzer analyzer: analyzers){
                Collection<IWord> suggested = analyzer.analyze(str);

                // todo form map<String, Collection<IWords>>
                criteria.evaluate(suggested, null);

            }
        }


    }

    // Not accurate for small sets
    private static void random_separation(Set<IWord> source,
                                          Set<IWord> train,
                                          Set<IWord> test,
                                          int percentage){
        if (percentage < 0 || percentage > 100){
            throw new IllegalArgumentException
                    ("Percentage must be in range [0; 100]");
        }

        Objects.requireNonNull(source);
        Objects.requireNonNull(train);
        Objects.requireNonNull(test);

        List<IWord> list = new ArrayList<>(source);
        Random rnd = new Random();

        for (IWord aList : list) {
            if (rnd.nextDouble() < ((float) percentage / 100)) {
                source.add(aList);
            } else {
                test.add(aList);
            }
        }

    }
}
