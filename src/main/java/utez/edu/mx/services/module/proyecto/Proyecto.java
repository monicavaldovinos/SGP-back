package utez.edu.mx.services.module.proyecto;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import utez.edu.mx.services.module.equipo.Equipo;
import utez.edu.mx.services.module.usuario.Usuario;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "PROYECTO")
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proyecto_seq")
    @SequenceGenerator(name = "proyecto_seq", sequenceName = "SEQ_PROYECTO", allocationSize = 1)
    @Column(name = "id_proyecto", nullable = false)
    private Long idProyecto;

    @NotBlank(message = "El nombre del proyecto es obligatorio")
    @Size(max = 150, message = "El nombre no puede tener más de 150 caracteres")
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @Lob
    @Column(name = "objetivo")
    private String objetivo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Future(message = "La fecha de fin debe ser una fecha futura")
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    @DecimalMin(value = "0.0", message = "El presupuesto no puede ser negativo")
    @Column(name = "presupuesto_total", precision = 10, scale = 2)
    private BigDecimal presupuestoTotal;

    @Column(name = "logo", length = 500)
    private String logo;

    // CORREGIDO: ya no obligatorio al crear el proyecto
    @ManyToOne
    @JoinColumn(name = "id_equipo", nullable = true)
    private Equipo equipo;

    @ManyToOne
    @JoinColumn(name = "id_lider", nullable = true)
    private Usuario lider;

    public Proyecto() {}

    public Proyecto(Long idProyecto, String nombre, String descripcion, String objetivo,
                    LocalDate fechaInicio, LocalDate fechaFin, String estado,
                    BigDecimal presupuestoTotal, String logo, Equipo equipo, Usuario lider) {
        this.idProyecto = idProyecto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.objetivo = objetivo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.presupuestoTotal = presupuestoTotal;
        this.logo = logo;
        this.equipo = equipo;
        this.lider = lider;
    }

    public Long getIdProyecto() { return idProyecto; }
    public void setIdProyecto(Long idProyecto) { this.idProyecto = idProyecto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public BigDecimal getPresupuestoTotal() { return presupuestoTotal; }
    public void setPresupuestoTotal(BigDecimal presupuestoTotal) { this.presupuestoTotal = presupuestoTotal; }

    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }

    public Equipo getEquipo() { return equipo; }
    public void setEquipo(Equipo equipo) { this.equipo = equipo; }

    public Usuario getLider() { return lider; }
    public void setLider(Usuario lider) { this.lider = lider; }
}