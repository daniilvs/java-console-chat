package app.client.command;

import app.IO;
import app.transport.Transport;

public class HelpCommand extends Command{
    public HelpCommand(Transport transport, IO io) {
        super(transport, io);
    }

    @Override
    public void perform() {
        super.perform();
        io.print("All commands: " +
                "\n-> exit" +
                "\n-> register" +
                "\n-> login" +
                "\n-> token" +
                "\n-> check auth" +
                "\n-> file list" +
                "\n-> file up" +
                "\n-> file down" +
                "\n-> del file" +
                "\n-> dir up" +
                "\n-> help\n");
    }
}
