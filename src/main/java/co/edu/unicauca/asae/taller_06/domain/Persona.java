package co.edu.unicauca.asae.taller_06.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @brief Entidad base de la jerarquía de personas.
 * @details Implementa herencia JOINED para normalización y para permitir
 *          subclases como Docente y Administrativo. El correo es único.
 * @note Tabla: persona; PK: id.
 */
@Entity
@Getter
@Setter
@Table(name = "persona", uniqueConstraints = @UniqueConstraint(columnNames = "correo"))
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Persona {

    /** @brief Identificador autogenerado (PK). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /** @brief Nombres de la persona. (VARCHAR(50), NOT NULL). */
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    /** @brief Apellidos de la persona. (VARCHAR(50), NOT NULL). */
    @Column(name = "apellido", nullable = false, length = 50)
    private String apellido;

    /**
     * @brief Correo único de la persona.
     * @details Restricción de unicidad exigida por el taller.
     */
    @Column(name = "correo", nullable = false, unique = true, length = 50)
    private String correo;
}
