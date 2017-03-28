package quality_assess;

import analyzers.IMorphAnalyzer;
import datamodel.IDataset;
import datamodel.IWord;


import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * A tool for comparing analyzers
 */
public class QualityAssessment {

    private final List<IMorphAnalyzer> analyzers;

    private IEvaluationCriteria criteria;

    public QualityAssessment(Collection<Function<IDataset, IMorphAnalyzer>> functions,
                             IDataset train) {

        analyzers = new ArrayList<>();

        // Add analysers to collection
        for (Function<IDataset, IMorphAnalyzer> func : functions){
            analyzers.add(func.apply(train));
        }
    }


    public Map<IMorphAnalyzer, QualityResult> start(IDataset test, IEvaluationCriteria criteria) {

        this.criteria = Objects.requireNonNull(criteria);

        Map<IMorphAnalyzer, QualityResult> res = new HashMap<>();

        for (IMorphAnalyzer analyzer : analyzers){

            res.put(analyzer, getQuality(analyzer, test));
        }

        return res;
    }

    private QualityResult getQuality(IMorphAnalyzer analyzer, IDataset test){

        Map<String, Set<IWord>> correct = formTestData(test);

        double result_precision = 0;
        double result_recall = 0;
        Set<IWord> difWords = new HashSet<>();

        for (Map.Entry<String, Set<IWord>> entry : correct.entrySet()){

            QualityResult forWord = evaluate(
                    analyzer.analyze(entry.getKey()),
                    entry.getValue());

            result_precision += forWord.getPrecision();
            result_recall += forWord.getRecall();
            difWords.addAll(forWord.getDifficultWords());
        }

        return new QualityResult(result_precision / correct.size(),
                result_recall / correct.size(), difWords);
    }


    private QualityResult evaluate(Set<IWord> predicted, Set<IWord> correct) {

        if (predicted.isEmpty()){
            return new QualityResult(0, 0, correct);
        }

        Set<IWord> difficultWords = new HashSet<>();
        int precision = 0;
        int recall = 0;

        // Count Recall
        for (IWord word: correct){
            if (contains(predicted, word)){
                recall++;
            }
            else {
                difficultWords.add(word);
            }
        }

        // Count Precision
        for (IWord word: predicted){
            if (contains(correct, word)){
                precision++;
            }
        }


        return new QualityResult(
                (double) precision / predicted.size(),
                (double) recall / correct.size(),
                difficultWords);
    }

    private boolean contains(Set<IWord> set, IWord word){

        for (IWord w: set){
            if (criteria.evaluate(word, w)){
                return true;
            }
        }
        return false;
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
