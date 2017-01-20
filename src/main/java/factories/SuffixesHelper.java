package factories;

public class SuffixesHelper {
	public static short getCommonPrefixLength(String w, String lemma) {
		short i = 0;

		while (i < Math.min(w.length(), lemma.length())){
		    if (w.charAt(i) == lemma.charAt(i))
		        i++;
		    else
		        break;
		}
		return i;
	}
}
