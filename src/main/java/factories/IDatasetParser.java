package factories;

import datamodel.IWord;
import java.util.Set;

public interface IDatasetParser {
	/**
	 * @return Dictionary of words with their lemma and PoS + morphological properties
     */
	Set<IWord> getDictionary();
}
