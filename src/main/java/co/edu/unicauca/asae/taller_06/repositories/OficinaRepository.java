package co.edu.unicauca.asae.taller_06.repositories;

import co.edu.unicauca.asae.taller_06.domain.Oficina;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @brief Repositorio de Oficina.
 */
public interface OficinaRepository extends JpaRepository<Oficina, Integer> {
}
