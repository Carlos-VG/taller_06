package co.edu.unicauca.asae.taller_06.repositories;

import co.edu.unicauca.asae.taller_06.domain.Asignatura;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @brief Repositorio de Asignatura.
 * @details Métodos CRUD + getReferenceById(int) para asociaciones con Curso.
 */
public interface AsignaturaRepository extends JpaRepository<Asignatura, Integer> {
}
