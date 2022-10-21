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

    public static void main(String[] args) {

        System.out.print("Enter action (1 - get a file, 2 - create a file, 3 - delete a file): ");
        String action = getInput();
        switch (action) {
            case "1" : request.append("GET ");
            break;
            case "2" : request.append("CREATE ");
            break;
            case "3" : request.append("DELETE ");
            break;
            case "exit" : request.append("EXIT");
            break;
            default :
                System.out.println("Unknown action");
                break;
        }
        if (!request.toString().contains("EXIT")) {
            System.out.print("Enter filename: ");
            request.append(getInput());
            request.append(" ");
            if (request.toString().contains("GET")) {
                System.out.print("Enter file content: ");
                request.append(getInput());
            }
        }


    }

    private static String getInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
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
