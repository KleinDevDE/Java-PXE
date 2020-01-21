package de.kleindev.tftpserver;

import de.kleindev.tftpserver.objects.LogType;
import de.kleindev.tftpserver.utils.*;
import de.kleindev.tftpserver.utils.configuration.file.FileConfiguration;
import de.kleindev.tftpserver.utils.configuration.file.YamlConfiguration;
import org.apache.commons.lang3.SystemUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static FileConfiguration configuration;
    public final static String version = "v0.1-pre1";

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        LogManager.init();
        LogManager.log(LogType.INFO, "Initializing...", true);
        ExceptionListener.registerExceptionHandler();
        if (!new File("data/config.yml").exists()) {
            LogManager.log(LogType.INFO, "Detected first Run! Creating files...", true);
            firstRun();
            LogManager.log(LogType.INFO, "Please edit the config.yml\nStopping..", true);
            System.exit(0);
        }
        configuration = YamlConfiguration.loadConfiguration(new File("data/config.yml"));
        checkArgs(args);
        checkRoot();
        CommandManager.registerCommand();
        TFTPServer.init();
        LogManager.log(LogType.INFO, "Starting TFTP server", true);
        TFTPServer.startServer();
        listenCommands();
    }

    private static void firstRun() {
        new File("data").mkdir();
        InputStream initialStream = Main.class.getClassLoader().getResourceAsStream("config.yml");
        File targetFile = new File("data/config.yml");

        try {
            java.nio.file.Files.copy(
                    initialStream,
                    targetFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
            initialStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void registerHooks() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                //TODO find alternate method
                new File("data/.running").delete();
            }
        });
    }

    private static void checkArgs(String[] args) {
        List<String> stringList = new ArrayList<String>(Arrays.asList(args));

        TempData.coloredConsole = stringList.contains("-color");
        TempData.runningAsRoot = stringList.contains("-RunningAsRootIsEvilAndIKnowThat");
        TempData.noConsole = stringList.contains("-noconsole");
        TempData.ignoreCrashCheck = stringList.contains("-ignoreCrashCheck");
        TempData.debug = stringList.contains("-debug");
        if (stringList.contains("-forceUpdate"))
            forceUpdate();
    }

    private static void forceUpdate() {
        LogManager.log(LogType.INFO, "Starting config conversion...", true);
        new File("data/config.yml").renameTo(new File("data/config.yml.bkp"));
        //TODO update to YAML format
        YamlConfiguration config_new = YamlConfiguration.loadConfiguration(new InputStreamReader(Main.class.getClassLoader().getResourceAsStream("config.yml")));


        for (String key : config_new.getKeys(true)) {
            if (configuration.contains(key))
                config_new.set(key, configuration.get(key));
        }

        try {
            config_new.save(new File("data/config.yml"));
            configuration = config_new;
            LogManager.log(LogType.INFO, "Done!", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void checkRoot() {
        if (SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_UNIX) {
            try {
                Process p = Runtime.getRuntime().exec("id -u");
                if (!DevTweaks.readInputStream(p.getInputStream()).equals("0") && Main.configuration.getInt("tftp_server.port") < 1025) {
                    LogManager.log(LogType.FATAL, "You must run this software as root if u want to listen on port " + Main.configuration.getInt("tftp_server.port"), true);
                    System.exit(1);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void listenCommands() {
        Thread thread = new Thread(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("> ");
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    // Trigger ConsoleCommandEvent
                    LogManager.log(LogType.RAW, "> " + line, false);
                    String cmd = line;
                    if (line.contains(" "))
                        cmd = line.split(" ")[0];
                    String[] args = line.replaceFirst(cmd + " ", "").replaceFirst(cmd, "").split(" ");
                    if (CommandManager.isCommand(CommandType.CONSOLE, cmd)) {
                        Command command = CommandManager.getCommand(CommandType.CONSOLE, cmd);
                        Method[] methods = command.getClass().getMethods();
                        boolean success = false;
                        for (Method m : methods) {
                            if (m.isAnnotationPresent(Command.ConsoleCommand.class)) {
                                try {
                                    m.invoke(command, line, args);
                                    success = true;
                                    break;
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                }
                            }
                        }
                        if (!success)
                            LogManager.log(LogType.ERROR, "There was an error while processing class \"" + command.getClass().getName() + "\"", true);
                        LogManager
                                .log(LogType.DEBUG,
                                        "Could not find any method with Annotation \"@ConsoleCommand\" of class \""
                                                + command.getClass().getName() + "\" or method throws an exception",
                                        true);
                    } else {
                        LogManager.log(LogType.INFO, "There is no command called \"" + cmd + "\"", true);
                    }
                    System.out.print("> ");
                }
            } catch (IOException e) {
                LogManager.log(LogType.ERROR, "Could not read console input!", true);
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }
}
