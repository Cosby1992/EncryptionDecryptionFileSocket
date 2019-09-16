package model;

import java.io.Serializable;

public class CosObject implements Serializable {

    private String filename;
    private byte[] fileBytes;

    public CosObject(String filename, byte[] fileBytes) {
        this.filename = filename;
        this.fileBytes = fileBytes;
    }

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
