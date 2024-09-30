package org.laetproject.dao.impl;

import org.laetproject.entities.Experiencia;
import org.laetproject.dao.ExperienciaDAO;
import org.laetproject.db.exceptions.DBException;
import org.laetproject.util.FileUtils;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExperienciaDAOImpl implements ExperienciaDAO {

    private final Connection connection;

    public ExperienciaDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void inserirExperiencia(Experiencia experiencia) {
        String sql = "insert into experiencia(guild_id, user_id, xp) values(?,?,?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, experiencia.getGuild());
            stmt.setString(2, experiencia.getUser());
            stmt.setDouble(3, experiencia.getXp());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet resultSet = stmt.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        int id = resultSet.getInt(1);
                        experiencia.setId(id);
                    }
                }
            } else {
                throw new DBException("Erro ao inserir linha");
            }
        } catch (SQLException e) {
            throw new DBException("Erro ao inserir experiencia: " + e.getMessage());
        }
    }

    @Override
    public void alterarExperiencia(Experiencia experiencia) {
        String sql = "update experiencia set guild_id = ?, user_id = ?, xp = ? where id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, experiencia.getGuild());
            preparedStatement.setString(2, experiencia.getUser());
            preparedStatement.setDouble(3, experiencia.getXp());
            preparedStatement.setInt(4, experiencia.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException("Erro ao alterar experiencia");
        }
    }

    @Override
    public void excluirExperiencia(Experiencia experiencia) {
        String sql = "DELETE FROM experiencia WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, experiencia.getId());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new DBException("Nenhuma experiência foi excluída. ID inválido: " + experiencia.getId());
            }
        } catch (SQLException e) {
            throw new DBException("Erro ao excluir experiência: " + e.getMessage());
        }
    }

    @Override
    public List<Experiencia> listarExperiencia() {
        String sql = "select * from experiencia";
        List<Experiencia> experiencias = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                experiencias.add(instanciarExperiencia(rs));
            }
        } catch (SQLException e) {
            throw new DBException("Erro ao listar experiencias" + e.getMessage());
        }

        return experiencias;
    }

    public void criarTabela() throws IOException {
        String sql = """
                CREATE TABLE IF NOT EXISTS experiencia (
                   id INTEGER PRIMARY KEY AUTOINCREMENT,
                   guild_id TEXT NOT NULL,
                   user_id TEXT NOT NULL,
                   xp REAL NOT NULL
                );
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DBException("Falha ao criar tabela");
        }
    }

    private Experiencia instanciarExperiencia(ResultSet rs) throws SQLException {
        return new Experiencia(
                rs.getInt("id"),
                rs.getString("guild_id"),
                rs.getString("user_id"),
                rs.getDouble("xp")
        );
    }


}
