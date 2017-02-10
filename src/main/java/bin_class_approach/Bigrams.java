package bin_class_approach;

import java.util.ArrayList;
import java.util.List;

/**
 * The only method forms the bigram for the word
 */
class Bigrams {

    static List<String> get(String word){
        List<String> bigrams = new ArrayList<>();
        for (int i = 0; i < word.length() - 1; i++){
            bigrams.add(word.substring(i, i + 2));
        }
        return bigrams;
    }
}
