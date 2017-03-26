package quality_assess;

import datamodel.IWord;

/**
 * Class represents modes of comparing predicted and correct IWord
 */
public class Comparators {

    private Comparators(){}

    public static boolean byLemma(IWord one, IWord another){
        return one.getLemma().equals(another.getLemma());
    }

    public static boolean byPoS(IWord one, IWord another){
        return one.getProperties().getPos()
                .equals(another.getProperties().getPos());
    }

    public static boolean byLemmaPoS(IWord one, IWord another){
        return byLemma(one, another) && byPoS(one, another);
    }

    public static boolean fullCoincidence(IWord one, IWord another){
        return one.equals(another);
    }
}


