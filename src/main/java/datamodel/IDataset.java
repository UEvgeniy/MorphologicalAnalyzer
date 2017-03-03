package datamodel;

import java.util.List;
import java.util.Set;

/**
 * Interface for storage tagged dictionary of IWords
 */
public interface IDataset {

    Set<IWord> get();
    List<IDataset> split(double percentage);
    int size();

}
