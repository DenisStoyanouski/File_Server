package server;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;



public class Files {

    private static Set<String> server = new HashSet<>();

    private static String response;

    static String run(String request) {
        server.add("text.txt");
        String command = request.split("\\s")[0];
        if ("EXIT".equals(command)) {
            return null;
        }
        String fileName = request.split("\\s")[1];
        String text = request.replaceFirst(command, "").replaceFirst(fileName,"");

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
        File file = new File(String.format(".\\%s", fileName));
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
        File file = new File(fileName);
        if (file.exists()) {
            return "403";
        }
        try (FileWriter writer = new FileWriter(String.format(".\\%s", fileName))) {
            writer.write(text);
        } catch (IOException e) {
            return "500";
        }
        return "200";
    }

    private static String delete(String fileName) {
        return server.remove(fileName) ? "200" : "404";
    }

}

