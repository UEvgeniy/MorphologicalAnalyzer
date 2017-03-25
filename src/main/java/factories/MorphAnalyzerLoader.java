package factories;


import analyzers.IMorphAnalyzer;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;


/**
 *  Class loads instance of classes IMorphAnalyzer
 */
public class MorphAnalyzerLoader implements IMorphAnalyzerFactory {

    private final Path path;

    public MorphAnalyzerLoader(File file) throws IOException{
    	this.path = Objects.requireNonNull(file.toPath());
    }

    @Override
    public IMorphAnalyzer create() {

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path, StandardOpenOption.READ))) {
            return (IMorphAnalyzer) ois.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}