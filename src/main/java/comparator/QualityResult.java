package comparator;

import datamodel.IWord;

import java.util.Collection;
import java.util.Objects;

/**
 *
 */
public class QualityResult {

    private final double accuracy;
    private final double recall;
    private final Collection<IWord> difficultWords;

    public QualityResult(double accuracy, double recall, Collection<IWord> difficultWords){

        this.accuracy = accuracy;
        this.recall = recall;
        this.difficultWords = Objects.requireNonNull(difficultWords);

    }


    public double getAccuracy() {
        return accuracy;
    }

    public double getRecall() {
        return recall;
    }

    public Collection<IWord> getDifficultWords() {
        return difficultWords;
    }

    public String getInfo(){
        StringBuilder res = new StringBuilder();

        res.append("Accuracy: ").append(accuracy).append("\n")
                .append("Recall: ").append(recall).append("\n")
                .append("Incorrect predictions for:\n");
        for (IWord word : difficultWords){
            res.append("\t").append(word.getWord()).append("\n");
        }

        return res.toString();
    }
}
