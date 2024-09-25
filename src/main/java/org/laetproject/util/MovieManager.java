package org.laetproject.util;


import java.util.HashSet;
import java.util.Set;

public final class MovieManager extends BadWordManager {

    private final static String PATH = "filmes.txt";
    private Set<String> filmes;

    public MovieManager(){
        filmes = new HashSet<>();
        createFile(PATH);
        loadConfiguration(PATH, filmes);
    }

}
