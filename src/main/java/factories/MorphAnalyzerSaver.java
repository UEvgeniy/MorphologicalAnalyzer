package factories;


import analyzers.IMorphAnalyzer;

import java.io.*;
import java.nio.file.Path;

/**
 *  Class saves instance of classes IMorphAnalyzer
 */
public class MorphAnalyzerSaver implements IMorphAnalyzerFactory, Closeable {

    private final IMorphAnalyzerFactory factory;
    private final ObjectOutputStream oos;


    public MorphAnalyzerSaver(IMorphAnalyzerFactory factory, File file) throws IOException{

        this.factory = factory;

        this.oos = new ObjectOutputStream(
                new FileOutputStream(file.toPath().toString())
        );
    }

    @Override
    public IMorphAnalyzer create() {
        IMorphAnalyzer analyzer = factory.create();

        // Serialize analyzer
        try {
            oos.writeObject(analyzer);
        }
        catch (IOException exception){
            throw new RuntimeException(exception);
        }

        return analyzer;
    }

    @Override
    public void close() throws IOException {
        oos.flush();
        oos.close();
    }
}
