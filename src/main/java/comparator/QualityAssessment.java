package comparator;

import analyzers.IMorphAnalyzer;
import datamodel.IDataset;
import datamodel.IWord;
import factories.IMorphAnalyzerFactory;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;


/**
 * A tool for comparing analyzers
 */
public class QualityAssessment {

    private final List<IMorphAnalyzer> analyzers;

    private final Map<String, Set<IWord>> test;
    private final IEvaluationCriteria criteria;

    public QualityAssessment(Collection<Function<IDataset, IMorphAnalyzer>> functions,
                             IDataset train, IDataset test,
                             IEvaluationCriteria criteria) {

        analyzers = new ArrayList<>();

        // Add analysers to collection
        for (Function<IDataset, IMorphAnalyzer> func : functions){
            analyzers.add(func.apply(train));
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

        double result_precision = 0;
        double result_recall = 0;
        Set<IWord> difWords = new HashSet<>();

        for (Map.Entry<String, Set<IWord>> entry : test.entrySet()){

            QualityResult forWord = criteria.evaluate(
                    analyzer.analyze(entry.getKey()),
                    entry.getValue());

            result_precision += forWord.getRecall();
            result_recall += forWord.getPrecision();
            difWords.addAll(forWord.getDifficultWords());
        }

        return new QualityResult(result_precision / test.size(),
                result_recall / test.size(), difWords);
    }



    private Map<String, Set<IWord>> formTestData(IDataset test){

        Map<String, Set<IWord>> result = new HashMap<>();

        for (IWord word : test.get()){

            if (!result.containsKey(word.getWord())){
                result.put(word.getWord(), new HashSet<>());
            }

            result.get(word.getWord()).add(word);
        }

        return result;
    }
}
