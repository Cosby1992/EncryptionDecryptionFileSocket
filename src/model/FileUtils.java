package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtils {


    public static void writeToFile(String path, byte[] fileBytes) throws IOException {

        FileOutputStream fos = new FileOutputStream(path);
        fos.write(fileBytes);
        fos.close();

    }


    public static byte[] readFile(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    public static String getBytesAsString(File file) throws IOException {

        StringBuilder builder = new StringBuilder();

        for (byte b : readFile(file)) {
            builder.append(b);
        }

        return builder.toString();

    }

    public static String getStringFromByteArray(byte[] bytes){
        StringBuilder builder = new StringBuilder();

        for (byte b : bytes) {
            builder.append(b);
        }

        return builder.toString();

    }


}