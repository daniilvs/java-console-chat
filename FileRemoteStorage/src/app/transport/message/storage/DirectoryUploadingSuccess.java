package app.transport.message.storage;

import app.transport.message.AuthorizedMessage;

public class DirectoryUploadingSuccess extends AuthorizedMessage {

    public DirectoryUploadingSuccess(String authToken) {
        super(authToken);
    }
}
