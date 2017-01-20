package factories;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import analyzers.IMorphAnalyzer;


/**
 *  Class loads instance of classes IMorphAnalyzer
 */
public class MorphAnalyzerLoader implements IMorphAnalyzerFactory {

    private final Path path;

    public MorphAnalyzerLoader(Path path) throws IOException{
    	this.path = path;
    }

    public IMorphAnalyzer create() {

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path, StandardOpenOption.READ))) {
            return (IMorphAnalyzer) ois.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}