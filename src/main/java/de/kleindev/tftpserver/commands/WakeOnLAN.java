package de.kleindev.tftpserver.commands;

import de.kleindev.tftpserver.utils.Command;

@Command.CommandClass
public class WakeOnLAN implements Command {
    @Override
    @ConsoleCommand(command = "wakeonlan", aliases = {"wol"})
    public void console(String raw, String[] args) {

    }
}
