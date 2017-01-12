package factories;


import analyzers.IMorphAnalyzer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Path;


/**
 *  Class loads instance of classes IMorphAnalyzer
 */
public class MorphAnalyzerLoader implements IMorphAnalyzerFactory{

    private final ObjectInputStream ois;

    public MorphAnalyzerLoader(Path path) throws IOException{

        FileInputStream fis = new FileInputStream(path.toString());
        this.ois = new ObjectInputStream(fis);
    }

    public IMorphAnalyzer create() {

        try {
            IMorphAnalyzer analyzer =  (IMorphAnalyzer) ois.readObject();

            // todo remove log
            System.out.println("Analyzer was successfully loaded...");

            return analyzer;
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}