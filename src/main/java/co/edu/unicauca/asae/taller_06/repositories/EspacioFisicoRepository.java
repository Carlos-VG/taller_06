package co.edu.unicauca.asae.taller_06.repositories;

import co.edu.unicauca.asae.taller_06.domain.EspacioFisico;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @brief Repositorio de Espacio Físico.
 */
public interface EspacioFisicoRepository extends JpaRepository<EspacioFisico, Integer> {

    // PUNTO 1 TALLER 6) (SOLA TABLA, keywords) nombre que empieza por patrón
    // (ignore case) y
    // capacidad >=, orden asc
    List<EspacioFisico> findByNombreStartingWithIgnoreCaseAndCapacidadGreaterThanEqualOrderByNombreAsc(
            String patron, Integer capacidadMinima);

    // PUNTO 7 TALLER 6) (MODIFICACIÓN) actualizar SOLO el estado (activo/inactivo)
    // por id
    @Modifying
    @Transactional
    @Query("update EspacioFisico e set e.activo = :activo where e.id = :id")
    int actualizarEstado(@Param("id") int id, @Param("activo") boolean activo);
}
