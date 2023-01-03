package server;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;


public class Files {
    static String dirPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "server" + File.separator + "data" + File.separator;

    //static String dirPath = System.getProperty("user.dir") + File.separator + "File Server" + File.separator + "task" + File.separator + "src" + File.separator + "server" + File.separator + "data" + File.separator;
    static String run(String request) {
        new File(dirPath).mkdirs();
        String command = request.split("\\s")[0];
        if ("EXIT".equals(command)) {
            return null;
        }
        String fileName = request.split("\\s")[1];
        String text = request.replaceFirst(command, "").replaceFirst(fileName,"");

        String response;
        switch (Objects.requireNonNull(command)) {
                case "GET" : response = get(fileName);
                    break;
                case "PUT" :  response = put(fileName, text);
                    break;
                case "DELETE" : response = delete(fileName);
                    break;
                default:
                    response = "Unknown command";
        }
        return response;
    }

    private static String get(String fileName) {
        StringBuilder line = new StringBuilder();
        File file = new File(String.format("%s%s", dirPath, fileName));
        if (!file.exists()) {
            return "404";
        }
        try (Scanner scanner = new Scanner(file)) {
            line.append("200 ");
            while (scanner.hasNext()) {
                line.append(scanner.nextLine());
            }
        } catch (IOException e) {
            return "500";
        }
        return line.toString();
    }

    private static String put(String fileName, String text) {
        File file = new File(String.format("%s%s", dirPath, fileName));
        if (file.exists()) {
            return "403";
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(text);
        } catch (IOException e) {
            return "500";
        }
        return "200";
    }

    private static String delete(String fileName) {
        File file = new File(String.format("%s%s", dirPath, fileName));
        if (!file.exists()) {
            return "404";
        }
        return  file.delete() ? "200" : "500";
    }

}

