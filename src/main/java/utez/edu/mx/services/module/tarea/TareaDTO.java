package utez.edu.mx.services.module.tarea.dto;

import utez.edu.mx.services.module.proyecto.dto.ProyectoDTO;
import utez.edu.mx.services.module.tarea.Tarea;
import utez.edu.mx.services.module.usuario.dto.UsuarioDTO;

import java.time.LocalDate;

public class TareaDTO {

    private Long idTarea;
    private String nombre;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String prioridad;
    private String estado;
    private ProyectoDTO proyecto;
    private UsuarioDTO usuarioAsignado;

    public TareaDTO() {}

    public TareaDTO(Tarea tarea) {
        this.idTarea = tarea.getIdTarea();
        this.nombre = tarea.getNombre();
        this.descripcion = tarea.getDescripcion();
        this.fechaInicio = tarea.getFechaInicio();
        this.fechaFin = tarea.getFechaFin();
        this.prioridad = tarea.getPrioridad();
        this.estado = tarea.getEstado();
        this.proyecto = new ProyectoDTO(tarea.getProyecto());
        this.usuarioAsignado = tarea.getUsuarioAsignado() != null
                ? new UsuarioDTO(tarea.getUsuarioAsignado()) : null;
    }

    public Long getIdTarea() { return idTarea; }
    public void setIdTarea(Long idTarea) { this.idTarea = idTarea; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public ProyectoDTO getProyecto() { return proyecto; }
    public void setProyecto(ProyectoDTO proyecto) { this.proyecto = proyecto; }

    public UsuarioDTO getUsuarioAsignado() { return usuarioAsignado; }
    public void setUsuarioAsignado(UsuarioDTO usuarioAsignado) { this.usuarioAsignado = usuarioAsignado; }
}