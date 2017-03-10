package datamodel;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Interface for storage tagged dictionary of IWords
 */
public interface IDataset extends Iterable<IWord>{

    Set<IWord> get();
    List<IDataset> split(int percentage, Random random);
    List<IDataset> split(int percentage);
    int size();
    
	@Override
	default Iterator<IWord> iterator() {
		return get().iterator();
	}
    
    

}
