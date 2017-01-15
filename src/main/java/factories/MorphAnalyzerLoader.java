package factories;


import analyzers.IMorphAnalyzer;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Path;


/**
 *  Class loads instance of classes IMorphAnalyzer
 */
public class MorphAnalyzerLoader implements IMorphAnalyzerFactory, Closeable{

    private final ObjectInputStream ois;

    public MorphAnalyzerLoader(Path path) throws IOException{

        FileInputStream fis = new FileInputStream(path.toString());
        this.ois = new ObjectInputStream(fis);
    }

    public IMorphAnalyzer create() {

        try {
            return (IMorphAnalyzer) ois.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void close() throws IOException {
        ois.close();
    }
}