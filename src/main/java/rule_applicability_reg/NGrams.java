package rule_applicability_reg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import datamodel.MorphemedWord;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;

/**
 * The only method forms the bigram for the word
 */
public class NGrams implements Function<MorphemedWord, Instance>{

	private final int ngramSize;
	private static Map<String, Integer> collectionSubstrings;

	static {
	    collectionSubstrings = new HashMap<>();
    }
	
    public NGrams(int ngramSize) {
		this.ngramSize = ngramSize;
	}

	@Override
	public Instance apply(MorphemedWord word) {
		Instance instance = new SparseInstance();
		instance.putAll(NGrams.get(word.getRoot(), ngramSize));
		return instance;
	}


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

        word = new String(new char[N - 1])
                .concat(word)
                .concat(new String(new char[N - 1]));


        Map<Integer, Double> result = new HashMap<>();

        if (word.length() <= N) {
            result.put(getOrder(word), 1.0);
            return result;
        }

        for (int i = 0; i < word.length() - N + 1; i++){

            int order = getOrder(word.substring(i, i + N));

            result.merge(order, 1.0, (a, b) -> a+b);
        }

        return result;
    }

    private static int getOrder(String str){

        return collectionSubstrings.computeIfAbsent(
                str,
                (a) -> collectionSubstrings.size()
        );
    }
}
