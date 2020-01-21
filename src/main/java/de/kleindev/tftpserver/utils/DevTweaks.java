package de.kleindev.tftpserver.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DevTweaks {
	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-";
	
    public static boolean isLong(String string) {
        try {
            Long.parseLong(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isFloat(String string) {
        try {
            Float.parseFloat(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isBoolean(String string) {
        try {
            Boolean.parseBoolean(string);
            return true;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }

    public static boolean isString(Object object) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(object);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidURL(String url) {
        try {
            URL u = new URL(url);
            if (u.openConnection() != null)
                return true;
        } catch (IOException e) {
            return false;
        }
        return false;
    }
    
    public static boolean isUUID(String string) {
		try {
			UUID.fromString(string);
			return true;
		} catch(Exception ex) {
			return false;
		}
	}
    
    /* https://dzone.com/articles/generate-random-alpha-numeric */
    public static String randomAlphaNumeric(int count) {
    StringBuilder builder = new StringBuilder();
    while (count-- != 0) {
    int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
    builder.append(ALPHA_NUMERIC_STRING.charAt(character));
    }
    return builder.toString();
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static String readInputStream(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }
    public static String listToString(List<String> list){
        StringBuilder builder = new StringBuilder();
        for (String s : list){
            if (builder.toString().equals(""))
                builder.append(s);
            else builder.append("\n").append(s);
        }
        return builder.toString();
    }
}
