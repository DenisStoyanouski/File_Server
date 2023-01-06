package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 23456;

    static String dirPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "client" + File.separator + "data" + File.separator;


    private static final StringBuilder request = new StringBuilder();

    private static String response;

    public static void main(String[] args) throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        String action;
        String fileName = null;

        System.out.print("Enter action (1 - get a file, 2 - save a file, 3 - delete a file): ");
        action = getInput().strip();

        switch (action) {
            case "1" : sendGet(fileName);
            break;
            case "2" : saveFile();
            break;
            case "3" : sendDelete(fileName);
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

    private static void sendGet(String fileName) {
        request.append("GET ").append(fileName);
        serve(request.toString());
        if (response.startsWith("200")) {
            System.out.printf("The content of the file is: %s%n", response.replaceFirst("200\\s+", ""));
        } else {
            System.out.println("The response says that the file was not found!");
        }

    }

    private static void saveFile() {
        System.out.println("Enter name of the file:");
        String fileName = getInput();
        System.out.println("Enter name of the file to be saved on server:");
        String fileNameForServer = getInput();
        request.append("PUT ").append(fileName + " ").append(fileNameForServer + " ");
        serve(request.toString());
        if ("200".equals(response)) {
            System.out.println("The response says that file was created!");
        } else {
            System.out.println("The response says that creating the file was forbidden!");
        }
    }

    private static void sendDelete(String fileName) {
        request.append("DELETE ").append(fileName);
        serve(request.toString());
        if ("200".equals(response)) {
            System.out.println("The response says that the file was successfully deleted!");
        } else {
            System.out.println("The response says that the file was not found!");
        }
    }

    private static void sendExit() {
        request.append("EXIT");
        serve(request.toString());
    }

    private static void serve(String request) {
        String[] req = request.split("\\s");

        try (
                Socket socket = new Socket(InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output  = new DataOutputStream(socket.getOutputStream())
        ) {

            output.writeUTF(request);
            System.out.println("The request was sent.");
            if (!"EXIT".equals(request)) {
                response = input.readUTF();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
