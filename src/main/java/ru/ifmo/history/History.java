package ru.ifmo.history;

import java.util.ArrayDeque;

/**
 * Класс, хранящий последние 12 выполненных команд
 */
public class History {
    //String filepath;
    ArrayDeque<String> lcoms = new ArrayDeque<>();
    public History() {

    }

    public String showHistory(){
        StringBuilder builder = new StringBuilder();
        for (String lcom : lcoms) {
            builder.append(lcom).append(", ");
        }
        String a = builder.toString();
        if(!a.isEmpty()) return a.substring(0,a.length()-2);
        return "история пока пуста";

    }
    public void add(String com){
        lcoms.addFirst(com);
        if(lcoms.size()>12)lcoms.pollLast();
    }
}
