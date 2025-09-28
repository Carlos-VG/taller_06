package co.edu.unicauca.asae.taller_06.repositories;

import co.edu.unicauca.asae.taller_06.domain.Docente;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @brief Repositorio de Docente.
 * @details Soporta operaciones CRUD y getReferenceById(int) para asociar a
 *          Curso.
 */
public interface DocenteRepository extends JpaRepository<Docente, Integer> {
}
