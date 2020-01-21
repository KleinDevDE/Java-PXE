package de.kleindev.tftpserver.commands;

import de.kleindev.tftpserver.utils.Command;

@Command.CommandClass
public class StopCommand implements Command {
    @Override
    @ConsoleCommand(command = "stop", aliases = {"exit", "end", "quit", "q"})
    public void console(String raw, String[] args) {
        System.exit(0);
    }
}
