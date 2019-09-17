package model;

import java.io.Serializable;

/**
 * This is an Object containing a filename and file contents.
 * It's used to transfer via ObjectStreams on sockets
 */
public class CosObject implements Serializable {

    //File information (name and content in bytes)
    private String filename;
    private byte[] fileBytes;

    //Constructor
    public CosObject(String filename, byte[] fileBytes) {
        this.filename = filename;
        this.fileBytes = fileBytes;
    }

    /////////////////////////////////// GETTERS AND SETTERS //////////////////////////////////////////
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }
}
