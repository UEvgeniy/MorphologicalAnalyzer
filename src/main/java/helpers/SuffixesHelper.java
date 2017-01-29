package helpers;


public class SuffixesHelper {

	/**
	 * @param word First string
	 * @param lemma Second string
     * @return The longest common substring of two strings from beginning
     */
	public static short getCommonPrefixLength(String word, String lemma) {
		short i = 0;

		while (i < Math.min(word.length(), lemma.length())){
		    if (word.charAt(i) == lemma.charAt(i))
		        i++;
		    else
		        break;
		}
		return i;
	}
}
