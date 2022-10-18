package server;

import java.util.HashSet;
import java.util.Set;

public class Main {
    Set<String> server = new HashSet<>();
    public static void main(String[] args) {
        run();
    }

    private static void run() {

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