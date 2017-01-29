package helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FileSearcher {

    private static String extension;

    /**
     * Method walk through all files into input directory and forms a list of files (recursively)
     * @param dataset File or directory where files with input extension will be extracted
     * @param extension Extension of dictionary files
     * @return List of files with input extension
     */
    public static List<File> getFileList(File dataset, String extension) {

        if (!extension.matches("^\\.[\\d\\w]+")) {
            throw new RuntimeException("Invalid extension");
        }

        FileSearcher.extension = extension;

        return getFileList(dataset);
    }

    private static List<File> getFileList(File dataset) {
        List<File> result = new ArrayList<>();

        // If dataset is directory, than read all files from this dir
        if (Objects.requireNonNull(dataset, "File cannot be null").isDirectory()) {

            File[] filesInDir = dataset.listFiles();

            if (filesInDir == null){
                return result;
            }

            for (File f: filesInDir) {
                result.addAll(
                        getFileList(f)
                );
            }
        }
        // else dataset is considered as file
        else {
            if (hasExtension(dataset)){
                result.add(dataset);
            }
        }
        return result;
    }

    private static boolean hasExtension(File file) {

        String name = file.getName();
        int dot = name.lastIndexOf('.');

        return dot > 0 && extension.equals(name.substring(dot));

    }

}
