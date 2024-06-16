package app.transport.message.storage;

import app.transport.message.AuthorizedMessage;

public class FileUploadRewriteConfirmation extends AuthorizedMessage {
    public FileUploadRewriteConfirmation(String authToken) {
        super(authToken);
    }
}
