package co.edu.unicauca.asae.taller_06.repositories;

import co.edu.unicauca.asae.taller_06.domain.EspacioFisico;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @brief Repositorio de Espacio Físico.
 */
public interface EspacioFisicoRepository extends JpaRepository<EspacioFisico, Integer> {
}
