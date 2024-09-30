package org.laetproject.dao;

import org.laetproject.entities.Experiencia;

import java.util.List;


public interface ExperienciaDAO {

    void inserirExperiencia(Experiencia experiencia);
    void alterarExperiencia(Experiencia experiencia);
    void excluirExperiencia(Experiencia experiencia);
    List<Experiencia> listarExperiencia();
}
