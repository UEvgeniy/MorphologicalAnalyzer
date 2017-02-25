package datamodel;

import java.util.*;

/**
 * Class for storage tagged dictionary of IWords
 */
public class DataSet implements  IDataset{

    private final Set<IWord> dictionary;

    public DataSet(Set<IWord> dictionary){
        this.dictionary = Objects.requireNonNull(dictionary,
                "Set of words cannot be null");
    }

    @Override
    public Set<IWord> get() {
        return dictionary;
    }

    @Override
    public List<IDataset> split(int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException
                    ("Percentage must be in range [0; 100]");
        }

        Set<IWord> train = new HashSet<>();
        Set<IWord> test = new HashSet<>();

        Random random = new Random();
        double p = (double)percentage / 100;

        for( IWord word: dictionary){
            if (random.nextDouble() < p){
                train.add(word);
            }
            else{
                test.add(word);
            }
        }

        List<IDataset> result = new ArrayList<>();

        result.add(new DataSet(train));
        result.add(new DataSet(test));

        return result;
    }
}
