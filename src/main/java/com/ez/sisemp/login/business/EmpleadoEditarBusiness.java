package com.ez.sisemp.login.business;

import com.ez.sisemp.empleado.model.Empleado;
import com.ez.sisemp.login.dao.EmpleadoDao;

import java.sql.SQLException;

public class EmpleadoEditarBusiness {

    private final EmpleadoDao empleadoDao;
   public EmpleadoEditarBusiness() {
        this.empleadoDao = new EmpleadoDao();
    }

    public Empleado editar(String id) throws SQLException, ClassNotFoundException {
        return empleadoDao.editar(id);
    }
}
