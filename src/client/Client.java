package client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 23456;

    static String dirPath = System.getProperty("user.dir") + File.separator + "File Server" + File.separator + "task" + File.separator + "src" + File.separator + "client" + File.separator + "data" + File.separator;


    private static final StringBuilder request = new StringBuilder();

    private static String response;

    public static void main(String[] args) throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        String action;
        String fileName = null;

        System.out.print("Enter action (1 - get a file, 2 - save a file, 3 - delete a file): ");
        action = getInput().strip();

        switch (action) {
            case "1":
                getFile();
                break;
            case "2":
                saveFile();
                break;
            case "3":
                deleteFile();
                break;
            case "exit":
                sendExit();
                break;
            default:
                System.out.println("Unknown action");
                break;
        }
    }

    private static String getInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine().strip();
    }

    private static void getFile() {
        request.append("GET ").append(getFileSearchOption());
        serve(request.toString());
        if (response.startsWith("200")) {
            System.out.printf("The content of the file is: %s%n", response.replaceFirst("200\\s+", ""));
        } else {
            System.out.println("The response says that the file was not found!");
        }

    }

    private static void saveFile() {
        System.out.print("Enter name of the file:");
        String fileName = getInput();
        System.out.print("Enter name of the file to be saved on server:");
        String fileNameForServer = getInput();
        request.append("PUT ").append(fileName).append(" ").append(fileNameForServer);
        serve(request.toString());
        if ("200".equals(response)) {
            System.out.println("The response says that file was created!");
        } else {
            System.out.println("The response says that creating the file was forbidden!");
        }
    }

    private static void deleteFile() {
        request.append("DELETE ").append(getFileSearchOption());
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
        File file = new File(String.format("%s%s", dirPath, req[1]));
        if (!file.exists()) {
            System.out.println("can't find file");
            return;
        }
        try (
                Socket socket = new Socket(InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            output.writeUTF(request);
            System.out.println("The request was sent.");
            /*if (!"EXIT".equals(request)) {
                response = input.readUTF();
            }*/
            if (request.contains("PUT")) {
                byte[] message = null;
                try (FileInputStream fis = new FileInputStream(file)) {
                    message = fis.readAllBytes();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                output.writeInt(message.length); // write length of the message
                System.out.println(message.length);
                output.write(message);// write the message
                System.out.println(message);
            }

            response = input.readUTF();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFileSearchOption() {
        String option = "";
        String input;
        while (option.isEmpty()) {
            System.out.print("Do you want to get the file by name or by id (1 - name, 2 - id): ");
            switch (getInput()) {
                case "1":
                    System.out.print("Enter name: ");
                    input = getInput();
                    option = input.matches(".+\\..+") ? "BY_NAME " + input : "";
                    break;
                case "2":
                    System.out.print("Enter id: ");
                    input = getInput();
                    option = input.matches("\\d+") ? "BY_ID " + input : "";
                    break;
                default:
                    System.out.println("Choose right option 1 or 2");
                    break;
            }
        }
        return option;
    }

}
