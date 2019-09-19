package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

/**
 * This class is used to encrypt and decrypt a file
 * it also handles the "encryption key"
 */
public class Encrypt {

    //Encryption key (change for new encryption)
    private final byte[] KEY = {'c', 'o', 's', 'b', 'y'};
    //keep track of position of array running through the encryption key multiple times
    private byte pos = 0;

    //method that encrypts file contents
    public byte[] cosEncrypt(File file) throws IOException {

        //Getting filebytes
        byte[] fileBytes = Files.readAllBytes(file.toPath());

        //Only printing unencrypted to console (not important)
        System.out.println("Message to encrypt: " + new String(fileBytes));


        //Encrypting (here by adding new key-letter to each filebyte)
        for (int i = 0; i < fileBytes.length; i++) {
            fileBytes[i] += getNextInKey(KEY);
        }

        //resetting position
        pos = 0;

        //Only printing encrypted bytes to console (not important)
        System.out.println("My encryption: " + new String(fileBytes));

        byte[] returnBytes = Base64.getEncoder().encode(fileBytes);

        System.out.println("Base64: " + new String(returnBytes));

        //returns encrypted file content as bytes
        return returnBytes;

    }

    //Decrypting file content encrypted with cosEncrypt
    public byte[] cosDecrypt(byte[] fileBytes) {

        System.out.println("Message to decrypt: " + new String(fileBytes));

        byte[] returnbytes = Base64.getDecoder().decode(new String(fileBytes));

        //Only printing encrypted bytes to console (not important)
        System.out.println("Base64: " + new String(returnbytes));


        //Decrypting filecontent
        for (int i = 0; i < returnbytes.length; i++) {
            returnbytes[i] -= getNextInKey(KEY);
        }

        //Resetting position
        pos = 0;

        //Only printing decrypted to console (not important)
        System.out.println("My decryption: " + new String(returnbytes));

        //Returns decrypted file content as bytes
        return returnbytes;

    }

    //Finds the next letter in the key array (resetting at the end of array and starts over)
    private byte getNextInKey(byte[] key){

        if(pos < key.length){
            return key[pos++];
        } else {
            pos = 0;
            return key[pos++];
        }

    }





}
