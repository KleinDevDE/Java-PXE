package de.kleindev.tftpserver.commands;

import de.kleindev.tftpserver.utils.Command;
@Command.CommandClass
public class InfoCommand implements Command{

	@Override
	@ConsoleCommand(command = "info", aliases = {"i"})
	public void console(String raw, String[] args) {

	}
}
