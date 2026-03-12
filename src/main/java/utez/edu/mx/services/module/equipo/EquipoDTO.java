package utez.edu.mx.services.module.equipo.dto;

import utez.edu.mx.services.module.equipo.Equipo;

import java.time.LocalDate;

public class EquipoDTO {

    private Long idEquipo;
    private String nombreEquipo;
    private String descripcion;
    private String logo;
    private LocalDate fechaCreacion;
    private String estatus;

    public EquipoDTO() {}

    public EquipoDTO(Equipo equipo) {
        this.idEquipo = equipo.getIdEquipo();
        this.nombreEquipo = equipo.getNombreEquipo();
        this.descripcion = equipo.getDescripcion();
        this.logo = equipo.getLogo();
        this.fechaCreacion = equipo.getFechaCreacion();
        this.estatus = equipo.getEstatus();
    }

    public Long getIdEquipo() { return idEquipo; }
    public void setIdEquipo(Long idEquipo) { this.idEquipo = idEquipo; }

    public String getNombreEquipo() { return nombreEquipo; }
    public void setNombreEquipo(String nombreEquipo) { this.nombreEquipo = nombreEquipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }

    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }
}