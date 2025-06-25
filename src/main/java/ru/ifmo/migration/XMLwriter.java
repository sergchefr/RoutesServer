package ru.ifmo.migration;

import ru.ifmo.coll.Route;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

/** Класс, записывающий объекты в XML.
 *
 */
public class XMLwriter implements Saver {

    /** Принимает массив путей
     * @param routes массив путей
     * @param filename путь к файлу ли название сохранения
     * @throws IOException если не удается создать файл
     */
    public void writeRoute(Route[] routes, String filename)throws IOException {

        filename=filename.replace("\\","/");
        Path filepath = Paths.get(filename);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try (PrintWriter writer = new PrintWriter(new FileWriter(filepath.toFile()))) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
            writer.println("<data>");
            for (Route route : routes) {
                writer.println("\t<route>");
                writer.println("\t\t<id>" + route.getId() + "</id>");
                writer.println("\t\t<name>" + route.getName() + "</name>");
                writer.println("\t\t<creationDate>" + simpleDateFormat.format(route.getCreationDate()) + "</creationDate>");
                writer.println("\t\t<locationFrom>");
                writer.println("\t\t\t<name>" + route.getFromLocation().getName() + "</name>");
                writer.println("\t\t\t<x>" + route.getFromLocation().getX() + "</x>");
                writer.println("\t\t\t<y>" + route.getFromLocation().getY() + "</y>");
                writer.println("\t\t\t<z>" + route.getFromLocation().getZ() + "</z>");
                writer.println("\t\t</locationFrom>");
                writer.println("\t\t<locationTo>");
                writer.println("\t\t\t<name>" + route.getToLocation().getName() + "</name>");
                writer.println("\t\t\t<x>" + route.getToLocation().getX() + "</x>");
                writer.println("\t\t\t<y>" + route.getToLocation().getY() + "</y>");
                writer.println("\t\t\t<z>" + route.getToLocation().getZ() + "</z>");
                writer.println("\t\t</locationTo>");
                writer.println("\t\t<distance>" + route.getDistance() + "</distance>");
                writer.println("\t</route>");
            }
            writer.println("</data>");
        } catch (IOException e) {
            throw new IOException("error while writing XML");
        }
    }
}
