package com.ez.sisemp.login.business;

import com.ez.sisemp.admin.entity.UsuarioEntity;
import com.ez.sisemp.login.dao.UsuarioDao;
import com.ez.sisemp.login.model.Usuario;
import java.sql.SQLException;

public class UsuarioBusiness {

    private final UsuarioDao usuarioDao;

    public UsuarioBusiness() {
        this.usuarioDao = new UsuarioDao();
    }

    public Usuario login(String username, String password) throws SQLException, ClassNotFoundException {

        return usuarioDao.login(username, password);
    }
    public UsuarioEntity loginJPA(String username, String password) throws SQLException, ClassNotFoundException {
        return usuarioDao.loginJPA(username,password);
        // return usuarioDao.login(username, password);
    }
}
