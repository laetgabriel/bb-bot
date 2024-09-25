package org.laetproject.util;

import java.io.*;
import java.util.HashSet;
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

    public void loadConfiguration(String path, Set<String> word) {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("Arquivo não encontrado.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                word.add(line.toLowerCase().trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
