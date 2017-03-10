package rule_applicability_reg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    static Map<Integer, Double> get(String word, int N){

        if (N < 1){
            throw new IllegalArgumentException("N value must be positive.");
        }

        List<String> ngrams = new ArrayList<>();

        Map<Integer, Double> result = new HashMap<>();

        if (word.length() <= N) {
            result.put(getOrder(word), 1.0);
            return result;
        }

        for (int i = 0; i < word.length() - N + 1; i++){

            int order = getOrder(word.substring(i, i + N));

            if (result.containsKey(order)){
                result.put(order, result.get(order) + 1);
            }
            else{
                result.put(order, 1.0);
            }
        }

        return result;
    }

    private static int getOrder(String str){

        int res = 0;

        for (int i = 0; i < str.length(); i++){
            res += (str.charAt(i) - 'а') * Math.pow(33, str.length() - 1 - i);
        }

        return Math.abs(res);
    }
}
