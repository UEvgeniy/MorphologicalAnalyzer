package analyzers;

import datamodel.ILemmaRule;
import datamodel.IWord;
import datamodel.MorphemedWord;
import datamodel.Word;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Class with baseline functionality. Used in order to show operability of the whole program
 */
public class TrivialRulePropertyPredictor implements IPropertyPredictor, Serializable{

    private static final long serialVersionUID = 3004147273529207635L;
    private Collection<ILemmaRule> rules;


    public TrivialRulePropertyPredictor(Collection<ILemmaRule> rules){
        this.rules = Objects.requireNonNull(rules);
    }


    @Override
    public Collection<IWord> predict(MorphemedWord word) {

        Collection<IWord> result = new ArrayList<>();

        for (ILemmaRule rule : rules){
            if (rule.isApplicable(word)){

                result.add(apply(word, rule));
            }
        }

        return result;
    }

    /**
     * Method applies a rule for the word
     * @param mWord Word with extracted morphemes
     * @param lemmaRule Rule of transforming the word to lemma
     * @return Word, its lemma and morphological properties
     */
    private IWord apply(MorphemedWord mWord, ILemmaRule lemmaRule){

        return new Word(
                mWord.getWord(),
                lemmaRule.apply(mWord).getWord(),
                lemmaRule.getMorphProperties()
        );
    }
}
