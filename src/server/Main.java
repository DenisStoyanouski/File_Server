package server;

import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static Set<String> server = new HashSet<>();

    public static void main(String[] args) {
        run();
    }

    private static void run() {
        String command = null;
        String fileName = null;

        while (true) {
            String[] commands = getInput().split("\\s");
            if (commands.length > 0) {
                command = commands[0];
            }
            if (commands.length > 1) {
                fileName = commands[1];
            }
            switch (Objects.requireNonNull(command)) {
                case "add" : add(fileName);
                break;
                case "get" : get(fileName);
                break;
                case "delete" : delete(fileName);
                break;
                case "exit" : System.exit(0);
                break;
                default:
                    System.out.println("Unknown command");
            }
        }
    }

    private static void add(String fileName) {
        Pattern pattern = Pattern.compile("\\bfile(1|2|3|4|5|6|7|8|9|10)\\b");
        Matcher matcher = pattern.matcher(fileName);

        if (matcher.matches() && server.add(fileName)) {
            System.out.printf("The file %s added successfully%n", fileName);
        } else {
            System.out.printf("Cannot add the file %s.%n", fileName);
        }

    }

    private static void get(String fileName) {
        if (server.contains(fileName)) {
            System.out.printf("The file %s was sent%n", fileName);
        } else {
            System.out.printf("The file %s not found%n", fileName);
        }
    }

    private static void delete(String fileName) {
        if (server.remove(fileName)) {
            System.out.printf("The file %s was deleted%n", fileName);
        } else {
            System.out.printf("The file %s not found%n", fileName);
        }
    }

    private static String getInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
    enum Command {
        ADD("add"),
        GET("get"),
        DELETE("delete"),
        EXIT("exit");

        String name;

        Command (String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }
}

