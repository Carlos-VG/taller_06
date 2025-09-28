package co.edu.unicauca.asae.taller_06.repositories;

import co.edu.unicauca.asae.taller_06.domain.DiaSemana;
import co.edu.unicauca.asae.taller_06.domain.FranjaHoraria;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

/**
 * @brief Repositorio de Franja Horaria.
 * @details Métodos alineados con los casos de consulta del taller.
 */
public interface FranjaHorariaRepository extends JpaRepository<FranjaHoraria, Integer> {

    /**
     * @brief Franjas de un curso con curso+espacio en fetch (EAGER puntual).
     */
    // PUNTO 3 TALLER 6) (RELACIONADAS, keywords) franjas por id de curso con
    // curso+espacio en
    // fetch
    @EntityGraph(attributePaths = { "curso", "curso.docentes", "espacioFisico" })
    List<FranjaHoraria> findByCurso_Id(int cursoId);

    /**
     * @brief Franjas de un docente, con curso en fetch y espacio LAZY.
     */
    @Query("""
                select distinct f
                from FranjaHoraria f
                join f.curso c2
                join c2.docentes d
                left join fetch f.curso c
                where d.id = :docenteId
            """)
    List<FranjaHoraria> findByDocenteIdFetchCurso(@Param("docenteId") int docenteId);

    // PUNTO 4 TALLER 6) (JPQL) ¿espacio físico ocupado? por día y solapamiento de
    // horas
    @Query("""
            select count(f)
            from FranjaHoraria f
            join f.espacioFisico e
            where e.id = :espacioId
              and f.dia = :dia
              and f.horaInicio < :horaFin
              and f.horaFin    > :horaInicio
            """)
    long countOcupacionEspacio(@Param("dia") DiaSemana dia,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin,
            @Param("espacioId") int espacioId);

    // PUNTO 5 TALLER 6) (SQL NATIVA) ¿docente ocupado? por día y solapamiento de
    // horas
    @Query(value = """
            SELECT COUNT(f.id)
            FROM franja_horaria f
            JOIN curso c          ON f.curso_id = c.id
            JOIN curso_docente cd ON cd.curso_id = c.id
            WHERE cd.docente_id = :docenteId
              AND f.dia = :dia
              AND f.hora_inicio < :horaFin
              AND f.hora_fin    > :horaInicio
            """, nativeQuery = true)
    long countOcupacionDocente(@Param("dia") String dia, // usar valores 'LUNES', 'MARTES', ...
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin,
            @Param("docenteId") int docenteId);

    // PUNTO 6 TALLER 6) (JOIN múltiple) detalle de franjas de un curso con curso y
    // espacio
    @Query("""
            select f.id, f.dia, f.horaInicio, f.horaFin,
                   c.id, c.nombre,
                   e.id, e.nombre, e.capacidad
            from FranjaHoraria f
            join f.curso c
            join f.espacioFisico e
            where c.id = :cursoId
            order by f.dia, f.horaInicio
            """)
    List<Object[]> obtenerDetalleFranjasCurso(@Param("cursoId") int cursoId);

    // PUNTO 8 TALLER 6) (DELETE) eliminar todas las franjas de un curso
    @Modifying
    @Transactional
    @Query("delete from FranjaHoraria f where f.curso.id = :cursoId")
    int eliminarFranjasPorCursoId(@Param("cursoId") int cursoId);

}
