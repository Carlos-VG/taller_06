package co.edu.unicauca.asae.taller_06.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @brief Entidad Asignatura.
 * @details Contiene nombre (255) y código (50) según esquema.
 * @note Tabla: asignatura; PK: id.
 */
@Entity
@Getter
@Setter
@Table(name = "asignatura")
public class Asignatura {

    /** @brief Identificador autogenerado (PK). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /** @brief Nombre de la asignatura. (VARCHAR(255), NOT NULL). */
    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    /** @brief Código de la asignatura. (VARCHAR(50), NOT NULL). */
    @Column(name = "codigo", nullable = false, length = 50, unique = true)
    private String codigo;

}
