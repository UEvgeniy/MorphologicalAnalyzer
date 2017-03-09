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
    public List<IDataset> split(int percentage, Random random) {

        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException
                    ("Percentage must be in range [0; 100]");
        }

        List<IWord> dictionaryList = new ArrayList<>(dictionary);
        Collections.shuffle(dictionaryList, random);


        Set<IWord> train = new HashSet<>();
        Set<IWord> test = new HashSet<>();

        double p = (double)this.size() * percentage / 100;

        for (int i = 0; i < dictionaryList.size(); i++){
            if (i < p){
                train.add(dictionaryList.get(i));
            }
            else{
                test.add(dictionaryList.get(i));
            }
        }

        List<IDataset> result = new ArrayList<>();

        result.add(new DataSet(train));
        result.add(new DataSet(test));

        return result;
    }

    @Override
    public List<IDataset> split(int percentage) {
        return split(percentage, new Random());
    }

    @Override
    public int size() {
        return dictionary.size();
    }
}
