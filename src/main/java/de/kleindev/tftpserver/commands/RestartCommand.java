package de.kleindev.tftpserver.commands;

import de.kleindev.tftpserver.utils.ApplicationManager;
import de.kleindev.tftpserver.utils.Command;

import java.io.IOException;

@Command.CommandClass
public class RestartCommand implements Command {
    @Override
    @ConsoleCommand(command = "restart")
    public void console(String raw, String[] args) {
        try {
            ApplicationManager.restartApplication(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
