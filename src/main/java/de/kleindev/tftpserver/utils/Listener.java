package de.kleindev.tftpserver.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface Listener {
    @Retention(RetentionPolicy.RUNTIME)
    @interface ListenerClass {}

    @Retention(RetentionPolicy.RUNTIME)
    @interface ChannelListener {
        long channelID();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface SocketListener {
        String action();
    }
}
