package server;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    Set<String> server = new HashSet<>();
    public static void main(String[] args) {
        run();
    }

    private static void run() {
        String[] commands = getInput().split("\\s");
    }

    private static String getInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
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