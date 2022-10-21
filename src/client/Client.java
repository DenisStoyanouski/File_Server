package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.Objects;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 23456;


    private static final StringBuilder request = new StringBuilder();

    private static String response;

    public static void main(String[] args) {

        System.out.print("Enter action (1 - get a file, 2 - create a file, 3 - delete a file): ");
        String action = getInput();
        switch (action) {
            case "1" : sendGet();
            break;
            case "2" : sendCreate();
            break;
            case "3" : sendDelete();
            break;
            case "exit" : sendExit();
            break;
            default :
                System.out.println("Unknown action");
                break;
        }
    }

    private static String getInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static void sendGet() {
        request.append("GET ");
        System.out.print("Enter filename: ");
        request.append(getInput());
        request.append(" ");
        System.out.print("Enter file content: ");
        request.append(getInput());
        response = serve(request.toString());

    }

    private static void sendCreate() {
        request.append("CREATE ");
        System.out.print("Enter filename: ");
        request.append(getInput());
        response = serve(request.toString());
    }

    private static void sendDelete() {
        request.append("DELETE ");
        System.out.print("Enter filename: ");
        request.append(getInput());
        response = serve(request.toString());
    }

    private static void sendExit() {
        request.append("EXIT");
    }

    private static String serve(String request) {
        String response = null;
        try (
                Socket socket = new Socket(InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output  = new DataOutputStream(socket.getOutputStream())
        ) {
            output.writeUTF(request);
            System.out.println("The request was sent.");
            response = input.readUTF();

            //}
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
