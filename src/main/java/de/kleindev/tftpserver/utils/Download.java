package de.kleindev.tftpserver.utils;

import java.io.*;
import java.net.URL;

/**
 * ===============================
 * Discord Bot
 * Created by Xylit
 * 2017
 * ==============================
 */
public class Download {
    public static void image(String url, String name){
        try {
            URL link = new URL(url);
            InputStream in = new BufferedInputStream(link.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();
            FileOutputStream fos = new FileOutputStream("images/" + name);
            fos.write(response);
            fos.close();
        }catch (IOException e) {

        }
    }
}
