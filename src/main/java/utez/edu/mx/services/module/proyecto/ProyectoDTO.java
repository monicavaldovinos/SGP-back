package utez.edu.mx.services.module.proyecto.dto;

import utez.edu.mx.services.module.equipo.Equipo;
import utez.edu.mx.services.module.proyecto.Proyecto;
import utez.edu.mx.services.module.usuario.dto.UsuarioDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProyectoDTO {

    private Long idProyecto;
    private String nombre;
    private String descripcion;
    private String objetivo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
    private BigDecimal presupuestoTotal;
    private String logo;
    private Equipo equipo;
    private UsuarioDTO lider;

    public ProyectoDTO() {}

    public ProyectoDTO(Proyecto proyecto) {
        this.idProyecto = proyecto.getIdProyecto();
        this.nombre = proyecto.getNombre();
        this.descripcion = proyecto.getDescripcion();
        this.objetivo = proyecto.getObjetivo();
        this.fechaInicio = proyecto.getFechaInicio();
        this.fechaFin = proyecto.getFechaFin();
        this.estado = proyecto.getEstado();
        this.presupuestoTotal = proyecto.getPresupuestoTotal();
        this.logo = proyecto.getLogo();
        this.equipo = proyecto.getEquipo();
        this.lider = new UsuarioDTO(proyecto.getLider());
    }

    public Long getIdProyecto() { return idProyecto; }
    public void setIdProyecto(Long idProyecto) { this.idProyecto = idProyecto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public BigDecimal getPresupuestoTotal() { return presupuestoTotal; }
    public void setPresupuestoTotal(BigDecimal presupuestoTotal) { this.presupuestoTotal = presupuestoTotal; }

    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }

    public Equipo getEquipo() { return equipo; }
    public void setEquipo(Equipo equipo) { this.equipo = equipo; }

    public UsuarioDTO getLider() { return lider; }
    public void setLider(UsuarioDTO lider) { this.lider = lider; }
}