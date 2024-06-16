package com.ez.sisemp.login.dao;

import com.ez.sisemp.admin.entity.UsuarioEntity;
import com.ez.sisemp.shared.config.MySQLConnection;
import com.ez.sisemp.login.exception.UserOrPassIncorrectException;
import com.ez.sisemp.login.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDao {
    private static final String SQL_GET_USER = "SELECT * FROM usuario WHERE nombre_usuario = ? AND contrasena = ?";
    private static final String SQL_GET_USER_JPA = """
            SELECT u FROM UsuarioEntity u WHERE u.nombreUsuario = :username AND u.contrasena = :password
            """;
    public Usuario login(String username, String password) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = MySQLConnection.getConnection()
                .prepareStatement(SQL_GET_USER);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            return mapResultSetToUsuario(resultSet);
        }else {
            throw new UserOrPassIncorrectException("Usuario o contraseña incorrectos");
        }
    }
    public UsuarioEntity loginJPA(String username, String password)  {
      EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("devUnit");
      EntityManager entityManager = entityManagerFactory.createEntityManager();

      TypedQuery<UsuarioEntity> query = entityManager.createQuery(SQL_GET_USER_JPA, UsuarioEntity.class);
      query.setParameter("username", username);
      query.setParameter("password", password);

      UsuarioEntity usuarioEntity = query.getSingleResult();
      return usuarioEntity;

    }

    private Usuario mapResultSetToUsuario(ResultSet resultSet) throws SQLException {
        return new Usuario(resultSet.getInt("id"),
                resultSet.getString("nombre_usuario"),
                resultSet.getString("contrasena"),
                resultSet.getString("primer_nombre"),
                resultSet.getString("apellido_pat"),
                resultSet.getString("foto_perfil"),
                resultSet.getInt("id_rol")
        );
    }
}
