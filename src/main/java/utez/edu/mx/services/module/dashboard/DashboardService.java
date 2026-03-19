package utez.edu.mx.services.module.dashboard;

import org.springframework.stereotype.Service;
import utez.edu.mx.services.module.equipo.Equipo;
import utez.edu.mx.services.module.equipo.EquipoRepository;
import utez.edu.mx.services.module.equipousuario.EquipoUsuario;
import utez.edu.mx.services.module.equipousuario.EquipoUsuarioRepository;
import utez.edu.mx.services.module.pago.Pago;
import utez.edu.mx.services.module.pago.PagoRepository;
import utez.edu.mx.services.module.proyecto.Proyecto;
import utez.edu.mx.services.module.proyecto.ProyectoRepository;
import utez.edu.mx.services.module.tarea.Tarea;
import utez.edu.mx.services.module.tarea.TareaRepository;
import utez.edu.mx.services.module.usuario.Usuario;
import utez.edu.mx.services.module.usuario.UsuarioRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final TareaRepository tareaRepository;
    private final EquipoUsuarioRepository equipoUsuarioRepository;
    private final ProyectoRepository proyectoRepository;
    private final PagoRepository pagoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EquipoRepository equipoRepository;

    public DashboardService(
            TareaRepository tareaRepository,
            EquipoUsuarioRepository equipoUsuarioRepository,
            ProyectoRepository proyectoRepository,
            PagoRepository pagoRepository,
            UsuarioRepository usuarioRepository,
            EquipoRepository equipoRepository
    ) {
        this.tareaRepository = tareaRepository;
        this.equipoUsuarioRepository = equipoUsuarioRepository;
        this.proyectoRepository = proyectoRepository;
        this.pagoRepository = pagoRepository;
        this.usuarioRepository = usuarioRepository;
        this.equipoRepository = equipoRepository;
    }

    public DashboardResponseDTO getDashboardIntegrante(Usuario usuario) {
        List<Tarea> tareas = tareaRepository.findByUsuarioAsignadoIdUsuario(usuario.getIdUsuario());

        int totalTareas = tareas.size();

        int tareasCompletadas = (int) tareas.stream()
                .filter(t -> "COMPLETADA".equalsIgnoreCase(t.getEstado()))
                .count();

        int tareasPendientes = totalTareas - tareasCompletadas;

        Map<Long, List<Tarea>> tareasPorProyecto = tareas.stream()
                .collect(Collectors.groupingBy(t -> t.getProyecto().getIdProyecto()));

        List<DashboardProyectoDTO> proyectos = tareasPorProyecto.entrySet().stream()
                .map(entry -> {
                    List<Tarea> tareasDelProyecto = entry.getValue();

                    long completadas = tareasDelProyecto.stream()
                            .filter(t -> "COMPLETADA".equalsIgnoreCase(t.getEstado()))
                            .count();

                    double porcentaje = tareasDelProyecto.isEmpty()
                            ? 0
                            : (double) completadas / tareasDelProyecto.size() * 100;

                    Proyecto proyecto = tareasDelProyecto.get(0).getProyecto();

                    return new DashboardProyectoDTO(
                            proyecto.getIdProyecto(),
                            proyecto.getNombre(),
                            proyecto.getEstado(),
                            Math.round(porcentaje * 10.0) / 10.0
                    );
                })
                .toList();

        return new DashboardResponseDTO(
                usuario.getNombre(),
                usuario.getApellidoPaterno(),
                totalTareas,
                tareasCompletadas,
                tareasPendientes,
                proyectos
        );
    }

    public LeaderDashboardDTO getDashboardLider(Usuario usuario) {
        List<EquipoUsuario> relaciones = equipoUsuarioRepository.findByUsuarioIdUsuario(usuario.getIdUsuario());

        if (relaciones.isEmpty()) {
            return new LeaderDashboardDTO(
                    usuario.getNombre(),
                    usuario.getApellidoPaterno(),
                    null,
                    null,
                    0,
                    null,
                    null,
                    null,
                    0,
                    0,
                    0,
                    0,
                    0,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO
            );
        }

        Equipo equipo = relaciones.get(0).getEquipo();

        List<EquipoUsuario> integrantesEquipo = equipoUsuarioRepository.findByEquipoIdEquipo(equipo.getIdEquipo());
        int totalIntegrantes = integrantesEquipo.size();

        List<Proyecto> proyectos = proyectoRepository.findByEquipoIdEquipo(equipo.getIdEquipo());

        Optional<Proyecto> proyectoOpt = proyectos.stream()
                .filter(p -> !"CANCELADO".equalsIgnoreCase(p.getEstado()))
                .findFirst();

        if (proyectoOpt.isEmpty()) {
            return new LeaderDashboardDTO(
                    usuario.getNombre(),
                    usuario.getApellidoPaterno(),
                    equipo.getIdEquipo(),
                    equipo.getNombreEquipo(),
                    totalIntegrantes,
                    null,
                    null,
                    null,
                    0,
                    0,
                    0,
                    0,
                    0,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO
            );
        }

        Proyecto proyecto = proyectoOpt.get();

        List<Tarea> tareasProyecto = tareaRepository.findByProyectoIdProyecto(proyecto.getIdProyecto());

        int totalTareas = tareasProyecto.size();

        int tareasCompletadas = (int) tareasProyecto.stream()
                .filter(t -> "COMPLETADA".equalsIgnoreCase(t.getEstado()))
                .count();

        int tareasPendientes = totalTareas - tareasCompletadas;

        List<Pago> pagosProyecto = pagoRepository.findByProyectoIdProyecto(proyecto.getIdProyecto());

        long pagosPagados = pagosProyecto.stream()
                .filter(p -> "PAGADO".equalsIgnoreCase(p.getEstatus()))
                .count();

        long pagosPendientes = pagosProyecto.stream()
                .filter(p -> "PENDIENTE".equalsIgnoreCase(p.getEstatus()))
                .count();

        BigDecimal montoPagado = pagosProyecto.stream()
                .filter(p -> "PAGADO".equalsIgnoreCase(p.getEstatus()))
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal montoPendiente = pagosProyecto.stream()
                .filter(p -> "PENDIENTE".equalsIgnoreCase(p.getEstatus()))
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new LeaderDashboardDTO(
                usuario.getNombre(),
                usuario.getApellidoPaterno(),
                equipo.getIdEquipo(),
                equipo.getNombreEquipo(),
                totalIntegrantes,
                proyecto.getIdProyecto(),
                proyecto.getNombre(),
                proyecto.getEstado(),
                totalTareas,
                tareasCompletadas,
                tareasPendientes,
                pagosPagados,
                pagosPendientes,
                montoPagado,
                montoPendiente
        );
    }

    public AdminDashboardDTO getDashboardAdmin() {
        long totalUsuarios = usuarioRepository.count();
        long totalEquipos = equipoRepository.count();
        long totalProyectos = proyectoRepository.count();
        long totalTareas = tareaRepository.count();
        long totalPagos = pagoRepository.count();

        List<Proyecto> proyectos = proyectoRepository.findAll();
        long proyectosActivos = proyectos.stream()
                .filter(p -> !"CANCELADO".equalsIgnoreCase(p.getEstado()))
                .count();

        long proyectosCancelados = proyectos.stream()
                .filter(p -> "CANCELADO".equalsIgnoreCase(p.getEstado()))
                .count();

        List<Tarea> tareas = tareaRepository.findAll();
        long tareasCompletadas = tareas.stream()
                .filter(t -> "COMPLETADA".equalsIgnoreCase(t.getEstado()))
                .count();

        long tareasPendientes = tareas.stream()
                .filter(t -> !"COMPLETADA".equalsIgnoreCase(t.getEstado()))
                .count();

        List<Pago> pagos = pagoRepository.findAll();
        long pagosPagados = pagos.stream()
                .filter(p -> "PAGADO".equalsIgnoreCase(p.getEstatus()))
                .count();

        long pagosPendientes = pagos.stream()
                .filter(p -> "PENDIENTE".equalsIgnoreCase(p.getEstatus()))
                .count();

        BigDecimal montoTotalPagado = pagos.stream()
                .filter(p -> "PAGADO".equalsIgnoreCase(p.getEstatus()))
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal montoTotalPendiente = pagos.stream()
                .filter(p -> "PENDIENTE".equalsIgnoreCase(p.getEstatus()))
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new AdminDashboardDTO(
                totalUsuarios,
                totalEquipos,
                totalProyectos,
                proyectosActivos,
                proyectosCancelados,
                totalTareas,
                tareasCompletadas,
                tareasPendientes,
                totalPagos,
                pagosPagados,
                pagosPendientes,
                montoTotalPagado,
                montoTotalPendiente
        );
    }
}