package co.edu.unicauca.asae.taller_06.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unicauca.asae.taller_06.domain.Curso;
import lombok.NonNull;

/**
 * @brief Repositorio de Curso.
 * @details Contiene queries personalizadas para cumplir los “ligados” del
 *          taller.
 */
public interface CursoRepository extends JpaRepository<Curso, Integer> {

    /**
     * @brief Carga un curso con sus franjas y espacios en un solo fetch.
     * @details Simula EAGER puntual (curso→franjas→espacioFisico).
     */
    @Query("""
                select distinct c
                from Curso c
                left join fetch c.franjas f
                left join fetch f.espacioFisico
                where c.id = :cursoId
            """)
    Optional<Curso> findDetalleCursoConFranjasYEspacios(@Param("cursoId") int cursoId);

    /**
     * @brief Cursos con franjas dictados por un docente concreto.
     * @details Trae curso+franjas+asignatura en fetch; deja espacio LAZY.
     */
    @Query("""
                select distinct c
                from Curso c
                join c.docentes d
                left join fetch c.franjas f
                left join fetch c.asignatura a
                where d.id = :docenteId
            """)
    List<Curso> findCursosYFranjasByDocente(@Param("docenteId") int docenteId);

    /**
     * @brief Carga un curso con sus franjas y espacios en un solo fetch.
     * @details Simula EAGER puntual (curso→franjas→espacioFisico).
     */
    @EntityGraph(attributePaths = { "franjas", "franjas.espacioFisico" })
    @NonNull
    Optional<Curso> findWithFranjasById(@NonNull Integer id);
}
