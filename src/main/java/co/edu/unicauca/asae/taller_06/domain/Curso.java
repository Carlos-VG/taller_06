package co.edu.unicauca.asae.taller_06.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * @brief Entidad Curso.
 * @details Relación N:1 con Asignatura; N:M con Docente (tabla intermedia
 *          curso_docente);
 *          1:N con FranjaHoraria (REMOVE + orphanRemoval para cumplir borrado
 *          en cascada).
 * @note Tabla: curso; PK: id.
 */
@Entity
@Getter
@Setter
@Table(name = "curso")
public class Curso {

    /** @brief Identificador autogenerado (PK). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /** @brief Nombre del curso. (VARCHAR(255), NOT NULL). */
    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    /**
     * @brief Asignatura a la que pertenece el curso (FK).
     * @details Columna: asignatura_id (NOT NULL).
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asignatura_id")
    private Asignatura asignatura;

    /**
     * @brief Docentes asociados (N:M).
     * @details Tabla intermedia: curso_docente(curso_id, docente_id).
     */
    @ManyToMany
    @JoinTable(name = "curso_docente", joinColumns = @JoinColumn(name = "curso_id"), inverseJoinColumns = @JoinColumn(name = "docente_id"))
    private Set<Docente> docentes = new HashSet<>();

    /**
     * @brief Franjas horarias del curso (1:N).
     * @details cascade=REMOVE + orphanRemoval=true para que al borrar el curso
     *          se eliminen sus franjas (requisito del taller).
     */
    @OneToMany(mappedBy = "curso", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<FranjaHoraria> franjas = new ArrayList<>();
}
