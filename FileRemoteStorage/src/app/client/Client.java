package app.client;

import app.IO;
import app.client.command.*;
import app.transport.SerializedTransport;
import app.transport.Transport;

public class Client {
    private final IO io = new IO();
    private final Transport transport = new SerializedTransport();
    private final TokenHolder tokenHolder = new TokenHolder();

    public static void main(String[] args) {
        new Client().commandLoop();
    }

    private void commandLoop() {
        String userInput;
        do {
            io.print(">>> ");
            userInput = io.readln().strip().toLowerCase();
            try {
                (switch (userInput) {
                    case "exit" -> new PrintCommand(transport, io, "bye!");
                    case "register" -> new RegisterCommand(transport, io);
                    case "login" -> new LoginCommand(transport, io, tokenHolder);
                    case "token" -> new PrintCommand(transport, io, tokenHolder.getToken());
                    case "check auth" -> new CheckAuthCommand(transport, io, tokenHolder);
                    case "file list" -> new FileListCommand(transport, io, tokenHolder);
                    case "file up" -> new FileUploadCommand(transport, io, tokenHolder);
                    case "file down" -> new FileDownloadCommand(transport, io, tokenHolder);
                    case "help" -> new HelpCommand(transport, io);
                    case "del file" -> new DeleteFileCommand(transport, io, tokenHolder);
                    case "dir up" -> new DirectoryUploadCommand(transport, io, tokenHolder);
                    default -> new PrintCommand(transport, io, unrecognizedCommand);
                }).perform();
            } catch (Exception e) {
                io.println(STR."Error: \{e.getMessage()}");
            }
        } while (!"exit".equals(userInput));
    }

}
