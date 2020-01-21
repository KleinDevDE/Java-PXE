package de.kleindev.tftpserver.utils;

import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class CommandManager {
    private static HashMap<String, Command> consoleCommands = new HashMap<>();
    private static HashMap<String, String> consoleAliases = new HashMap<>(); //TODO make list as 2nd value

    public static void registerCommand() throws IllegalAccessException, InstantiationException {
        try {
            Reflections reflections = new Reflections("de.kleindev.tftpserver.commands");
            Set<Class<?>> allClasses =
                    reflections.getTypesAnnotatedWith(Command.CommandClass.class);
            for (Class<?> c : allClasses) {
                Constructor[] ctors = c.getDeclaredConstructors();
                Constructor ctor = null;
                for (Constructor constructor : ctors) {
                    ctor = constructor;
                    if (ctor.getGenericParameterTypes().length == 0)
                        break;
                }
                assert ctor != null;
                ctor.setAccessible(true);
                Command command = (Command) ctor.newInstance();

                for (Method method : command.getClass().getMethods()) {
                    if (method.isAnnotationPresent(Command.ConsoleCommand.class)) {
                        consoleCommands.put((method.getAnnotation(Command.ConsoleCommand.class)).command(), command);
                        if ((method.getAnnotation(Command.ConsoleCommand.class)).aliases().length != 0) {
                            for (String s : (method.getAnnotation(Command.ConsoleCommand.class)).aliases()) {
                                consoleAliases.put(s, (method.getAnnotation(Command.ConsoleCommand.class)).command());
                            }
                        }
                    }
                }
            }
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isCommand(CommandType commandType, String command) {
        if (commandType == CommandType.CONSOLE) {
            if (consoleCommands.containsKey(command))
                return true;
            else return consoleAliases.containsKey(command);
        }
        return false;
    }

    public static Command getCommand(CommandType commandType, String command) {
        if (commandType == CommandType.CONSOLE) {
            if (consoleCommands.containsKey(command))
                return consoleCommands.get(command);
            else return consoleCommands.get(consoleAliases.get(command));
        }
        return null;
    }

    public static ArrayList<String> getCommands(CommandType commandType) {
        ArrayList<String> end = new ArrayList<>();
        if (commandType == CommandType.CONSOLE) {
            for (String s : consoleCommands.keySet()) {
                end.add(s);
                if (consoleAliases.containsKey(s)) {
                    //TODO for each list entry
                    end.add("  " + consoleAliases.get(s));
                }
            }
        }
        return end;
    }
}
