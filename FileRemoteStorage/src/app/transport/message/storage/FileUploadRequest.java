package app.transport.message.storage;

import app.transport.message.AuthorizedMessage;

public class FileUploadRequest extends AuthorizedMessage {
    private final String filename;
    private final long size;
    private final String subDir;

    public FileUploadRequest(String authToken, String filename, long size, String subDir) {
        super(authToken);
        this.filename = filename;
        this.size = size;
        this.subDir = subDir;
    }

    public String getFilename() {
        return filename;
    }

    public long getSize() {
        return size;
    }

    public String getSubDir() {
        return subDir;
    }
}
