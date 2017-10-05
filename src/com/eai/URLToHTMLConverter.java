package com.eai;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class URLToHTMLConverter {

    //Gets the input url returns it as a String
    public static String download(URL input) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (InputStream in = input.openStream()) {
            try {
                copy(in, out);
            } finally {
                out.flush();
            }
        }
        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }

    //Creates a buffer of 1024 bytes and copies data from the url to the ByteArrayOutputStream
    private static void copy(InputStream in, ByteArrayOutputStream out)
            throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int readCount = in.read(buffer);
            if (readCount == -1) {
                break;
            }
            out.write(buffer, 0, readCount);
        }
    }

}
