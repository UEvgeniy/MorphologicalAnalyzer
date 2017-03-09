package comparator;

import datamodel.IWord;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple class of comparing expected and actual answers
 */
public class EvaluationCriteria implements IEvaluationCriteria {


    @Override
    public QualityResult evaluate(Set<IWord> predicted, Set<IWord> correct) {


        if (predicted.isEmpty()){
            return new QualityResult(0, 0, correct);
        }

        int counter = 0;

        Set<IWord> difficultWords = new HashSet<>();

        for (IWord word : correct){
            if (predicted.contains(word))
                counter ++;
            else{
                difficultWords.add(word);
            }
        }


        return new QualityResult(
                (double) counter / predicted.size(),
                (double) counter / correct.size(),
                difficultWords);
    }
}
