package app.transport.message.storage;

import app.transport.message.Message;

public class DeleteFileResponse extends Message {
    private final String message;

    public DeleteFileResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
