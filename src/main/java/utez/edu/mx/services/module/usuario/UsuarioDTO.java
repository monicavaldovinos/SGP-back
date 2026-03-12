package utez.edu.mx.services.module.usuario.dto;

import utez.edu.mx.services.module.rol.Rol;
import utez.edu.mx.services.module.usuario.Usuario;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UsuarioDTO {

    private Long idUsuario;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correo;
    private String username;
    private BigDecimal salario;
    private LocalDate fechaRegistro;
    private String estatus;
    private Rol rol;

    public UsuarioDTO() {}

    // Constructor que convierte desde entidad
    public UsuarioDTO(Usuario usuario) {
        this.idUsuario = usuario.getIdUsuario();
        this.nombre = usuario.getNombre();
        this.apellidoPaterno = usuario.getApellidoPaterno();
        this.apellidoMaterno = usuario.getApellidoMaterno();
        this.correo = usuario.getCorreo();
        this.username = usuario.getUsername();
        this.salario = usuario.getSalario();
        this.fechaRegistro = usuario.getFechaRegistro();
        this.estatus = usuario.getEstatus();
        this.rol = usuario.getRol();
    }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidoPaterno() { return apellidoPaterno; }
    public void setApellidoPaterno(String apellidoPaterno) { this.apellidoPaterno = apellidoPaterno; }

    public String getApellidoMaterno() { return apellidoMaterno; }
    public void setApellidoMaterno(String apellidoMaterno) { this.apellidoMaterno = apellidoMaterno; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}