package utez.edu.mx.services.module.pago;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.services.kernel.AppiResponse;
import utez.edu.mx.services.module.categoriagasto.CategoriaGasto;
import utez.edu.mx.services.module.categoriagasto.CategoriaGastoRepository;
import utez.edu.mx.services.module.equipousuario.EquipoUsuario;
import utez.edu.mx.services.module.equipousuario.EquipoUsuarioRepository;
import utez.edu.mx.services.module.pago.dto.GenerarPagosPeriodoDTO;
import utez.edu.mx.services.module.pago.dto.RealizarPagoDTO;
import utez.edu.mx.services.module.presupuesto.Presupuesto;
import utez.edu.mx.services.module.presupuesto.PresupuestoRepository;
import utez.edu.mx.services.module.proyecto.Proyecto;
import utez.edu.mx.services.module.proyecto.ProyectoRepository;
import utez.edu.mx.services.module.usuario.Usuario;
import utez.edu.mx.services.module.usuario.UsuarioRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final PresupuestoRepository presupuestoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProyectoRepository proyectoRepository;
    private final EquipoUsuarioRepository equipoUsuarioRepository;
    private final CategoriaGastoRepository categoriaGastoRepository;

    public PagoService(
            PagoRepository pagoRepository,
            PresupuestoRepository presupuestoRepository,
            UsuarioRepository usuarioRepository,
            ProyectoRepository proyectoRepository,
            EquipoUsuarioRepository equipoUsuarioRepository,
            CategoriaGastoRepository categoriaGastoRepository
    ) {
        this.pagoRepository = pagoRepository;
        this.presupuestoRepository = presupuestoRepository;
        this.usuarioRepository = usuarioRepository;
        this.proyectoRepository = proyectoRepository;
        this.equipoUsuarioRepository = equipoUsuarioRepository;
        this.categoriaGastoRepository = categoriaGastoRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findAll() {
        List<PagoDTO> pagos = pagoRepository.findAll()
                .stream()
                .map(PagoDTO::new)
                .toList();

        return ResponseEntity.ok(new AppiResponse("Operación exitosa", pagos, HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findById(Long id) {
        Optional<Pago> pagoOpt = pagoRepository.findById(id);

        if (pagoOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Pago no encontrado", HttpStatus.BAD_REQUEST));
        }

        return ResponseEntity.ok(
                new AppiResponse("Operación exitosa", new PagoDTO(pagoOpt.get()), HttpStatus.OK)
        );
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findMisPagos(String username) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AppiResponse("Usuario no encontrado", HttpStatus.NOT_FOUND));
        }

        Usuario usuario = usuarioOpt.get();
        String rol = usuario.getRol() != null ? usuario.getRol().getNombre().toUpperCase() : "";

        List<PagoDTO> data = ("SUPERADMIN".equals(rol)
                ? pagoRepository.findAll().stream()
                : pagoRepository.findByUsuarioIdUsuario(usuario.getIdUsuario()).stream())
                .map(PagoDTO::new)
                .toList();

        return ResponseEntity.ok(new AppiResponse("Operación exitosa", data, HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findByProyecto(Long idProyecto) {
        List<PagoDTO> pagos = pagoRepository.findByProyectoIdProyecto(idProyecto)
                .stream()
                .map(PagoDTO::new)
                .toList();

        return ResponseEntity.ok(new AppiResponse("Operación exitosa", pagos, HttpStatus.OK));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> findByUsuario(Long idUsuario) {
        List<PagoDTO> pagos = pagoRepository.findByUsuarioIdUsuario(idUsuario)
                .stream()
                .map(PagoDTO::new)
                .toList();

        return ResponseEntity.ok(new AppiResponse("Operación exitosa", pagos, HttpStatus.OK));
    }

    @Transactional
    public ResponseEntity<AppiResponse> generarPagosPeriodo(GenerarPagosPeriodoDTO dto, String username) {
        if (dto.getIdProyecto() == null || dto.getPeriodo() == null || dto.getPeriodo().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Proyecto y periodo son obligatorios", HttpStatus.BAD_REQUEST));
        }

        Optional<Usuario> registradorOpt = usuarioRepository.findByUsername(username);
        if (registradorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AppiResponse("Usuario autenticado no encontrado", HttpStatus.NOT_FOUND));
        }

        Optional<Proyecto> proyectoOpt = proyectoRepository.findById(dto.getIdProyecto());
        if (proyectoOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Proyecto no encontrado", HttpStatus.BAD_REQUEST));
        }

        Proyecto proyecto = proyectoOpt.get();

        if ("CANCELADO".equalsIgnoreCase(proyecto.getEstado())) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse(
                            "No se pueden generar pagos para un proyecto cancelado",
                            HttpStatus.BAD_REQUEST
                    ));
        }

        Optional<CategoriaGasto> categoriaOpt = categoriaGastoRepository.findByNombreIgnoreCase("NOMINA");
        if (categoriaOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse(
                            "No existe la categoría de gasto 'NOMINA'. Debes crearla en la base de datos.",
                            HttpStatus.BAD_REQUEST
                    ));
        }

        if (proyecto.getEquipo() == null) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El proyecto no tiene equipo asignado", HttpStatus.BAD_REQUEST));
        }

        List<EquipoUsuario> integrantesEquipo =
                equipoUsuarioRepository.findByEquipoIdEquipo(proyecto.getEquipo().getIdEquipo());

        List<PagoDTO> generados = new ArrayList<>();

        for (EquipoUsuario relacion : integrantesEquipo) {
            Usuario usuario = relacion.getUsuario();

            if (usuario == null || !"ACTIVO".equalsIgnoreCase(usuario.getEstatus())) {
                continue;
            }

            boolean yaExiste = pagoRepository.existsByProyectoIdProyectoAndUsuarioIdUsuarioAndPeriodo(
                    proyecto.getIdProyecto(),
                    usuario.getIdUsuario(),
                    dto.getPeriodo()
            );

            if (yaExiste) {
                continue;
            }

            Pago pago = new Pago();
            pago.setProyecto(proyecto);
            pago.setUsuario(usuario);
            pago.setRegistradoPor(registradorOpt.get());
            pago.setCategoria(categoriaOpt.get());
            pago.setConcepto("Pago quincenal");
            pago.setDescripcion("Pago generado automáticamente para el periodo " + dto.getPeriodo());
            pago.setHoras(BigDecimal.ZERO);
            pago.setTarifa(BigDecimal.ZERO);
            pago.setMonto(BigDecimal.ZERO);
            pago.setFormaPago("TRANSFERENCIA");
            pago.setPeriodo(dto.getPeriodo());
            pago.setFechaPago(null);
            pago.setEstatus("PENDIENTE");

            Pago saved = pagoRepository.save(pago);
            generados.add(new PagoDTO(saved));
        }

        return ResponseEntity.ok(
                new AppiResponse("Pagos del periodo generados correctamente", generados, HttpStatus.OK)
        );
    }

    @Transactional
    public ResponseEntity<AppiResponse> realizarPago(Long id, RealizarPagoDTO dto, String username) {
        Optional<Pago> pagoOpt = pagoRepository.findById(id);
        if (pagoOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Pago no encontrado", HttpStatus.BAD_REQUEST));
        }

        Optional<Usuario> registradorOpt = usuarioRepository.findByUsername(username);
        if (registradorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AppiResponse("Usuario autenticado no encontrado", HttpStatus.NOT_FOUND));
        }

        if (dto.getHoras() == null || dto.getHoras().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Las horas deben ser mayores a 0", HttpStatus.BAD_REQUEST));
        }

        if (dto.getTarifa() == null || dto.getTarifa().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("La tarifa debe ser mayor a 0", HttpStatus.BAD_REQUEST));
        }

        Pago pago = pagoOpt.get();

        if ("CANCELADO".equalsIgnoreCase(pago.getProyecto().getEstado())) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse(
                            "No se puede realizar un pago en un proyecto cancelado",
                            HttpStatus.BAD_REQUEST
                    ));
        }

        if (!"PENDIENTE".equalsIgnoreCase(pago.getEstatus())) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Solo se pueden realizar pagos con estatus PENDIENTE", HttpStatus.BAD_REQUEST));
        }

        BigDecimal monto = dto.getHoras().multiply(dto.getTarifa());

        Long idProyecto = pago.getProyecto().getIdProyecto();
        Optional<Presupuesto> presupuestoOpt = presupuestoRepository.findByProyectoIdProyecto(idProyecto);

        if (presupuestoOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El proyecto no tiene presupuesto asignado", HttpStatus.BAD_REQUEST));
        }

        Presupuesto presupuesto = presupuestoOpt.get();
        BigDecimal disponible = presupuesto.getMontoDisponible();

        if (disponible.compareTo(monto) < 0) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("No hay suficiente presupuesto para realizar el pago", HttpStatus.BAD_REQUEST));
        }

        presupuesto.setMontoUtilizado(presupuesto.getMontoUtilizado().add(monto));
        presupuesto.setMontoDisponible(disponible.subtract(monto));
        presupuestoRepository.save(presupuesto);

        pago.setHoras(dto.getHoras());
        pago.setTarifa(dto.getTarifa());
        pago.setMonto(monto);
        pago.setDescripcion(dto.getDescripcion());
        pago.setFormaPago("TRANSFERENCIA");
        pago.setFechaPago(LocalDate.now());
        pago.setEstatus("PAGADO");
        pago.setRegistradoPor(registradorOpt.get());

        Pago saved = pagoRepository.save(pago);

        return ResponseEntity.ok(
                new AppiResponse("Pago realizado correctamente", new PagoDTO(saved), HttpStatus.OK)
        );
    }

    @Transactional
    public ResponseEntity<AppiResponse> save(Pago pago) {
        Long idProyecto = pago.getProyecto().getIdProyecto();

        Optional<Presupuesto> presupuestoOpt = presupuestoRepository.findByProyectoIdProyecto(idProyecto);
        if (presupuestoOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El proyecto no tiene presupuesto asignado", HttpStatus.BAD_REQUEST));
        }

        Presupuesto presupuesto = presupuestoOpt.get();
        BigDecimal disponible = presupuesto.getMontoDisponible();

        if (disponible.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Fondos insuficientes para realizar el pago", HttpStatus.BAD_REQUEST));
        }

        if (pago.getMonto().compareTo(disponible) > 0) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse(
                            "No hay suficiente presupuesto para realizar este pago. Disponible: $" + disponible,
                            HttpStatus.BAD_REQUEST
                    ));
        }

        if (pago.getPeriodo() != null && pago.getUsuario() != null) {
            boolean periodoDuplicado = pagoRepository
                    .findByProyectoIdProyectoAndUsuarioIdUsuario(idProyecto, pago.getUsuario().getIdUsuario())
                    .stream()
                    .anyMatch(p -> pago.getPeriodo().equals(p.getPeriodo()));

            if (periodoDuplicado) {
                return ResponseEntity.badRequest()
                        .body(new AppiResponse(
                                "Este periodo ya fue pagado para este usuario en este proyecto",
                                HttpStatus.BAD_REQUEST
                        ));
            }
        }

        presupuesto.setMontoUtilizado(presupuesto.getMontoUtilizado().add(pago.getMonto()));
        presupuesto.setMontoDisponible(disponible.subtract(pago.getMonto()));
        presupuestoRepository.save(presupuesto);

        pago.setFechaPago(LocalDate.now());

        if (pago.getEstatus() == null || pago.getEstatus().isBlank()) {
            pago.setEstatus("PENDIENTE");
        }

        Pago saved = pagoRepository.save(pago);

        return ResponseEntity.ok(
                new AppiResponse("Pago registrado exitosamente", new PagoDTO(saved), HttpStatus.OK)
        );
    }

    @Transactional
    public ResponseEntity<AppiResponse> update(Long id, Pago pago) {
        Optional<Pago> existing = pagoRepository.findById(id);

        if (existing.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Pago no encontrado", HttpStatus.BAD_REQUEST));
        }

        Pago p = existing.get();
        p.setConcepto(pago.getConcepto());
        p.setDescripcion(pago.getDescripcion());
        p.setMonto(pago.getMonto());
        p.setHoras(pago.getHoras());
        p.setTarifa(pago.getTarifa());
        p.setFormaPago(pago.getFormaPago());
        p.setPeriodo(pago.getPeriodo());
        p.setCategoria(pago.getCategoria());

        return ResponseEntity.ok(
                new AppiResponse("Pago actualizado exitosamente", new PagoDTO(pagoRepository.save(p)), HttpStatus.OK)
        );
    }

    @Transactional
    public ResponseEntity<AppiResponse> cambiarEstatus(Long id, String estatus) {
        Optional<Pago> existing = pagoRepository.findById(id);

        if (existing.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Pago no encontrado", HttpStatus.BAD_REQUEST));
        }

        Pago p = existing.get();
        p.setEstatus(estatus);
        pagoRepository.save(p);

        return ResponseEntity.ok(
                new AppiResponse("Estatus actualizado exitosamente", HttpStatus.OK)
        );
    }

    @Transactional
    public ResponseEntity<AppiResponse> delete(Long id) {
        Optional<Pago> existing = pagoRepository.findById(id);

        if (existing.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Pago no encontrado", HttpStatus.BAD_REQUEST));
        }

        pagoRepository.deleteById(id);

        return ResponseEntity.ok(
                new AppiResponse("Pago eliminado exitosamente", HttpStatus.OK)
        );
    }
    @Transactional(readOnly = true)
    public ResponseEntity<AppiResponse> resumenPorProyecto(Long idProyecto) {
        List<Pago> pagos = pagoRepository.findByProyectoIdProyecto(idProyecto);

        BigDecimal totalPagado = pagos.stream()
                .filter(p -> "PAGADO".equalsIgnoreCase(p.getEstatus()))
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPendiente = pagos.stream()
                .filter(p -> "PENDIENTE".equalsIgnoreCase(p.getEstatus()))
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long pagosRealizados = pagos.stream()
                .filter(p -> "PAGADO".equalsIgnoreCase(p.getEstatus()))
                .count();

        long pagosPendientes = pagos.stream()
                .filter(p -> "PENDIENTE".equalsIgnoreCase(p.getEstatus()))
                .count();

        java.util.Map<String, Object> data = new java.util.HashMap<>();
        data.put("totalPagado", totalPagado);
        data.put("totalPendiente", totalPendiente);
        data.put("pagosRealizados", pagosRealizados);
        data.put("pagosPendientes", pagosPendientes);

        return ResponseEntity.ok(
                new AppiResponse("Resumen de pagos obtenido correctamente", data, HttpStatus.OK)
        );
    }

}
