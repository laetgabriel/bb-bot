package org.laetproject.util;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordManager {


    public void createFile(String src) {
        File file = new File(src);
        try {
            if (file.createNewFile()) {
                System.out.println("Arquivo criado: " + file.getAbsolutePath());
            } else {
                System.out.println("Arquivo já existe: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Erro ao criar o arquivo: " + e.getMessage());
        }
    }

    public static String loadConfiguration(InputStream inputStream, String filmes) {
        StringBuilder filmesBuilder = new StringBuilder(filmes);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                filmesBuilder.append(line.toLowerCase().trim()).append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return filmesBuilder.toString();
    }

    public void writeLine(String line, String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("Arquivo não encontrado.");
            return;
        }

        String[] palavras = line.split(",");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            for (String palavra : palavras) {
                bw.write(palavra);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
