package org.laetproject.commands.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class BadWordsManager {

    private Set<String> badWords;

    public BadWordsManager() {
        badWords = new HashSet<>();
        createFile("badwords.txt"); // Cria o arquivo se não existir
        loadConfiguration();
    }

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

    private void loadConfiguration() {
        File file = new File("badwords.txt");
        if (!file.exists()) {
            System.out.println("Arquivo badwords.txt não encontrado.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                badWords.add(line.toLowerCase().trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean containsBadWord(String word) {
        for(String badWord : badWords) {
            if(word.toLowerCase().contains(badWord.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
