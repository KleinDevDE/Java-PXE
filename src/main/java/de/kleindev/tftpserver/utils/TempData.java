package de.kleindev.tftpserver.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class TempData {
	public static boolean coloredConsole = false;
	public static boolean runningAsRoot = false;
	public static boolean noConsole = false;
	public static boolean ignoreCrashCheck = false;
	public static boolean debug = true;
	public static boolean debug_trace = true;
	public static HashMap<Long, Long> welcomeMessages = new HashMap<Long, Long>();
	public static ArrayList<Long> messagesWhoDontNeedToBeLogged = new ArrayList<>();
}