package org.laetproject.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class FileUtils {

    private static final Logger LOG = Logger.getLogger(FileUtils.class.getName());

    public static String loadTextFile(final String filename) throws IOException {
        long time = System.currentTimeMillis();

        BufferedReader br = new BufferedReader(new FileReader(filename));
        StringBuilder sb = new StringBuilder();
        String line = null;

        while((line = br.readLine()) != null){
            sb.append(line).append("\n");
        }
        br.close();

        time = System.currentTimeMillis() - time;

        LOG.info(filename + " foi lido " + time + " ms");

        return sb.toString();
    }

}
