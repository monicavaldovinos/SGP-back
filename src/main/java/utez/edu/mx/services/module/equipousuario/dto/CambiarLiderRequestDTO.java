package utez.edu.mx.services.module.equipousuario.dto;

import jakarta.validation.constraints.NotNull;

public class CambiarLiderRequestDTO {

    @NotNull(message = "El id del equipo es obligatorio")
    private Long idEquipo;

    @NotNull(message = "El líder actual es obligatorio")
    private Long idLiderActual;

    @NotNull(message = "El nuevo líder es obligatorio")
    private Long idNuevoLider;

    public CambiarLiderRequestDTO() {
    }

    public Long getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Long idEquipo) {
        this.idEquipo = idEquipo;
    }

    public Long getIdLiderActual() {
        return idLiderActual;
    }

    public void setIdLiderActual(Long idLiderActual) {
        this.idLiderActual = idLiderActual;
    }

    public Long getIdNuevoLider() {
        return idNuevoLider;
    }

    public void setIdNuevoLider(Long idNuevoLider) {
        this.idNuevoLider = idNuevoLider;
    }
}