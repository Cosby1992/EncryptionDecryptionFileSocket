package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Encrypt {

    private final byte[] KEY = {'c', 'o', 's', 'b', 'y'};
    private byte pos = 0;

    public byte[] cosEncrypt(File file) throws IOException {

        byte[] fileBytes = Files.readAllBytes(file.toPath());

        System.out.print("Filebytes: ");
        for (byte b : fileBytes) {
            System.out.print(b);
        }
        System.out.println();



        for (int i = 0; i < fileBytes.length; i++) {
            fileBytes[i] += getNextInKey(KEY);
        }

        pos = 0;

        System.out.print("New Filebytes: ");
        for (byte b : fileBytes) {
            System.out.print(b);
        }
        System.out.println();

        return fileBytes;

    }

    public byte[] cosDecrypt(File file) throws IOException {

        byte[] fileBytes = Files.readAllBytes(file.toPath());

        System.out.print("Filebytes: ");
        for (byte b : fileBytes) {
            System.out.print(b);
        }
        System.out.println();



        for (int i = 0; i < fileBytes.length; i++) {
            fileBytes[i] -= getNextInKey(KEY);
        }

        pos = 0;

        System.out.print("New Filebytes: ");
        for (byte b : fileBytes) {
            System.out.print(b);
        }
        System.out.println();

        return fileBytes;

    }

    private byte getNextInKey(byte[] key){

        if(pos < key.length){
            return key[pos++];
        } else {
            pos = 0;
            return key[pos++];
        }


    }





}
