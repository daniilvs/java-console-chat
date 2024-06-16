package app.client.command;

import app.IO;
import app.client.TokenHolder;
import app.transport.Transport;
import app.transport.message.storage.DeleteFileRequest;
import app.transport.message.storage.DeleteFileResponse;

public class DeleteFileCommand extends Command {

    public final TokenHolder tokenHolder;

    public DeleteFileCommand(Transport transport, IO io, TokenHolder tokenHolder) {
        super(transport, io);
        this.tokenHolder = tokenHolder;
    }

    @Override
    protected void performConnected(){
        io.print("enter file name: ");
        var fileName = io.readln();

        try{
            transport.send(new DeleteFileRequest(tokenHolder.getToken(),fileName));
            var response = expectMessage(DeleteFileResponse.class);
            io.print(response.getMessage());
        }catch (RuntimeException e){
            System.out.println("File doesn't exist");
        }

    }
}
