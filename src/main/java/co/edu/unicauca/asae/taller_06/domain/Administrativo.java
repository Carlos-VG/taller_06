package co.edu.unicauca.asae.taller_06.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @brief Subtipo de Persona que representa un administrativo.
 * @note Tabla: administrativo; PK=FK(id) → persona.id (herencia JOINED).
 */
@Entity
@Getter
@Setter
@Table(name = "administrativo")
@PrimaryKeyJoinColumn(name = "id")
public class Administrativo extends Persona {

    /** @brief Rol o dependencia del administrativo. (VARCHAR(255), NOT NULL). */
    @Column(name = "rol", nullable = false, length = 255)
    private String rol;
}
