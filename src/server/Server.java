package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 23456;

    private static final String SERVER_ADDRESS = "127.0.0.1";

    public static void main(String[] args) {
        System.out.println("Server started!");
        try (ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(SERVER_ADDRESS))) {
            //while (true) {
                Session session = new Session(server.accept());
                session.start();// it does not block this thread
                session.join(5000);
            //}
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
            //for (int i = 0; i < 5; i++) {
            String inputMsg = input.readUTF(); // reading the next client message
            System.out.printf("Received: %s%n", inputMsg);
            String outputMsg = "All files were sent!";
            output.writeUTF(outputMsg); // resend it to the client
            System.out.printf("Sent: %s%n", outputMsg);
            //}
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
