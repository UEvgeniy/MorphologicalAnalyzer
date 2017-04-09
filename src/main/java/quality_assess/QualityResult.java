package quality_assess;

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

    QualityResult(double precision, double recall, Set<IWord> difficultWords){

        this.recall = recall;
        this.precision = precision;
        this.difficultWords = Objects.requireNonNull(difficultWords);
    }


    double getRecall() {
        return recall;
    }

    double getPrecision() {
        return precision;
    }

    Set<IWord> getDifficultWords() {
        return difficultWords;
    }

    public String getInfo(Info info){
        StringBuilder res = new StringBuilder();

        if (info != Info.Recall) {
            res.append("Precision: ").append(round(precision, 2)).append("\n");
        }

        if (info != Info.Precision) {
            res.append("Recall: ").append(round(recall, 2)).append("\n");
        }

        if (info == Info.PrecisionRecallDiffWords) {
            res.append("Incorrect predictions for:\n");
            for (IWord word : difficultWords) {
                res.append("\t").append(word.getWord()).append("\n");
            }
        }
        return res.toString();
    }

    private static double round(double val, int places){
        if (places < 0)
            throw new IllegalArgumentException();
        double tmp = Math.pow(10, places);
        return (double)Math.round(val * tmp) / tmp;
    }

    public enum Info{
        Precision, Recall, PrecisionRecall, PrecisionRecallDiffWords
    }
}
