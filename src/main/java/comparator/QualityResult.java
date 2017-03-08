package comparator;

import datamodel.IWord;
import java.util.Objects;
import java.util.Set;

/**
 *
 */
public class QualityResult {

    private final double recall;
    private final double precision;
    private final Set<IWord> difficultWords;

    public QualityResult(double precision, double recall, Set<IWord> difficultWords){

        this.recall = recall;
        this.precision = precision;
        this.difficultWords = Objects.requireNonNull(difficultWords);
    }


    public double getRecall() {
        return recall;
    }

    public double getPrecision() {
        return precision;
    }

    public Set<IWord> getDifficultWords() {
        return difficultWords;
    }

    public String getInfo(){
        StringBuilder res = new StringBuilder();

        res.append("Precision: ").append(precision).append("\n")
                .append("Recall: ").append(recall).append("\n")
                .append("Incorrect predictions for:\n");
        for (IWord word : difficultWords){
            res.append("\t").append(word.getWord()).append("\n");
        }

        return res.toString();
    }
}
