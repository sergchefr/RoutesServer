package ru.ifmo;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.*;
import java.util.Scanner;

public class Console {

    public String checkInput(){
        try {
            if(System.in.available()!=0){
                return (new Scanner(System.in)).nextLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
