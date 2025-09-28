package co.edu.unicauca.asae.taller_06.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @brief Entidad Oficina.
 * @details Nombre único (requisito del taller). Ubicación con límite de 20
 *          caracteres.
 * @note Tabla: oficina; PK: id.
 */
@Entity
@Getter
@Setter
@Table(name = "oficina", uniqueConstraints = @UniqueConstraint(columnNames = "nombre"))
public class Oficina {

    /** @brief Identificador autogenerado (PK). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /** @brief Nombre único de la oficina. (VARCHAR(20), UNIQUE, NOT NULL). */
    @Column(name = "nombre", nullable = false, unique = true, length = 20)
    private String nombre;

    /** @brief Ubicación física (p. ej., bloque/piso). (VARCHAR(20), NOT NULL). */
    @Column(name = "ubicacion", nullable = false, length = 20)
    private String ubicacion;
}
