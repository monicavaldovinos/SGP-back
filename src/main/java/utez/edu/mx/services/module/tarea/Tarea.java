package utez.edu.mx.services.module.tarea;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import utez.edu.mx.services.module.proyecto.Proyecto;
import utez.edu.mx.services.module.usuario.Usuario;

import java.time.LocalDate;

@Entity
@Table(name = "TAREA")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tarea_seq")
    @SequenceGenerator(name = "tarea_seq", sequenceName = "SEQ_TAREA", allocationSize = 1)
    @Column(name = "id_tarea", nullable = false)
    private Long idTarea;

    @NotBlank(message = "El nombre de la tarea es obligatorio")
    @Size(max = 150, message = "El nombre no puede tener más de 150 caracteres")
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Future(message = "La fecha de fin debe ser una fecha futura")
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @NotBlank(message = "La prioridad es obligatoria")
    @Pattern(regexp = "ALTA|MEDIA|BAJA", message = "La prioridad debe ser ALTA, MEDIA o BAJA")
    @Column(name = "prioridad", nullable = false, length = 10)
    private String prioridad;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    @NotNull(message = "El proyecto es obligatorio")
    @ManyToOne
    @JoinColumn(name = "id_proyecto", nullable = false)
    private Proyecto proyecto;

    @ManyToOne
    @JoinColumn(name = "id_usuario_asignado")
    private Usuario usuarioAsignado;

    public Tarea() {}

    public Tarea(Long idTarea, String nombre, String descripcion, LocalDate fechaInicio,
                 LocalDate fechaFin, String prioridad, String estado,
                 Proyecto proyecto, Usuario usuarioAsignado) {
        this.idTarea = idTarea;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.prioridad = prioridad;
        this.estado = estado;
        this.proyecto = proyecto;
        this.usuarioAsignado = usuarioAsignado;
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

    public Proyecto getProyecto() { return proyecto; }
    public void setProyecto(Proyecto proyecto) { this.proyecto = proyecto; }

    public Usuario getUsuarioAsignado() { return usuarioAsignado; }
    public void setUsuarioAsignado(Usuario usuarioAsignado) { this.usuarioAsignado = usuarioAsignado; }
}