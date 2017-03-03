package comparator;

import datamodel.IWord;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Simple class of comparing expected and actual answers
 */
public class EvaluationCriteria implements IEvaluationCriteria {


    @Override
    public QualityResult evaluate(Collection<IWord> suggested, Collection<IWord> actual) {

        StringBuilder result = new StringBuilder();

        if (suggested.isEmpty()){
            return new QualityResult(0, 0, actual);
        }

        int correct = 0;

        Collection<IWord> difficultWords = new ArrayList<>();

        for (IWord word : actual){
            if (suggested.contains(word))
                correct ++;
            else{
                difficultWords.add(word);
            }
        }

        return new QualityResult(
                (double) correct / suggested.size(),
                (double) correct / actual.size(),
                difficultWords);
    }
}
