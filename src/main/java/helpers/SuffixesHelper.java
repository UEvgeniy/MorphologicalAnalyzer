package helpers;


import java.util.Objects;

public class SuffixesHelper {

	/**
	 * @param w1 First string
	 * @param w2 Second string
     * @return The longest common substring of two strings from beginning
     */
	public static short getCommonPrefixLength(String w1, String w2) {

		checkNotNull(w1, w2);

		short i = 0;
		while (i < Math.min(w1.length(), w2.length())){
		    if (w1.charAt(i) == w2.charAt(i))
		        i++;
		    else
		        break;
		}
		return i;
	}

	/**
	 * @param w1 First string
	 * @param w2 Second string
	 * @return The longest common substring of two strings in the end
	 */
	public static short getCommonSuffixesLength(String w1, String w2){

		checkNotNull(w1, w2);

		short i = 0;
		while (i < Math.min(w1.length(), w2.length())){
			if (w1.charAt(w1.length() - 1 - i) == w2.charAt(w2.length() - 1 - i))
				i++;
			else
				break;
		}
		return i;
	}

	public static short getDifferingSuffixesLength(String w1, String w2){

		checkNotNull(w1, w2);

		return (short)(w1.length() - getCommonPrefixLength(w1, w2));
	}



	private static void checkNotNull(String... values){
		for (String str: values){
			Objects.requireNonNull(str);
		}
	}
}
