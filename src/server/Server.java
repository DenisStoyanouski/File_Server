package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {
    private static final int PORT = 23456;
    private static final String SERVER_ADDRESS = "127.0.0.1";

    static boolean isServerClosed = false;

    public static void main(String[] args) {

        System.out.println("Server started!");
        try (ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(SERVER_ADDRESS))) {
            while (!isServerClosed) {
                Session session = new Session(server.accept());
                session.start();// it does not block this thread
                session.join(500);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class Session extends Thread {
    private final Socket socket;

    public Session(Socket socketForClient) {
        this.socket = socketForClient;
    }

    public void run() {
        try (
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            String inputMsg = input.readUTF();
            byte[] message = null;
            if (inputMsg.contains("PUT")) {
                int length = input.readInt();
                message = new byte[length]; // read length of incoming message
                if (length > 0) {
                    input.readFully(message, 0, message.length); // read the message
                }
            }
            String outputMsg = Files.run(inputMsg, message); // reading the next client message

            if (outputMsg != null) {
                output.writeUTF(outputMsg); // resend it to the client
            } else {
                Server.isServerClosed = true;
            }
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
