package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

/**
 * This class handles file saving and file reading
 */
public class FileUtils {

    //Writes file to a path taken as argument
    public static void writeToFile(String path, byte[] fileBytes) throws IOException {

        //writing file content bytes to path
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(fileBytes);
        fos.close();

    }


    //Reads file using the path of a File and returns the content as byte-array
    public static byte[] readFile(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    //Reads file using the path of a File and returns the content as String
    public static String getBytesAsString(File file) throws IOException {

        StringBuilder builder = new StringBuilder();

        //using read file method that returns a byte-array
        for (byte b : readFile(file)) {
            builder.append(b);
        }

        return builder.toString();

    }

    //Transforming byte-array (from file) to String using StringBuilder
    public static String getStringFromByteArray(byte[] bytes){
        StringBuilder builder = new StringBuilder();

        for (byte b : bytes) {
            builder.append(b);
        }

        return builder.toString();

    }


}