package comparator;

import analyzers.IMorphAnalyzer;
import aot_based.AotBasedFactory;
import datamodel.IDataset;
import datamodel.IWord;
import factories.IDatasetParser;
import factories.IMorphAnalyzerFactory;


import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


/**
 * A tool for comparing analyzers
 */
public class AnalyzersComparator {

    private final List<IMorphAnalyzer> analyzers;

    private final Map<String, Collection<IWord>> test;
    private final IEvaluationCriteria criteria;

    public AnalyzersComparator(Collection<Constructor> factories,
                               IDataset train, IDataset test, IEvaluationCriteria criteria)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {

        analyzers = new ArrayList<>();

        for (Constructor c : factories) {
            analyzers.add(((IMorphAnalyzerFactory) c.newInstance(train)).create());

        }

        this.test = Objects.requireNonNull(formTestData(test));
        this.criteria = Objects.requireNonNull(criteria);
    }


    public Map<IMorphAnalyzer, QualityResult> start() {

        Map<IMorphAnalyzer, QualityResult> res = new HashMap<>();

        for (IMorphAnalyzer analyzer : analyzers){

            res.put(analyzer, getQuality(analyzer));
        }

        return res;

    }

    private QualityResult getQuality(IMorphAnalyzer analyzer){

        double result_acc = 0;
        double result_rec = 0;
        Collection<IWord> difWords = new HashSet<>();

        for (Map.Entry<String, Collection<IWord>> entry : test.entrySet()){

            QualityResult forWord = criteria.evaluate(
                    analyzer.analyze(entry.getKey()),
                    entry.getValue());

            result_acc += forWord.getAccuracy();
            result_rec += forWord.getRecall();
            difWords.addAll(forWord.getDifficultWords());
        }

        return new QualityResult(result_acc / test.size(),
                result_rec / test.size(), difWords);
    }



    private Map<String, Collection<IWord>> formTestData(IDataset test){

        Map<String, Collection<IWord>> result = new HashMap<>();

        for (IWord word : test.get()){

            if (!result.containsKey(word.getWord())){
                result.put(word.getWord(), new HashSet<>());
            }

            result.get(word.getWord()).add(word);
        }

        return result;
    }
}
