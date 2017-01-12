package factories;


import analyzers.IMorphAnalyzer;

import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Path;

/**
 *  Class saves instance of classes IMorphAnalyzer
 */
public class MorphAnalyzerSaver implements IMorphAnalyzerFactory, Closeable {

    private final IMorphAnalyzerFactory factory;
    private final ObjectOutputStream oos;


    public MorphAnalyzerSaver(IMorphAnalyzerFactory factory, Path path) throws IOException{

        this.factory = factory;

        this.oos = new ObjectOutputStream(
                new FileOutputStream(path.toString())
        );
    }


    public IMorphAnalyzer create() {
        IMorphAnalyzer analyzer = factory.create();

        // Serialize analyzer
        try {
            oos.writeObject(analyzer);
            // todo remove log
            System.out.println("Instance of IMorphAnalyzer was successfully saved...");
        }
        catch (IOException exception){
            throw new RuntimeException(exception);
        }

        return analyzer;
    }


    public void close() throws IOException {
        oos.flush();
        oos.close();
    }
}
