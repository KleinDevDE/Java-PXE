package de.kleindev.tftpserver.utils;

import de.kleindev.tftpserver.objects.LogType;

import java.io.*;

public class ExceptionListener implements Thread.UncaughtExceptionHandler {
    public void uncaughtException(Thread t, Throwable e) {
        handle(e);
    }

    public void handle(Throwable throwable) {
        try {
        	//TODO Exception handling
            LogManager.log(LogType.ERROR, "An error occurred! See latest log for more information.", true);
            throwable.printStackTrace(new PrintStream(new FileOutputStream("logs/latest.log", true)));
        } catch (Throwable t) {
            // don't let the exception get thrown out, will cause infinite looping!
        }
    }

    public static void registerExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionListener());
        System.setProperty("sun.awt.exception.handler", ExceptionListener.class.getName());
    }
}