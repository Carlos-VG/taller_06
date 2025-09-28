package co.edu.unicauca.asae.taller_06.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @brief Entidad Espacio Físico (aula/lab).
 * @details Nombre único (requisito del taller). Capacidad opcional (entero).
 * @note Tabla: espacio_fisico; PK: id.
 */
@Entity
@Getter
@Setter
@Table(name = "espacio_fisico", uniqueConstraints = @UniqueConstraint(columnNames = "nombre"))
public class EspacioFisico {

    /** @brief Identificador autogenerado (PK). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /** @brief Nombre único del espacio. (VARCHAR(255), UNIQUE, NOT NULL). */
    @Column(name = "nombre", nullable = false, unique = true, length = 255)
    private String nombre;

    /** @brief Capacidad del espacio (nullable según esquema). */
    @Column(name = "capacidad")
    private Integer capacidad;
}
