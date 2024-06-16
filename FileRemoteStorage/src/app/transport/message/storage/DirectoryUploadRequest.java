package app.transport.message.storage;

import app.transport.message.AuthorizedMessage;

public class DirectoryUploadRequest extends AuthorizedMessage {

    private final String directoryName;
    private final int fileCount;
    private final String subdirectoryName;

    public DirectoryUploadRequest(String authToken, String directoryName, int fileCount, String subdirectoryName) {
        super(authToken);
        this.directoryName = directoryName;
        this.fileCount = fileCount;
        this.subdirectoryName = subdirectoryName;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public int getFileCount() {
        return fileCount;
    }

    public String getSubdirectoryName() {
        return subdirectoryName;
    }
}
