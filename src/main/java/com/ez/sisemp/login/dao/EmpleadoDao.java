package com.ez.sisemp.login.dao;

import com.ez.sisemp.empleado.model.Empleado;
import com.ez.sisemp.login.exception.UserOrPassIncorrectException;
import com.ez.sisemp.shared.config.MySQLConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmpleadoDao {
    private static final String SQL_GET_USER_BY_ID = "SELECT * FROM empleado WHERE id = ?";

    public Empleado editar(String id) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = MySQLConnection.getConnection()
                .prepareStatement(SQL_GET_USER_BY_ID);
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            return mapResultSetToEmpleado(resultSet);
        }else {
            throw new UserOrPassIncorrectException("no se encontro datos");
        }
    }
    private Empleado mapResultSetToEmpleado(ResultSet resultSet) throws SQLException {
        return new Empleado(resultSet.getInt("id"),
                resultSet.getString("codigo_empleado"),
                resultSet.getString("nombres"),
                resultSet.getString("apellido_pat"),
                resultSet.getString("apellido_mat"),
                resultSet.getString("id_departamento"),
                resultSet.getString("correo"),
                resultSet.getDouble("salario")
        );
    }

}
