package com.ez.sisemp.empleado.business;

import com.ez.sisemp.empleado.dao.EmpleadoDao;
import com.ez.sisemp.empleado.dao.EmpleadoDashboardDao;
import com.ez.sisemp.empleado.entity.EmpleadoEntity;
import com.ez.sisemp.empleado.exception.EmailAlreadyInUseException;
import com.ez.sisemp.empleado.exception.EmpleadosNotFoundException;
import com.ez.sisemp.empleado.model.Empleado;
import com.ez.sisemp.empleado.model.EmpleadoDashboard;
import com.ez.sisemp.parametro.dao.ParametroDao;
import com.ez.sisemp.shared.utils.EdadUtils;
import org.apache.commons.lang3.StringUtils;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;


public class EmpleadoBusiness {

    private final EmpleadoDao empleadoDao;
    private final EmpleadoDashboardDao empleadoDashboardDao;
    private final ParametroDao parametroDao;

    public EmpleadoBusiness(){
        this.empleadoDao = new EmpleadoDao();
        this.empleadoDashboardDao = new EmpleadoDashboardDao();
        this.parametroDao = new ParametroDao();
    }

    public void registrarEmpleado(Empleado empleado) throws SQLException, ClassNotFoundException {
        empleado = new Empleado(generarCodigoEmpleado(), empleado.nombres(), empleado.apellidoPat(), empleado.apellidoMat(), empleado.idDepartamento(), empleado.correo(), empleado.salario(), empleado.fechaNacimiento());
        validarCampos(empleado);
        try {
            empleadoDao.agregarEmpleado(empleado);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new EmailAlreadyInUseException(String.format("El correo %s ya se encuentra registrado", empleado.correo()));
        }
    }
    public void registrarEmpleadoJpa(EmpleadoEntity empleadoEntity) {
        empleadoEntity.setCodigoEmpleado(generarCodigoEmpleado());
        empleadoDao.agregarEmpleadoJPA(empleadoEntity);
    }

    public void editarEmpleado(Empleado empleado) throws SQLException, ClassNotFoundException {
        empleado = new Empleado(empleado.id(),empleado.codigoEmpleado(), empleado.nombres(), empleado.apellidoPat(), empleado.apellidoMat(), empleado.idDepartamento(), empleado.correo(), empleado.salario(), empleado.fechaNacimiento());
        validarCampos(empleado);
        try {
            empleadoDao.editarEmpleado(empleado);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new EmailAlreadyInUseException(String.format("El correo %s ya se encuentra registrado", empleado.correo()));
        }
    }

    public void eliminarEmpleado(int id) throws SQLException, ClassNotFoundException {
        empleadoDao.eliminarEmpleado(id);
    }
    public void eliminarEmpleadoJPA(int id) throws SQLException, ClassNotFoundException {
        empleadoDao.eliminarEmpleadoJpa(id);
    }


    public List<Empleado> obtenerEmpleados() throws SQLException, ClassNotFoundException {
        var empleados = empleadoDao.obtenerEmpleados();
        if(empleados.isEmpty()){
            throw new EmpleadosNotFoundException("No se encontraron empleados");
        }
        return empleadoDao.obtenerEmpleados();
    }

    public List<Empleado> obtenerEmpleadosJpa() {
        var empleados = empleadoDao.obtenerEmpleadosJPA();
        if(empleados.isEmpty()){
            throw new EmpleadosNotFoundException("No se encontraron empleados");
        }
        var empleadosToReturn = new ArrayList<Empleado>();
        empleados.forEach(
                e -> {
                    var empleadoRecord = mapToRecord(e);
                    empleadosToReturn.add(empleadoRecord);
                }
        );
        return empleadosToReturn;
    }

    private Empleado mapToRecord(EmpleadoEntity e) {
        var departamento = parametroDao.getById(e.getIdDepartamento());
        return new Empleado(
                Math.toIntExact(e.getId()),
                e.getCodigoEmpleado(),
                e.getNombres(),
                e.getApellidoPat(),
                e.getApellidoMat(),
                e.getIdDepartamento(),
                departamento.getNombre(),
                e.getCorreo(),
                EdadUtils.calcularEdad(e.getFechaNacimiento()),
                e.getSalario(),
                e.getFechaNacimiento()
        );
    }

    public EmpleadoDashboard obtenerDatosDashboardJPA() {
        return empleadoDashboardDao.getJPA();
    }

    public EmpleadoDashboard obtenerDatosDashboard() throws SQLException, ClassNotFoundException {
        return empleadoDashboardDao.get();
    }

    private String generarCodigoEmpleado(){
        return "EMP" + (int) (Math.random() * 1000000);
    }

    private EmpleadoEntity consultarEmpleadoById(Long id)  {
        return empleadoDao.consultarEmpleadoById(id);
    }

    private void validarCampos (Empleado empleado){
        if(StringUtils.isBlank(empleado.codigoEmpleado())){
            throw new IllegalArgumentException("El codigo del empleado no puede ser nulo");
        }
        if(StringUtils.isBlank(empleado.nombres())){
            throw new IllegalArgumentException("El nombre del empleado no puede ser nulo");
        }
        if(StringUtils.isBlank(empleado.apellidoPat())){
            throw new IllegalArgumentException("El apellido paterno del empleado no puede ser nulo");
        }
        if(StringUtils.isBlank(empleado.correo())){
            throw new IllegalArgumentException("El correo del empleado no puede ser nulo");
        }
        if(StringUtils.isBlank(empleado.fechaNacimiento().toString())){
            throw new IllegalArgumentException("La fecha de nacimiento del empleado no puede ser nula");
        }
        if(empleado.salario() < 0){
            throw new IllegalArgumentException("El salario del empleado no puede ser negativo");
        }
    }
}
