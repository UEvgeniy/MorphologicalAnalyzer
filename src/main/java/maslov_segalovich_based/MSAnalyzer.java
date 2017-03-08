package maslov_segalovich_based;

import analyzers.IMorphAnalyzer;
import datamodel.IWord;
import datamodel.Word;
import helpers.SuffixesHelper;

import java.io.Serializable;
import java.util.*;

/**
 * This approach based on http://grusha-store.narod.ru/olderfiles/1/I_Segalovich_M_Maslov_Russkii_morf-63209.pdf
 */
public class MSAnalyzer implements IMorphAnalyzer, Serializable {

    private static final long serialVersionUID = -5189166420941524292L;
    private List<Lexeme> list;

    public MSAnalyzer(List<Lexeme> dict){
        this.list = Objects.requireNonNull(dict);
    }


    @Override
    public Set<IWord> analyze(String word) {

        short endLen = 0;

        Set<IWord> result = new HashSet<>();

        short maxSuff = 0;

        while (endLen < word.length()){
            String sub = word.substring(0, word.length() - endLen);

            int ind = binarySearch(new Lexeme(sub));


            short commonSuf = SuffixesHelper.getCommonSuffixesLength(sub, list.get(ind).getBasis());
            if (commonSuf > maxSuff){
                maxSuff = commonSuf;
                result = new HashSet<>();
            }
            if (commonSuf == maxSuff){

                Lexeme lex = list.get(ind);

                result.add(
                        new Word(
                                word,
                                sub + lex.getLexeme().getWord().substring(lex.getBasis().length()),
                                lex.getLexeme().getProperties().get()
                        )
                );
            }

            endLen++;

        }
        return result;
    }

    @Override
    public Boolean canHandle(String word) {
        return !analyze(word).isEmpty();
    }


    /**
     * The implementation of binary search method. Unlike Collections.binarySearch() method
     *      method ALWAYS return valid index
     * @param lex searched element
     * @return index of searched element XOR the closest element, if searched element is not exists
     */
    private int binarySearch(Lexeme lex){

        int left = 0;
        int right = list.size() - 1;

        while(right - left > 1){

            int med = (left + right) / 2;

            if(list.get(med).compareTo(lex) < 0){
                left = med;
            }
            else {
                right = med;
            }
        }

        return list.get(left).compareTo(lex) == 0 ? left : right;
    }
}
