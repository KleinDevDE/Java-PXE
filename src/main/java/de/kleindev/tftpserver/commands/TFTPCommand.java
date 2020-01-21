package de.kleindev.tftpserver.commands;

import de.kleindev.tftpserver.TFTPServer;
import de.kleindev.tftpserver.objects.LogType;
import de.kleindev.tftpserver.utils.Command;
import de.kleindev.tftpserver.utils.LogManager;

@Command.CommandClass
public class TFTPCommand implements Command {
    @Override
    @ConsoleCommand(command = "tftp")
    public void console(String raw, String[] args) {
        if (args.length == 0){
            printHelp();
            return;
        }
        switch (args[0].toLowerCase()){
            case "help":
                printHelp();
                break;
            case "start":
                TFTPServer.startServer();
                break;
            case "stop":
                TFTPServer.stopServer();
                break;
            case "status":
                LogManager.log(LogType.RAW, TFTPServer.isOnline() ? "TFTPServer is RUNNING" : "TFTPServer is OFFLINE", true);
                break;
            case "kill":
                TFTPServer.kilLServer();
                break;
            case "restart":
                TFTPServer.stopServer();
                TFTPServer.startServer();
                break;
        }
    }

    private void printHelp() {
        LogManager.log(LogType.RAW, "" +
                "tftp start         Start TFTP server\n" +
                "tftp stop          Stop TFTP server\n" +
                "tftp status        Check if TFTP server running or not\n" +
                "tftp kill          Kill running TFTP server\n" +
                "tftp help          See this message", true);
    }
}
