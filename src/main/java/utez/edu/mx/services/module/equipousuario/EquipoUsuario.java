package utez.edu.mx.services.module.equipousuario;

import jakarta.persistence.*;
import utez.edu.mx.services.module.equipo.Equipo;
import utez.edu.mx.services.module.usuario.Usuario;

import java.time.LocalDate;

@Entity
@Table(name = "EQUIPO_USUARIO")
public class EquipoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "equipo_usuario_seq")
    @SequenceGenerator(name = "equipo_usuario_seq", sequenceName = "SEQ_EQUIPO_USUARIO", allocationSize = 1)
    @Column(name = "id_equipo_usuario", nullable = false)
    private Long idEquipoUsuario;

    @ManyToOne
    @JoinColumn(name = "id_equipo", nullable = false)
    private Equipo equipo;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "rol_en_equipo", nullable = false, length = 50)
    private String rolEnEquipo;

    @Column(name = "fecha_union", nullable = false)
    private LocalDate fechaUnion;

    public EquipoUsuario() {}

    public EquipoUsuario(Long idEquipoUsuario, Equipo equipo, Usuario usuario,
                         String rolEnEquipo, LocalDate fechaUnion) {
        this.idEquipoUsuario = idEquipoUsuario;
        this.equipo = equipo;
        this.usuario = usuario;
        this.rolEnEquipo = rolEnEquipo;
        this.fechaUnion = fechaUnion;
    }

    public Long getIdEquipoUsuario() { return idEquipoUsuario; }
    public void setIdEquipoUsuario(Long idEquipoUsuario) { this.idEquipoUsuario = idEquipoUsuario; }

    public Equipo getEquipo() { return equipo; }
    public void setEquipo(Equipo equipo) { this.equipo = equipo; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getRolEnEquipo() { return rolEnEquipo; }
    public void setRolEnEquipo(String rolEnEquipo) { this.rolEnEquipo = rolEnEquipo; }

    public LocalDate getFechaUnion() { return fechaUnion; }
    public void setFechaUnion(LocalDate fechaUnion) { this.fechaUnion = fechaUnion; }
}