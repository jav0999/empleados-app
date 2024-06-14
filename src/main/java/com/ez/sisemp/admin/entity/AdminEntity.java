package com.ez.sisemp.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name= "usuario")
public class AdminEntity {
    @Id
    int id;
    @Column(name = "nombre_usuario")
    String nombreUsuario;
    String contrasena;
}
