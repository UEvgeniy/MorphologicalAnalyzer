package datamodel;

import java.io.Serializable;

/**
 * An interface for classes containing the initial form of the word and its own PoS and properties
 */
public interface IWord extends Serializable{
    String getLemma();
    String getProperties();
}
