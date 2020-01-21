package de.kleindev.tftpserver.utils;

import de.kleindev.tftpserver.objects.LogType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogManager {
    public static void init(){
        if (!new File("logs").exists())
            new File("logs").mkdir();

        File latest_log = new File("logs/latest.log");
        if (latest_log.exists()){
            try {
                BasicFileAttributes attr = Files.readAttributes(latest_log.toPath(), BasicFileAttributes.class);
                String date = new SimpleDateFormat("dd.MM.yyyy_HH-mm-ss").format(attr.creationTime().toMillis());
                File old_log = new File("logs/"+ date + ".log");
                latest_log.renameTo(old_log);
                latest_log = old_log;
            } catch (IOException e) {
                }
            try {
                new File("logs/latest.log").createNewFile();
            } catch (IOException e) {
                }
        } else {
            try {
                latest_log.createNewFile();
                latest_log = null;
            } catch (IOException e) {
                }
        }
    }

    public static void log(LogType logType, String line, boolean print) {
        String line_new = "";
        switch (logType){
            case INFO:
                line_new = new SimpleDateFormat("[HH:mm:ss.SS]").format(new Date()) + " INFO    | " + line;
                if (print && !TempData.noConsole)
                    System.out.println(line_new);
                break;
            case ERROR:
                line_new = new SimpleDateFormat("[HH:mm:ss.SS]").format(new Date()) + " ERROR   | " + line;
                if (!print && TempData.noConsole)
                    break;
                if (TempData.coloredConsole)
                    System.out.println(new SimpleDateFormat("[HH:mm:ss.SS]").format(new Date()) + TextColor.RED + " ERROR" + TextColor.RESET + "   | " + line + TextColor.RESET);
                else System.out.println(new SimpleDateFormat("[HH:mm:ss.SS]").format(new Date()) + " ERROR   | " + line);
                break;
            case WARNING:
                if (!print && TempData.noConsole)
                    break;
                line_new = new SimpleDateFormat("[HH:mm:ss.SS]").format(new Date()) + " WARNING | " + line;
                if (TempData.coloredConsole)
                    System.out.println(new SimpleDateFormat("[HH:mm:ss.SS]").format(new Date()) + TextColor.YELLOW + " WARNING" + TextColor.RESET + " | " + line + TextColor.RESET);
                else System.out.println(new SimpleDateFormat("[HH:mm:ss.SS]").format(new Date()) + " WARNING | " + line);
                break;
            case DEBUG:
                if ((!print && TempData.noConsole) || !TempData.debug)
                    break;
                line_new = new SimpleDateFormat("[HH:mm:ss.SS]").format(new Date()) + " DEBUG   | " + line;
                if (!print && TempData.coloredConsole)
                    System.out.println(new SimpleDateFormat("[HH:mm:ss.SS]").format(new Date()) + TextColor.PURPLE + " DEBUG" + TextColor.RESET + "   | " + line + TextColor.RESET);
                else System.out.println(new SimpleDateFormat("[HH:mm:ss.SS]").format(new Date()) + " DEBUG   | " + line);
                break;
            case TRACE:
                if ((!print && TempData.noConsole) || !TempData.debug_trace)
                    break;
                line_new = new SimpleDateFormat("[HH:mm:ss.SS]").format(new Date()) + " TRACE   | " + line;
                if (!print && TempData.coloredConsole)
                    System.out.println(new SimpleDateFormat("[HH:mm:ss.SS]").format(new Date()) + TextColor.PURPLE + " TRACE" + TextColor.RESET + "   | " + line + TextColor.RESET);
                else System.out.println(new SimpleDateFormat("[HH:mm:ss.SS]").format(new Date()) + " TRACE   | " + line);
                break;
            case FATAL:
                if (!print && TempData.noConsole)
                    break;
                line_new = new SimpleDateFormat("[HH:mm:ss.SS]").format(new Date()) + " FATAL   | " + line;
                if (TempData.coloredConsole)
                    System.out.println(new SimpleDateFormat("[HH:mm:ss.SS]").format(new Date()) + " " + TextColor.RED + "FATAL" + TextColor.RESET + "   | " + TextColor.RED + line + TextColor.RESET);
                else System.out.println(new SimpleDateFormat("[HH:mm:ss.SS]").format(new Date()) + " FATAL   | " + line);
                break;
            case UPDATE:
                line_new = new SimpleDateFormat("[HH:mm:ss.SS]").format(new Date()) + " UPDATE  | " + line;
                if (!print && TempData.noConsole)
                    break;
                if (TempData.coloredConsole)
                    System.out.println(new SimpleDateFormat("[HH:mm:ss.SS]").format(new Date()) + TextColor.CYAN  + " UPDATE" + TextColor.RESET + "  | " + line + TextColor.RESET);
                else System.out.println(new SimpleDateFormat("[HH:mm:ss.SS]").format(new Date()) + " UPDATE  | " + line);
                break;
            case COMMAND:
                line_new = "> " + line;
                if (!print && TempData.noConsole)
                    break;
                System.out.println("> " + line);
                break;
            case RAW:
                line_new = line;
                if (print && !TempData.noConsole)
                    System.out.println(line);
                break;
        }
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter("logs/latest.log", true));
            output.write(line_new);
            output.newLine();
            output.flush();
            output.close();
        } catch (IOException e) {
            }
    }
}