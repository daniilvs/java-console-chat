package app.transport.message.storage;

import app.transport.message.Message;

public class DirectoryUploadResponse extends Message {

    private final String response;

    public DirectoryUploadResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}
