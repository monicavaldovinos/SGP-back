package utez.edu.mx.services.module.equipo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateTeamRequestDTO {

    @NotBlank(message = "El nombre del equipo es obligatorio")
    private String nombreEquipo;

    private String descripcion;

    @NotNull(message = "Debes seleccionar un proyecto")
    private Long idProyecto;

    @NotNull(message = "Debes seleccionar un líder")
    private Long idLider;

    @NotEmpty(message = "Debes seleccionar integrantes")
    private List<Long> integrantesIds;

    private String logo;

    public CreateTeamRequestDTO() {}

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Long idProyecto) {
        this.idProyecto = idProyecto;
    }

    public Long getIdLider() {
        return idLider;
    }

    public void setIdLider(Long idLider) {
        this.idLider = idLider;
    }

    public List<Long> getIntegrantesIds() {
        return integrantesIds;
    }

    public void setIntegrantesIds(List<Long> integrantesIds) {
        this.integrantesIds = integrantesIds;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}