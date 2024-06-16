package com.ez.sisemp.empleado.dao;

import com.ez.sisemp.empleado.model.EmpleadoDashboard;
import com.ez.sisemp.shared.config.MySQLConnection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;

public class EmpleadoDashboardDao {

    private static final String SQL_GET_TOTAL_EMPLEADOS = "SELECT COUNT(*) FROM empleado";
    private static final String SQL_GET_PROMEDIO_EDAD = "SELECT FLOOR(AVG(DATEDIFF(NOW(), fecha_nacimiento) / 365.25)) AS avg_age FROM empleado;";
    private static final String SQL_GET_MAYOR_SALARIO = "SELECT MAX(salario) FROM empleado";
    private static final String SQL_GET_TOTAL_DEPARTAMENTOS = "SELECT COUNT(DISTINCT id_departamento) FROM empleado"; //TODO

    //JPQL
    private static final String GET_TOTAL_EMPLEADOS_JPA = "SELECT COUNT(e) FROM EmpleadoEntity e";
    private static final String GET_PROMEDIO_EDAD_JPA = "SELECT e.fechaNacimiento FROM EmpleadoEntity e";
    private static final String GET_MAYOR_SALARIO_JPA = "SELECT MAX(salario) FROM EmpleadoEntity e";
    private static final String GET_TOTAL_DEPARTAMENTOS_JPA = "SELECT COUNT(DISTINCT e.idDepartamento) FROM EmpleadoEntity e"; //TODO


    public EmpleadoDashboard get() throws SQLException, ClassNotFoundException {
        return new EmpleadoDashboard(
            getTotalEmpleados(),
            getMayorSalario(),
            getPromedioEdad(),
            getTotalDepartamentos()
        );
    }
    public EmpleadoDashboard getJPA() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("devUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

         Long totalEmpleados =  getTotalEmpleadosJPA(entityManager);
         Double mayorSalario = getPromedioEdadJPA(entityManager);
         Double promedioEdad = getMayorSalarioJPA(entityManager);
         Long totalDepartamentos = getTotalDepartamentosJPA(entityManager);
         entityManager.close();
         return new EmpleadoDashboard(totalEmpleados.intValue(), mayorSalario, promedioEdad.intValue(), totalDepartamentos.intValue());
    }
    public int getTotalEmpleados() throws SQLException, ClassNotFoundException {
        var result = MySQLConnection.executeQuery(SQL_GET_TOTAL_EMPLEADOS);
        result.next();
        return result.getInt(1);
    }
    public Long getTotalEmpleadosJPA(EntityManager entityManager) {
        TypedQuery<Long> query = entityManager.createQuery(GET_TOTAL_EMPLEADOS_JPA, Long.class);
        return query.getSingleResult();
    }
    public int getPromedioEdad() throws SQLException, ClassNotFoundException {
       var result = MySQLConnection.executeQuery(SQL_GET_PROMEDIO_EDAD);
       result.next();
       return result.getInt(1);
    }
    public Double getPromedioEdadJPA(EntityManager entityManager) {
        TypedQuery<Date> query = entityManager.createQuery(GET_PROMEDIO_EDAD_JPA, Date.class);


        List<Date> fechasNacimiento = query.getResultList();
        if(!fechasNacimiento.isEmpty())
        {
            long sumaEdades = 0;
            for(Date fechaNacimiento : fechasNacimiento){
                sumaEdades += calcularEdad(fechaNacimiento);
            }
            return (double) sumaEdades /fechasNacimiento.size();
        }
        else {
                return null;
        }

    }
    public int calcularEdad(Date fechaNac) {
        LocalDate nacimiento = fechaNac.toLocalDate();
        LocalDate fechaactual = LocalDate.now();
        Period period = Period.between(nacimiento,fechaactual);
        return period.getYears();
    }
    public double getMayorSalario() throws SQLException, ClassNotFoundException {
        var result = MySQLConnection.executeQuery(SQL_GET_MAYOR_SALARIO);
        result.next();
        return result.getDouble(1);
    }

    public Double getMayorSalarioJPA(EntityManager entityManager) {
        TypedQuery<Double> query = entityManager.createQuery(GET_MAYOR_SALARIO_JPA, Double.class);
        return query.getSingleResult();
    }


    public int getTotalDepartamentos() {
        return 0;
    }

    public Long getTotalDepartamentosJPA(EntityManager entityManager) {
        TypedQuery<Long> query = entityManager.createQuery(GET_TOTAL_DEPARTAMENTOS_JPA, Long.class);
        return query.getSingleResult();
    }


}
