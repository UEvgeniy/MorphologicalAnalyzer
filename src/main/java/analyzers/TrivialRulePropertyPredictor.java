package analyzers;

import datamodel.ILemmaRule;
import datamodel.IWord;
import datamodel.MorphemedWord;
import datamodel.Word;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Class with baseline functionality. Used in order to show operability of the whole program
 */
public class TrivialRulePropertyPredictor implements IPropertyPredictor{


    private Collection<ILemmaRule> rules;


    public TrivialRulePropertyPredictor(Collection<ILemmaRule> rules){
        this.rules = rules;
    }


    @Override
    public Collection<ILemmaRule> predict(MorphemedWord word) {

        Collection<ILemmaRule> result = new ArrayList<>();

        for (ILemmaRule rule : rules){
            if (rule.isApplicable(word)){
                result.add(rule);
            }
        }

        return result;
    }


    public IWord apply(MorphemedWord mWord, ILemmaRule lemmaRule){

        return new Word(
                mWord.getWord(),
                lemmaRule.apply(mWord).getWord(),
                lemmaRule.getMorphProperties()
        );
    }
}