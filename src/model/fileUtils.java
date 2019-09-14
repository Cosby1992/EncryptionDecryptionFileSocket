package model;

import java.io.FileOutputStream;
import java.io.IOException;

public class fileUtils {



    public static void writeToFile(String path, byte[] fileBytes) throws IOException {

        FileOutputStream fos = new FileOutputStream(path);
        fos.write(fileBytes);
        fos.close();

    }

    /* not used yet */
//    public static byte[] readFile(String path){
//
//
//        return null;
//    }







}
