package de.kleindev.tftpserver.commands;

import de.kleindev.tftpserver.Main;
import de.kleindev.tftpserver.objects.LogType;
import de.kleindev.tftpserver.utils.Command;
import de.kleindev.tftpserver.utils.LogManager;
import de.kleindev.tftpserver.utils.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@Command.CommandClass
public class ConfigCommand implements Command {
    @Override
    @ConsoleCommand(command = "config")
    public void console(String raw, String[] args) {
        if (args.length == 0) {
            LogManager.log(LogType.INFO, "config [list/get/set/reload] (key) (value)", true);
            return;
        }
        switch (args[0].toLowerCase()){
            case "help":
                printHelp();
                break;
            case "list":
                for (String key : Main.configuration.getKeys(true)){
                    if(!Main.configuration.isConfigurationSection(key))
                        LogManager.log(LogType.RAW, "Key: "+key + "\nValue: " + Main.configuration.get(key)+"\n", true);
                }
                break;
            case "set":
                if (args.length != 3) {
                    printHelp();
                    break;
                }
                Main.configuration.set(args[1], args[2]);
                try {
                    Main.configuration.save(new File("data/config.yml"));
                    LogManager.log(LogType.RAW, "Done!", true);
                } catch (IOException e) {

                }
                break;
            case "get":
                if (args.length != 2) {
                    printHelp();
                    break;
                }
                if(Main.configuration.contains(args[1]))
                    LogManager.log(LogType.RAW, Main.configuration.getString(args[1]), true);
                else LogManager.log(LogType.ERROR, "Key \""+args[1]+"\" doesn't exists in configuration!", true);
                break;
            case "reload":
                Main.configuration = YamlConfiguration.loadConfiguration(new File("data/config.yml"));
                break;
            default:
                printHelp();
                break;
        }
    }

    private void printHelp(){
        LogManager.log(LogType.RAW, "\n" +
                "config list                    List all config values\n" +
                "config set [key] [value]       Set config value and save it automatically\n" +
                "config get [key]               Get config value\n" +
                "config reload                  Reload config if manually changed", true);
    }
}
