package ru.ifmo;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.*;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;

public class Console {
    private LinkedList<String> printQueue;
    private CommandManager commandManager;

    public Console(CommandManager commandManager) {
        this.commandManager = commandManager;
        printQueue = new LinkedList<>();
    }

    public void manage(){
        print();
        var input = checkInput();
        if(input==null) return;

        System.out.println(commandManager.executeServer(input));
    }


    private String checkInput(){
        try {
            if(System.in.available()!=0){
                return (new Scanner(System.in)).nextLine();
            }
        } catch (IOException e) {
            System.out.println("error in console input");
        }
        return null;
    }

    private void print(){
        while (!printQueue.isEmpty()){
            System.out.println(printQueue.pollLast());
        }
    }

    public void addToPrintQueue(String command){
        printQueue.addLast(command);
    }



}
