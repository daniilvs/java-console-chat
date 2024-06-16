package app.client.command;

import app.IO;
import app.Settings;
import app.client.TokenHolder;
import app.transport.Transport;
import app.transport.message.storage.FileUploadRequest;
import app.transport.message.storage.FileUploadResponse;
import app.transport.message.storage.FileUploadRewriteConfirmation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUploadCommand extends Command {
    public final TokenHolder tokenHolder;
    private final long MAX_SIZE = 10_000;

    public FileUploadCommand(Transport transport, IO io, TokenHolder tokenHolder) {
        super(transport, io);
        this.tokenHolder = tokenHolder;
    }

    @Override
    protected void performConnected() throws IOException {
        io.print(STR."enter file name (default '\{Settings.DEFAULT_FILENAME_TO_UPLOAD}'):");
        var file = io.readln();

        io.print(STR."enter folder where you want upload in your storage:");
        var subDir = io.readln();

        if (file.isBlank()) {
            file = Settings.DEFAULT_FILENAME_TO_UPLOAD;
        }
        var path = Path.of(file);
        if (!Files.isRegularFile(path)) {
            throw new CommandException("file doesn't exist or is not a regular file");
        }

        var filename = path.getFileName().toString();
        var fileSize = Files.size(path);

        if(fileSize > MAX_SIZE){
            throw new CommandException(STR."File size is bigger than '\{MAX_SIZE}' bytes ");
        }

        transport.send(new FileUploadRequest(tokenHolder.getToken(), filename,fileSize, subDir));
        var response = expectMessage(FileUploadResponse.class);

        if (response.isRewriteCollision()) {
            io.print(STR."file '\{filename}' exists. Rewrite (Y/n)?");
            var answer = io.readln();
            if (!answer.isBlank() && !"y".equals(answer)) {
                throw new CommandException("file upload cancelled");
            }
            transport.send(new FileUploadRewriteConfirmation(tokenHolder.getToken()));
        }

        // TODO progress bar
        try (var fileInputStream = Files.newInputStream(path)) {
            var transportOutputStream = transport.getOutputStream();
            var transferred = fileInputStream.transferTo(transportOutputStream);
            transportOutputStream.flush();
            assert transferred == fileSize;
        }catch (Exception e){
            throw new CommandException(e);
        }

        io.println("uploading done!");
    }
}
