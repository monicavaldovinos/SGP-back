package utez.edu.mx.services.module.equipousuario.dto;

import utez.edu.mx.services.module.equipo.dto.EquipoDTO;
import utez.edu.mx.services.module.equipousuario.EquipoUsuario;
import utez.edu.mx.services.module.usuario.dto.UsuarioDTO;

import java.time.LocalDate;

public class EquipoUsuarioDTO {

    private Long idEquipoUsuario;
    private EquipoDTO equipo;
    private UsuarioDTO usuario;
    private String rolEnEquipo;
    private LocalDate fechaUnion;

    public EquipoUsuarioDTO() {}

    public EquipoUsuarioDTO(EquipoUsuario equipoUsuario) {
        this.idEquipoUsuario = equipoUsuario.getIdEquipoUsuario();
        this.equipo = new EquipoDTO(equipoUsuario.getEquipo());
        this.usuario = new UsuarioDTO(equipoUsuario.getUsuario());
        this.rolEnEquipo = equipoUsuario.getRolEnEquipo();
        this.fechaUnion = equipoUsuario.getFechaUnion();
    }

    public Long getIdEquipoUsuario() { return idEquipoUsuario; }
    public void setIdEquipoUsuario(Long idEquipoUsuario) { this.idEquipoUsuario = idEquipoUsuario; }

    public EquipoDTO getEquipo() { return equipo; }
    public void setEquipo(EquipoDTO equipo) { this.equipo = equipo; }

    public UsuarioDTO getUsuario() { return usuario; }
    public void setUsuario(UsuarioDTO usuario) { this.usuario = usuario; }

    public String getRolEnEquipo() { return rolEnEquipo; }
    public void setRolEnEquipo(String rolEnEquipo) { this.rolEnEquipo = rolEnEquipo; }

    public LocalDate getFechaUnion() { return fechaUnion; }
    public void setFechaUnion(LocalDate fechaUnion) { this.fechaUnion = fechaUnion; }
}