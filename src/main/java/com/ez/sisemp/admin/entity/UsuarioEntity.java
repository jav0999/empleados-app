package com.ez.sisemp.admin.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "usuario")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column (name="nombre_usuario")
    String nombreUsuario;
    @Column (name="contrasena")
    String contrasena;
    @Column (name="contrasena_anterior")
    String contrasenaAnterior;
    @Column (name="utlima_conexion")
    String utlimaConexion;
    @Column (name="active")
    String estado;
    @Column (name="primer_nombre")
    String primerNombre;
    @Column (name="apellido_pat")
    String apellidoPat;
    @Column (name="foto_perfil")
    String urlFoto;
    @Column (name="id_rol")
    String rol;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getContrasenaAnterior() {
        return contrasenaAnterior;
    }

    public void setContrasenaAnterior(String contrasenaAnterior) {
        this.contrasenaAnterior = contrasenaAnterior;
    }

    public String getUtlimaConexion() {
        return utlimaConexion;
    }

    public void setUtlimaConexion(String utlimaConexion) {
        this.utlimaConexion = utlimaConexion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getApellidoPat() {
        return apellidoPat;
    }

    public void setApellidoPat(String apellidoPat) {
        this.apellidoPat = apellidoPat;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

}