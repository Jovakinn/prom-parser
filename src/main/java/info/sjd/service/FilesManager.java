package info.sjd.service;

import info.sjd.model.Product;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FilesManager {

    private static final String MAIN_DIR = System.getProperty("user.dir");
    private static final String SEP = System.getProperty("file.separator");
    private static final String FILE_PATH = MAIN_DIR + SEP + "files" + SEP + "items_info.txt";

    public static void write(Product product) {
        checkDirectory();

        try (FileWriter fileWriter = new FileWriter(FILE_PATH, true)){
            fileWriter.write(product.toString() + "\n");
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void checkDirectory() {
        String filesDir = MAIN_DIR + SEP + "files";
        File file = new File(filesDir);
        if (!file.exists()) {
            file.mkdir();
        }
    }

}
