package co.edu.unicauca.asae.taller_06.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

/**
 * @brief Entidad Franja Horaria.
 * @details Relación N:1 con Curso y con Espacio Físico. Campos hora_inicio y
 *          hora_fin son TIME.
 * @note Tabla: franja_horaria; PK: id.
 */
@Entity
@Getter
@Setter
@Table(name = "franja_horaria")
public class FranjaHoraria {

    /** @brief Identificador autogenerado (PK). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /** @brief Día de la franja (VARCHAR(20), NOT NULL). */
    @Enumerated(EnumType.STRING)
    @Column(name = "dia", nullable = false, length = 20)
    private DiaSemana dia;

    /** @brief Hora de inicio (TIME, NOT NULL). */
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    /** @brief Hora de fin (TIME, NOT NULL). */
    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    /**
     * @brief Curso asociado (FK).
     * @details Columna: curso_id (NOT NULL). LAZY por defecto.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "curso_id")
    private Curso curso;

    /**
     * @brief Espacio físico asociado (FK).
     * @details Columna: espacio_fisico_id (NOT NULL). LAZY por defecto.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "espacio_fisico_id")
    private EspacioFisico espacioFisico;
}
