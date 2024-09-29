package org.laetproject.dao;

import entities.Experiencia;


public interface ExperienciaDAO {

    void inserirExperiencia(Experiencia experiencia);
    void alterarExperiencia(Experiencia experiencia);
    void excluirExperiencia(Experiencia experiencia);
    Experiencia obterExperiencia();

}
