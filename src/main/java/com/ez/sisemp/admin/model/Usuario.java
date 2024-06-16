package com.ez.sisemp.admin.model;

import java.util.Date;

public record Usuario(Integer id,
                      String nombreUsuario,
                      String contrasena,
                      String contrasenaAnterior,
                      //SEe cambio de date a string
                      String utlimaConexion,
                      Boolean estado,
                      String primerNombre,
                      String apellidoPat,
                      String urlFoto,
                      String rol
                        )
{
    public Usuario(String nombreUsuario, String contrasena, String contrasenaAnterior, String utlimaConexion, Boolean estado, String primerNombre, String apellidoPat, String utlFoto, String rol){
        this(null, nombreUsuario, contrasena, contrasenaAnterior, utlimaConexion, estado, primerNombre, apellidoPat, utlFoto, rol);
    }

}
