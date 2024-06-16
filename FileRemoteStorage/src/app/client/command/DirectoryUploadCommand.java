package app.client.command;

import app.IO;
import app.Settings;
import app.client.TokenHolder;
import app.server.filestorage.FileStorageException;
import app.transport.Transport;
import app.transport.message.storage.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DirectoryUploadCommand extends Command {

    public final TokenHolder tokenHolder;

    public DirectoryUploadCommand(Transport transport, IO io, TokenHolder tokenHolder) {
        super(transport, io);
        this.tokenHolder = tokenHolder;
    }

    @Override
    protected void performConnected() {
        io.print("enter directory name: ");
        var input = io.readln();
        var dirName = "";

        if (input.isBlank()) {
            dirName = "/home/parviz/t1";
        }else{
            dirName = STR."/home/parviz/\{input}";
        }

        io.print("enter folder where you want upload in your storage: ");
        var subdirectoryName = io.readln();

        var directoryPath = Path.of(dirName);
        if (!Files.exists(directoryPath)) {
            throw new CommandException("Directory doesn't exist");
        }

        try (var files = Files.list(directoryPath)) {
            List<String> listOfFiles = files
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .sorted()
                    .toList();
            var i = 1;
            System.out.println(STR."directoryPath.toString() \{directoryPath.toString()}");
            transport.send(new DirectoryUploadRequest(tokenHolder.getToken(), directoryPath.toString(), listOfFiles.size(), subdirectoryName));
            for (var file : listOfFiles) {
                var filePath = Path.of(dirName, file);
                var fileSize = Files.size(filePath);
                try (var fileInputStream = Files.newInputStream(filePath)) {
                    io.println(STR."File \{file} is \{i++} send");
                    transport.send(new FileUploadRequest(tokenHolder.getToken(), file, fileSize, ""));

                    var response = expectMessage(FileUploadResponse.class);

                    var transportOutputStream = transport.getOutputStream();
                    io.println(response.getResponse());
                    fileInputStream.transferTo(transportOutputStream);
                    transportOutputStream.flush();
                } catch (Exception e) {
                    throw new CommandException(e);
                }
            }

            transport.send(new DirectoryUploadingSuccess(tokenHolder.getToken()));
            var response = expectMessage(DirectoryUploadResponse.class);
            io.println(response.getResponse());
        } catch (Exception e) {
            throw new FileStorageException(e);
        }

    }
}
