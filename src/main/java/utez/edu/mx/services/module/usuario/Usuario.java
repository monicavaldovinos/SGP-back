package utez.edu.mx.services.module.usuario;

import jakarta.persistence.*;
import utez.edu.mx.services.module.rol.Rol;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    @SequenceGenerator(name = "usuario_seq", sequenceName = "SEQ_USUARIO", allocationSize = 1)
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellido_paterno", nullable = false, length = 100)
    private String apellidoPaterno;

    @Column(name = "apellido_materno", length = 100)
    private String apellidoMaterno;

    @Column(name = "correo", nullable = false, unique = true, length = 150)
    private String correo;

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    // ✅ Campo nuevo agregado según BD final
    @Column(name = "salario", precision = 10, scale = 2)
    private BigDecimal salario;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @Column(name = "estatus", nullable = false, length = 20)
    private String estatus;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    public Usuario() {}

    public Usuario(Long idUsuario, String nombre, String apellidoPaterno, String apellidoMaterno,
                   String correo, String username, String password, BigDecimal salario,
                   LocalDate fechaRegistro, String estatus, Rol rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.correo = correo;
        this.username = username;
        this.password = password;
        this.salario = salario;
        this.fechaRegistro = fechaRegistro;
        this.estatus = estatus;
        this.rol = rol;
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

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}