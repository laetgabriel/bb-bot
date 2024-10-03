package org.laetproject.dao.impl;

import org.laetproject.dao.CargoDAO;
import org.laetproject.db.exceptions.DBException;
import org.laetproject.entities.Cargo;
import org.laetproject.entities.Experiencia;

import java.io.IOException;
import java.sql.*;
import java.util.List;

public class CargoDAOImpl implements CargoDAO {

    private final Connection connection;

    public CargoDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void inserirCargo(Cargo cargo) {
        String sql = "insert into cargo(guild_id, role_id) values (?,?)";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, cargo.getGuildId());
            preparedStatement.setLong(2, cargo.getRoleId());
            int linhasAfetadas = preparedStatement.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    int id = resultSet.getInt(1);
                    cargo.setId(id);
                }
            }
        }catch (SQLException e) {
            throw new DBException("Erro ao inserir cargo");
        }
    }

    @Override
    public void alterarCargo(Cargo cargo) {
        String sql = "update cargo set guild_id = ?, role_id = ? where id = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, cargo.getGuildId());
            preparedStatement.setLong(2, cargo.getRoleId());
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            throw new DBException("Erro ao alterar cargo");
        }
    }

    @Override
    public void excluirCargo(Cargo cargo) {
        String sql = "delete from cargo where id = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            int linhas = preparedStatement.executeUpdate();
            if(linhas == 0) {
                throw new DBException("Cargo não encontrado");
            }
        }catch (SQLException e) {
            throw new DBException("Erro ao excluir cargo");
        }
    }

    @Override
    public List<Cargo> listarCargos() {
        return List.of();
    }

    @Override
    public Cargo buscarCargoPorId(Cargo id) {
        String sql = "select * from cargo where id = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, id.getId());
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    return instanciarCargo(resultSet);
                }
            }
        }catch (SQLException e) {
            throw new DBException("Erro ao buscar cargo");
        }
        return null;
    }

    @Override
    public void criarTabela() throws IOException {
        String sql =
                """
                CREATE TABLE IF NOT EXISTS cargo (
                   id INTEGER PRIMARY KEY AUTOINCREMENT,
                   guild_id TEXT NOT NULL,
                   role_id TEXT NOT NULL,
                );
                """;

        try(PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
         ps.execute();
        } catch (SQLException e) {
            throw new DBException("Erro ao criar tabela");
        }

    }

    private Cargo instanciarCargo(ResultSet rs) throws SQLException {
        return new Cargo(
                rs.getInt("id"),
                rs.getString("guild_id"),
                rs.getLong("role_id")
        );
    }
}
