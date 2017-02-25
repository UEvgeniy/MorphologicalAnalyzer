package maslov_segalovich_based;

import analyzers.IMorphAnalyzer;
import datamodel.IDataset;
import datamodel.IWord;
import datamodel.Word;
import factories.IMorphAnalyzerFactory;
import helpers.SuffixesHelper;

import java.io.Serializable;
import java.util.*;

/**
 * This approach based on http://grusha-store.narod.ru/olderfiles/1/I_Segalovich_M_Maslov_Russkii_morf-63209.pdf
 */
public class MSFactory implements IMorphAnalyzerFactory, Serializable{

    private static final long serialVersionUID = 3064004026055689999L;
    private final IDataset dataset;

    public MSFactory(IDataset dictionary){
        this.dataset = Objects.requireNonNull(dictionary);
    }

    @Override
    public IMorphAnalyzer create() {

        Set<IWord> dict =  dataset.get();


        Map<String, Integer> frequency = new HashMap<>();
        TreeSet<Lexeme> sortedSet = new TreeSet<>();

        for (IWord iWord: dict){

            String lemma = iWord.getLemma();

            // First time when lexeme appears
            if (!frequency.containsKey(lemma)){
                frequency.put(lemma, 0);
            }

            // Increment counter
            frequency.put(lemma, frequency.get(lemma) + 1);

            // Find word basis
            String basis = iWord.getWord().substring(
                    0,
                    SuffixesHelper.getCommonPrefixLength(iWord.getWord(), lemma)
            );

            // Add new record to the sorted set
            sortedSet.add(new Lexeme(
                    basis,
                    new Word(lemma, lemma, iWord.getProperties()),
                    frequency.get(lemma)
            ));

        }

        List<Lexeme> result = new ArrayList<>();
        result.addAll(sortedSet);

        return new MSAnalyzer(result);
    }
}
