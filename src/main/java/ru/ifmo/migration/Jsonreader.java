package ru.ifmo.migration;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.ifmo.coll.Route;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class Jsonreader implements Loader{

    @Override
    public ArrayList<Route> getRoutes(String filename) throws IOException, IllegalParamException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        String json = Files.readString(Paths.get(filename));

        Route[] routesArray = objectMapper.readValue(json, Route[].class);
        return new ArrayList<>(Arrays.asList(routesArray));
    }

    private String readfile(String filepath)throws IOException {
        String content = Files.readString(Paths.get(filepath));
        return content;
    }
}