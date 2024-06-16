package app.transport.message.storage;

import app.transport.message.AuthorizedMessage;

public class DeleteFileRequest extends AuthorizedMessage {
    private final String filename;
    public DeleteFileRequest(String authToken, String filename) {
        super(authToken);
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

}
