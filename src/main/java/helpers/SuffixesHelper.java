package helpers;


import java.util.Objects;

public class SuffixesHelper {

	/**
	 * @param word First string
	 * @param lemma Second string
     * @return The longest common substring of two strings from beginning
     */
	public static short getCommonPrefixLength(String word, String lemma) {

		checkNotNull(word, lemma);

		short i = 0;
		while (i < Math.min(word.length(), lemma.length())){
		    if (word.charAt(i) == lemma.charAt(i))
		        i++;
		    else
		        break;
		}
		return i;
	}

	public static short getCommonSuffixesLength(String word, String lemma){

		checkNotNull(word, lemma);

		short i = 0;
		while (i < Math.min(word.length(), lemma.length())){
			if (word.charAt(word.length() - 1 - i) == lemma.charAt(lemma.length() - 1 - i))
				i++;
			else
				break;
		}
		return i;
	}

	private static void checkNotNull(String... values){
		for (String str: values){
			Objects.requireNonNull(str);
		}
	}
}
