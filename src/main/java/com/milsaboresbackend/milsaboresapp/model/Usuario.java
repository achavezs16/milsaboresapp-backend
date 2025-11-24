package com.milsaboresbackend.milsaboresapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 200, message = "Nombre no puede exceder los 200 caracteres")
    @Column(name = "nombre_usuario", nullable = false, length = 200)
    private String nombreUsuario;

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 200, message = "Apellido no puede exceder los 200 caracteres")
    @Column(name = "apellido_usuario", nullable = false, length = 200)
    private String apellidoUsuario;

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 300, message = "Email no puede exceder los 200 caracteres")
    @Column(name = "email_usuario", nullable = false, length = 300)
    private String emailUsuario;

    @NotBlank(message = "Campo obligatorio")
    @Size(max = 50, message = "Contraseña no puede exceder los 50 caracteres")
    @Column(name = "password_usuario", nullable = false, length = 50)
    private String passwordUsuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol_usuario", nullable = false)
    private RolUsuario rolUsuario;
}





