package de.kleindev.tftpserver.commands;

import de.kleindev.tftpserver.objects.LogType;
import de.kleindev.tftpserver.utils.Command;
import de.kleindev.tftpserver.utils.LogManager;

@Command.CommandClass
public class TestCommand implements Command {
    @Override
    @ConsoleCommand(command = "test")
    public void console(String raw, String[] args) {
        for (LogType type : LogType.values()){
            LogManager.log(type, "Log type \""+type.name()+"\"", true);
        }
    }
}
