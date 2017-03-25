package datamodel;

import java.util.*;
import java.util.function.Predicate;

/**
 * Interface for storage tagged dictionary of IWords
 */
public interface IDataset extends Iterable<IWord>{

    Set<IWord> get();
    List<IDataset> split(int percentage, Random random);
    List<IDataset> split(int percentage);
    IDataset filter(Predicate<IWord> predicate);
    int size();
    
	@Override
	default Iterator<IWord> iterator() {
		return get().iterator();
	}
}
