package app.transport.message.storage;

import app.transport.message.Message;

public class FileUploadResponse extends Message {
    private final boolean rewriteCollision;
    private final String response;

    public FileUploadResponse(boolean rewriteCollision, String response) {
        this.rewriteCollision = rewriteCollision;
        this.response = response;
    }

    public boolean isRewriteCollision() {
        return rewriteCollision;
    }

    public String getResponse() {
        return response;
    }
}
