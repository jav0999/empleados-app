package com.ez.sisemp.empleado.dao;

import com.ez.sisemp.empleado.entity.EmpleadoEntity;
import com.ez.sisemp.empleado.model.Empleado;
import com.ez.sisemp.shared.config.MySQLConnection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;


import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDao{
    private static final String SQL_GET_ALL_EMPLEADOS = """
    SELECT 
        e.id, 
        e.codigo_empleado, 
        e.nombres, 
        e.apellido_pat, 
        e.apellido_mat, 
        d.nombre AS departamento, 
        e.correo, 
        FLOOR(DATEDIFF(NOW(), e.fecha_nacimiento) / 365.25) AS edad, 
        e.salario 
    FROM 
        empleado e
    INNER JOIN departamentos d ON d.id = e.id_departamento  
    WHERE 
        e.activo = 1;
    """;
    private static final String SQL_UPDATE_EMPLEADO = "UPDATE empleado SET nombres = ?, apellido_pat = ?, apellido_mat = ?, id_departamento = ?, correo = ?, salario = ? WHERE id = ?;";
    private static final String SQL_DELETE_EMPLEADO = "UPDATE empleado set activo=0 WHERE id = ?;";
    private static final String SQL_INSERT_EMPLEADO = "INSERT INTO empleado (codigo_empleado, nombres, apellido_pat, apellido_mat, id_departamento, correo, fecha_nacimiento, salario) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String SQL_GET_NEW_EMPLEADO_CODE = "SELECT CONCAT('EMP', LPAD(MAX(CAST(SUBSTRING(codigo_empleado, 4) AS UNSIGNED)) + 1, 4, '0')) AS next_emp_code FROM empleado;";

    private static final String SQL_GET_ALL_EMPLEADOS_JPA = """
            Select  e
            from EmpleadoEntity e
            """;

    private static final String SQL_DELETE_EMPLEADO_JPA = "UPDATE EmpleadoEntity e SET e.estado = 0 WHERE e.id = :id";



    public List<Empleado> obtenerEmpleados() throws SQLException, ClassNotFoundException {
        List<Empleado> empleados = new ArrayList<>();
        PreparedStatement preparedStatement = MySQLConnection.getConnection()
                                                .prepareStatement(SQL_GET_ALL_EMPLEADOS);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            empleados.add(mapResultSetToEmpleado(resultSet));
        }
        return empleados;
    }

    public List<EmpleadoEntity> obtenerEmpleadosJPA () {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("devUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        var empleados = entityManager.createQuery(SQL_GET_ALL_EMPLEADOS_JPA, EmpleadoEntity.class).getResultList();
        return empleados;
    }

    public void editarEmpleado (Empleado empleado) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = MySQLConnection.getConnection()
                .prepareStatement(SQL_UPDATE_EMPLEADO);
        preparedStatement.setString(1, empleado.nombres());
        preparedStatement.setString(2, empleado.apellidoPat());
        preparedStatement.setString(3, empleado.apellidoMat());
        preparedStatement.setInt(4, empleado.idDepartamento());
        preparedStatement.setString(5, empleado.correo());
        preparedStatement.setDouble(6, empleado.salario());
        preparedStatement.setDouble(7, empleado.id());
        preparedStatement.executeUpdate();
    }

    public void eliminarEmpleado(int id) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = MySQLConnection.getConnection()
                                                .prepareStatement(SQL_DELETE_EMPLEADO);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }
    public void eliminarEmpleadoJpa(int id) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("devUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery(SQL_DELETE_EMPLEADO_JPA);
        query.setParameter("id",id);
        int rowsUpdated = query.executeUpdate();
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    public void agregarEmpleado(Empleado empleado) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = MySQLConnection.getConnection()
                                                .prepareStatement(SQL_INSERT_EMPLEADO);
        preparedStatement.setString(1, empleado.codigoEmpleado());
        preparedStatement.setString(2, empleado.nombres());
        preparedStatement.setString(3, empleado.apellidoPat());
        preparedStatement.setString(4, empleado.apellidoMat());
        preparedStatement.setInt(5, empleado.idDepartamento());
        preparedStatement.setString(6, empleado.correo());
        preparedStatement.setDate(7, new Date(empleado.fechaNacimiento().getTime()));
        preparedStatement.setDouble(8, empleado.salario());
        preparedStatement.executeUpdate();
    }

    private Empleado mapResultSetToEmpleado(ResultSet resultSet) throws SQLException {
        return new Empleado(resultSet.getInt("id"),
                resultSet.getString("codigo_empleado"),
                resultSet.getString("nombres"),
                resultSet.getString("apellido_pat"),
                resultSet.getString("apellido_mat"),
                resultSet.getString("departamento"),
                resultSet.getString("correo"),
                resultSet.getDouble("salario")
        );
    }

    public void agregarEmpleadoJPA (EmpleadoEntity empleado) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("devUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(empleado);
            entityManager.getTransaction().commit();
        } finally {
                entityManager.close();
        }
    }

    public EmpleadoEntity consultarEmpleadoById (long id) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("devUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            var empleado = entityManager.find(EmpleadoEntity.class, id);
            return empleado;
        } finally {
            entityManager.close();
        }
    }

    public void editarEmpleadoJPA (EmpleadoEntity empleado) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("devUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.merge(empleado);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }
}
