package utez.edu.mx.services.module.equipo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import utez.edu.mx.services.module.proyecto.Proyecto;
import utez.edu.mx.services.module.usuario.Usuario;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "EQUIPO")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "equipo_seq")
    @SequenceGenerator(name = "equipo_seq", sequenceName = "SEQ_EQUIPO", allocationSize = 1)
    @Column(name = "id_equipo", nullable = false)
    private Long idEquipo;

    @NotBlank(message = "El nombre del equipo es obligatorio")
    @Size(max = 150, message = "El nombre no puede tener más de 150 caracteres")
    @Column(name = "nombre_equipo", nullable = false, length = 150)
    private String nombreEquipo;

    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Lob
    @Column(name = "logo")
    private String logo;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;

    @Column(name = "estatus", nullable = false, length = 20)
    private String estatus;

    @Transient
    private Usuario lider;

    @Transient
    private Proyecto proyecto;

    @Transient
    private List<Usuario> miembros;

    public Equipo() {}

    public Equipo(Long idEquipo, String nombreEquipo, String descripcion, String logo,
                  LocalDate fechaCreacion, String estatus) {
        this.idEquipo = idEquipo;
        this.nombreEquipo = nombreEquipo;
        this.descripcion = descripcion;
        this.logo = logo;
        this.fechaCreacion = fechaCreacion;
        this.estatus = estatus;
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

    public Usuario getLider() { return lider; }
    public void setLider(Usuario lider) { this.lider = lider; }

    public Proyecto getProyecto() { return proyecto; }
    public void setProyecto(Proyecto proyecto) { this.proyecto = proyecto; }

    public List<Usuario> getMiembros() { return miembros; }
    public void setMiembros(List<Usuario> miembros) { this.miembros = miembros; }
}