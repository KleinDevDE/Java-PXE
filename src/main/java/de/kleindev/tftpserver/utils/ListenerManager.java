package de.kleindev.tftpserver.utils;

import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

public class ListenerManager {
    private static HashMap<Long, Listener> channelListenerMap = new HashMap<>();
    private static HashMap<String, Listener> socketListenerMap = new HashMap<>();

    public static void registerListeners() throws IllegalAccessException, InstantiationException {
        Reflections reflections = new Reflections("de.kleindev.tftpserver.listeners.custom");
        Set<Class<?>> allClasses =
                reflections.getTypesAnnotatedWith(Listener.ListenerClass.class);
        for (Class<?> c :  allClasses) {
            Listener listener = (Listener) c.newInstance();
            for (Method method : listener.getClass().getMethods()) {
                if (method.isAnnotationPresent(Listener.ChannelListener.class))
                    channelListenerMap.put((method.getAnnotation(Listener.ChannelListener.class)).channelID(), (Listener) c.newInstance());
                if (method.isAnnotationPresent(Listener.SocketListener.class))
                    socketListenerMap.put((method.getAnnotation(Listener.SocketListener.class)).action(), (Listener) c.newInstance());
            }
        }
    }

    public static boolean isListening(long channelID) {
        return channelListenerMap.containsKey(channelID);
    }

    public static Listener getListeningClass(long channelID) {
        return channelListenerMap.get(channelID);
    }

    public static boolean isListening(String action) {
        return socketListenerMap.containsKey(action);
    }

    public static Listener getListeningClass(String action) {
        return socketListenerMap.get(action);
    }
}
