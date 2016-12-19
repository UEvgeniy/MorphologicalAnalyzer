package datamodel;

/**
 * An interface for classes containing the initial form of the word and its own PoS and properties
 */
public interface IWord {
    String getLemma();
    // todo add IMorphProperties getProperties();
}
