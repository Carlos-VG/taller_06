package co.edu.unicauca.asae.taller_06;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unicauca.asae.taller_06.domain.*;
import co.edu.unicauca.asae.taller_06.repositories.*;

@SpringBootApplication
@Transactional
public class Taller06Application implements CommandLineRunner {

	// ====== Repositorios ======
	@Autowired
	private DocenteRepository docenteRepo;
	@Autowired
	private AsignaturaRepository asignaturaRepo;
	@Autowired
	private CursoRepository cursoRepo;
	@Autowired
	private EspacioFisicoRepository espacioRepo;
	@Autowired
	private FranjaHorariaRepository franjaRepo;
	// ==========================

	public static void main(String[] args) {
		SpringApplication.run(Taller06Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// // 1) Crear Docente + Oficina con un solo save (correo y oficina distintos)
		// crearDocenteConOficinaUnSoloSave(
		// "María", "Lozano", "maria.lozano@unicauca.edu.co",
		// "Of-402", "Bloque C, Piso 4");

		// // 2) Crear Curso con Asignatura y Docentes existentes
		// int asignaturaId = 2; // "Bases de Datos II" ya existe en import.sql
		// List<Integer> docentesIds = List.of(1, 2); // docentes existentes (IDs 1 y 2)
		// crearCursoConAsignaturaYDocentes(
		// "Bases de Datos II - Grupo C",
		// asignaturaId,
		// docentesIds);

		// // 3) Crear Franja para Curso y Espacio existentes (horario distinto)
		// int cursoId = 2; // curso existente: "Bases de Datos II - Grupo B"
		// int espacioId = 2; // "Laboratorio Redes 2"
		// crearFranjaParaCursoYEspacio(
		// cursoId,
		// espacioId,
		// DiaSemana.MARTES,
		// LocalTime.of(16, 0),
		// LocalTime.of(18, 0));

		// // 4) Consultar franjas de un curso
		// consultarFranjasDeUnCurso(1);

		// // 5) Consultar franjas por docente
		// consultarFranjasPorDocente(2);

		// // 6) Eliminar curso
		// eliminarCurso(2);

	}

	// ===========================
	// 1) CREAR DOCENTE + OFICINA
	// ===========================
	/**
	 * @brief Crea un Docente con su Oficina en un solo save (cascade=PERSIST).
	 */
	@Transactional
	public Docente crearDocenteConOficinaUnSoloSave(String nombre, String apellido, String correo,
			String nombreOficina, String ubicacionOficina) {
		Oficina of = new Oficina();
		of.setNombre(nombreOficina);
		of.setUbicacion(ubicacionOficina);

		Docente d = new Docente();
		d.setNombre(nombre);
		d.setApellido(apellido);
		d.setCorreo(correo);
		d.setOficina(of); // cascade PERSIST en Docente.oficina

		Docente guardado = docenteRepo.save(d); // único save
		System.out.println("== Docente creado ==");
		System.out.println("Id: " + guardado.getId() + ", " + guardado.getNombre() + " " + guardado.getApellido());
		if (guardado.getOficina() != null) {
			System.out.println("Oficina: " + guardado.getOficina().getNombre() +
					" - " + guardado.getOficina().getUbicacion());
		}
		System.out.println("---- ---- ----");
		return guardado;
	}

	// ==========================================
	// 2) CREAR CURSO (getReferenceById requerido)
	// ==========================================
	/**
	 * @brief Crea un Curso asociando Asignatura y varios Docentes existentes
	 *        mediante getReferenceById.
	 */
	@Transactional
	public Curso crearCursoConAsignaturaYDocentes(String nombreCurso, int asignaturaId, List<Integer> docentesIds) {
		Curso c = new Curso();
		c.setNombre(nombreCurso);
		c.setAsignatura(asignaturaRepo.getReferenceById(asignaturaId)); // proxy sin cargar entidad completa

		// Asociar docentes por referencia (sin SELECT previo)
		Set<Docente> docentes = docentesIds.stream()
				.map(docenteRepo::getReferenceById)
				.collect(java.util.stream.Collectors.toSet());
		c.setDocentes(docentes);

		Curso guardado = cursoRepo.save(c);
		System.out.println("== Curso creado ==");
		System.out.println("Id: " + guardado.getId() + " - " + guardado.getNombre());
		System.out.println("Asignatura: " + guardado.getAsignatura().getNombre());
		System.out.println("Docentes: " + guardado.getDocentes().size());
		System.out.println("---- ---- ----");
		return guardado;
	}

	// ==================================================
	// 3) CREAR FRANJA (getReferenceById en curso/espacio)
	// ==================================================
	/**
	 * @brief Crea una FranjaHoraria para un Curso y un EspacioFísico existentes con
	 *        getReferenceById.
	 */
	@Transactional
	public FranjaHoraria crearFranjaParaCursoYEspacio(int cursoId, int espacioId, DiaSemana dia,
			LocalTime horaInicio, LocalTime horaFin) {
		FranjaHoraria f = new FranjaHoraria();
		f.setCurso(cursoRepo.getReferenceById(cursoId)); // proxy
		f.setEspacioFisico(espacioRepo.getReferenceById(espacioId)); // proxy
		f.setDia(dia);
		f.setHoraInicio(horaInicio);
		f.setHoraFin(horaFin);

		FranjaHoraria guardada = franjaRepo.save(f);
		System.out.println("== Franja creada ==");
		System.out.println("Id: " + guardada.getId() + " - " + guardada.getDia() +
				" " + guardada.getHoraInicio() + "-" + guardada.getHoraFin());
		System.out.println("CursoId: " + cursoId + ", EspacioId: " + espacioId);
		System.out.println("---- ---- ----");
		return guardada;
	}

	// =====================================================================
	// 4) CONSULTAR FRANJAS DE UN CURSO (curso + espacio EAGER puntual)
	// =====================================================================
	/**
	 * @brief Muestra las franjas de un curso, cargando curso y espacio en la misma
	 *        consulta.
	 * @details Usa @EntityGraph en FranjaHorariaRepository o join fetch en
	 *          CursoRepository.
	 */
	@Transactional(readOnly = true)
	public void consultarFranjasDeUnCurso(int cursoId) {
		// Opción A: directamente desde franjaRepo con EntityGraph
		List<FranjaHoraria> franjas = franjaRepo.findByCurso_Id(cursoId);

		System.out.println("== Franjas del Curso (id=" + cursoId + ") ==");
		for (FranjaHoraria f : franjas) {
			System.out.println("Franja #" + f.getId() + ": " + f.getDia() +
					" " + f.getHoraInicio() + "-" + f.getHoraFin());
			// curso y espacio están cargados (EAGER puntual por EntityGraph)
			System.out.println("Curso: " + f.getCurso().getNombre());
			System.out.println("Espacio: " + f.getEspacioFisico().getNombre());
			for (Docente d : f.getCurso().getDocentes()) {
				System.out.println("Docente: " + d.getNombre() + " " + d.getApellido());
			}
			System.out.println("---- ---- ----");
		}

		// Opción B: traer el curso con sus franjas+espacios
		// Optional<Curso> opt = cursoRepo.findDetalleCursoConFranjasYEspacios(cursoId);
		// opt.ifPresent(c -> {
		// System.out.println("Curso: " + c.getNombre());
		// c.getFranjas().forEach(fr -> {
		// System.out.println("Franja #" + fr.getId() + " " + fr.getDia() +
		// " " + fr.getHoraInicio() + "–" + fr.getHoraFin());
		// System.out.println("Espacio: " + fr.getEspacioFisico().getNombre());
		// System.out.println("---- ---- ----");
		// });
		// });
	}

	// ==================================================================================
	// 5) CONSULTAR FRANJAS POR DOCENTE (curso EAGER, espacio LAZY — solo al
	// acceder)
	// ==================================================================================
	/**
	 * @brief Muestra las franjas dictadas por un docente, con curso precargado y
	 *        espacio LAZY.
	 */
	@Transactional(readOnly = true)
	public void consultarFranjasPorDocente(int docenteId) {
		Optional<Docente> docenteOpt = docenteRepo.findById(docenteId);
		String etiquetaDocente = docenteOpt
				.map(d -> d.getNombre() + " " + d.getApellido())
				.orElse("Docente id=" + docenteId);

		List<FranjaHoraria> franjas = franjaRepo.findByDocenteIdFetchCurso(docenteId);
		System.out.println("== Franjas del docente con id: " + docenteId + " ==");

		for (FranjaHoraria f : franjas) {
			System.out.println("Franja #" + f.getId() + ": " + f.getDia()
					+ " " + f.getHoraInicio() + "-" + f.getHoraFin());

			// Datos del docente (por cada franja, como exige la guía)
			System.out.println("Docente: " + etiquetaDocente);

			// Curso viene en fetch (eager puntual)
			System.out.println("Curso: " + f.getCurso().getNombre());

			// Espacio LAZY: aquí sí se dispara la carga, como pide la guía
			System.out.println("Espacio: " + f.getEspacioFisico().getNombre());
			System.out.println("---- ---- ----");
		}
	}

	// ===========================================
	// 6) ELIMINAR CURSO (cascade REMOVE a franjas)
	// ===========================================
	/**
	 * @brief Elimina un curso por id. Las franjas se eliminan por cascada (REMOVE +
	 *        orphanRemoval).
	 */
	@Transactional
	public void eliminarCurso(int cursoId) {
		Optional<Curso> opt = cursoRepo.findWithFranjasById(cursoId);
		if (opt.isEmpty()) {
			System.out.println("No existe curso con id " + cursoId);
			return;
		}

		Curso c = opt.get();
		System.out.println("== Se eliminará el curso (id=" + cursoId + "): " + c.getNombre() + " ==");

		List<FranjaHoraria> franjas = c.getFranjas();
		if (franjas.isEmpty()) {
			System.out.println("No tiene franjas asociadas.");
		} else {
			System.out.println("Franjas a eliminar (" + franjas.size() + "):");
			for (FranjaHoraria f : franjas) {
				System.out.println("  - Franja #" + f.getId() + " | "
						+ f.getDia() + " " + f.getHoraInicio() + "-" + f.getHoraFin()
						+ " | Espacio: " + f.getEspacioFisico().getNombre());
			}
		}

		cursoRepo.delete(c); // cascada REMOVE + orphanRemoval sobre las franjas
		System.out.println("== Curso y franjas eliminados ==");
	}

}
