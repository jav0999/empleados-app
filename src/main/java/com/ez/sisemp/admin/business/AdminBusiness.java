package com.ez.sisemp.admin.business;

import com.ez.sisemp.admin.dao.AdminDao;
import com.ez.sisemp.admin.entity.UsuarioEntity;
import com.ez.sisemp.admin.model.Usuario;
import com.ez.sisemp.parametro.dao.ParametroDao;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class AdminBusiness {
    private final AdminDao adminDao;
    private final ParametroDao parametroDao;

    public AdminBusiness() {
        this.adminDao = new AdminDao();
        this.parametroDao = new ParametroDao();

    }

    public List<Usuario> obtenerUsuariosJPA() {
        var usuarios = adminDao.obtenerUsuariosJPA();
        if (usuarios.isEmpty()) {
            throw new AssertionError("No se encontraron usuarios");
        }
        var usuariosToReturn = new ArrayList<Usuario>();
        usuarios.forEach(
                u -> {
                    var usuarioRecord = mapToRecord(u);
                    usuariosToReturn.add(usuarioRecord);
                }
        );
        return usuariosToReturn;
    }

    private Usuario mapToRecord(UsuarioEntity e) {
        var rol = parametroDao.getById(Integer.valueOf(e.getRol()));

        boolean estado = false;
        if (Integer.valueOf(e.getEstado()) == 1) {
            estado = true;

        } else {
            estado = false;
        }
        return new Usuario(
                e.getNombreUsuario(),
                e.getContrasena(),
                "",
                e.getUtlimaConexion(),
                estado,
                e.getPrimerNombre(),
                e.getApellidoPat(),
                e.getUrlFoto(),
                rol.getNombre()
        );
    }

    }