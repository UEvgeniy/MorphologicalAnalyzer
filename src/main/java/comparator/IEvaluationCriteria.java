package comparator;

import datamodel.IWord;

import java.util.Collection;

/**
 *  Different ways to assess analyzers
 */
public interface IEvaluationCriteria {

   QualityResult evaluate(Collection<IWord> suggested, Collection<IWord> actual);
}
