package parsers;

import datamodel.IWord;

import java.io.File;
import java.util.Collection;
import java.util.function.Function;

/**
 * The way of extracting dataset from the file
 */
@FunctionalInterface
public interface IWordsExtractor extends Function<File, Collection<IWord>> {
}
