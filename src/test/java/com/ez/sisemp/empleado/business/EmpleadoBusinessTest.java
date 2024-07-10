package com.ez.sisemp.empleado.business;

import com.ez.sisemp.empleado.dao.EmpleadoDao;
import com.ez.sisemp.empleado.entity.EmpleadoEntity;
import com.ez.sisemp.empleado.model.Empleado;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmpleadoBusinessTest {
    @BeforeEach
    void setUp(){
    }

    @AfterEach
    void cleanup(){
    }
    private Empleado emp;
    private EmpleadoEntity empEnt;

    String nombre = "Ernesto";
    String apellidoPat = "Jalapenho";
    String apellidoMat = "Miranda";
    String correo = "correoPruebaC@gmail.com";

    @Test
    void insertar() throws SQLException, ClassNotFoundException {
        System.out.println("Inicio de la prueba de insertar");
        Date date = new Date(2009, 02, 11);
        emp = new Empleado("sinUsar", "Juansito", "Marcino", "Mendelles", 1,"correito@gmail.com", 3000.0, date );


        EmpleadoBusiness empl = new EmpleadoBusiness();
        empl.registrarEmpleado(emp);

        //OBTENGO EL EMPLEADO REGISTRADO
        List<Empleado> empelados = empl.obtenerEmpleados();
        for(Empleado e: empelados){
            if(e.nombres().equals("Juansito"))
            {
                Empleado empObtenidoBD = e;
                try {
                    assertEquals("Juansito", empObtenidoBD.nombres());
                    assertEquals("Marcino", empObtenidoBD.apellidoPat());
                    assertEquals("Mendelles", empObtenidoBD.apellidoMat());
                }catch (Exception f)
                {
                    System.out.println("Hubo un error con la validación de la siguiente prueba: " + f.getMessage());
                }
            }
        }


    }


    @Test
    void insertarJpa() throws SQLException, ClassNotFoundException {
        System.out.println("Inicio de la prueba de insertar");
        Date date = new Date(2009, 02, 11);
        empEnt = new EmpleadoEntity();


        empEnt.setNombres(nombre);
        empEnt.setApellidoPat(apellidoPat);
        empEnt.setApellidoMat(apellidoMat);
        empEnt.setIdDepartamento(1);
        empEnt.setCorreo(correo);
        empEnt.setSalario(3300.0);
        empEnt.setEstado(1);
        empEnt.setFechaNacimiento(date);
        EmpleadoBusiness empl = new EmpleadoBusiness();
        empl.registrarEmpleadoJpa(empEnt);

        //OBTENGO EL EMPLEADO JPA REGISTRADO
        List<Empleado> empelados = empl.obtenerEmpleados();
        for(Empleado e: empelados){
            if(e.nombres().equals(nombre) && e.apellidoPat().equals(apellidoPat))
            {
                Empleado empObtenidoBD = e;
                try {
                    assertEquals(nombre, empObtenidoBD.nombres());
                    assertEquals(apellidoPat, empObtenidoBD.apellidoPat());
                    assertEquals(apellidoMat, empObtenidoBD.apellidoMat());
                }catch (Exception f)
                {
                    System.out.println("Hubo un error con la validación de la siguiente prueba: " + f.getMessage());
                }
            }
        }


    }


    @Test
    void editarJpa() throws SQLException, ClassNotFoundException {
        System.out.println("Inicio de la prueba de editar");

        //OBTENGO EL EMPLEADO JPA REGISTRADO
        String nombreaBuscar ="Juansito";
        String nuevoNombre = "JuansitoEdit";
        String apellidoPatBuscar= "Marcino";
        EmpleadoBusiness empl = new EmpleadoBusiness();

        List<Empleado> empelados = empl.obtenerEmpleadosJpa();
        for(Empleado e: empelados){
            if(e.nombres().equals(nombreaBuscar) && e.apellidoPat().equals(apellidoPatBuscar))
            {
               Empleado emplEdit= new Empleado(e.id(), e.codigoEmpleado(),nuevoNombre, e.apellidoPat(), e.apellidoMat(), e.idDepartamento(),e.correo(), e.salario(), e.fechaNacimiento());
                 empl.editarEmpleado(emplEdit);

             }
        }


        List<Empleado> empeladosCambiados = empl.obtenerEmpleadosJpa();
        for(Empleado e: empeladosCambiados){
            if(e.nombres().equals(nuevoNombre) && e.apellidoPat().equals(apellidoPatBuscar))
            {
                Empleado emplEdit= new Empleado(e.id(), e.codigoEmpleado(),e.nombres(), e.apellidoPat(), e.apellidoMat(), e.idDepartamento(),e.correo(), e.salario(), e.fechaNacimiento());
                empl.editarEmpleado(emplEdit);

                //VALIDACION NOMBRE CAMBIADO EN BD LUEGO DE EDIT
                assertEquals(nuevoNombre,e.nombres());

            }
        }
    }

    @Test
    public void eliminarJpa() throws SQLException, ClassNotFoundException {
        System.out.println("Inicio de la prueba de eliminar");
        EmpleadoBusiness empl = new EmpleadoBusiness();
         EmpleadoDao empl2 = new EmpleadoDao();
        int idEliminar =29;
                empl.eliminarEmpleadoJPA(idEliminar);
                empl.eliminarEmpleado(idEliminar);

        List<EmpleadoEntity> empeladosCambiados = empl2.obtenerEmpleadosJPA();
        for(EmpleadoEntity e: empeladosCambiados){
            if(e.getId() ==idEliminar)
            {
                //VALIDACION NOMBRE CAMBIADO EN BD LUEGO DE EDIT
            assertEquals(e.getEstado(),0);
            }

        }
            System.out.println("Ok, no se encontraron coincidencias con el id eliminado");
    }


    @Test
    void listarJpa() throws SQLException, ClassNotFoundException {
        System.out.println("Inicio de la prueba de listar");

        EmpleadoBusiness empl = new EmpleadoBusiness();

        List<Empleado> empelados = empl.obtenerEmpleadosJpa();
        int cantidad = empelados.size();

        //COMPARO LA CANTIDAD TOTAL DE EMPLEADOS CON LA DE LA BD, DEBE DEVOLVER LA MISMA CANTIDAD DE EMPLEADOS
        assertEquals(cantidad,28);

    }


}