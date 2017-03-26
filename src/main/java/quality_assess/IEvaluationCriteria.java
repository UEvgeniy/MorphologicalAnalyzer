package quality_assess;

import datamodel.IWord;


/**
 *  Different ways to assess analyzers
 */

@FunctionalInterface
public interface IEvaluationCriteria {
   boolean evaluate(IWord predicted, IWord correct);
}
