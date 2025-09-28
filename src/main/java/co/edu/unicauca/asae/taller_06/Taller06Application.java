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

		/*
		 * ===========================
		 * PUNTOS TALLER 5
		 * ===========================
		 */

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

		/*
		 * ===========================
		 * PUNTOS TALLER 6
		 * ===========================
		 */

		// // 1) SOLA TABLA (keywords)
		// invocar_findEspaciosPorPatronYCapacidad();

		// // 2) RELACIONADAS (keywords por nombre de asignatura)
		// invocar_findCursosPorNombreDeAsignatura();

		// // 3) RELACIONADAS (franjas por id de curso)
		// invocar_findFranjasPorCursoId();

		// // 4) JPQL (ocupación de espacio)
		// invocar_countOcupacionEspacio();

		// // 5) SQL NATIVA (ocupación de docente)
		// invocar_countOcupacionDocente();

		// // 6) JOIN múltiple (detalle franjas/curso/espacio)
		// invocar_obtenerDetalleFranjasCurso();

		// // 7) UPDATE (estado activo/inactivo de espacio físico)
		// invocar_actualizarEstadoEspacio();

		// // 8) DELETE (eliminar franjas por curso)
		// invocar_eliminarFranjasPorCurso();

		/*
		 * ===========================
		 * PUNTO TALLER 5
		 * ===========================
		 */

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

	/*
	 * ===========================
	 * PUNTOS TALLER 6
	 * ===========================
	 */

	/*
	 * ===========================
	 * 1) SOLA TABLA (keywords)
	 * ===========================
	 */
	private void invocar_findEspaciosPorPatronYCapacidad() {
		List<EspacioFisico> lista = this.espacioRepo
				.findByNombreStartingWithIgnoreCaseAndCapacidadGreaterThanEqualOrderByNombreAsc("A", 20);
		System.out.println("[1] Espacios (patrón='A', capacidad>=20, orden nombre ASC)");
		for (EspacioFisico e : lista) {
			System.out.println("  - id=" + e.getId() + " | nombre=" + e.getNombre()
					+ " | capacidad=" + e.getCapacidad());
		}
	}

	/*
	 * ===========================================
	 * 2) RELACIONADAS (por nombre de asignatura)
	 * ===========================================
	 */
	private void invocar_findCursosPorNombreDeAsignatura() {
		List<Curso> cursos = this.cursoRepo.findByAsignatura_NombreIgnoreCase("Bases de Datos II");
		System.out.println("[2] Cursos por asignatura = 'Bases de Datos II'");
		for (Curso c : cursos) {
			System.out.println("  - id=" + c.getId() + " | curso=" + c.getNombre());
		}
	}

	/*
	 * ======================================
	 * 3) RELACIONADAS (franjas por curso id)
	 * ======================================
	 */
	private void invocar_findFranjasPorCursoId() {
		int cursoId = 1;
		List<FranjaHoraria> franjas = this.franjaRepo.findByCurso_Id(cursoId);
		System.out.println("[3] Franjas del curso id=" + cursoId + " (con curso+espacio en fetch)");
		for (FranjaHoraria f : franjas) {
			System.out.println("  - franja#" + f.getId() + " | " + f.getDia()
					+ " " + f.getHoraInicio() + "-" + f.getHoraFin()
					+ " | curso=" + f.getCurso().getNombre()
					+ " | espacio=" + f.getEspacioFisico().getNombre());
		}
	}

	/*
	 * ============================================
	 * 4) JPQL (ocupación de un espacio específico)
	 * ============================================
	 */
	private void invocar_countOcupacionEspacio() {
		long count = this.franjaRepo.countOcupacionEspacio(
				DiaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(9, 0), 1);
		System.out.println("[4] Ocupación espacio(id=1) LUNES 08:00-09:00 => " + count + " franja(s)");
	}

	/*
	 * =========================================
	 * 5) SQL NATIVA (ocupación de un docente)
	 * =========================================
	 */
	private void invocar_countOcupacionDocente() {
		long count = this.franjaRepo.countOcupacionDocente(
				"LUNES", LocalTime.of(8, 0), LocalTime.of(9, 0), 2);
		System.out.println("[5] Ocupación docente(id=2) LUNES 08:00-09:00 => " + count + " franja(s)");
	}

	/*
	 * ==========================================================
	 * 6) JOIN múltiple (curso–franja–espacio) detalle combinado
	 * ==========================================================
	 */
	private void invocar_obtenerDetalleFranjasCurso() {
		int cursoId = 1;
		List<Object[]> filas = this.franjaRepo.obtenerDetalleFranjasCurso(cursoId);
		System.out.println("[6] Detalle franjas del curso id=" + cursoId + " (JOIN curso+espacio)");
		for (Object[] r : filas) {
			// f.id, f.dia, f.horaInicio, f.horaFin, c.id, c.nombre, e.id, e.nombre,
			// e.capacidad
			System.out.println("  - franja#" + r[0] + " | " + r[1] + " " + r[2] + "-" + r[3]
					+ " | curso=(" + r[4] + ", " + r[5] + ")"
					+ " | espacio=(" + r[6] + ", " + r[7] + ", cap=" + r[8] + ")");
		}
	}

	/*
	 * =========================================================
	 * 7) UPDATE (modificar solo estado del espacio físico)
	 * =========================================================
	 */
	@Transactional(readOnly = false)
	private void invocar_actualizarEstadoEspacio() {
		int afectados = this.espacioRepo.actualizarEstado(1, false);
		System.out.println("[7] UPDATE estado espacio(id=1 -> activo=false) => filas afectadas: " + afectados);
	}

	/*
	 * ================================================
	 * 8) DELETE (eliminar franjas de un curso por id)
	 * ================================================
	 */
	@Transactional(readOnly = false)
	private void invocar_eliminarFranjasPorCurso() {
		int cursoId = 1;
		int eliminadas = this.franjaRepo.eliminarFranjasPorCursoId(cursoId);
		System.out.println("[8] DELETE franjas del curso id=" + cursoId + " => filas eliminadas: " + eliminadas);
	}

}
