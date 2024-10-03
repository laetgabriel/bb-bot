package org.laetproject.dao;

import org.laetproject.entities.Cargo;
import org.laetproject.entities.Experiencia;

import java.io.IOException;
import java.util.List;

public interface CargoDAO {

    void inserirCargo(Cargo cargo);
    void alterarCargo(Cargo cargo);
    void excluirCargo(Cargo cargo);
    List<Cargo> listarCargos();
    Cargo buscarCargoPorId(Cargo id);
    void criarTabela() throws IOException;

}
