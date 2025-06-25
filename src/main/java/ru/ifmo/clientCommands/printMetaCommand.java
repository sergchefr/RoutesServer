package ru.ifmo.clientCommands;

import ru.ifmo.Main;
import ru.ifmo.ServerManager;
import ru.ifmo.coll.Route;
import ru.ifmo.migration.IllegalParamException;
import ru.ifmo.migration.Jsonreader;
import ru.ifmo.migration.Jsonwriter;
import ru.ifmo.migration.XMLreader;
import ru.ifmo.passwordmanager.PasswordManager;
import ru.ifmo.transfer.Request;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;

public class printMetaCommand implements Icommand{
    XMLreader xmLreader = new XMLreader();
    Jsonreader jsonreader = new Jsonreader();

    @Override
    public String execute(Request com) {
        if(!PasswordManager.getInstance().checkPassword(com.getUser(), com.getPassword())) return "ошибка доступа";

        String command = com.getCommand();
        String filename = command.split(" ")[1].split("=")[1];
        ServerManager.getInstance().addCommandToHistory(command.split(" ")[0]);

        StringBuilder sb = new StringBuilder();
        File file = new File(filename);

        sb.append("Файл: ").append(file.getAbsolutePath()).append("\n");

        long sizeBytes = file.length();
        sb.append(String.format("Размер: %.1f КБ%n", sizeBytes / 1024.0));

        try {
            BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            Instant lastModified = attrs.lastModifiedTime().toInstant();
            sb.append("Дата изменения: ")
                    .append(LocalDateTime.ofInstant(lastModified, ZoneId.systemDefault()))
                    .append("\n");
        } catch (IOException e) {
            sb.append("Не удалось прочитать дату изменения\n");
        }

        sb.append("Кодировка: UTF-8\n");

        sb.append("Расширение: "+filename.split("\\.")[1]+"\n");

        ArrayList<Route> routes = null;
        try {
            if(filename.split("\\.")[1].equals("xml")){
                routes = xmLreader.getRoutes(filename);
            }else if (filename.split("\\.")[1].equals("json")){
                routes = jsonreader.getRoutes(filename);
            }
            sb.append("структура файла не нарушена\n");
        } catch (IOException |IllegalParamException e) {
            sb.append("файл проврежден");
            return sb.toString();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            int count = routes.size();
            Date oldest = new Date();
            int minid=Integer.MAX_VALUE;
            int maxid = Integer.MIN_VALUE;
            double maxlength = 0;
            Hashtable<String, Integer> userData = new Hashtable<>();
            for (Route route : routes) {
                minid = Math.min(minid, route.getId());
                maxid = Math.max(maxid, route.getId());
                maxlength = Math.max(maxlength, route.getDistance());
                if (route.getCreationDate().before(oldest)) oldest=route.getCreationDate();

                userData.put(route.getOwnername(), userData.getOrDefault(route.getOwnername(), 0) + 1);
            }

            sb.append("Статистика:\n");
            sb.append("  - Всего записей: ").append(count).append("\n");
            sb.append("  - Самый старый объект: ")
                    .append(simpleDateFormat.format(oldest)).append("\n");
            sb.append("  - Самый длинный путь: ")
                            .append(maxlength).append("\n");
            sb.append("  - Диапазон ID: ").append(minid).append("-").append(maxid).append("\n");
            sb.append("пользователи:\n");
            for (String s : userData.keySet()) {
                sb.append(s).append(": ").append(userData.get(s));
            }



        } catch (Exception e) {
            sb.append("Ошибка. ").append(e.getMessage()).append("\n");
        }

        return sb.toString();
    }

    @Override
    public String getName() {
        return "print_meta";
    }
}
