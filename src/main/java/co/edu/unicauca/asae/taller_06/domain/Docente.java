package co.edu.unicauca.asae.taller_06.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @brief Subtipo de Persona que representa un docente.
 * @details Mantiene relación 1:1 opcional con Oficina con cascade=PERSIST para
 *          permitir el caso "un solo save" al crear Docente + Oficina
 *          (requisito del taller).
 * @note Tabla: docente; PK=FK(id) → persona.id (herencia JOINED).
 */
@Entity
@Getter
@Setter
@Table(name = "docente")
@PrimaryKeyJoinColumn(name = "id") // PK=FK hacia persona.id
public class Docente extends Persona {

    /**
     * @brief Oficina asignada (opcional).
     * @details Columna: docente.oficina_id; UNIQUE.
     *          CascadeType.PERSIST permite persistir la oficina al guardar el
     *          docente.
     */
    @OneToOne(optional = true, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "oficina_id", unique = true)
    private Oficina oficina;
}
