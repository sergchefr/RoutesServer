package ru.ifmo.migration;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.ifmo.coll.Route;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

public class Jsonwriter implements Saver{
    @Override
    public void writeRoute(Route[] routes, String filename) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        filename=filename.replace("\\","/");
        Path filepath = Paths.get(filename);

        try (PrintWriter writer = new PrintWriter(new FileWriter(filepath.toFile()))){
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            objectMapper.writeValue(filepath.toFile(), routes);
        } catch (IOException e) {
            throw new IOException("error while writing XML");
        }
    }
}
