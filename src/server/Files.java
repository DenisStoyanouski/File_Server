package server;

import java.util.HashSet;
import java.util.Objects;
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
                case "PUT" :  response = add(fileName, text);
                break;
                case "GET" : response = get(fileName);
                break;
                case "DELETE" : response = delete(fileName);
                break;
                default:
                    response = "Unknown command";
        }
        return response;
    }

    private static String add(String fileName, String text) {

        return server.add(fileName) ? "200" : "403";
    }

    private static String get(String fileName) {
        return server.contains(fileName) ? "200" : "404";
    }

    private static String delete(String fileName) {
        return server.remove(fileName) ? "200" : "404";
    }

}

