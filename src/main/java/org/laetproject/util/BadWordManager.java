package org.laetproject.util;

import java.util.HashSet;
import java.util.Set;

public final class BadWordManager extends WordManager {

    private final static String PATH = "badwords.txt";
    private Set<String> badWords = new HashSet<>();
/*
    public BadWordManager() {
        createFile(PATH);
        loadConfiguration(PATH, badWords);
    }
*/
    public boolean containsWord(String word) {
        for(String w : badWords) {
            if(w.toLowerCase().contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
