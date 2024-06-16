package app.transport;

import app.IO;
import app.Settings;
import app.transport.message.Message;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;

public class SerializedTransport implements Transport {
    private Socket socket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private final IO logger = new IO();
    private LocalDateTime expiredAt;

    public SerializedTransport() {
    }

    public SerializedTransport(Socket socket) {
        try {
            this.socket = socket;
            writer = new ObjectOutputStream(socket.getOutputStream());
            reader = new ObjectInputStream(socket.getInputStream());
            logger.debug(STR."transport joined to socket \{socket}");
        } catch (Exception e) {
            disconnect();
            throw new TransportException(e);
        }
    }

    @Override
    public void connect() throws TransportException {
        try {
            if (socket == null || socket.isClosed()) {
                expiredAt = LocalDateTime.now().plusMinutes(20);
                socket = new Socket(Settings.HOST, Settings.PORT);
                writer = new ObjectOutputStream(socket.getOutputStream());
                reader = new ObjectInputStream(socket.getInputStream());
            }
        } catch (Exception e) {
            disconnect();
            throw new TransportException(e);
        }
    }

    @Override
    public void disconnect() throws TransportException {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                logger.debug(STR."transport disconnected from \{socket}");
            }
        } catch (Exception e) {
            throw new TransportException(e);
        }
    }

    @Override
    public void send(Message m) throws TransportException {
        checkIsConnected();
        try {
            writer.writeObject(m);
            writer.flush();
            logger.debug(STR."transport sended \{m}");
        } catch (Exception e) {
//            disconnect();
            throw new TransportException(e);
        }
    }

    private void isExpired(){
        if(expiredAt != null){
            if(LocalDateTime.now().isAfter(expiredAt)){
                disconnect();
            }
        }
    }

    @Override
    public Message receive() throws TransportException {
        return receive(Message.class);
    }

    @Override
    public <T extends Message> T receive(Class<T> type) throws TransportException {
        checkIsConnected();
        try {
            var msg = type.cast(reader.readObject());
            logger.debug(STR."transport received \{msg}");
            return msg;
        } catch (Exception e) {
//            disconnect();
            throw new TransportException(e);
        }
    }

    @Override
    public InputStream getInputStream() {
        checkIsConnected();
        try {
            return socket.getInputStream();
        } catch (Exception e) {
             throw new TransportException(e);
        }
    }

    @Override
    public OutputStream getOutputStream() {
        checkIsConnected();
        try {
            return socket.getOutputStream();
        } catch (Exception e) {
            throw new TransportException(e);
        }
    }

    private void checkIsConnected() {
        if (socket == null || socket.isClosed())
            throw new TransportException("transport closed");
    }

    {
        var t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                    isExpired();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

}
