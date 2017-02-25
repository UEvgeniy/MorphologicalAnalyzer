package rule_applicability_reg;

import java.util.ArrayList;
import java.util.List;

/**
 * The only method forms the bigram for the word
 */
class NGrams {

    /**
     * Method divides the word at list of pieces of N length
     * @param word The string broken into pieces
     * @param N The length of pieces
     * @return The list of word's pieces
     */
    static List<String> get(String word, int N){

        if (N < 1){
            throw new IllegalArgumentException("N value must be positive.");
        }

        List<String> ngrams = new ArrayList<>();

        if (word.length() <= N) {
            ngrams.add(word);
            return ngrams;
        }

        for (int i = 0; i < word.length() - N + 1; i++){
            ngrams.add(word.substring(i, i + N));
        }

        return ngrams;
    }
}
