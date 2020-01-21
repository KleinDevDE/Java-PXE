package de.kleindev.tftpserver;

import de.kleindev.tftpserver.objects.*;
import de.kleindev.tftpserver.utils.LogManager;
import de.kleindev.tftpserver.objects.*;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class TFTPServer {
    private static Thread thread;
    private static ArrayList<String> clients = new ArrayList<>();

    public static void startServer(){
        if(thread.isAlive())
            LogManager.log(LogType.ERROR, "Server already running!", true);
        else {
            LogManager.log(LogType.INFO, "Starting TFTP Server", true);
            thread.start();
        }
    }

    public static void stopServer(){
        if(!thread.isAlive())
            LogManager.log(LogType.ERROR, "Server already stopped!", true);
        else {
            LogManager.log(LogType.INFO, "Stopping TFTP Server", true);
            thread.stop();
        }
    }

    public static void killServer(){
        if(!thread.isAlive())
            LogManager.log(LogType.ERROR, "Server already stopped!", true);
        else {
            LogManager.log(LogType.INFO, "Killing TFTP Server", true);
            thread.interrupt();
        }
    }

    public static boolean isOnline(){
        return thread.isAlive();
    }

    public static void init(){
        LogManager.log(LogType.TRACE, "TFTPServer.class | init() | thread = new Thread [...]", true);
        thread = new Thread(() -> {
            try {
                LogManager.log(LogType.TRACE, "TFTPServer.class | init() | Thread | DatagramSocket [...]", true);
                DatagramSocket sock = new DatagramSocket(Main.configuration.getInt("tftp_server.port", 69), Inet4Address.getByName(Main.configuration.getString("tftp_server.ip", "0.0.0.0")));

                LogManager.log(LogType.TRACE, "TFTPServer.class | init() | while(true) {", true);
                while (true) {
                    TFTPpacket in = TFTPpacket.receive(sock);
                    LogManager.log(LogType.TRACE, "TFTPServer.class | init() | TFTPacket received", true);
                    LogManager.log(LogType.TRACE, "TFTPServer.class | init() | in instanceof TFTPread", true);
                    if (in instanceof TFTPread) {
                        System.out.println("Read Request from " + in.getAddress());
                        LogManager.log(LogType.TRACE, "TFTPServer.class | init() | new TFTPserverRRQ", true);
                        TFTPserverRRQ r = new TFTPserverRRQ((TFTPread) in);
                        LogManager.log(LogType.TRACE, "TFTPServer.class | init() | continue;", true);
                        continue;
                    }
                    // receive write request
                    LogManager.log(LogType.TRACE, "TFTPServer.class | init() | in instanceof TFTPwrite", true);
                    if (in instanceof TFTPwrite) {
                        System.out.println("Write Request from " + in.getAddress());
                        LogManager.log(LogType.TRACE, "TFTPServer.class | init() | new TFTPserverQRW", true);
                        TFTPserverWRQ w = new TFTPserverWRQ((TFTPwrite) in);
                        LogManager.log(LogType.TRACE, "TFTPServer.class | init() | continue;", true);
                        continue;
                    }
                }
            } catch (IOException | TftpException e) {

            }
        });
    }
}
