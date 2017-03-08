package comparator;

import datamodel.IWord;

import java.util.Collection;
import java.util.Set;

/**
 *  Different ways to assess analyzers
 */
public interface IEvaluationCriteria {

   QualityResult evaluate(Set<IWord> expected, Set<IWord> actual);
}
