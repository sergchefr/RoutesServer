package ru.ifmo.migration;

import ru.ifmo.coll.Location;
import ru.ifmo.coll.Route;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Класс, читающий XML и возвращающий объекты из них
 */
public class XMLreader implements Loader {
    private String read(String filename) throws IOException {
        Path filepath;
        try {
            filename = filename.replace("\\", "/");
            filepath = Paths.get(filename);
        }catch (InvalidPathException e){
            throw new IOException(e);
        }
        String str ="";
        try (BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(filepath.toFile())))) {
            String nl;
            String version = bfr.readLine();
            while (true) {
                nl = bfr.readLine();
                if (nl != null) str = str+nl.strip();
                else break;
            }
        } catch (IOException e) {
            throw new IOException(e);
        }
        str=str.replace("<","#<").replace(">",">#").replace("##","#").substring(1);
        return str;
    }

    public ArrayList<Route> getRoutes(String filename)throws IOException, IllegalParamException{
        String[] coms;
        try {
            String str1 = read(filename);
            coms = str1.split("#");
        }catch (IOException e){
            throw new IOException(e);
        }
        Stack<String> cond = new Stack<>();
        HashMap<String, String> constr = new HashMap<>();
        ArrayList<Route> routes= new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (String s : coms) {
            if(s.contains("/")) {
                String a = cond.pop();
                if (a.equals("<route>")){
                    try {
                        Route route = new Route(
                                Integer.parseInt(constr.get("[<data>, <route>, <id>]")),
                                constr.get("[<data>, <route>, <name>]"),
                                simpleDateFormat.parse(constr.get("[<data>, <route>, <creationDate>]")),
                                new Location(Integer.parseInt(constr.get("[<data>, <route>, <locationFrom>, <x>]")),
                                        Integer.parseInt(constr.get("[<data>, <route>, <locationFrom>, <y>]")),
                                        Integer.parseInt(constr.get("[<data>, <route>, <locationFrom>, <z>]")),
                                        constr.get("[<data>, <route>, <locationFrom>, <name>]")),
                                new Location(Integer.parseInt(constr.get("[<data>, <route>, <locationTo>, <x>]")),
                                        Integer.parseInt(constr.get("[<data>, <route>, <locationTo>, <y>]")),
                                        Integer.parseInt(constr.get("[<data>, <route>, <locationTo>, <z>]")),
                                        constr.get("[<data>, <route>, <locationTo>, <name>]")),
                                Float.parseFloat(constr.get("[<data>, <route>, <distance>]")));
                        route.setOwnername(constr.get("[<data>, <route>, <ownername>]"));
                        routes.add(route);
                    } catch (Exception e) {
                        throw new IllegalParamException(e.getMessage());
                    }
                }
            }
            else if(s.contains("<")) cond.add(s);
            else constr.put(cond.toString(),s);
        }
        return routes;
    }

//    private Date dateParse(String str){
//        LocalDate date = LocalDate.parse(str);
//        ZonedDateTime zdt = date.atStartOfDay(ZoneId.systemDefault());
//        return Date.from(zdt.toInstant());
//    }


}
