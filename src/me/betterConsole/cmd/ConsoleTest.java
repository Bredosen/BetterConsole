package me.betterConsole.cmd;

import java.io.IOException;

public class ConsoleTest {

    public static void main(String[] args) throws InterruptedException {
        long system, console, start;
        for(int i = 0; i < 1000; i++) {
            Console.write(".");
        }

        start = System.currentTimeMillis();
        for(int i = 0; i < 1_000_000; i++) System.out.print("S");
        system = System.currentTimeMillis() - start;


        start = System.currentTimeMillis();
        for(int i = 0; i < 1_000_000; i++) Console.write("C");
        console = System.currentTimeMillis() - start;

        Console.write("\n");

        Console.print("System time: " + system);
        Console.print("Console time: " + console);
    }
}
