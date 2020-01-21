package de.kleindev.tftpserver.utils;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface Command{
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ConsoleCommand{
        String command();
        String[] aliases() default {};
    }


    public void console(String raw, String[] args);

    @Retention(RetentionPolicy.RUNTIME)
    public @interface CommandClass{}
}
