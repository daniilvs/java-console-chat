package app.transport.message.storage;

import app.transport.message.Message;

public class FileDownloadResponse extends Message {
    private final long size;

    public FileDownloadResponse(long size) {
        this.size = size;
    }

    public long getSize() {
        return size;
    }
}
